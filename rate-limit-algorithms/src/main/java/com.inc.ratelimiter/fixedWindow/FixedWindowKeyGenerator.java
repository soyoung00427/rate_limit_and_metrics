package com.inc.ratelimiter.fixedWindow;

import java.time.Duration;
import java.time.Instant;

/**
 * 고정 윈도우 정렬 도구 (분 단위, 초 단위 등)
 */
public class FixedWindowKeyGenerator {

    public static Instant alignToWindow(Instant timestamp, Duration window) {
        long seconds = timestamp.getEpochSecond();
        long windowSeconds = window.getSeconds();
        long aligned = (seconds / windowSeconds) * windowSeconds;
        return Instant.ofEpochSecond(aligned);
    }
}
