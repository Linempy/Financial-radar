package com.financeRadar.manticore.dto;

import com.financeRadar.manticore.entity.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DTO для отображения транзакции в админ-панели
 *
 * @author bozya
 * @since 25.10.2025
 */
public record TransactionViewDto(
        Long id,
        String correlationId,
        BigDecimal amount,
        String currency,
        String description,
        TransactionStatus status,
        Boolean isFraud,
        Long senderId,
        Long receiverId,
        String ipAddress,
        String userAgent,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Форматированная дата создания для отображения
     */
    public String getFormattedCreatedAt() {
        return createdAt != null
                ? createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
                : "-";
    }

    /**
     * Форматированная дата обновления для отображения
     */
    public String getFormattedUpdatedAt() {
        return updatedAt != null
                ? updatedAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
                : "-";
    }

    /**
     * Статус в человекочитаемом виде
     */
    public String getStatusDisplay() {
        if (status == null) return "Неизвестно";
        return switch (status) {
            case CREATED -> "Создана";
            case FRAUD_CHECKING -> "Проверка мошенничества";
            case APPROVED -> "Одобрена";
            case FRAUD_DETECTED -> "Мошенничество";
            case REVIEW_REQUIRED -> "Требует проверки";
            case REJECTED -> "Отклонена";
            case COMPLETED -> "Выполнена";
        };
    }

    /**
     * CSS класс для статуса (для Bootstrap badge)
     */
    public String getStatusBadgeClass() {
        if (status == null) return "badge bg-secondary";
        return switch (status) {
            case CREATED -> "badge bg-info";
            case FRAUD_CHECKING -> "badge bg-warning";
            case APPROVED -> "badge bg-success";
            case FRAUD_DETECTED -> "badge bg-danger";
            case REVIEW_REQUIRED -> "badge bg-warning text-dark";
            case REJECTED -> "badge bg-dark";
            case COMPLETED -> "badge bg-primary";
        };
    }

    /**
     * Отображение признака мошенничества
     */
    public String getFraudDisplay() {
        if (isFraud == null) return "Не определено";
        return isFraud ? "Да" : "Нет";
    }

    /**
     * CSS класс для признака мошенничества
     */
    public String getFraudBadgeClass() {
        if (isFraud == null) return "badge bg-secondary";
        return isFraud ? "badge bg-danger" : "badge bg-success";
    }

    /**
     * Форматированная сумма с валютой
     */
    public String getFormattedAmount() {
        return String.format("%.2f %s", amount, currency);
    }
}
