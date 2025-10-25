package com.financeRadar.manticore.repository.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeRadar.manticore.dto.TransactionStats;
import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import com.financeRadar.manticore.dto.redis.TransactionalContextRedisDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RuleCacheRepository — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 21.10.2025
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RuleCacheRepository {

    @Value("${spring.data.redis.schema.rule.ttl-days}")
    private int ttlDay;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String RULE_KEY = "rule:";
    private static final String TX_CONTEXT = "tx_context:";

    public void saveRulesBatch(List<RuleRedisDto> rules) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (RuleRedisDto rule : rules) {
                String key = getFormattedKey(rule.id());
                redisTemplate.opsForValue().set(key, rule, Duration.ofDays(ttlDay));
            }
            return null;
        });
    }

    public void clearAllRules() {
        try {
            RedisScript<Long> script = getClearRedisScript();
            Long deletedCount = redisTemplate.execute(script, Collections.emptyList());

            log.info("Удалено {} правил из кэша", deletedCount);

        } catch (Exception e) {
            log.error("Ошибка в очистке кэша правил", e);
        }
    }

    public List<RuleRedisDto> findAllRules() {
        try {
            Set<String> keys = redisTemplate.keys(RULE_KEY + "*");
            if (keys == null || keys.isEmpty()) {
                return Collections.emptyList();
            }

            List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String key : keys) {
                    connection.get(Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(key)));
                }
                return null;
            });

            return results.stream()
                    .filter(Objects::nonNull)
                    .map(obj -> objectMapper.convertValue(obj, RuleRedisDto.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Ошибка получения правил из Redis", e);
            return Collections.emptyList();
        }
    }

    public TransactionStats getTransactionalContext(Long userId, Duration period) {
        return redisTemplate.execute(new RedisCallback<TransactionStats>() {
            @Override
            public TransactionStats doInRedis(RedisConnection connection) throws DataAccessException {
                String key = getFormattedTxKey(userId);
                long windowStart = System.currentTimeMillis() - period.toMillis();

                Long count = connection.zCount(key.getBytes(), windowStart, Double.MAX_VALUE);

                Set<byte[]> amounts = connection.zRangeByScore(key.getBytes(), windowStart, Double.MAX_VALUE);

                BigDecimal total = BigDecimal.ZERO;
                for (byte[] amountBytes : amounts) {
                    String amountStr = new String(amountBytes);
                    total = total.add(new BigDecimal(amountStr));
                }

                return TransactionStats.builder()
                        .countTransactional(count != null ? count : 0L)
                        .totalAmount(total)
                        .build();
            }
        });
    }

    private static RedisScript<Long> getClearRedisScript() {
        String luaScript = """
            -- Находим все ключи с префиксом rule:
            local keys = redis.call('KEYS', 'rule:*')
            
            -- Если ключи найдены - удаляем их
            if #keys > 0 then
                return redis.call('DEL', unpack(keys))
            else
                return 0
            end
            """;

        RedisScript<Long> script = RedisScript.of(luaScript, Long.class);
        return script;
    }

    private String getFormattedKey(Long id) {
        return RULE_KEY + id;
    }

    private List<RuleRedisDto> getEmptyList() {
        return Collections.emptyList();
    }

    private String getFormattedTxKey(Long userId) {
        return TX_CONTEXT + userId;
    }
}