package com.financeRadar.manticore.service.engine;


import com.financeRadar.manticore.dto.avro.TransactionalRiskCheckEvent;
import com.financeRadar.manticore.entity.Rule;
import com.financeRadar.manticore.entity.RuleResult;
import com.financeRadar.manticore.entity.RuleType;
import com.financeRadar.manticore.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class SpELRule implements ExecutableRule {

    private final Rule rule;
    private final Expression compiledExpression;
    private final StandardEvaluationContext evaluationContext;

    public SpELRule(Rule rule) {
        this.rule = rule;
        this.compiledExpression = new SpelExpressionParser().parseExpression(rule.getExpression());
        this.evaluationContext = createEvaluationContext();
    }

    private StandardEvaluationContext createEvaluationContext() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("isNight", (Function<LocalDateTime, Boolean>) TimeUtils::isNight);
        context.setVariable("isWeekend", (Function<LocalDateTime, Boolean>) TimeUtils::isWeekend);
        context.setVariable("isHoliday", (Function<LocalDateTime, Boolean>) TimeUtils::isHoliday);
        return context;
    }

    @Override
    public RuleResult evaluate(TransactionalRiskCheckEvent transaction) {
        long startTime = System.currentTimeMillis();
        try {
            evaluationContext.setVariable("tx", transaction);
            Boolean result = compiledExpression.getValue(evaluationContext, Boolean.class);
            boolean triggered = Boolean.TRUE.equals(result);

            return RuleResult.builder()
                    .ruleId(rule.getId().toString())
                    .triggered(triggered)
                    .executionTimeMs(System.currentTimeMillis() - startTime)
                    .build();
        } catch (Exception e) {
            return RuleResult.builder()
                    .ruleId(rule.getId().toString())
                    .triggered(false)
                    .errorMessage(e.getMessage())
                    .executionTimeMs(System.currentTimeMillis() - startTime)
                    .build();
        }
    }

    @Override
    public Long getId() {
        return rule.getId();
    }

    @Override
    public RuleType getType() {
        return rule.getRuleType();
    }

    @Override
    public Integer getPriority() {
        return rule.getPriority();
    }
}