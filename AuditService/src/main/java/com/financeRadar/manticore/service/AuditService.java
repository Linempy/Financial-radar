package com.financeRadar.manticore.service;

import com.financeRadar.manticore.context.ProcessingContext;
import com.financeRadar.manticore.entity.ProcessingAuditEntity;
import com.financeRadar.manticore.entity.ProcessingStatus;
import com.financeRadar.manticore.entity.ProcessingStepEntity;
import com.financeRadar.manticore.mapper.AuditMapper;
import com.financeRadar.manticore.repository.ProcessingAuditRepository;
import com.financeRadar.manticore.repository.ProcessingStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AuditService — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 20.10.2025
 */
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditMapper mapper;
    private final ProcessingAuditRepository auditRepository;
    private final ProcessingStepRepository stepRepository;


    @Transactional
    public void saveProcessingContext(ProcessingContext context,
                                      ProcessingStatus status,
                                      String nameModule) {
        ProcessingAuditEntity audit = mapper.toAuditEntity(context, status, nameModule, LocalDateTime.now());
        auditRepository.save(audit);

        List<ProcessingStepEntity> stepEntities = mapper.toStepEntities(context.getSteps(), audit);
        stepRepository.saveAll(stepEntities);
    }
}