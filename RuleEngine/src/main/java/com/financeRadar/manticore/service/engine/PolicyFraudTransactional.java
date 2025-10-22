package com.financeRadar.manticore.service.engine;

import com.financeRadar.manticore.dto.avro.TransactionalRiskCheckEvent;
import com.financeRadar.manticore.entity.RuleResult;

import java.util.List;

/**
 * PolicyFraudTransactional — описание интерфейса.
 * <p>
 * TODO: описать, какие обязанности реализует интерфейс.
 * </p>
 *
 * @author Linempy
 * @since 22.10.2025
 */
public class PolicyFraudTransactional {

    public boolean firstMatch(List<SpELRule> rules, TransactionalRiskCheckEvent event) {
        for (SpELRule rule : rules) {
            if (rule.evaluate(event).triggered()) {
                return true;
            }
        }
        return false;
    }
}