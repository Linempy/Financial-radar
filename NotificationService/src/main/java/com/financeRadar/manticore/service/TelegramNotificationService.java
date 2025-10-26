package com.financeRadar.manticore.service;

import com.financeRadar.manticore.dto.avro.SuspiciousTransactionNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * TelegramService — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 26.10.2025
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramNotificationService extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.group-id}")
    private String chatId;

    @Override
    public String getBotUsername() {
        return "FinanceRadarBot";
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // заглушка
    }

    public void sendAlert(SuspiciousTransactionNotificationEvent dto) {
        String message = String.format("""
            🚨 Подозрительная транзакция
            
            CorrelationId: %s
            Сумма: %s RUB
            Время: %s
            Отправитель: %d
            Получатель: %d
            Причины: %s
            """,
                dto.getCorrelationId(),
                dto.getAmount(),
                dto.getTimestamp().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                dto.getSenderId(),
                dto.getReceiverId(),
                dto.getReasons()
        );

        SendMessage msg = new SendMessage(chatId, message);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}