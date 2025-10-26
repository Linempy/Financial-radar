package com.financeRadar.manticore.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * EmailService — Сервис для отправки сообщений через mail
 *
 * @author bozya
 * @since 20.10.2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendMail(com.financeRadar.manticore.dto.avro.SuspiciousTransactionNotificationEvent dto) {
        try {
            Context context = new Context();
            context.setVariable("message", dto);

            String body = templateEngine.process("notification-email.html", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(dto.getReceiverEmail());
            helper.setSubject(dto.getSubject());
            helper.setText(body, true);
            helper.setFrom("Kir32kir@mail.ru");


            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Ошибка. Сообщение не обработано");
        }
    }
}