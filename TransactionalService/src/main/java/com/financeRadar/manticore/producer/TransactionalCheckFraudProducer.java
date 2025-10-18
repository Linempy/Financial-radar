package com.financeRadar.manticore.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * TransactionalCheckFraudProducer — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 18.10.2025
 */
@Component
@RequiredArgsConstructor
public class TransactionalCheckFraudProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage() {
    }
}