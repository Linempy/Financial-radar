package com.financeRadar.manticore.dto;

import com.financeRadar.manticore.dto.avro.TransactionRiskCheckEvent;
import com.financeRadar.manticore.repository.redis.RuleCacheRepository;
import com.financeRadar.manticore.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * TransactionalEventWrapper — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 25.10.2025
 */
@Slf4j
@RequiredArgsConstructor
public class TransactionalEventWrapper {
    private final TransactionRiskCheckEvent event;
    private final RuleCacheRepository repository;

    public BigDecimal getAmount() {
        return new BigDecimal(event.getAmount());
    }

    public Long getSenderId() {
        return Long.valueOf(event.getSenderId());
    }

    public Long getUserId() {
        return Long.valueOf(event.getSenderId());
    }

    public Long getTransactionalId() {
        return Long.valueOf(event.getTransactionId());
    }

    public String getCorrelationId() {
        return event.getCorrelationId();
    }

    public String getCurrency() {
        return event.getCurrency();
    }

    public LocalDateTime getTimestamp() {
        return event.getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public LocalDateTime getCreatedAt() {
        return getTimestamp();
    }

    public Long count(Long userId, String durationString) {
        try {
            Duration window = Duration.parse(durationString); // 🔥 парсим строку
            return repository.getTransactionalContext(userId, window).countTransactional();
        } catch (Exception e) {
            log.warn("Ошибка парсинга duration {}: {}", durationString, e);
            return 0L;
        }
    }

    public BigDecimal amountSum(Long userId, String durationString) {
        try {
            Duration window = Duration.parse(durationString); // 🔥 парсим строку
            return repository.getTransactionalContext(userId, window).totalAmount();
        } catch (Exception e) {
            log.warn("Ошибка парсинга duration {}: {}", durationString, e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public Boolean isNight(LocalDateTime timestamp) {
        return TimeUtils.isNight(timestamp);
    }

    public Boolean isWeekend(LocalDateTime timestamp) {
        return TimeUtils.isWeekend(timestamp);
    }
}