package com.financeRadar.manticore.consumer;

import com.financeRadar.manticore.dto.TransactionalEventWrapper;
import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.entity.transaction.TransactionRiskResult;
import com.financeRadar.manticore.entity.transaction.TransactionStatus;
import com.financeRadar.manticore.logs.LokiLogger;
import com.financeRadar.manticore.repository.redis.RuleCacheRepository;
import com.financeRadar.manticore.service.engine.RuleEngineService;
import com.financeRadar.manticore.service.transaction.idempotencyKey.IdempotencyService;
import com.financeRadar.manticore.service.transaction.TransactionService;
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
    private final IdempotencyService idempotencyService;
    private final TransactionService transactionService;
    private final LokiLogger lokiLogger;

    @KafkaListener(topics = "${spring.kafka.topics.transactions.check.name}")
    public void processReceiveEvent(ConsumerRecord<String, TransactionRiskCheckEvent> consumerRecord) {
        TransactionRiskCheckEvent event = consumerRecord.value();

        try {
            lokiLogger.logKafkaEventReceived(event.getCorrelationId(), event.getTransactionId());

            TransactionalEventWrapper eventWrapper = new TransactionalEventWrapper(event, repository);
            TransactionRiskResult result = service.checkTransaction(eventWrapper);

            lokiLogger.logRiskDecision(
                    event.getCorrelationId(),
                    event.getTransactionId(),
                    result.riskDecision(),
                    result.ruleResults()
            );

            boolean isFraud = result.riskDecision().isFraud();
            TransactionStatus status = isFraud ? TransactionStatus.FRAUD_DETECTED : TransactionStatus.COMPLETED;
            transactionService.updateStatus(Long.valueOf(event.getTransactionId()), isFraud, status);
            idempotencyService.markCompletedSuccess(event.getIdempotencyKey());

            lokiLogger.logTransactionStatusUpdate(
                    event.getCorrelationId(),
                    event.getTransactionId(),
                    TransactionStatus.FRAUD_CHECKING,
                    status,
                    isFraud
            );

        } catch (Exception e) {
            lokiLogger.logProcessingError(
                    event.getCorrelationId(),
                    event.getTransactionId(),
                    e,
                    "consumer_processing"
            );
            log.info(e.getMessage());
            transactionService.updateStatus(Long.valueOf(event.getTransactionId()), null, TransactionStatus.FAILED);
            idempotencyService.markCompletedError(event.getIdempotencyKey());
        }
    }
}