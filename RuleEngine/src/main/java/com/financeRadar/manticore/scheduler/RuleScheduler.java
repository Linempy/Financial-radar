package com.financeRadar.manticore.scheduler;

import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import com.financeRadar.manticore.entity.rule.Rule;
import com.financeRadar.manticore.mapper.RuleMapper;
import com.financeRadar.manticore.repository.RuleRepository;
import com.financeRadar.manticore.repository.redis.RuleCacheRepository;
import com.financeRadar.manticore.service.engine.RuleManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Scheduler для загрузки изменений правил из БД в кэш
 *
 * @author Linempy
 * @since 22.10.2025
 */
@Component
@RequiredArgsConstructor
public class RuleScheduler {

    private final RuleCacheRepository ruleCacheRepository;
    private final RuleRepository ruleRepository;
    private final RuleManager ruleManager;
    private final RuleMapper mapper;

    private static LocalDateTime scheduleTime = LocalDateTime.now();

    @Scheduled(cron = "${scheduling.redis.rules.reload}")
    public void reloadCacheRule() {
        Optional<List<Rule>> updatedRules = ruleRepository.findAllByUpdatedAtAfterAndEnabledTrue(scheduleTime);

        updatedRules.ifPresent(rules -> {
            List<RuleRedisDto> rulesDto = mapper.toRedisDtos(rules);
            ruleCacheRepository.saveRulesBatch(rulesDto);
            ruleManager.refreshRules();
        });

        scheduleTime = LocalDateTime.now();
    }
}