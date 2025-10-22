package com.financeRadar.manticore.service.engine;

import com.financeRadar.manticore.dto.avro.TransactionalRiskCheckEvent;
import com.financeRadar.manticore.repository.redis.RuleCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис представляющий движок правил
 * TODO дописать
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Service
@RequiredArgsConstructor
public class RuleEngineServiceImpl {

    private final PolicyFraudTransactional policyFraud;
    private final RuleCacheRepository cacheRepository;

    public void checkTransaction(TransactionalRiskCheckEvent event) {
        // берем правила
        List<SpELRule> rules = cacheRepository.findAllRules();

    }


    private List<SpELRule>

}