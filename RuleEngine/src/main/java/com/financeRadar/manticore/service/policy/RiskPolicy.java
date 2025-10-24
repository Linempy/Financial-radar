package com.financeRadar.manticore.service.policy;

import com.financeRadar.manticore.entity.RiskDecision;
import com.financeRadar.manticore.entity.RuleResult;

import java.util.List;

/**
 * RiskPolicy — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 23.10.2025
 */
public interface RiskPolicy {
    RiskDecision evaluate(List<RuleResult> ruleResults);
}