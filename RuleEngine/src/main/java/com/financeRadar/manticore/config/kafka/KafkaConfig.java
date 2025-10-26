package com.financeRadar.manticore.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Конфигурационный класс для Kafka
 *
 * @author Linempy
 * @since 18.10.2025
 */
@Slf4j
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.topics.transactions.check.partitions}")
    private int partitionCount;

    @Value("${spring.kafka.topics.transactions.check.replicas}")
    private int replicasCount;

    @Bean
    public NewTopic checkTransactional(@Value("${spring.kafka.topics.transactions.check.name}") String topic) {
        return TopicBuilder.name(topic)
                .partitions(partitionCount)
                .replicas(replicasCount)
                .build();
    }

    @Bean
    public NewTopic checkNotification(@Value("${spring.kafka.topics.notification.name}") String topic) {
        return TopicBuilder.name(topic)
                .partitions(partitionCount)
                .replicas(replicasCount)
                .build();
    }

    @Bean
    public CommonErrorHandler kafkaErrorHandler() {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                (record, exception) -> {
                    log.error("Пропущено проблемный ивент offset {}: {}",
                            record.offset(), exception.getMessage());
                },
                new FixedBackOff(1000L, 2)
        );
        return errorHandler;
    }

}