package com.financeRadar.manticore.service.engine;

import com.financeRadar.manticore.dto.TransactionalEventWrapper;
import com.financeRadar.manticore.entity.TransactionRiskResult;

/**
 * Интерфейс для реализации логики определения подозрительной транзакции по правилам
 *
 * @author Linempy
 * @since 19.10.2025
 */
public interface RuleEngineService {
    TransactionRiskResult checkTransaction(TransactionalEventWrapper event);

}