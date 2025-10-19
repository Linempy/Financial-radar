package com.financeRadar.manticore.producer;

import com.financeRadar.manticore.dto.avro.TransactionalRiskCheckEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

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
public class TransactionalCheckFraudProducer {

    @Value("${spring.kafka.topics.transactions.check}")
    private String topicForTransaction;

    private final KafkaTemplate<String, TransactionalRiskCheckEvent> kafkaTemplate;

    public void sendMessage(TransactionalRiskCheckEvent event) {
        CompletableFuture<SendResult<String, TransactionalRiskCheckEvent>> future = kafkaTemplate.send(
                topicForTransaction,
                event
        );

        future.whenComplete((success, failure) -> {
            if (failure == null) {
                log.info("CorrelationId: {}. Ивент был успешно отправлен в топик: {}",
                        event.getCorrelationId(),
                        topicForTransaction
                );
            } else {
                log.warn("CorrelationId: {}. Ивент не был отправлен в топик: {}",
                        event.getCorrelationId(),
                        topicForTransaction
                );
            }
        });

    }
}