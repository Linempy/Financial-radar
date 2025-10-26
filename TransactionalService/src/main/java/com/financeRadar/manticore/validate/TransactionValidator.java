package com.financeRadar.manticore.validate;

import com.financeRadar.manticore.dto.TransactionCreateDto;
import com.financeRadar.manticore.exception.DataValidationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * TransactionValidator — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 25.10.2025
 */
@Service
public class TransactionValidator {

    public void validate(TransactionCreateDto dto) {
        LocalDateTime now = LocalDateTime.now();

        if (dto.createdAt().isAfter(now)) {
            throw new DataValidationException("Транзакция не может быть совершена в будущем");
        }

        if (dto.createdAt().isBefore(now.minusDays(1))) {
            throw new DataValidationException("Транзакция является слишком старой (макс. 1 день)");
        }

        if (dto.senderId().equals(dto.receiverId())) {
            throw new DataValidationException("Отправитель и получатель не могут быть одной и той же сущностью");
        }

        if (dto.amount().compareTo(BigDecimal.ONE) < 0) {
            throw new DataValidationException("Сумма не может быть меньше 1");
        }
    }
}
