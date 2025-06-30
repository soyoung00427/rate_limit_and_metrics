import java.util.concurrent.atomic.AtomicInteger;

public class LeakyBucket {
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
        if(currentBucketAmount.get() < capacity) {
            currentBucketAmount.incrementAndGet();
            return true;
        }

        return false;
    }
}
