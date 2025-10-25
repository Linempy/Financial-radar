package com.financeRadar.manticore.repository.sql;

import com.financeRadar.manticore.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TransactionalRepostitory — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 23.10.2025
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}