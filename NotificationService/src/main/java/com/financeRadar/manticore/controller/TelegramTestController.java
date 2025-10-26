package com.financeRadar.manticore.controller;

import com.financeRadar.manticore.dto.avro.SuspiciousTransactionNotificationEvent;
import com.financeRadar.manticore.service.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TelegramTestController — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 26.10.2025
 */
@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramTestController {

    private final TelegramNotificationService bot;

    @PostMapping("/alert")
    public String sendAlert(@RequestBody SuspiciousTransactionNotificationEvent dto) {
        bot.sendAlert(dto);
        return "Сообщение отправлено";
    }
}