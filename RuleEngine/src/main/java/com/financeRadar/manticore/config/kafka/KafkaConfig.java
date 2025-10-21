package com.financeRadar.manticore.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Конфигурационный класс для Kafka
 *
 * @author Linempy
 * @since 18.10.2025
 */
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
}