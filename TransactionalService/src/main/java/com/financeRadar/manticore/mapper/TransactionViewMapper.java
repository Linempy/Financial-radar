package com.financeRadar.manticore.mapper;

import com.financeRadar.manticore.dto.TransactionViewDto;
import com.financeRadar.manticore.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования Transaction в ViewDto
 *
 * @author bozya
 * @since 25.10.2025
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class TransactionViewMapper {

    public TransactionViewDto toViewDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        //todo ЗАХАРДКОЖЕНО
        return new TransactionViewDto(
                transaction.getId(),
                "1",
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDescription(),
                transaction.getStatus(),
                transaction.getIsFraud(),
                transaction.getSenderId(),
                transaction.getReceiverId(),
                transaction.getIpAddress(),
                transaction.getUserAgent(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    /**
     * Преобразование списка Entity в список ViewDto
     */
    public List<TransactionViewDto> toViewDtoList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::toViewDto)
                .collect(Collectors.toList());
    }
}
