package com.financeRadar.manticore.dto;

import java.time.LocalDateTime;

/**
 * RuleViewDto — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 19.10.2025
 */
public record RuleViewDto(
        String name,
        String description,
        Boolean enabled,
        String expression,
        Integer priority,
        Long version,
        LocalDateTime updatedAt
) {
}