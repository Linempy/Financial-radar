package com.financeRadar.manticore.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Объект idempotency
 *
 * @author Linempy
 * @since 26.10.2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdempotencyResult {
    private IdempotencyStatus status;
    private String cachedResponse;

    public static IdempotencyResult processing() {
        return IdempotencyResult.builder()
                .status(IdempotencyStatus.PROCESSING)
                .build();
    }

    public static IdempotencyResult completed(String cachedResponse) {
        return IdempotencyResult.builder()
                .status(IdempotencyStatus.COMPLETED)
                .cachedResponse(cachedResponse)
                .build();
    }

    public static IdempotencyResult completedError(String cachedResponse) {
        return IdempotencyResult.builder()
                .status(IdempotencyStatus.FAILED)
                .cachedResponse(cachedResponse)
                .build();
    }

    public static IdempotencyResult available() {
        return IdempotencyResult.builder()
                .status(null)
                .build();
    }

    public boolean isProcessing() {
        return status == IdempotencyStatus.PROCESSING;
    }

    public boolean isCompleted() {
        return status == IdempotencyStatus.COMPLETED ||
                status == IdempotencyStatus.FAILED;
    }

    public boolean isAvailable() {
        return status == null;
    }
}