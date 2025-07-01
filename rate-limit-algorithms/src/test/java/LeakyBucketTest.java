import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LeakyBucketTest {
    private final int MAX_CAPACITY = 5;
    private final int TWO_PER_SECOND = 2;

    private LeakyBucket leakyBucket;

    @BeforeEach
    void setUp() {
        leakyBucket = new LeakyBucket(MAX_CAPACITY, TWO_PER_SECOND);
    }

    @Test
    @DisplayName("버킷 수용 안이면 true 넘치면 false")
    void allow() {
        for(int i = 0; i < MAX_CAPACITY; i++) {
            assertTrue(leakyBucket.allow());
        }

        assertFalse(leakyBucket.allow());
    }

    @Test
    @DisplayName("버킷의 누수 처리양 확인")
    void leak() throws InterruptedException {
        for(int i = 0; i < MAX_CAPACITY; i++) {
            assertTrue(leakyBucket.allow());
        }

        Thread.sleep(LeakyBucket.STANDARD_PER_MILLISECOND);

        for(int i = 0; i < TWO_PER_SECOND; i++) {
            assertTrue(leakyBucket.allow());
        }

        assertFalse(leakyBucket.allow());
    }
}
