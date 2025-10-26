package com.financeRadar.manticore.entity.rule;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * RiskDecision — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 23.10.2025
 */
@Getter
@Setter
@Builder
@ToString
public class RiskDecision {
    private boolean isFraud;
    private RiskLevel riskLevel;
    private Double riskScore;
    private String reason;
    private List<String> triggeredRules;
    private boolean requiresReview;
}