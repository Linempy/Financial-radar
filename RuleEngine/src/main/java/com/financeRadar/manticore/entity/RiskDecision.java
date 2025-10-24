package com.financeRadar.manticore.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
public class RiskDecision {
    private boolean isFraud;
    private RiskLevel riskLevel;
    private Double riskScore;
    private String reason;
    private List<String> triggeredRules;
    private boolean requiresReview;
}