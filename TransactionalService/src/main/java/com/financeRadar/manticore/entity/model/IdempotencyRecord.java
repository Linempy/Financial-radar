package com.financeRadar.manticore.entity.model;

/**
 * IdempotencyRecord — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 26.10.2025
 */
public record IdempotencyRecord(
        String key,
        IdempotencyStatus status,
        String requestHash,
        Object response
) {
    public enum IdempotencyStatus {
        PROCESSING,
        COMPLETED,
        FAILED
    }
}