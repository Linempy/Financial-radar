package com.financeRadar.manticore.repository.redis;

import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

/**
 * RuleCacheRepository — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 21.10.2025
 */
@Repository
@RequiredArgsConstructor
public class RuleCacheRepository {

    @Value("${spring.data.redis.schema.rule.ttl-days}")
    private int ttlDay;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String RULE_KEY = "rule:";

    public void saveRulesBatch(List<RuleRedisDto> rules) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (RuleRedisDto rule : rules) {
                String key = getFormattedKey(rule.id());
                redisTemplate.opsForValue().set(key, rule, Duration.ofDays(ttlDay));
            }
            return null;
        });
    }

    private String getFormattedKey(Long id) {
        return RULE_KEY + id;
    }

}