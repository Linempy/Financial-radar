package com.financeRadar.manticore.dto.admin;

import com.financeRadar.manticore.entity.rule.RuleType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Расширенный DTO для отображения правил в админ-панели
 *
 * @author bozya
 * @since 25.10.2025
 */
public record RuleAdminViewDto(
        Long id,
        String name,
        String description,
        Boolean enabled,
        RuleType ruleType,
        Integer priority,
        String expression,
        Long version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public String getFormattedCreatedAt() {
        return createdAt != null
                ? createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                : "-";
    }

    public String getFormattedUpdatedAt() {
        return updatedAt != null
                ? updatedAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                : "-";
    }

    public String getEnabledStatus() {
        return enabled ? "Активно" : "Неактивно";
    }

    public String getEnabledBadgeClass() {
        return enabled ? "badge bg-success" : "badge bg-secondary";
    }

    public String getRuleTypeDisplay() {
        if (ruleType == null) return "-";
        return switch (ruleType) {
            case THRESHOLD -> "Пороговое";
            case PATTERN -> "Паттерн";
            case COMPOSITE -> "Композитное";
            case ML -> "ML модель";
        };
    }

    public String getRuleTypeBadgeClass() {
        if (ruleType == null) return "badge bg-secondary";
        return switch (ruleType) {
            case THRESHOLD -> "badge bg-primary";
            case PATTERN -> "badge bg-info";
            case COMPOSITE -> "badge bg-warning";
            case ML -> "badge bg-danger";
        };
    }

    public String getShortExpression() {
        if (expression == null) return "-";
        return expression.length() > 50
                ? expression.substring(0, 50) + "..."
                : expression;
    }
}
