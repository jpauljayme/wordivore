package dev.jp.wordivore.config;

import dev.jp.wordivore.interceptor.ClientLoggerRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient openLibraryRestClient(RestClient.Builder builder){

        var httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        return RestClient.builder()
                .baseUrl("https://openlibrary.org")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .defaultHeader(HttpHeaders.USER_AGENT, "wordivore (jpaul.jayme@gmail.com)")
//                .requestInterceptor(new ClientLoggerRequestInterceptor())
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                .build();

    }

}
