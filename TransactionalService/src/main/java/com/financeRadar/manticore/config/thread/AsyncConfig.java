package com.financeRadar.manticore.config.thread;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Конфигурация для создания пула потоков
 *
 * @author Linempy
 * @since 06.08.2025
 */

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean("afterCommitExecutor")
    public ThreadPoolTaskExecutor afterCommitExecutor(
            @Value("${thread-pool.async.commit.core-pool-size}") int corePoolSize,
            @Value("${thread-pool.async.commit.max-pool-size}") int maxPoolSize,
            @Value("${thread-pool.async.commit.queue-capacity}") int queueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setQueueCapacity(queueCapacity);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);

        executor.setThreadNamePrefix("After-Commit-Async-");
        executor.initialize();
        return executor;
    }
}