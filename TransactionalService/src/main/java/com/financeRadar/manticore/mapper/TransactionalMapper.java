package com.financeRadar.manticore.mapper;

import com.financeRadar.manticore.dto.TransactionalRiskCheckDto;
import com.financeRadar.manticore.dto.avro.TransactionalRiskCheckEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.ZoneOffset;

/**
 * Маппер для преобразования DTO в Event транзакции
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionalMapper {

    default TransactionalRiskCheckEvent toEvent(TransactionalRiskCheckDto dto, String correlationId) {
        return new TransactionalRiskCheckEvent(
                String.valueOf(dto.amount()),
                correlationId,
                dto.createdAt().atZone(ZoneOffset.UTC).toInstant()
        );
    }
}