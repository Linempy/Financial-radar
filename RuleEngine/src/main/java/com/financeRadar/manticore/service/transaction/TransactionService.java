package com.financeRadar.manticore.service.transaction;

import com.financeRadar.manticore.entity.transaction.TransactionStatus;
import com.financeRadar.manticore.repository.sql.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TransactionService — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 26.10.2025
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    @Transactional
    public void updateStatus(Long id, Boolean isFraud, TransactionStatus result) {
        repository.updateStatus(id, isFraud, result.name());
    }
}