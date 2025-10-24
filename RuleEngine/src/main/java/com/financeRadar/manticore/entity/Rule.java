package com.financeRadar.manticore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
@NoArgsConstructor
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

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}