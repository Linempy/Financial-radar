package com.financeRadar.manticore.service.engine;

import com.financeRadar.manticore.dto.TransactionalEventWrapper;
import com.financeRadar.manticore.entity.rule.RuleResult;
import com.financeRadar.manticore.entity.rule.RuleType;

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
    Long getVersion();
    Integer getPriority();
    RuleResult evaluate(TransactionalEventWrapper tx);
}