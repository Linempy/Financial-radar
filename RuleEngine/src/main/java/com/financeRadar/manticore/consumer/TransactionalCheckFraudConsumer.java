package com.financeRadar.manticore.consumer;

import com.financeRadar.manticore.dto.TransactionStats;
import com.financeRadar.manticore.dto.TransactionalEventWrapper;
import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.entity.TransactionRiskResult;
import com.financeRadar.manticore.repository.redis.RuleCacheRepository;
import com.financeRadar.manticore.service.engine.RuleEngineService;
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

    private final RuleEngineService service;
    private final RuleCacheRepository repository;

    @KafkaListener(topics = "${spring.kafka.topics.transactions.check.name}")
    public void processReceiveEvent(ConsumerRecord<String, TransactionRiskCheckEvent> consumerRecord) {
        TransactionRiskCheckEvent event = consumerRecord.value();
        log.info("CorrelationId: {}. Ивент был получен слушателем", event.getCorrelationId());
        //TODO>>> ЛОГИИИ
        TransactionalEventWrapper eventWrapper = new TransactionalEventWrapper(event, repository);
        TransactionRiskResult result = service.checkTransaction(eventWrapper);
        //TODO>>> ЛОГИИ
        log.info("RESULT. correlationId: {}, is_fraud: {}", event.getCorrelationId(), result.riskDecision().isFraud());
        //TODO поменять статус транзакции (см статусы)
        log.info("{}", result.riskDecision());
    }
}