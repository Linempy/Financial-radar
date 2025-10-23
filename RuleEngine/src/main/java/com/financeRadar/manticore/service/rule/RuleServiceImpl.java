package com.financeRadar.manticore.service.rule;

import com.financeRadar.manticore.dto.RuleCreateDto;
import com.financeRadar.manticore.dto.RuleUpdateDto;
import com.financeRadar.manticore.dto.RuleViewDto;
import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import com.financeRadar.manticore.entity.Rule;
import com.financeRadar.manticore.mapper.RuleMapper;
import com.financeRadar.manticore.repository.RuleRepository;
import com.financeRadar.manticore.repository.redis.RuleCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для взаимодействия с сущностью {@link Rule}
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;
    private final RuleCacheRepository ruleCacheRepository;
    private final RuleMapper mapper;

    @Override
    @Transactional
    public String create(RuleCreateDto dto) {
        Rule rule = mapper.toEntity(dto);
        Rule savedRule = ruleRepository.save(rule);
        log.info("Правило \"{}\" было создано", rule.getName());
        return savedRule.getId().toString();
    }

    @Override
    @Transactional(readOnly = true)
    public RuleViewDto get(Long id) {
        Rule rule = ruleRepository.findByIdOrThrow(id);
        return mapper.toDto(rule);
    }

    @Override
    @Transactional
    public RuleViewDto  update(Long id, RuleUpdateDto dto) {
        Rule rule = ruleRepository.findByIdOrThrow(id);
        mapper.update(rule, dto);
        Rule savedRule = ruleRepository.save(rule);
        log.info("Правило \"{}\" было обновлено", rule.getName());
        return mapper.toDto(savedRule);
    }

    public List<RuleRedisDto> getFromRedisOrDb() {
        List<RuleRedisDto> rulesFromRedis = ruleCacheRepository.findAllRules();
        if (rulesFromRedis != null) {
            return rulesFromRedis;
        }

        List<Rule> ruleFromDb = ruleRepository.findAllByEnabledTrue();
        List<RuleRedisDto> rulesAfterMapper = mapper.toRedisDtos(ruleFromDb);
        ruleCacheRepository.saveRulesBatch(rulesAfterMapper);
        return rulesAfterMapper;
    }
}