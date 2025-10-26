package com.financeRadar.manticore.service.transaction.idempotencyKey;

import com.financeRadar.manticore.entity.model.IdempotencyStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

    private static final String KEY_PREFIX = "idempotency:";
    private final RedisTemplate<String, Object> redisTemplate;


    public void markCompletedSuccess(String idempotencyKey) {
        markCompleted(idempotencyKey, IdempotencyStatus.COMPLETED);
    }

    public void markCompletedError(String idempotencyKey) {
        markCompleted(idempotencyKey, IdempotencyStatus.FAILED);
    }


    private void markCompleted(String idempotencyKey, IdempotencyStatus status) {
        String key = KEY_PREFIX + idempotencyKey;
        try {
            redisTemplate.delete(key);
            log.debug("Ключ {} отмечен как {} в ответе. Ключ освобожден", idempotencyKey, status);
        } catch (Exception e) {
            log.error("Ошибка пометкой ключа {} как завершенного: {}", idempotencyKey, e.getMessage());
            throw new RuntimeException("Не удалось пометить ключ как выполненный", e);
        }
    }
}