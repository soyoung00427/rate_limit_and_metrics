import java.util.concurrent.atomic.AtomicInteger;

public class LeakyBucket {
    public static final int STANDARD_PER_MILLISECOND = 1_000;

    private final long capacity;
    private final long leakRate;
    private AtomicInteger currentBucketAmount;
    private long lastLeakTimestamp;

    public LeakyBucket(long capacity, long leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.currentBucketAmount = new AtomicInteger(0);
        this.lastLeakTimestamp = System.currentTimeMillis();
    }

    public synchronized boolean allow() {
        leak();

        if(currentBucketAmount.get() < capacity) {
            currentBucketAmount.incrementAndGet();
            return true;
        }

        return false;
    }

    private void leak() {
        final long now = System.currentTimeMillis();
        final long elapsed = now - lastLeakTimestamp;

        if(now > lastLeakTimestamp) {
            // 경과 시간 동안 누수된 양 계산
            int leakedAmount = (int) ((elapsed / STANDARD_PER_MILLISECOND) * leakRate);

            // 현재 버킷 레벨에서 누수된 양을 뺍니다.
            int remainingAmount = Math.max(0, currentBucketAmount.get() - leakedAmount);

            currentBucketAmount.set(remainingAmount);
            lastLeakTimestamp = now;
        }
    }
}
