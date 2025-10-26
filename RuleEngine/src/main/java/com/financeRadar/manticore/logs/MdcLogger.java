package com.financeRadar.manticore.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * MdcLogger — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 26.10.2025
 */
@Component
public class MdcLogger {

    private static final Logger log = LoggerFactory.getLogger(MdcLogger.class);

    /**
     * Логирует INFO сообщение с correlationId
     */
    public void info(String correlationId, String message, Object... args) {
        withMdc(correlationId, () -> log.info(message, args));
    }

    /**
     * Логирует ERROR сообщение с correlationId
     */
    public void error(String correlationId, String message, Object... args) {
        withMdc(correlationId, () -> log.error(message, args));
    }

    /**
     * Логирует ERROR сообщение с correlationId и исключением
     */
    public void error(String correlationId, String message, Throwable throwable) {
        withMdc(correlationId, () -> log.error(message, throwable));
    }

    /**
     * Логирует WARN сообщение с correlationId
     */
    public void warn(String correlationId, String message, Object... args) {
        withMdc(correlationId, () -> log.warn(message, args));
    }

    /**
     * Логирует DEBUG сообщение с correlationId
     */
    public void debug(String correlationId, String message, Object... args) {
        withMdc(correlationId, () -> log.debug(message, args));
    }

    /**
     * Базовый метод для выполнения с MDC контекстом
     */
    private void withMdc(String correlationId, Runnable logAction) {
        String previousCorrelationId = MDC.get("correlationId");

        MDC.put("correlationId", correlationId);

        try {
            logAction.run();
        } finally {
            if (previousCorrelationId != null) {
                MDC.put("correlationId", previousCorrelationId);
            } else {
                MDC.remove("correlationId");
            }
        }
    }
}