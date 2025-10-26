package com.financeRadar.manticore.service.transactions;

import com.financeRadar.manticore.config.context.CorrelationContext;
import com.financeRadar.manticore.dto.RequestContext;
import com.financeRadar.manticore.dto.TransactionCreateDto;
import com.financeRadar.manticore.dto.TransactionViewDto;
import com.financeRadar.manticore.dto.TransactionStats;
import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.entity.Transaction;
import com.financeRadar.manticore.entity.TransactionStatus;
import com.financeRadar.manticore.mapper.TransactionMapper;
import com.financeRadar.manticore.mapper.TransactionViewMapper;
import com.financeRadar.manticore.producer.TransactionCheckFraudProducer;
import com.financeRadar.manticore.repository.sql.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import com.financeRadar.manticore.repository.redis.TransactionRedisRepository;
import com.financeRadar.manticore.repository.sql.TransactionRepostitory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.math.BigDecimal;

/**
 * Сервис для взаимодействия с транзакциями
 *
 * @author Linempy
 * @since 18.10.2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionCheckFraudProducer producer;
    private final TransactionMapper mapper;
    private final TransactionViewMapper viewMapper;
    private final TransactionRepository repository;
    private final TransactionRepostitory repository;
    private final TransactionRedisRepository redisRepository;

    @Override
    @Transactional
    public void createWithCheckRisk(TransactionCreateDto dto, RequestContext context) {
        Transaction transaction = mapper.toEntity(dto, context);
        transaction.setStatus(TransactionStatus.FRAUD_CHECKING);
        Transaction savedTransaction = repository.save(transaction);
        recordTransaction(dto.senderId(), dto.amount());

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


    public void recordTransaction(Long userId, BigDecimal amount) {
        TransactionStats result = redisRepository.recordTransaction(userId, amount);
        log.debug("Была записана транзакция для пользователя {}. Итого за 5 мин: {}",
                userId, result.countTransactional()
        );
    }
}