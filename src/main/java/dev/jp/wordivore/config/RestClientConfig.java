package dev.jp.wordivore.config;

import dev.jp.wordivore.interceptor.ClientLoggerRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final ClientLoggerRequestInterceptor interceptor;

    @Bean
    public RestClient openLibraryRestClient(RestClient.Builder builder) {

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();


        return RestClient.builder()
                .baseUrl("https://openlibrary.org")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .defaultHeader(HttpHeaders.USER_AGENT, "wordivore/1.0 (jpaul.jayme@gmail.com)")
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                .requestInterceptor(interceptor)
                .build();
    }

    @Bean
    public RestClient wordnikRestClient(RestClient.Builder builder){
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        return RestClient.builder()
                .baseUrl("https://api.wordnik.com/v4")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .defaultHeader(HttpHeaders.USER_AGENT, "wordivore/1.0 (jpaul.jayme@gmail.com)")
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                .build();
    }
}
