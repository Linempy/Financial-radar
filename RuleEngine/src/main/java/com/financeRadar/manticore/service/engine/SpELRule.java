package com.financeRadar.manticore.service.engine;


import com.financeRadar.manticore.dto.TransactionalEventWrapper;
import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import com.financeRadar.manticore.entity.RuleResult;
import com.financeRadar.manticore.entity.RuleType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

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


    @PostConstruct
    public void init() {
        try {
            this.compiledExpression = new SpelExpressionParser()
                    .parseExpression(rule.expression());
            log.debug("Инициализация правила: {} - {}", rule.id(), rule.name());
        } catch (Exception e) {
            log.error("Ошибка парсинга выражения правила {}: '{}'", rule.id(), rule.expression(), e);
            throw new RuntimeException("Invalid rule expression", e);
        }
    }

    @Override
    public RuleResult evaluate(TransactionalEventWrapper event) {
        //TODO ЛОГИИИ
        long startTime = System.currentTimeMillis();

        try {
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext(event);

            Boolean result = compiledExpression.getValue(evaluationContext, Boolean.class);
            boolean triggered = Boolean.TRUE.equals(result);

            long durationMs = System.currentTimeMillis() - startTime;
            //TODO ЛОГИИИ
            log.info("rule@{}. name: {}; priority: {}, rule type: {}, durationMs: {}",
                    rule.version(), rule.name(), rule.priority(), rule.ruleType(), durationMs
            );
            return RuleResult.builder()
                    .ruleId(rule.id().toString())
                    .ruleName(rule.name())
                    .triggered(triggered)
                    .executionTimeMs(durationMs)
                    .build();
        } catch (Exception e) {
            //TODO логии
            log.warn("Ошибка выполнения правила {} '{}': {}",
                    rule.id(), rule.expression(), e.getMessage());
            return RuleResult.builder()
                    .ruleId(rule.id().toString())
                    .triggered(false)
                    .ruleName(rule.name())
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