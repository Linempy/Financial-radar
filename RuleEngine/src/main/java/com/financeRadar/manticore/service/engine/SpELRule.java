package com.financeRadar.manticore.service.engine;


import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import com.financeRadar.manticore.entity.RuleResult;
import com.financeRadar.manticore.entity.RuleType;
import com.financeRadar.manticore.utils.TimeUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * SpELRule — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 20.10.2025
 */
@Slf4j
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class SpELRule implements ExecutableRule {

    private final RuleRedisDto rule;
    private Expression compiledExpression;
    private StandardEvaluationContext evaluationContext;

    @PostConstruct
    public void init() {
        this.compiledExpression = new SpelExpressionParser()
                .parseExpression(rule.expression());
        this.evaluationContext = createEvaluationContext();
        log.debug("Инициализация правила: {} - {}", rule.id(), rule.name());
    }
    
    private StandardEvaluationContext createEvaluationContext() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("isNight", (Function<LocalDateTime, Boolean>) TimeUtils::isNight);
        context.setVariable("isWeekend", (Function<LocalDateTime, Boolean>) TimeUtils::isWeekend);
        return context;
    }

    @Override
    public RuleResult evaluate(TransactionRiskCheckEvent transaction) {
        //TODO ЛОГИИИ
        log.info("rule@{}. name: {}; priority: {}, rule type: {}",
                rule.version(), rule.name(), rule.priority(), rule.ruleType()
        );
        long startTime = System.currentTimeMillis();
        try {
            evaluationContext.setVariable("tx", transaction);
            Boolean result = compiledExpression.getValue(evaluationContext, Boolean.class);
            boolean triggered = Boolean.TRUE.equals(result);

            return RuleResult.builder()
                    .ruleId(rule.id().toString())
                    .triggered(triggered)
                    .executionTimeMs(System.currentTimeMillis() - startTime)
                    .build();
        } catch (Exception e) {
            return RuleResult.builder()
                    .ruleId(rule.id().toString())
                    .triggered(false)
                    .errorMessage(e.getMessage())
                    .executionTimeMs(System.currentTimeMillis() - startTime)
                    .build();
        }
    }

    @Override
    public Long getId() {
        return rule.id();
    }

    @Override
    public RuleType getType() {
        return rule.ruleType();
    }

    @Override
    public Integer getPriority() {
        return rule.priority();
    }
}