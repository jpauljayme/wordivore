package dev.jp.wordivore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.time.Duration;

@Configuration
public class S3Config {

    @Bean Region region(@Value("${AWS_REGION}") String region){
        return Region.of(region);
    }

    @Bean
    AwsCredentialsProvider awsCredentialsProvider(){
        return DefaultCredentialsProvider.builder().build();
    }

    @Bean
    public S3Client s3(Region region, AwsCredentialsProvider creds){
        var http = ApacheHttpClient.builder()
                .connectionTimeout(Duration.ofSeconds(30))
                .maxConnections(100)
                .build();
        var overrides = ClientOverrideConfiguration.builder()
                .apiCallAttemptTimeout(Duration.ofSeconds(30))
                .apiCallTimeout(Duration.ofSeconds(60))
                .build();

        return S3Client.builder()
                .region(region)
                .credentialsProvider(creds)
                .httpClient(http)
                .overrideConfiguration(overrides)
                .build();
    }

    @Bean
    S3Presigner s3Presigner(Region region, AwsCredentialsProvider creds){
        return S3Presigner.builder()
                .region(region)
                .credentialsProvider(creds)
                .build();
    }
}
