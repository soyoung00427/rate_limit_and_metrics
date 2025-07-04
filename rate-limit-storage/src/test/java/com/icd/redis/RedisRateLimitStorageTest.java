package com.icd.redis;

import com.icd.ObjectMapper;
import com.icd.ratelimiter.api.RedisCommands;
import com.icd.ratelimiter.api.StatefulRedisConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisRateLimitStorageTest {
    @Mock
    private StatefulRedisConnection<String, String> connection;

    @Mock // connection.sync()가 반환할 가짜 객체입니다.
    private RedisCommands<String, String> commands;

    @InjectMocks // @Mock으로 선언된 객체들을 자동으로 주입하여 테스트 대상 인스턴스를 생성합니다.
    private RedisRateLimitStorage<TempAlgorithmsState> redisStorage;

    // 테스트에서 JSON 직렬화를 위해 사용할 객체
    private final ObjectMapper objectMapper = new ObjectMapper();


    public class TempAlgorithmsState {
        private final long capacity;
        private final long leakRate;
        private AtomicInteger currentBucketAmount;
        private long lastLeakTimestamp;

        public TempAlgorithmsState(long capacity, long leakRate) {
            this.capacity = capacity;
            this.leakRate = leakRate;
            this.currentBucketAmount = new AtomicInteger(0);
            this.lastLeakTimestamp = System.currentTimeMillis();
        }

        public long getCapacity() {
            return capacity;
        }

        public long getLeakRate() {
            return leakRate;
        }

    }

    @BeforeEach
    void setUp() {
        // 핵심 설정: connection.sync()가 호출되면, 우리가 만든 가짜 commands 객체를 반환하도록 설정합니다.
        // 이 설정을 통해 redisStorage 내부에서 connection.sync()를 호출할 때 NullPointerException이 발생하지 않습니다.
        when(connection.sync()).thenReturn(commands);
    }

    @Test
    @DisplayName("키가 존재할 때, 역직렬화된 상태 객체를 반환해야 한다")
    void getState_shouldReturnDeserializedState_whenKeyExists() throws IOException {
        // given: Redis에 저장되어 있을 것으로 예상되는 키와 JSON 데이터
        String key = "existing-key";
        TempAlgorithmsState expectedState = new TempAlgorithmsState(10, 4);
        String jsonState = objectMapper.writeValueAsString(expectedState);

        // Mockito 설정: commands.get(key)가 호출되면, 미리 준비된 jsonState를 반환하도록 정의합니다.
        when(commands.get(key)).thenReturn(jsonState);

        // when: 테스트 대상 메소드를 호출합니다.
        Optional<TempAlgorithmsState> actualState = redisStorage.getState(key, TempAlgorithmsState.class);

        // then: 반환된 Optional 객체는 기대했던 상태 값을 포함해야 합니다.
        assertThat(actualState).isPresent();
        assertThat(actualState.get().getCapacity()).isEqualTo(expectedState.getCapacity());
        assertThat(actualState.get().getLeakRate()).isEqualTo(expectedState.getLeakRate());
    }

    @Test
    @DisplayName("키가 존재하지 않을 때, Optional.empty를 반환해야 한다")
    void getState_shouldReturnEmpty_whenKeyDoesNotExist() {
        // given: 존재하지 않는 키
        String key = "non-existent-key";

        // Mockito 설정: commands.get(key)가 호출되면 null을 반환하도록 정의합니다.
        when(commands.get(key)).thenReturn(null);

        // when: 테스트 대상 메소드를 호출합니다.
        Optional<TempAlgorithmsState> actualState = redisStorage.getState(key, TempAlgorithmsState.class);

        // then: 결과는 비어있어야 합니다.
        assertThat(actualState).isEmpty();
    }

    @Test
    @DisplayName("상태 저장 시, 올바른 파라미터로 setex 명령어를 호출해야 한다")
    void saveState_shouldCallSetexWithCorrectParameters() throws IOException {
        // given: 저장할 키, 상태, TTL
        String key = "save-key";
        Duration ttl = Duration.ofSeconds(60);
        TempAlgorithmsState stateToSave = new TempAlgorithmsState(15, 2);
        String expectedJsonState = objectMapper.writeValueAsString(stateToSave);

        // when: 테스트 대상 메소드를 호출합니다.
        redisStorage.saveState(key, stateToSave, ttl);

        // then: commands.setex 메소드가 정확히 1번, 기대했던 파라미터들로 호출되었는지 검증합니다.
        verify(commands, times(1)).setex(key, ttl.toSeconds(), expectedJsonState);
    }
}
