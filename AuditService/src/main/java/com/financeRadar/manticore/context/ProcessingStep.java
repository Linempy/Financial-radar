package com.financeRadar.manticore.context;

import com.financeRadar.manticore.entity.ProcessingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * ProcessingStep — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 20.10.2025
 */
@Getter
public final class ProcessingStep {
    private final String stepName;
    private final ProcessingStatus status;
    private final String details;
    private final LocalDateTime startAt;
    private final Long durationMs;

    public ProcessingStep(String stepName, ProcessingStatus status, String details, Long durationMs) {
        this.stepName = stepName;
        this.status = status;
        this.details = details;
        this.startAt = LocalDateTime.now();
        this.durationMs = durationMs;
    }
}