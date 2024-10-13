package com.papazandreas.branch.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    // Consider making this dynamic or changing the cache name.
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("gitHubUserData");
    }
}
