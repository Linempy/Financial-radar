package com.financeRadar.manticore.service.idempotencyKey;

import com.financeRadar.manticore.entity.model.IdempotencyResult;
import com.financeRadar.manticore.entity.model.IdempotencyStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Сервис с реализацией основный методов работы с защитой от дубликатов транзакций (запросов)
 *
 * @author Linempy
 * @since 25.10.2025
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IdempotencyService {

    @Value("${idempotency-key.ttl-min:3}")
    private int keyTtl;
    private static final String KEY_PREFIX = "idempotency:";
    private final RedisTemplate<String, Object> redisTemplate;

    public IdempotencyResult checkAndReserveKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return IdempotencyResult.available();
        }

        String key = KEY_PREFIX + idempotencyKey;

        try {
            Object statusObj = redisTemplate.opsForValue().get(key);
            IdempotencyStatus status = convertToIdempotencyStatus(statusObj);

            if (status == IdempotencyStatus.PROCESSING) {
                log.info("Ключ {} уже обрабатывается", idempotencyKey);
                return IdempotencyResult.processing();

            } else if (status == IdempotencyStatus.COMPLETED ||
                    status == IdempotencyStatus.FAILED) {
                String cachedResponse = getCachedResponse(idempotencyKey);
                log.debug("Ключ {} обрабатывается, возвращается кэш ответа", idempotencyKey);
                return status == IdempotencyStatus.COMPLETED ?
                        IdempotencyResult.completed(cachedResponse) :
                        IdempotencyResult.completedError(cachedResponse);

            } else {
                reserveKey(idempotencyKey);
                log.info("Ключ {} зарезервирован", idempotencyKey);
                return IdempotencyResult.available();
            }

        } catch (Exception e) {
            log.error("Error checking idempotency key: {}", idempotencyKey, e);
            return IdempotencyResult.available();
        }
    }

    public void markFailed(String idempotencyKey) {
        try {
            String key = KEY_PREFIX + idempotencyKey;

            redisTemplate.delete(key);

            log.debug("Ключ {} удален из-за ошибки", idempotencyKey);
        } catch (Exception e) {
            log.error("Ошибка удаления ключа {}: {}", idempotencyKey, e.getMessage());
        }
    }

    public String getCachedResponse(String idempotencyKey) {
        try {
            String responseKey = KEY_PREFIX + idempotencyKey + ":response";
            String response = (String) redisTemplate.opsForValue().get(responseKey);
            return response != null ? response :
                    "{\"status\":\"completed\",\"message\":\"Response not cached\"}";
        } catch (Exception e) {
            log.error("Ошибка получения кэша ответа для ключа: {}", idempotencyKey, e);
            return "{\"status\":\"error\",\"message\":\"Failed to get cached response\"}";
        }
    }

    public void reserveKey(String idempotencyKey) {
        try {
            String key = KEY_PREFIX + idempotencyKey;
            redisTemplate.opsForValue().set(key, IdempotencyStatus.PROCESSING, Duration.ofMinutes(keyTtl));
            log.debug("Ключ {} отмечен как PROCESSING", idempotencyKey);
        } catch (Exception e) {
            log.error("Ошибка резервирования ключа: {}", idempotencyKey, e);
            throw new RuntimeException("Ошибка резервирования Idempotency key", e);
        }
    }

    public void markCompletedSuccess(String idempotencyKey) {
        markCompleted(idempotencyKey, IdempotencyStatus.COMPLETED);
    }

    public void markCompletedError(String idempotencyKey) {
        markCompleted(idempotencyKey, IdempotencyStatus.FAILED);
    }

    public IdempotencyStatus getKeyStatus(String idempotencyKey) {
        try {
            String key = KEY_PREFIX + idempotencyKey;
            return (IdempotencyStatus) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Ошибка получения статуса ключа: {}", idempotencyKey, e);
            return null;
        }
    }

    private void markCompleted(String idempotencyKey, IdempotencyStatus status) {
        try {
            String key = KEY_PREFIX + idempotencyKey;

            redisTemplate.opsForValue().set(key, status, Duration.ofMinutes(keyTtl));

            log.debug("Ключ {} отмечен как {} в ответе", idempotencyKey, status);
        } catch (Exception e) {
            log.error("Ошибка пометкой ключа {} как завершенного: {}", idempotencyKey, e.getMessage());
            throw new RuntimeException("Не удалось пометить ключ как выполненный", e);
        }
    }

    private IdempotencyStatus convertToIdempotencyStatus(Object statusObj) {
        if (statusObj == null) {
            return null;
        }

        try {
            if (statusObj instanceof IdempotencyStatus) {
                return (IdempotencyStatus) statusObj;
            } else if (statusObj instanceof String) {
                return IdempotencyStatus.valueOf((String) statusObj);
            } else {
                log.warn("Неизвестный объект статуса: {}", statusObj.getClass());
                return null;
            }
        } catch (Exception e) {
            log.warn("Ошибка конвертации статуса: {}", statusObj, e);
            return null;
        }
    }
}