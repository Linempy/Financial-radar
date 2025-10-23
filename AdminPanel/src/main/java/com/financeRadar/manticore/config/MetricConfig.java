package com.financeRadar.manticore.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MetricConfig — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author bozya
 * @since 23.10.2025
 */
@Configuration
public class MetricConfig {

    @Bean
    public Timer transactionProcessingTimer(MeterRegistry meterRegistry) {
        return Timer.builder("transaction_processing_duration")
                .publishPercentiles(0.5, 0.95)  // p50 и p95
                .register(meterRegistry);
    }
}