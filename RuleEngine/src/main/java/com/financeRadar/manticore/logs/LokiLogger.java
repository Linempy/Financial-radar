package com.financeRadar.manticore.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeRadar.manticore.entity.TransactionStatus;
import com.financeRadar.manticore.entity.rule.RiskDecision;
import com.financeRadar.manticore.entity.rule.RuleResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
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
                    "transactionId", transactionId,
                    "data", data,
                    "timestamp", Instant.now().toString(),
                    "service", "fraud-detection"
            );

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

    public void logRuleExecution(String correlationId, String transactionId, RuleResult ruleResult, Long version) {
        Map<String, Object> ruleData = Map.of(
                "ruleId", ruleResult.ruleId(),
                "version", version,
                "ruleName", ruleResult.ruleName(),
                "triggered", ruleResult.triggered(),
                "executionTimeMs", ruleResult.executionTimeMs(),
                "error", ruleResult.errorMessage() != null ? ruleResult.errorMessage() : "null",
                "type", "rule_execution"
        );

        logTransaction(correlationId, transactionId, "RULE_EVALUATED", ruleData);
    }

    public void logRiskDecision(String correlationId, String transactionId,
                                RiskDecision decision, List<RuleResult> rules) {
        Map<String, Object> decisionData = Map.of(
                "isFraud", decision.isFraud(),
                "riskLevel", decision.getRiskLevel(),
                "riskScore", decision.getRiskScore(),
                "triggeredRules", rules.stream()
                        .filter(RuleResult::triggered)
                        .map(RuleResult::ruleName)
                        .toList(),
                "totalRules", rules.size(),
                "type", "risk_decision"
        );

        logTransaction(correlationId, transactionId, "RISK_DECISION", decisionData);
    }

    /**
     * Логирует получение Kafka ивента
     */
    public void logKafkaEventReceived(String correlationId, String transactionId) {
        Map<String, Object> eventData = Map.of(
                "action", "Ивент был получен слушателем",
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
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("errorMessage", error != null ? error.getMessage() : "Unknown error");
        errorData.put("exceptionType", error != null ? error.getClass().getSimpleName() : "Unknown");
        errorData.put("stage", stage != null ? stage : "unknown");
        errorData.put("type", "error");

        logTransaction(correlationId, transactionId, "PROCESSING_ERROR", errorData);
    }
}