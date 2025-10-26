package com.financeRadar.manticore.service;

import com.financeRadar.manticore.dto.TransactionCreateDto;
import com.financeRadar.manticore.dto.TransactionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис-фасад для интеграции с TransactionalService
 */
@Service
@RequiredArgsConstructor
public class TransactionalFacadeService {
    private final TransactionServiceImpl transactionService;
    // TODO: Внедрить зависимость на TransactionalService через конструктор

    public List<TransactionViewDto> getAllTransactions() {
        // TODO: Реализовать вызов метода из TransactionalService
        return List.of();
    }

    public TransactionViewDto getTransactionById(String id) {
        // TODO: Реализовать получение транзакции по ID
        return null;
    }
}
