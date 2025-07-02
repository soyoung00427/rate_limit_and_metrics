package com.icd.ratelimiter;

public enum Algorithm {
    FIXED_WINDOW,
    SLIDING_WINDOW,
    TOKEN_BUCKET,
    LEAKY_BUCKET
}