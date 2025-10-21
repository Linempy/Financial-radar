package com.financeRadar.manticore.consumer;

import com.financeRadar.manticore.context.ProcessingContext;
import com.financeRadar.manticore.dto.avro.TransactionalRiskCheckEvent;
import com.financeRadar.manticore.entity.ProcessingStatus;
import com.financeRadar.manticore.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Слушатель ивентов топика {@code check_transactional}
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionalCheckFraudConsumer {

    private final AuditService auditService;

    @KafkaListener(topics = "${spring.kafka.topics.transactions.check.name}")
    public void processSendEvent(ConsumerRecord<String, TransactionalRiskCheckEvent> consumerRecord) {
        TransactionalRiskCheckEvent event = consumerRecord.value();
        log.info("CorrelationId: {}. Ивент был получен слушателем", event.getCorrelationId());

        ProcessingContext context = new ProcessingContext(event.getCorrelationId());
        context.addStep("KAFKA_RECEIVED", ProcessingStatus.COMPLETED, "Ивент был получен");

        // вызов ruleEngien там будте происходить тоже audit шагов по правилам
    }
}
















