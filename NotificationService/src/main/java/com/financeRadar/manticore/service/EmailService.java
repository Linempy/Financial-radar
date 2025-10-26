package com.financeRadar.manticore.service;


import com.financeRadar.manticore.dto.avro.SuspiciousTransactionNotificationEvent;

/**
 * EmailService — Интерфейс для сервиса отправки email-уведомлений о подозрительных транзакциях.
 *
 * @author bozya
 * @since 21.10.2025
 */
public interface EmailService {

    /**
     * Отправляет mail с уведомлением, основанным на данных подозрительной транзакции
     */
    public void sendMail(SuspiciousTransactionNotificationEvent suspiciousTransactionNotificationEvent);
}