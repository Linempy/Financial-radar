package com.financeRadar.manticore.repository.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    public void saveRulesBatch(List<RuleRedisDto> rules) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (RuleRedisDto rule : rules) {
                String key = getFormattedKey(rule.id());
                redisTemplate.opsForValue().set(key, rule, Duration.ofDays(ttlDay));
            }
            return null;
        });
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

    private String getFormattedKey(Long id) {
        return RULE_KEY + id;
    }

    private List<RuleRedisDto> getEmptyList() {
        return Collections.emptyList();
    }

}