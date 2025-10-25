package com.financeRadar.manticore.dto.redis;

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
public record TransactionalContextRedisDto(
        Long countTransactional,
        BigDecimal totalAmount
) {
}