package com.financeRadar.manticore.repository;

import com.financeRadar.manticore.entity.ProcessingAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProcessingAuditRepository — описание интерфейса.
 * <p>
 * TODO: описать, какие обязанности реализует интерфейс.
 * </p>
 *
 * @author Linempy
 * @since 21.10.2025
 */
public interface ProcessingAuditRepository extends JpaRepository<ProcessingAuditEntity, Long> {
}