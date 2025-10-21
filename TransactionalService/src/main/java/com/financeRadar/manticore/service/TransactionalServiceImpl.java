package com.financeRadar.manticore.service;

import com.financeRadar.manticore.config.context.CorrelationContext;
import com.financeRadar.manticore.context.ProcessingContext;
import com.financeRadar.manticore.dto.TransactionalCreateDto;
import com.financeRadar.manticore.dto.avro.TransactionalRiskCheckEvent;
import com.financeRadar.manticore.entity.ProcessingStatus;
import com.financeRadar.manticore.mapper.TransactionalMapper;
import com.financeRadar.manticore.producer.TransactionalCheckFraudProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
    private final AuditService auditService;

    public void createWithChechRisk(TransactionalCreateDto dto) {
        ProcessingContext context = new ProcessingContext(UUID.randomUUID().toString());

        TransactionalRiskCheckEvent event = mapper.toEvent(dto, CorrelationContext.getCorrelationId());
        producer.sendMessage(event)
                .thenRun(() -> {
                    context.addStep("KAFKA_SEND", ProcessingStatus.COMPLETED, "Ивент отправлен");
                    auditService.saveProcessingContext(context, ProcessingStatus.COMPLETED, "TransactionalService");
                })
                .exceptionally(throwable -> {
                    context.addStep("KAFKA_SEND", ProcessingStatus.FAILED, "Ошибка отправки ивента");
                    auditService.saveProcessingContext(context, ProcessingStatus.FAILED, "TransactionalService");
                    return null;
                });
    }
}