package com.financeRadar.manticore.entity.model;

/**
 * Статусы для защиты от дубликатов транзакций
 *
 * @author Linempy
 * @since 26.10.2025
 */
public enum IdempotencyStatus {
    PROCESSING,
    COMPLETED,
    FAILED,
    AVAILABLE
}