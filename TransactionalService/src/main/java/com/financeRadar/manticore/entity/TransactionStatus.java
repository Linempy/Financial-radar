package com.financeRadar.manticore.entity;

/**
 * TransactionStatus — перечисление.
 * <p>
 * TODO: описать назначение enum и значения.
 * </p>
 *
 * @author Linempy
 * @since 23.10.2025
 */
public enum TransactionStatus {
    CREATED,           // Создана
    FRAUD_CHECKING,    // В процессе проверки
    APPROVED,          // Одобрена
    FRAUD_DETECTED,    // Обнаружено мошенничество
    REVIEW_REQUIRED,   // Требует ручной проверки
    REJECTED,          // Отклонена (не мошенничество, но по другим причинам)
    COMPLETED          // Успешно выполнена
}