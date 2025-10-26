package com.financeRadar.manticore.service.idempotencyKey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeRadar.manticore.entity.model.IdempotencyRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

/**
 * IdempotencyKeyService — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 25.10.2025
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IdempotencyService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration KEY_TTL = Duration.ofMinutes(3);
    private final ObjectMapper objectMapper;


    public void markAsFailed(String idempotencyKey) {
        redisTemplate.delete("idempotency:" + idempotencyKey);
    }

    public Optional<Object> getResult(String idempotencyKey) {
        String redisKey = "idempotency:" + idempotencyKey;
        try {
            Object data = redisTemplate.opsForValue().get(redisKey);
            if (data instanceof String json) {
                IdempotencyRecord record = objectMapper.readValue(json, IdempotencyRecord.class);
                if (record.status() == IdempotencyRecord.IdempotencyStatus.COMPLETED) {
                    return Optional.ofNullable(record.response());
                }
            }
        } catch (Exception e) {
            log.warn("Ошибка считывания idempotency key", e);
        }
        return Optional.empty();
    }
}