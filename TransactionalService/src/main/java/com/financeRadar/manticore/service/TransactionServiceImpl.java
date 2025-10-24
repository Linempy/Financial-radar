package com.financeRadar.manticore.service;

import com.financeRadar.manticore.config.context.CorrelationContext;
import com.financeRadar.manticore.dto.RequestContext;
import com.financeRadar.manticore.dto.TransactionCreateDto;
import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.entity.Transaction;
import com.financeRadar.manticore.entity.TransactionStatus;
import com.financeRadar.manticore.mapper.TransactionMapper;
import com.financeRadar.manticore.producer.TransactionCheckFraudProducer;
import com.financeRadar.manticore.repository.sql.TransactionRepostitory;
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
public class TransactionServiceImpl implements TransactionService{

    private final TransactionCheckFraudProducer producer;
    private final TransactionMapper mapper;
    private final TransactionRepostitory repository;

    public void createWithChechRisk(TransactionCreateDto dto, RequestContext context) {

        Transaction transaction = mapper.toEntity(dto, context, CorrelationContext.getCorrelationId());
        transaction.setStatus(TransactionStatus.FRAUD_CHECKING);
        Transaction savedTransaction = repository.save(transaction);

        TransactionRiskCheckEvent event = mapper.toEvent(
                dto, 
                savedTransaction.getId().toString(),
                CorrelationContext.getCorrelationId()
        );

        producer.sendMessage(event);
    }
}