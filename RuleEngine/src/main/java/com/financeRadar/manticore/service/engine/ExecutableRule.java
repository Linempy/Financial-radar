package com.financeRadar.manticore.service.engine;

import com.financeRadar.manticore.dto.avro.TransactionalRiskCheckEvent;
import com.financeRadar.manticore.entity.RuleResult;
import com.financeRadar.manticore.entity.RuleType;

/**
 * ExecutableRule — описание интерфейса.
 * <p>
 * TODO: описать, какие обязанности реализует интерфейс.
 * </p>
 *
 * @author Linempy
 * @since 19.10.2025
 */
public interface ExecutableRule {
    Long getId();
    RuleType getType();
    Integer getPriority();
    RuleResult evaluate(TransactionalRiskCheckEvent tx);
}