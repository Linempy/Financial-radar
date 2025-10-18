package com.financeRadar.manticore.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

/**
 * TransactionalRedisRepository — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 18.10.2025
 */
@Repository
@RequiredArgsConstructor
public class TransactionalRedisRepository {

    @Value("${spring.data.redis.schema.idempotency-key.ttl-min}")
    private int ttlMin;

    private static final String KEY = "idempotency:";

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean addIfAbsent(String idempotencyKey) {
        String key = KEY + idempotencyKey;

        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "processed", Duration.ofMinutes(ttlMin));
        return Boolean.TRUE.equals(result);
    }

}