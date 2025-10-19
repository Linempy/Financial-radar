package com.financeRadar.manticore.consumer;

import com.financeRadar.manticore.dto.avro.TransactionalRiskCheckEvent;
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

    @KafkaListener(topics = "${spring.kafka.topics.transactions.check}")
    public void processSendEvent(ConsumerRecord<String, TransactionalRiskCheckEvent> consumerRecord) {
        TransactionalRiskCheckEvent event = consumerRecord.value();
        log.info("CorrelationId: {}. Ивент был получен слушателем", event.getCorrelationId());
    }
}