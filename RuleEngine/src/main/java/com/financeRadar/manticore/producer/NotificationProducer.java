package com.financeRadar.manticore.producer;

import com.financeRadar.manticore.dto.avro.SuspiciousTransactionNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * NotificationProducer — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 26.10.2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    @Value("${spring.kafka.topics.notification.name}")
    private String topicForNotification;

    private final KafkaTemplate<String, SuspiciousTransactionNotificationEvent> kafkaTemplate;

    public void sendMessage(SuspiciousTransactionNotificationEvent event) {
        CompletableFuture<SendResult<String, SuspiciousTransactionNotificationEvent>> future = kafkaTemplate.send(
                topicForNotification,
                event
        );

        future.whenComplete((success, failure) -> {
            if (failure == null) {
                log.info("CorrelationId {}: ивент для нотификации был отправлен", event.getCorrelationId());
            } else {
                log.info("CorrelationId {}: ивент для нотификации не был отправлен", event.getCorrelationId());
            }
        });
    }
}