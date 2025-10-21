package com.financeRadar.manticore.dto;

import com.financeRadar.manticore.aop.ValidSpELExpression;
import com.financeRadar.manticore.entity.RuleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO для создания правила
 *
 * @author Linempy
 * @since 19.10.2025
 */
public record RuleCreateDto (
        @NotBlank
        @Size(max = 64, message = "Слишком длинное имя")
        String name,
        @Size(max = 512)
        String description,
        Boolean enabled,
        @NotNull(message = "Приоритет не может быть пустым")
        @Positive
        Integer priority,
        @NotNull(message = "Выражение не может быть пустым")
        @Size(max = 1024)
        @ValidSpELExpression()
        String expression,
        @NotNull(message = "Должен быть указан тип правила")
        RuleType ruleType
) {

}