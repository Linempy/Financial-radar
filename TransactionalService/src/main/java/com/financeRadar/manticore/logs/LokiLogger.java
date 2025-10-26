package com.financeRadar.manticore.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeRadar.manticore.entity.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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
@RequiredArgsConstructor
public class LokiLogger {

    private final MdcLogger mdcLogger;
    private final ObjectMapper objectMapper;

    public void logTransaction(String correlationId, String transactionId,
                               String stage, Object data) {
        try {
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("stage", stage);
            logEntry.put("transactionId", transactionId != null ? transactionId : "null");
            logEntry.put("data", data);
            logEntry.put("timestamp", Instant.now().toString());
            logEntry.put("service", "fraud-detection");

            String jsonData = objectMapper.writeValueAsString(logEntry);

            MDC.put("correlationId", correlationId);
            MDC.put("stage", stage);

            mdcLogger.info(correlationId, jsonData);

        } catch (JsonProcessingException e) {
            mdcLogger.error(correlationId, "Ошибка сериализации лога: {}", e.getMessage());
        } finally {
            MDC.remove("correlationId");
            MDC.remove("stage");
        }
    }



    /**
     * Логирует получение Kafka ивента
     */
    public void logKafka(String correlationId, String transactionId) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("action", "Ивент был отправлен");
        eventData.put("source", "kafka_consumer");


        logTransaction(correlationId, transactionId, "KAFKA_EVENT_RECEIVED", eventData);
    }

    /**
     * Логирует обновление статуса транзакции
     */
    public void logTransactionStatusUpdate(String correlationId, String transactionId,
                                           TransactionStatus oldStatus, TransactionStatus newStatus,
                                           Boolean isFraud) {
        Map<String, Object> statusData = new HashMap<>();
        statusData.put("oldStatus", oldStatus != null ? oldStatus.name() : "null");
        statusData.put("newStatus", newStatus.name());
        statusData.put("isFraud", isFraud);
        statusData.put("type", "status_update");


        logTransaction(correlationId, transactionId, "TRANSACTION_STATUS_UPDATED", statusData);
    }

    /**
     * Логирует ошибку обработки
     */
    public void logProcessingError(String correlationId, String transactionId,
                                   Exception error, String stage) {
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("errorMessage", error != null ? error.getMessage() : "Unknown error");
        errorData.put("exceptionType", error != null ? error.getClass().getSimpleName() : "Unknown");
        errorData.put("stage", stage != null ? stage : "unknown");
        errorData.put("type", "error");


        logTransaction(correlationId, transactionId, "PROCESSING_ERROR", errorData);
    }

    public void logTransactionSaved(String correlationId, String transactionId,
                                    String status, Double amount, String currency) {
        Map<String, Object> data = new HashMap<>();
        data.put("action", "transaction_saved");
        data.put("status", status);
        data.put("amount", amount);
        data.put("currency", currency);

        logTransaction(correlationId, transactionId, "TRANSACTION_SAVED", data);
    }

    /**
     * Логирует назначение correlationId
     */
    public void logCorrelationAssigned(String correlationId, boolean isNew, String source) {
        Map<String, Object> data = new HashMap<>();
        data.put("action", "correlation_id_assigned");
        data.put("isNew", isNew);
        data.put("source", source);
        data.put("type", "correlation");

        logTransaction(correlationId, null, "CORRELATION_ASSIGNED", data);
    }
}