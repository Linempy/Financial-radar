package com.financeRadar.manticore.consumer;

import com.financeRadar.manticore.dto.SuspiciousTransactionNotificationDto;
import com.financeRadar.manticore.service.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.financeRadar.manticore.service.EmailServiceImpl;

/**
 * NotificationKafkaListener — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author bozya
 * @since 20.10.2025
 */
@Service
@RequiredArgsConstructor
public class NotificationKafkaListener {
    private final EmailServiceImpl emailServiceImpl;
    private final TelegramNotificationService telegramNotificationService;

    @KafkaListener(topics = "notification_topic", groupId = "notification_group")
    public void listen(SuspiciousTransactionNotificationDto message) {
        emailServiceImpl.sendMail(message);
        telegramNotificationService.sendAlert(message);

    }
}