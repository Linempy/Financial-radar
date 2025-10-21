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
public record TransactionalCreateDto(
        @Positive(message = "Сумма транзакции должна быть положительна")
        @NotNull(message = "Сумма транзакции обязательное поле")
        BigDecimal amount,
        @NotNull(message = "ID отправителя обязательное поле")
        Long senderId,
        @NotNull(message = "ID получателя обязательное поле")
        Long receiverId,
        @NotNull(message = "Дата транзакции обязательное поле")
        @PastOrPresent(message = "Дата транзакции не может быть совершена в будущем")
        LocalDateTime createdAt,
        @Size(max = 512, message = "Длина не может превышать 512 символов")
        @Nullable String description
) {

    public TransactionalCreateDto {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt.isAfter(now)) {
            throw new DataValidationException("Транзакция не может быть совершена в будущем");
        }

        if (createdAt.isBefore(now.minusDays(1))) {
            throw new DataValidationException("Транзакция является слишком старой (макс. 1 день)");
        }

        if (senderId.equals(receiverId)) {
            throw new DataValidationException("Отправитель и получатель не могут быть одной и той же сущностью");
        }

        if (amount.compareTo(BigDecimal.ONE) < 0) {
            throw new DataValidationException("Сумма не может быть меньше 1");
        }
    }
}