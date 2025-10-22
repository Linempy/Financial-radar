package com.financeRadar.manticore.configuration.kafka;

import com.financeRadar.manticore.dto.SuspiciousTransactionNotificationDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;

/**
 * KafkaConfig — конфигурационный файл Кафка.
 *
 * @author bozya
 * @since 20.10.2025
 */
@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topics.notification.partition-count}")
    private int notificationPartitionsCount;

    @Value("${spring.kafka.topics.notification.replica-count}")
    private int notificationReplicasCount;

    @Value("${spring.kafka.topics.dlq.partition-count}")
    private int dlqPartitionsCount;

    @Value("${spring.kafka.topics.dlq.replica-count}")
    private int dlqReplicasCount;

    /**
     * Топик для нотификаций
     */
    @Bean
    public NewTopic sendNotification(@Value("notification_topic") String topic) {
        return TopicBuilder.name(topic)
                .partitions(notificationPartitionsCount)
                .replicas(notificationReplicasCount)
                .build();
    }

    /**
     * Топик для DLQ(Dead Letter Queue)
     */
    @Bean
    public NewTopic deadLetterQueue(@Value("dlq") String topic) {
        return TopicBuilder.name(topic)
                .partitions(dlqPartitionsCount)
                .replicas(dlqReplicasCount)
                .build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SuspiciousTransactionNotificationDto>
        kafkaListenerContainerFactory(ConsumerFactory<String, SuspiciousTransactionNotificationDto> consumerFactory,
        KafkaTemplate<String, SuspiciousTransactionNotificationDto> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, SuspiciousTransactionNotificationDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> new org.apache.kafka.common.TopicPartition("dlq", record.partition())
        );

        //экспоненциальный backoff
        ExponentialBackOff backOff = new ExponentialBackOff(1000L, 2.0);
        backOff.setMaxElapsedTime(15000L);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}