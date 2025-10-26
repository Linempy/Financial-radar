package com.financeRadar.manticore.dto;

/**
 * Контекст запроса для создания транзакции (и ее проверки)
 *
 * @author Linempy
 * @since 23.10.2025
 */
public record RequestContext(
        String ip,
        String userAgent,
        String idempotencyKey
) {
}