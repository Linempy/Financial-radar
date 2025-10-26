package com.financeRadar.manticore.service.engine;

import com.financeRadar.manticore.dto.TransactionalEventWrapper;
import com.financeRadar.manticore.entity.rule.RiskDecision;
import com.financeRadar.manticore.entity.rule.RuleResult;
import com.financeRadar.manticore.entity.transaction.TransactionRiskResult;
import com.financeRadar.manticore.logs.LokiLogger;
import com.financeRadar.manticore.service.policy.RiskPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Сервис представляющий движок правил
 * TODO дописать
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Service
@RequiredArgsConstructor
public class RuleEngineServiceImpl implements RuleEngineService{

    private final RiskPolicy riskPolicy;
    private final RuleManager ruleManager;
    private final LokiLogger lokiLogger;

    public TransactionRiskResult checkTransaction(TransactionalEventWrapper event) {
        long startTime = System.currentTimeMillis();

        lokiLogger.logTransaction(event.getCorrelationId(), event.getTransactionalId().toString(), "PROCESSING_START",
                Map.of("rulesCount", ruleManager.getRules().size()));

        List<RuleResult> ruleResults = ruleManager.getRules().stream()
                .map(rule -> {
                    RuleResult result = rule.evaluate(event);
                    lokiLogger.logRuleExecution(
                            event.getCorrelationId(),
                            event.getTransactionalId().toString(),
                            result,
                            rule.getVersion());
                    return result;
                })
                .toList();

        RiskDecision riskDecision = riskPolicy.evaluate(ruleResults);

        long processingTime = System.currentTimeMillis() - startTime;

        return TransactionRiskResult.builder()
                .transactionId(event.getTransactionalId())
                .correlationId(event.getCorrelationId())
                .riskDecision(riskDecision)
                .ruleResults(ruleResults)
                .processingTimeMs(processingTime)
                .evaluatedAt(Instant.now())
                .build();

    }
}