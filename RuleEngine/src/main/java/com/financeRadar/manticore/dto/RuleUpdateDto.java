package com.financeRadar.manticore.dto;

import com.financeRadar.manticore.aop.ValidSpELExpression;
import com.financeRadar.manticore.entity.rule.RuleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO для обновления правила
 *
 * @author Linempy
 * @since 19.10.2025
 */
public record RuleUpdateDto (
        @NotBlank
        @Size(max = 64, message = "Имя слишком длинное")
        String name,
        @Size(max = 512, message = "Описание слишком длинное")
        String description,
        @NotNull(message = "Приоритет не может быть пустым")
        @Positive
        Integer priority,
        Boolean enabled,
        @NotNull(message = "Выражение не может быть пустым")
        @Size(max = 1024)
        @ValidSpELExpression()
        String expression,
        @NotNull(message = "Должен быть указан тип правила")
        RuleType ruleType
) {
}