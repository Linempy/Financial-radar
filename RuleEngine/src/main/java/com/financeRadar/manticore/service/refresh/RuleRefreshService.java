package com.financeRadar.manticore.service.refresh;

import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import com.financeRadar.manticore.entity.Rule;
import com.financeRadar.manticore.mapper.RuleMapper;
import com.financeRadar.manticore.repository.RuleRepository;
import com.financeRadar.manticore.repository.redis.RuleCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * RuleRefreshService — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 25.10.2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RuleRefreshService {

    private final RuleCacheRepository ruleCacheRepository;
    private final RuleRepository ruleRepository;
    private final RuleMapper mapper;

    public List<RuleRedisDto> getFromRedisOrDb() {
        List<RuleRedisDto> rulesFromRedis = ruleCacheRepository.findAllRules();
        if (rulesFromRedis != null) {
            return rulesFromRedis;
        }

        List<Rule> ruleFromDb = ruleRepository.findAllByEnabledTrue();
        List<RuleRedisDto> rulesAfterMapper = mapper.toRedisDtos(ruleFromDb);
        ruleCacheRepository.saveRulesBatch(rulesAfterMapper);
        return rulesAfterMapper;
    }

    @Async("RuleExecutor")
    public void refreshAllRulesAsync() {
        try {
            log.info("Начало обновления правил в кэше...");

            List<Rule> dbRules = ruleRepository.findAllByEnabledTrue();
            List<RuleRedisDto> redisDtos = mapper.toRedisDtos(dbRules);

            ruleCacheRepository.clearAllRules();
            ruleCacheRepository.saveRulesBatch(redisDtos);

            log.info("Правила успешно обновлены: {} шт", redisDtos.size());
        } catch (Exception e) {
            log.error("Ошибка в обновлении правил в кэше", e);
        }
    }
}