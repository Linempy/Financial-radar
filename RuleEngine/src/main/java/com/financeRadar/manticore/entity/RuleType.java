package com.financeRadar.manticore.entity;

/**
 * Перечисления представляющее собой типы правил для проверки на подозрительность транзакции
 * Используется в {@link Rule}
 *
 * @author Linempy
 * @since 19.10.2025
 */
public enum RuleType {
    THRESHOLD, PATTERN, COMPOSITE, ML
}