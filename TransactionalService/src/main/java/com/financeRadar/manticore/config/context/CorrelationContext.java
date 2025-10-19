package com.financeRadar.manticore.config.context;

import org.springframework.stereotype.Component;

/**
 * Класс для управления контекстом correlationId
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Component
public class CorrelationContext {
    private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<>();

    public static String getCorrelationId() {
        return CORRELATION_ID.get();
    }

    public static void setCorrelationId(String correlationId) {
        CORRELATION_ID.set(correlationId);
    }

    public static void clear() {
        CORRELATION_ID.remove();
    }
}