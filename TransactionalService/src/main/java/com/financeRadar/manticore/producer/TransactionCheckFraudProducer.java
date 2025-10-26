package com.financeRadar.manticore.producer;

import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.logs.LokiLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Продюсер для отправки ивентов в топик {@code check_transactional}
 *
 * @author Linempy
 * @since 18.10.2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionCheckFraudProducer {

    @Value("${spring.kafka.topics.transactions.check.name}")
    private String topicForTransaction;

    private final KafkaTemplate<String, TransactionRiskCheckEvent> kafkaTemplate;
    private final LokiLogger lokiLogger;

    public void sendMessage(TransactionRiskCheckEvent event) {
        CompletableFuture<SendResult<String, TransactionRiskCheckEvent>> future = kafkaTemplate.send(
                topicForTransaction,
                event
        );

        future.whenComplete((success, failure) -> {
            if (failure == null) {
                lokiLogger.logTransaction(event.getCorrelationId(), event.getTransactionId(), "KAFKA_SENT",
                    Map.of(
                        "action", "kafka_message_sent",
                        "eventType", event.getClass().getSimpleName(),
                        "correlationId", event.getCorrelationId(),
                        "transactionId", event.getTransactionId()
                    ));
            } else {
                lokiLogger.logTransaction(event.getCorrelationId(), event.getTransactionId(), "KAFKA_SEND_ERROR",
                    Map.of(
                        "action", "kafka_send_error",
                        "error", "Ошибка отправки ивента",
                        "eventType", event.getClass().getSimpleName()
                    ));
            }
        });
    }
}