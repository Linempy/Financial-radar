package com.financeRadar.manticore.mapper;

import com.financeRadar.manticore.dto.RequestContext;
import com.financeRadar.manticore.dto.TransactionCreateDto;
import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.ZoneOffset;

/**
 * Маппер для преобразования DTO в Event транзакции
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    @Mapping(target = "amount", source = "dto.amount")
    @Mapping(target = "currency", source = "dto.currency")
    @Mapping(target = "senderId", source = "dto.senderId")
    @Mapping(target = "receiverId", source = "dto.receiverId")
    @Mapping(target = "createdAt", source = "dto.createdAt")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "userAgent", source = "context.userAgent")
    @Mapping(target = "ipAddress", source = "context.ip")
    Transaction toEntity(TransactionCreateDto dto, RequestContext context);

    default Transaction enrichWithRequestData(Transaction transaction, RequestContext context) {
        transaction.setIpAddress(context.ip());
        transaction.setUserAgent(context.userAgent());

        return transaction;
    }

    default TransactionRiskCheckEvent toEvent(TransactionCreateDto dto,
                                              String transactionId,
                                              String correlationId,
                                              String idempotencyKey) {
        return new TransactionRiskCheckEvent(
                String.valueOf(dto.amount()),
                dto.senderId().toString(),
                dto.receiverId().toString(),
                transactionId,
                correlationId,
                idempotencyKey,
                dto.currency(),
                dto.createdAt().atZone(ZoneOffset.UTC).toInstant()
        );
    }
}