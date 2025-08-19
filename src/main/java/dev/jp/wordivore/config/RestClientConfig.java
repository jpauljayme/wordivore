package dev.jp.wordivore.config;

import dev.jp.wordivore.interceptor.ClientLoggerRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient openLibraryRestClient(){
        return RestClient.builder()
                .baseUrl("https://openlibrary.org")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("User-Agent", "emancipate-the-self (jpaul.jayme@gmail.com)")
                .requestInterceptor(new ClientLoggerRequestInterceptor())
                .build();
    }

}
