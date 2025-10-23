package com.financeRadar.manticore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NotificationMessageDto — DTO для отправки уведомления о подозрительной транзакции.
 *
 * @author bozya
 * @since 20.10.2025
 */
public record SuspiciousTransactionNotificationDto(
        Long senderId,                      //отправитель
        Long receiverId,                    //получатель
        String receiverEmail,               //почта получателя админа
        String subject,                     //шаблон письма
        String correlationId,
        BigDecimal amount,                  //сумма операции
        LocalDateTime timestamp,            //время
        String reasons                      //причина подозрительности
) { }