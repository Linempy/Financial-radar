package com.financeRadar.manticore.service.transactions;

import com.financeRadar.manticore.dto.RequestContext;
import com.financeRadar.manticore.dto.TransactionCreateDto;
import com.financeRadar.manticore.dto.TransactionViewDto;

import java.util.List;

/**
 * Интерфейс для взаимодействия с транзакциями
 *
 * @author Linempy
 * @since 18.10.2025
 */
public interface TransactionService {

    void createWithCheckRisk(TransactionCreateDto dto, RequestContext context);

    /**
     * Получить все транзакции для отображения в админ-панели
     */
    List<TransactionViewDto> getAllTransactionsForView();

    /**
     * Получить транзакцию по ID для отображения в админ-панели
     */
//    TransactionViewDto getTransactionByIdForView(Long id);
}