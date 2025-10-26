package com.financeRadar.manticore.repository.sql;

import com.financeRadar.manticore.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Репозиторий для работы с транзакциями
 *
 * @author Linempy
 * @since 26.10.2025
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE transactions
            SET is_fraud = :isFraud, status = :status
            WHERE id = :id
            """)
    void updateStatus(@Param("id") Long id,
                      @Param("isFraud") Boolean isFraud,
                      @Param("status") String status);
}