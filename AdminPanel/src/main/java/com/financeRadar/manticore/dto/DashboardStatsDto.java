package com.financeRadar.manticore.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO для статистики дашборда
 *
 * @author bozya
 * @since 25.10.2025
 */
@Data
@Builder
public class DashboardStatsDto {
    // Статистика транзакций
    private Long totalTransactions;
    private Long todayTransactions;
    private Long approvedTransactions;
    private Long fraudTransactions;
    private Long reviewRequiredTransactions;

    // Статистика правил
    private Long totalRules;
    private Long activeRules;
    private Long inactiveRules;

    // Проценты
    private Double fraudPercentage;
    private Double approvalRate;

    /**
     * Процент мошеннических транзакций
     */
    public String getFraudPercentageFormatted() {
        return fraudPercentage != null
                ? String.format("%.2f%%", fraudPercentage)
                : "0%";
    }

    /**
     * Процент одобренных транзакций
     */
    public String getApprovalRateFormatted() {
        return approvalRate != null
                ? String.format("%.2f%%", approvalRate)
                : "0%";
    }
}
