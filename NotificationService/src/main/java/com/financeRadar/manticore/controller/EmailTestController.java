package com.financeRadar.manticore.controller;

import com.financeRadar.manticore.dto.avro.SuspiciousTransactionNotificationEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financeRadar.manticore.service.EmailServiceImpl;

/**
 * NotificationTestController — тестовый контроллер для проверки отправления метода sendMail класса {@link EmailServiceImpl}
 *
 * @author bozya
 * @since 20.10.2025
 */
@RestController
@RequestMapping("/email")
public class EmailTestController {
    private final EmailServiceImpl emailServiceImpl;

    public EmailTestController(EmailServiceImpl emailServiceImpl) {
        System.out.println("Контроллер сконструирован!");
        this.emailServiceImpl = emailServiceImpl;
    }

    @PostMapping("/test")
    public ResponseEntity<String> sendTestEmail(@RequestBody SuspiciousTransactionNotificationEvent dto) {
        emailServiceImpl.sendMail(dto);
        return ResponseEntity.ok("Письмо отправлено!");
    }
}
