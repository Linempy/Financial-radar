package com.financeRadar.manticore.entity;

/**
 * Сущность представляющая собой результат применения правил {@link Rule} к транзакции
 *
 * @author Linempy
 * @since 19.10.2025
 */
public record RuleResult(
    boolean triggered,
    String ruleId,
    String ruleName,
    String reason,
    Long executionTimeMs
) {
}