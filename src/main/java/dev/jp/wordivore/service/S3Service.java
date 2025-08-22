package dev.jp.wordivore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final S3Client s3Client;

    @Value("${AWS_BUCKET}")
    private String bucket;

    @Value("${aws.s3.prefix}")
    private String prefix;

    public void uploadCover(String key, String coverUrl) throws IOException, InterruptedException {
        String finalKey = prefix + key + "-M.jpg";

        if(objectExists(finalKey)){
            log.info("Key already exists. Cannot upload.");
            return;
        }
        var request = PutObjectRequest.builder()
                .key(finalKey)
                .bucket(bucket)
                .contentType("image/jpeg")
                .build();

        HttpClient http = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        String fullUrl = coverUrl + "-M.jpg";

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<byte[]> response = http.send(req, HttpResponse.BodyHandlers.ofByteArray());

        if(response.statusCode() / 100 != 2) {
            throw new IOException("Failed to fetch image. " + response.statusCode());
        }

        byte[] body = response.body();

        s3Client.putObject(request, RequestBody.fromBytes(body));
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
            if(e.statusCode() == 404){
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
