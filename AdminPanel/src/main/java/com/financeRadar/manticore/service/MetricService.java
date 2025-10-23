package com.financeRadar.manticore.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * MetricService — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author bozya
 * @since 23.10.2025
 */
@Service
@RequiredArgsConstructor
public class MetricService {

    private final MeterRegistry meterRegistry;

    private final Timer transactionProcessingTimer;

    public void processTransaction(Runnable processLogic) {
        transactionProcessingTimer.record(processLogic);
    }

    public void incrementProcessed() {
        meterRegistry.counter("transactions_processed_total").increment();
    }

    public void incrementAlerted() {
        meterRegistry.counter("transactions_alerted_total").increment();
    }

    public void incrementReviewed() {
        meterRegistry.counter("transactions_reviewed_total").increment();
    }

    public void recordProcessingDuration(Runnable processLogic) {
        Timer timer = meterRegistry.timer("transaction_processing_duration");
        timer.record(processLogic);
    }
}