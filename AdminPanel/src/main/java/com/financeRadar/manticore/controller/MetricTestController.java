package com.financeRadar.manticore.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MetricTestController — контроллер для тестирования бизнес-метрик.
 *
 * @author bozya
 * @since 23.10.2025
 */
@RestController
@RequestMapping("/metric")
public class MetricTestController {
    private final Counter processedCounter;
    private final Counter alertedCounter;
    private final Counter reviewedCounter;
    private final Timer processingTimer;

    public MetricTestController(MeterRegistry meterRegistry) {
        this.processedCounter = meterRegistry.counter("transactions_processed_total");
        this.alertedCounter = meterRegistry.counter("transactions_alerted_total");
        this.reviewedCounter = meterRegistry.counter("transactions_reviewed_total");
        this.processingTimer = Timer.builder("transaction_processing_duration")
                .publishPercentiles(0.5, 0.95)
                .register(meterRegistry);
    }

    @PostMapping("/processed")
    public ResponseEntity<String> incrementProcessed() {
        processedCounter.increment();
        return ResponseEntity.ok("Processed counter incremented");
    }

    @PostMapping("/alerted")
    public ResponseEntity<String> incrementAlerted() {
        alertedCounter.increment();
        return ResponseEntity.ok("Alerted counter incremented");
    }

    @PostMapping("/reviewed")
    public ResponseEntity<String> incrementReviewed() {
        reviewedCounter.increment();
        return ResponseEntity.ok("Reviewed counter incremented");
    }

    @PostMapping("/simulate-processing")
    public ResponseEntity<String> simulateProcessing() {
        processingTimer.record(() -> {
            try {
                Thread.sleep(200); // имитация работы
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        return ResponseEntity.ok("Processing time recorded");
    }
}