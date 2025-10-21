package com.financeRadar.manticore.context;

import com.financeRadar.manticore.entity.ProcessingStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Объект для бизнес логики, для сохранения audit транзакции
 *
 * @author Linempy
 * @since 20.10.2025
 */
public final class ProcessingContext {
    @Getter
    private final String correlationId;
    @Getter
    private final LocalDateTime startTime;
    private final List<ProcessingStep> steps;
    private final Map<String, Object> metadata;

    public ProcessingContext(String correlationId) {
        this.correlationId = correlationId;
        this.startTime = LocalDateTime.now();
        this.steps = new ArrayList<>();
        this.metadata = new HashMap<>();
    }

    public void addStep(String stepName, ProcessingStatus status, String details) {
        steps.add(new ProcessingStep(stepName, status, details, null));
    }

    public void addStep(String stepName, ProcessingStatus status, String details, Long durationMs) {
        steps.add(new ProcessingStep(stepName, status, details, durationMs));
    }

    public List<ProcessingStep> getSteps() {
        return List.copyOf(steps);
    }

    public void putMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public Map<String, Object> getMetadata() {
        return Map.copyOf(metadata);
    }

}