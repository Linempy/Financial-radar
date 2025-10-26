package com.financeRadar.manticore.entity;

import com.financeRadar.manticore.entity.rule.RiskDecision;
import com.financeRadar.manticore.entity.rule.RuleResult;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

/**
 * TransactionRiskResult — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 23.10.2025
 */
@Builder
public record TransactionRiskResult(
    Long transactionId,
    String correlationId,
    RiskDecision riskDecision,
    List<RuleResult> ruleResults,
    Long processingTimeMs,
    Instant evaluatedAt
) {
}

