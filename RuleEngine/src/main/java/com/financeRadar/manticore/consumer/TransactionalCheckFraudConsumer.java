package com.financeRadar.manticore.consumer;

import com.financeRadar.manticore.dto.TransactionalEventWrapper;
import com.financeRadar.manticore.dto.avro.SuspiciousTransactionNotificationEvent;
import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.entity.TransactionRiskResult;
import com.financeRadar.manticore.entity.TransactionStatus;
import com.financeRadar.manticore.entity.rule.RuleResult;
import com.financeRadar.manticore.logs.LokiLogger;
import com.financeRadar.manticore.producer.NotificationProducer;
import com.financeRadar.manticore.repository.redis.RuleCacheRepository;
import com.financeRadar.manticore.service.engine.RuleEngineService;
import com.financeRadar.manticore.service.idempotencyKey.IdempotencyService;
import com.financeRadar.manticore.service.transactions.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.Collectors;

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

    @Value("${spring.mail.username}")
    private String adminEmail;

    private final RuleEngineService service;
    private final RuleCacheRepository repository;
    private final IdempotencyService idempotencyService;
    private final TransactionService transactionService;
    private final LokiLogger lokiLogger;
    private final NotificationProducer notificationProducer;

    @KafkaListener(topics = "${spring.kafka.topics.transactions.check.name}",
    groupId = "${spring.kafka.consumer.group-id")
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

            if (isFraud) {
                SuspiciousTransactionNotificationEvent notificationEvent = buildSuspiciousEvent(event, result);
                notificationProducer.sendMessage(notificationEvent);
            }

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

    private SuspiciousTransactionNotificationEvent buildSuspiciousEvent(TransactionRiskCheckEvent event,
            TransactionRiskResult result) {

        String reasons = result.ruleResults().stream()
                .filter(RuleResult::triggered)
                .map(rule -> String.format("Правило '%s' сработало", rule.ruleName()))
                .collect(Collectors.joining("; "));

        return new SuspiciousTransactionNotificationEvent(
                event.getSenderId() != null ? Long.valueOf(event.getSenderId()) : null,
                event.getReceiverId() != null ? Long.valueOf(event.getReceiverId()) : null,
                adminEmail,
                "Подозрительная транзакция обнаружена",
                event.getCorrelationId(),
                event.getAmount() != null ? new BigDecimal(event.getAmount()) : BigDecimal.ZERO,
                Instant.now(),
                reasons
        );
    }

}