package dev.jp.wordivore.service;

import dev.jp.wordivore.exception.CoverDuplicateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final S3Client s3Client;

    @Value("${AWS_BUCKET}")
    private String bucket;

    @Value("${aws.s3.prefix}")
    private String prefix;

    public Optional<String> uploadCover(String key, String coverUrl) {
        String finalKey = prefix + key + "-M.jpg";

        if(objectExists(finalKey)){
            log.info("Key already exists. Returning key.");
           return Optional.of(finalKey);
        }

        HttpClient http = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        String fullUrl = coverUrl + "-M.jpg?default=false";

        HttpRequest headReq = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(20))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            var head = http.send(headReq, HttpResponse.BodyHandlers.discarding());

            //No cover in Cover Api
            if(head.statusCode() == 404) return Optional.empty();

            //Missing
            if(head.statusCode() / 100 !=2 ) return Optional.empty();

            HttpRequest getReq = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = http.send(getReq, HttpResponse.BodyHandlers.ofByteArray());

            if(response.statusCode() / 100 !=2 ) return Optional.empty();

            byte[] body = response.body();

            if(body == null || body.length == 0) return Optional.empty();

            var putObjectRequest = PutObjectRequest.builder()
                    .key(finalKey)
                    .bucket(bucket)
                    .contentType("image/jpeg")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(body));

            return Optional.of(finalKey);
        }catch (IOException | InterruptedException ie){
            return Optional.empty();
        }
    }

    public boolean objectExists(String key){
        try{
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
            return true;
        }catch (NoSuchKeyException e){
            return false;
        }catch (S3Exception e){
            if(e.statusCode() == HttpStatus.NOT_FOUND.value()){
                return false;
            }
            throw e;
        }
    }

    public void listFolders() {
        ListObjectsV2Request req = ListObjectsV2Request.builder()
                .bucket("mayhmblog")
                .prefix("wordivore/")
                .delimiter("/")
                .build();

        ListObjectsV2Response resp = s3Client.listObjectsV2(req);
    }
}
