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
            Map<String, Object> logEntry = Map.of(
                    "stage", stage,
                    "transactionId", transactionId != null ? transactionId : "null",
                    "data", data,
                    "timestamp", Instant.now().toString(),
                    "service", "fraud-detection"
            );

            String jsonData = objectMapper.writeValueAsString(logEntry);

            MDC.put("correlationId", transactionId);
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
        Map<String, Object> eventData = Map.of(
                "action", "Ивент был отправлен",
                "source", "kafka_consumer"
        );

        logTransaction(correlationId, transactionId, "KAFKA_EVENT_RECEIVED", eventData);
    }

    /**
     * Логирует обновление статуса транзакции
     */
    public void logTransactionStatusUpdate(String correlationId, String transactionId,
                                           TransactionStatus oldStatus, TransactionStatus newStatus,
                                           Boolean isFraud) {
        Map<String, Object> statusData = Map.of(
                "oldStatus", oldStatus != null ? oldStatus.name() : "null",
                "newStatus", newStatus.name(),
                "isFraud", isFraud,
                "type", "status_update"
        );

        logTransaction(correlationId, transactionId, "TRANSACTION_STATUS_UPDATED", statusData);
    }

    /**
     * Логирует ошибку обработки
     */
    public void logProcessingError(String correlationId, String transactionId,
                                   Exception error, String stage) {
        Map<String, Object> errorData = Map.of(
                "errorMessage", error.getMessage(),
                "exceptionType", error.getClass().getSimpleName(),
                "stage", stage,
                "type", "error"
        );

        logTransaction(correlationId, transactionId, "PROCESSING_ERROR", errorData);
    }

    public void logTransactionSaved(String correlationId, String transactionId,
                                    String status, Double amount, String currency) {
        logTransaction(correlationId, transactionId, "TRANSACTION_SAVED",
                Map.of(
                        "action", "transaction_saved",
                        "status", status,
                        "amount", amount,
                        "currency", currency)
        );
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