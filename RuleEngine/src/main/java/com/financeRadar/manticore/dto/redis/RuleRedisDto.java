package com.financeRadar.manticore.dto.redis;

import com.financeRadar.manticore.entity.RuleType;

import java.time.LocalDateTime;

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
        RuleType ruleType,
        String expression,
        Integer priority,
        LocalDateTime updatedAt
) {
}