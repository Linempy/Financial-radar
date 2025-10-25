package com.financeRadar.manticore.service.engine;

import com.financeRadar.manticore.dto.TransactionalEventWrapper;
import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.entity.RiskDecision;
import com.financeRadar.manticore.entity.RuleResult;
import com.financeRadar.manticore.entity.TransactionRiskResult;
import com.financeRadar.manticore.service.policy.RiskPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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

    public TransactionRiskResult checkTransaction(TransactionalEventWrapper event) {
        long startTime = System.currentTimeMillis();

        //TODO>>> логиии
        List<RuleResult> ruleResults = ruleManager.getRules().stream()
                .map(rule -> rule.evaluate(event))
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