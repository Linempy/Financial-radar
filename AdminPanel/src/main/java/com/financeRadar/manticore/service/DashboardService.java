package com.financeRadar.manticore.service;

import com.financeRadar.manticore.dto.DashboardStatsDto;
import com.financeRadar.manticore.entity.Transaction;
import com.financeRadar.manticore.entity.TransactionStatus;
import com.financeRadar.manticore.repository.RuleRepository;
import com.financeRadar.manticore.repository.sql.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для получения статистики дашборда
 *
 * @author bozya
 * @since 25.10.2025
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final RuleRepository ruleRepository;

    /**
     * Получить статистику для дашборда
     */
    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        // Получаем все транзакции
        List<Transaction> allTransactions = transactionRepository.findAll();
        long totalTransactions = allTransactions.size();

        // Транзакции за сегодня
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long todayTransactions = allTransactions.stream()
                .filter(tx -> tx.getCreatedAt().isAfter(todayStart))
                .count();

        // Статистика по статусам
        long approvedTransactions = allTransactions.stream()
                .filter(tx -> tx.getStatus() == TransactionStatus.APPROVED
                        || tx.getStatus() == TransactionStatus.COMPLETED)
                .count();

        long fraudTransactions = allTransactions.stream()
                .filter(tx -> tx.getStatus() == TransactionStatus.FRAUD_DETECTED)
                .count();

        long reviewRequiredTransactions = allTransactions.stream()
                .filter(tx -> tx.getStatus() == TransactionStatus.REVIEW_REQUIRED)
                .count();

        // Статистика правил
        long totalRules = ruleRepository.count();
        long activeRules = ruleRepository.findAllByEnabledTrue().size();
        long inactiveRules = totalRules - activeRules;

        // Проценты
        double fraudPercentage = totalTransactions > 0
                ? (fraudTransactions * 100.0) / totalTransactions
                : 0.0;

        double approvalRate = totalTransactions > 0
                ? (approvedTransactions * 100.0) / totalTransactions
                : 0.0;

        return DashboardStatsDto.builder()
                .totalTransactions(totalTransactions)
                .todayTransactions(todayTransactions)
                .approvedTransactions(approvedTransactions)
                .fraudTransactions(fraudTransactions)
                .reviewRequiredTransactions(reviewRequiredTransactions)
                .totalRules(totalRules)
                .activeRules(activeRules)
                .inactiveRules(inactiveRules)
                .fraudPercentage(fraudPercentage)
                .approvalRate(approvalRate)
                .build();
    }
}
