package com.financeRadar.manticore.entity.rule;

import lombok.Builder;

/**
 * Сущность представляющая собой результат применения правил {@link Rule} к транзакции
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Builder
public record RuleResult(
    boolean triggered,
    String ruleId,
    String ruleName,
    String reason,
    String errorMessage,
    Long executionTimeMs
) {
}