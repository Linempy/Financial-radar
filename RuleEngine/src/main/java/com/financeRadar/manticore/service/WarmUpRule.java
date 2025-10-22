package com.financeRadar.manticore.service;

import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import com.financeRadar.manticore.entity.Rule;
import com.financeRadar.manticore.mapper.RuleMapper;
import com.financeRadar.manticore.repository.RuleRepository;
import com.financeRadar.manticore.repository.redis.RuleCacheRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для разогрева кэша при помощи БД
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarmUpRule {

    private final RuleRepository ruleRepository;
    private final RuleCacheRepository ruleCacheRepository;
    private final RuleMapper mapper;

    @PostConstruct
    private void warmUpCache() {
        long startTime = System.currentTimeMillis();

        try {
            List<Rule> enabledRules = ruleRepository.findAllWithEnabled();
            if (enabledRules.isEmpty()) {
                log.warn("Не найдено активных правил для загрузки в кэш");
                return;
            }
            List<RuleRedisDto> ruleDtos = mapper.toRedisDtos(enabledRules);
            ruleCacheRepository.saveRulesBatch(ruleDtos);

            long durationMs = System.currentTimeMillis() - startTime;
            log.info("Загружено {} правил в Redis за {} мс",
                    enabledRules.size(), durationMs);
        } catch (Exception e) {
            log.error("Ошибка при разогреве кэша правил", e);
        }
    }
}