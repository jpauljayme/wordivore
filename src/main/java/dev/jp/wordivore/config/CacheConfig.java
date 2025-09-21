package dev.jp.wordivore.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){

        var cm = new CaffeineCacheManager(
            "shelf", "shelfSections", "ol:isbn"
        );

        cm.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(10000)
                        .expireAfterWrite(Duration.ofMinutes(2))
        );

        return cm;

    }
}
