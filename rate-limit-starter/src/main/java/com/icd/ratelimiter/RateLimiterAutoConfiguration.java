package com.icd.ratelimiter;

import com.icd.TokenBucketRateLimiter;
import com.icd.ratelimiter.storage.RateLimiterStorage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RateLimiterProperties.class)
public class RateLimiterAutoConfiguration {

    @Bean
    public TokenBucketRateLimiter rateLimiter(RateLimiterProperties properties, RateLimiterStorage storage) {
        return new TokenBucketRateLimiter(
                properties.getCapacity(),
                properties.getRefillRate(),
                storage
        );
    }
}
