package com.financeRadar.manticore.dto;

import com.financeRadar.manticore.exception.DataValidationException;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для проверки на подозрительность транзакции
 *
 * @author Linempy
 * @since 18.10.2025
 */
public record TransactionCreateDto(
        @Positive(message = "Сумма транзакции должна быть положительна")
        @NotNull(message = "Сумма транзакции обязательное поле")
        BigDecimal amount,

        @NotNull(message = "Валюта обязательное поле")
        @Size(min = 3, max = 3, message = "Валюта должна быть в формате ISO 4217 (3 символа)")
        String currency,

        @NotNull(message = "ID отправителя обязательное поле")
        Long senderId,

        @NotNull(message = "ID получателя обязательное поле")
        Long receiverId,

        @NotNull(message = "Дата транзакции обязательное поле")
        @PastOrPresent(message = "Дата транзакции не может быть совершена в будущем")
        LocalDateTime createdAt,

        @Size(max = 512, message = "Длина не может превышать 512 символов")
        @Nullable
        String description
) {
}