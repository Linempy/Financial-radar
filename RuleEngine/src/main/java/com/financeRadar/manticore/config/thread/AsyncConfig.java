package com.financeRadar.manticore.config.thread;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

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

    @Bean("RuleExecutor")
    public ThreadPoolTaskExecutor postgresTaskExecutor(
            @Value("${thread-pool.async.rule.core-pool-size}") int corePoolSize,
            @Value("${thread-pool.async.rule.max-pool-size}") int maxPoolSize,
            @Value("${thread-pool.async.rule.queue-capacity}") int queueCapacity
    ) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("Rule-Async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}