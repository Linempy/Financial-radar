package com.financeRadar.manticore.service.engine;

import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import com.financeRadar.manticore.service.refresh.RuleRefreshService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 *
 * @author Linempy
 * @since 23.10.2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RuleManager {

    private final RuleRefreshService refreshService;
    private final ApplicationContext applicationContext;

    private final AtomicReference<List<ExecutableRule>> rulesRef =
            new AtomicReference<>(Collections.emptyList());

    @Getter
    private volatile boolean initialized = false;

    @PostConstruct
    public void init() {
        refreshRules();
        initialized = true;
        log.info("RuleManager инициализировал {} правил", rulesRef.get().size());
    }

    public List<ExecutableRule> getRules() {
        if (!initialized) {
            log.warn("RuleManager еще не инициализирован, выполнение отложенной инициализации");
            refreshRules();
        }
        return new ArrayList<>(rulesRef.get());
    }

    @Async("RuleExecutor")
    public void refreshRules() {
        try {
            List<RuleRedisDto> ruleDtos = refreshService.getFromRedisOrDb();

            List<ExecutableRule> newRules = ruleDtos.stream()
                    .sorted(Comparator.comparing(RuleRedisDto::priority))
                    .map(this::createExecutableRule)
                    .filter(Objects::nonNull)
                    .toList();

            rulesRef.set(newRules);

            log.info("Правила обновлены: {} активных правил", newRules.size());

        } catch (Exception e) {
            log.error("Ошибка обновления правил", e);
        }
    }

    private ExecutableRule createExecutableRule(RuleRedisDto ruleDto) {
        try {
            return applicationContext.getBean(SpELRule.class, ruleDto);
        } catch (Exception e) {
            log.error("Не удалось создать бин правила: {}", ruleDto.id(), e);
            return null;
        }
    }

    public int getRulesCount() {
        return rulesRef.get().size();
    }
}