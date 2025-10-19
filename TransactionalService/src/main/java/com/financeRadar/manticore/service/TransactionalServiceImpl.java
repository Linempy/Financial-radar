package com.financeRadar.manticore.service;

import com.financeRadar.manticore.config.context.CorrelationContext;
import com.financeRadar.manticore.dto.TransactionalRiskCheckDto;
import com.financeRadar.manticore.dto.avro.TransactionalRiskCheckEvent;
import com.financeRadar.manticore.mapper.TransactionalMapper;
import com.financeRadar.manticore.producer.TransactionalCheckFraudProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис для взаимодействия с транзакциями
 *
 * @author Linempy
 * @since 18.10.2025
 */
@Service
@RequiredArgsConstructor
public class TransactionalServiceImpl implements TransactionalService{

    private final TransactionalCheckFraudProducer producer;
    private final TransactionalMapper mapper;

    public void handleTransactionalOnFraud(TransactionalRiskCheckDto dto) {
        TransactionalRiskCheckEvent event = mapper.toEvent(dto, CorrelationContext.getCorrelationId());
        producer.sendMessage(event);
    }


}