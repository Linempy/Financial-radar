package com.financeRadar.manticore.service.policy;

import com.financeRadar.manticore.entity.rule.RiskDecision;
import com.financeRadar.manticore.entity.rule.RiskLevel;
import com.financeRadar.manticore.entity.rule.RuleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SimpleCountPolicy — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 23.10.2025
 */
@Slf4j
@Component
public class SimpleCountPolicy implements RiskPolicy{

    @Value("${risk.threshold}")
    private int threshold;

    @Override
    public RiskDecision evaluate(List<RuleResult> ruleResults) {
        long triggeredCount = ruleResults.stream()
                .filter(RuleResult::triggered)
                .count();

        boolean isFraud = triggeredCount >= threshold;

        return RiskDecision.builder()
                .isFraud(isFraud)
                .riskLevel(isFraud ? RiskLevel.HIGH : RiskLevel.LOW)
                .riskScore(isFraud ? 1.0 : 0.0)
                .reason(isFraud ?
                        "Мошенничество: сработало " + triggeredCount + " правил (требуется >= " + threshold + ")" :
                        "Нормально: сработало " + triggeredCount + " правил (требуется >= " + threshold + ")")
                .triggeredRules(ruleResults.stream()
                        .filter(RuleResult::triggered)
                        .map(RuleResult::ruleName)
                        .toList())
                .requiresReview(false)
                .build();
    }
}