package com.financeRadar.manticore.dto;

import com.financeRadar.manticore.aop.ValidSpELExpression;
import com.financeRadar.manticore.entity.RuleType;
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
        @NotNull(message = "Приоритет не может быть пустым")
        @Positive
        Integer priority,
        @NotNull(message = "Выберите: включаете ли вы правило или нет")
        Boolean enabled,
        @NotNull(message = "Выражение не может быть пустым")
        @Size(max = 1024)
        @ValidSpELExpression()
        String expression,
        @NotNull(message = "Должен быть указан тип правила")
        RuleType ruleType
) {
}