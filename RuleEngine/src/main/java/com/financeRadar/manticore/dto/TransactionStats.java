package com.financeRadar.manticore.dto;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * TransactionalContextRedisDto — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 24.10.2025
 */
@Builder
public record TransactionStats(
        Long countTransactional,
        BigDecimal totalAmount
) {
    public static TransactionStats empty() {
        return new TransactionStats(0L, BigDecimal.ZERO);
    }
}