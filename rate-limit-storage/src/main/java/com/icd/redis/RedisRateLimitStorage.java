package com.icd.redis;

import com.icd.ObjectMapper;
import com.icd.ratelimiter.api.RedisCommands;
import com.icd.ratelimiter.api.StatefulRedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;

public class RedisRateLimitStorage<T> implements TempRateLimitStorage<T> {
    private static final Logger logger = LoggerFactory.getLogger(RedisRateLimitStorage.class);

    private final StatefulRedisConnection<String, String> connection;
    private final ObjectMapper objectMapper;

    public RedisRateLimitStorage(StatefulRedisConnection<String, String> connection) {
        this.connection = connection;
        this.objectMapper = new ObjectMapper();
    }

    public RedisRateLimitStorage(StatefulRedisConnection<String, String> connection, ObjectMapper objectMapper) {
        this.connection = connection;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveState(String key, T state, Duration ttl) {
        try {
            RedisCommands<String, String> commands = connection.sync();
            String jsonState = objectMapper.writeValueAsString(state);
            commands.setex(key, ttl.toSeconds(), jsonState);

            logger.debug("{} : 직렬화된 상태 저장 성공(TTL: {}s)", key, ttl.toSeconds());
        } catch (Exception e) {
            logger.error("상태 저장 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Redis에 상태를 저장할 수 없습니다.", e);
        }
    }

    @Override
    public Optional<T> getState(String key, Class<T> stateType) {
        RedisCommands<String, String> commands = connection.sync();
        String jsonState = commands.get(key);

        if (jsonState == null || jsonState.isEmpty()) {
            logger.warn("{} 키에 해당하는 상태가 없습니다.", key);
            return Optional.empty();
        }

        logger.debug("{} : 직렬화된 상태 조회 성공.", key);
        T state = objectMapper.readValue(jsonState, stateType);
        return Optional.of(state);
    }
}
