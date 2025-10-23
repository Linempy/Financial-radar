package com.financeRadar.manticore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность представляющая собой правило
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Getter
@Setter
@Entity
@Table(name = "rules")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "rule_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RuleType ruleType;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "expression", columnDefinition = "TEXT", nullable = false)
    private String expression;

}