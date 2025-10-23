package com.financeRadar.manticore.service;

import com.financeRadar.manticore.entity.RuleResult;

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

    RuleResult evaluate();
}