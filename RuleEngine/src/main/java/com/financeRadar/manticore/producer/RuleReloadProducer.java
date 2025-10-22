package com.financeRadar.manticore.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * RuleReloadProducer — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 22.10.2025
 */
@Component
@RequiredArgsConstructor
public class RuleReloadProducer {

    @Value("${spring.kafka.topics.rules.reload.name}")
    private String ruleReloadTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage() {
        kafkaTemplate.send(ruleReloadTopic)
    }
}