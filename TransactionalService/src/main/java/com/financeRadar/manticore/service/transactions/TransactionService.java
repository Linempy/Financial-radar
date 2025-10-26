package com.financeRadar.manticore.service.transactions;

import com.financeRadar.manticore.dto.RequestContext;
import com.financeRadar.manticore.dto.TransactionCreateDto;

/**
 * Интерфейс для взаимодействия с транзакциями
 *
 * @author Linempy
 * @since 18.10.2025
 */
public interface TransactionService {

    void createWithCheckRisk(TransactionCreateDto dto, RequestContext context);
}