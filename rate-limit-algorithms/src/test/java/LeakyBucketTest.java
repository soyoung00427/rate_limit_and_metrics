import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LeakyBucketTest {
    private final long MAX_CAPACITY = 5;
    private final long TWO_PER_PROCESS = 2;

    private LeakyBucket leakyBucket;

    @BeforeEach
    void setUp() {
        leakyBucket = new LeakyBucket(MAX_CAPACITY, TWO_PER_PROCESS);
    }

    @Test
    @DisplayName("버킷이 수용할 수 있는지 확인")
    void allow() {
        for(int i = 0; i < MAX_CAPACITY; i++) {
            assertTrue(leakyBucket.allow());
        }

        assertFalse(leakyBucket.allow());
    }
}
