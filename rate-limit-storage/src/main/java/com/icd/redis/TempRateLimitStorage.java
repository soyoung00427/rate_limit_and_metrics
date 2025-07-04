package com.icd.redis;

import java.time.Duration;
import java.util.Optional;

public interface TempRateLimitStorage<T> {
    /**
     * 지정된 키에 대한 상태를 저장합니다.
     *
     * @param key    요청을 식별하는 고유 키 (예: 사용자 ID, IP 주소)
     * @param state  저장할 상태 객체
     * @param ttl    데이터 만료 시간
     */
    void saveState(String key, T state, Duration ttl);

    /**
     * 지정된 키에 해당하는 상태를 불러옵니다.
     *
     * @param key       요청을 식별하는 고유 키
     * @param stateType 반환될 상태 객체의 클래스 타입
     * @return 상태 객체 (Optional로 래핑), 키가 없으면 Optional.empty() 반환
     */
    Optional<T> getState(String key, Class<T> stateType);
}
