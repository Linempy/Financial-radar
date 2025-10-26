package com.financeRadar.manticore.repository.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeRadar.manticore.dto.TransactionStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * TransactionalRedisRepository — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 18.10.2025
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TransactionRedisRepository {

    @Value("${spring.data.redis.schema.idempotency-key.ttl-min}")
    private int defaultTtlMin;

    private static final String IDEMPOTENCY = "idempotency:";
    private static final String TX_CONTEXT = "tx_context:";

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean addIfAbsent(String idempotencyKey) {
        String key = getFormattedIdempotencyKey(idempotencyKey);

        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(key, "processed", Duration.ofMinutes(defaultTtlMin));
        return Boolean.TRUE.equals(result);
    }

    public TransactionStats recordTransaction(Long userId, BigDecimal amount) {
        String key = getFormattedTxKey(userId);
        try {
            redisTemplate.opsForZSet().add(key, amount.doubleValue(), System.currentTimeMillis());
            redisTemplate.expire(key, Duration.ofSeconds(getSecondsFromMin(defaultTtlMin)));

            Long count = redisTemplate.opsForZSet().size(key);
            return TransactionStats.builder()
                    .countTransactional(count != null ? count : 0L)
                    .totalAmount(amount)
                    .build();
        } catch (Exception e) {
            log.error("Ошибка записи транзакции для пользователя: {}", userId, e);
            return TransactionStats.empty();
        }
    }

    public TransactionStats getTransactionalContext(Long userId,  Duration period) {
        String key = getFormattedTxKey(userId);
        long windowStart = System.currentTimeMillis() - period.toMillis();

        List<String> keys = Collections.singletonList(key);
        Object[] args = { String.valueOf(windowStart) };

        try {
            List<Long> results = redisTemplate.execute(loadGetStatsScript(), keys, args);
            if (results != null && results.size() == 2) {
                return TransactionStats.builder()
                        .countTransactional(results.get(0))
                        .totalAmount(BigDecimal.valueOf(results.get(1)))
                        .build();
            }
            return TransactionStats.empty();
        } catch (Exception e) {
            log.error("Ошибка получения статистики для пользователя {}: {}", userId, e.getMessage());
            return TransactionStats.empty();
        }
    }

    private int getSecondsFromMin(int ttlMin) {
        return ttlMin * 60;
    }


    private RedisScript<List> loadGetStatsScript() {
        String luaScript = """
                local key = KEYS[1]
                local windowStart = tonumber(ARGV[1])
            
                local count = redis.call('ZCOUNT', key, windowStart, '+inf')
                
                if count == 0 then
                    return {0, 0}
                end
            
                local amounts = redis.call('ZRANGEBYSCORE', key, windowStart, '+inf')
                local total = 0
            
                for i, amount_str in ipairs(amounts) do
                    local amount = tonumber(amount_str)
                    total = total + amount
                end
            
                return {count, total}
                """;
        return RedisScript.of(luaScript, List.class);
    }

    private String getFormattedIdempotencyKey(String id) {
        return IDEMPOTENCY + id;
    }

    private String getFormattedTxKey(Long userId) {
        return TX_CONTEXT + userId;
    }

}