package com.financeRadar.manticore.dto.redis;

import com.financeRadar.manticore.entity.RuleType;

/**
 * RuleRedisDto — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 21.10.2025
 */
public record RuleRedisDto(
    Long id,
    String name,
    RuleType ruleType,
    String expression,
    Integer priority,
    Long version
) {
}