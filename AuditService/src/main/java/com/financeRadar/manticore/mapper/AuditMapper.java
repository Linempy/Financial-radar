package com.financeRadar.manticore.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeRadar.manticore.context.ProcessingContext;
import com.financeRadar.manticore.context.ProcessingStep;
import com.financeRadar.manticore.entity.ProcessingAuditEntity;
import com.financeRadar.manticore.entity.ProcessingStatus;
import com.financeRadar.manticore.entity.ProcessingStepEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ProcessingAuditMapper — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 20.10.2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditMapper {

    private final ObjectMapper objectMapper;

    public ProcessingAuditEntity toAuditEntity(ProcessingContext context,
                                   ProcessingStatus status,
                                   String nameModule,
                                   LocalDateTime endTime) {
        ProcessingAuditEntity audit = ProcessingAuditEntity.builder()
                .correlationId(context.getCorrelationId())
                .totalDurationMs(Duration.between(context.getStartTime(), endTime).toMillis())
                .status(status)
                .serviceModule(nameModule)
                .startTime(context.getStartTime())
                .build();
        try {
            audit.setMetadata(objectMapper.writeValueAsString(context.getMetadata()));
        } catch (JsonProcessingException exception) {
            log.warn("Ошибка сериализации метаданных для correlationId: {}. Используется null значение",
                    context.getCorrelationId()
            );
            audit.setMetadata(null);
        }

        return audit;
    }

    public List<ProcessingStepEntity> toStepEntities(List<ProcessingStep> steps, ProcessingAuditEntity audit) {
        List<ProcessingStepEntity> stepEntities = new ArrayList<>();
        for (ProcessingStep step : steps) {
            ProcessingStepEntity stepEntity = ProcessingStepEntity.builder()
                    .audit(audit)
                    .details(step.getDetails())
                    .durationMs(step.getDurationMs())
                    .stepName(step.getStepName())
                    .status(step.getStatus())
                    .build();
            stepEntities.add(stepEntity);
        }
        return stepEntities;
    }

}