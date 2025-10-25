package com.financeRadar.manticore.service;

import com.financeRadar.manticore.config.context.CorrelationContext;
import com.financeRadar.manticore.dto.RequestContext;
import com.financeRadar.manticore.dto.TransactionCreateDto;
import com.financeRadar.manticore.dto.TransactionViewDto;
import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.entity.Transaction;
import com.financeRadar.manticore.entity.TransactionStatus;
import com.financeRadar.manticore.mapper.TransactionMapper;
import com.financeRadar.manticore.mapper.TransactionViewMapper;
import com.financeRadar.manticore.producer.TransactionCheckFraudProducer;
import com.financeRadar.manticore.repository.sql.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для взаимодействия с транзакциями
 *
 * @author Linempy
 * @since 18.10.2025
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionCheckFraudProducer producer;
    private final TransactionMapper mapper;
    private final TransactionViewMapper viewMapper;
    private final TransactionRepository repository;

    @Override
    @Transactional
    public void createWithChechRisk(TransactionCreateDto dto, RequestContext context) {

        Transaction transaction = mapper.toEntity(dto, context, CorrelationContext.getCorrelationId());
        transaction.setStatus(TransactionStatus.FRAUD_CHECKING);
        Transaction savedTransaction = repository.save(transaction);

        TransactionRiskCheckEvent event = mapper.toEvent(
                dto,
                savedTransaction.getId().toString(),
                CorrelationContext.getCorrelationId()
        );

        producer.sendMessage(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionViewDto> getAllTransactionsForView() {
        List<Transaction> transactions = repository.findAll();
        return viewMapper.toViewDtoList(transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionViewDto getTransactionByIdForView(Long id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Транзакция с ID " + id + " не найдена"));
        return viewMapper.toViewDto(transaction);
    }
}