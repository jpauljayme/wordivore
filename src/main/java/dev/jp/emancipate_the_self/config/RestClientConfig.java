package dev.jp.emancipate_the_self.config;

import dev.jp.emancipate_the_self.interceptor.ClientLoggerRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient openLibraryRestClient(){
        return RestClient.builder()
                .baseUrl("https://openlibrary.org")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("User-Agent", "emancipate-the-self")
                .requestInterceptor(new ClientLoggerRequestInterceptor())
                .build();
    }

}
