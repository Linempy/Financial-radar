package com.financeRadar.manticore.service.rule;

import com.financeRadar.manticore.dto.RuleCreateDto;
import com.financeRadar.manticore.dto.RuleUpdateDto;
import com.financeRadar.manticore.dto.RuleViewDto;
import com.financeRadar.manticore.entity.rule.Rule;
import com.financeRadar.manticore.mapper.RuleMapper;
import com.financeRadar.manticore.repository.RuleRepository;
import com.financeRadar.manticore.service.refresh.RuleRefreshService;
import com.financeRadar.manticore.utils.AfterCommitManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RuleRefreshService refreshService;
    private final AfterCommitManager afterCommitManager;
    private final RuleMapper mapper;

    @Override
    @Transactional
    public String create(RuleCreateDto dto) {
        Rule rule = mapper.toEntity(dto);
        Rule savedRule = ruleRepository.save(rule);
        log.info("Правило \"{}\" было создано", rule.getName());

        afterCommitManager.executeAfterCommit(refreshService::refreshAllRules);
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
    public RuleViewDto update(Long id, RuleUpdateDto dto) {
        Rule rule = ruleRepository.findByIdOrThrow(id);
        mapper.update(rule, dto);
        Rule savedRule = ruleRepository.save(rule);
        log.info("Правило \"{}\" было обновлено", rule.getName());

        afterCommitManager.executeAfterCommit(refreshService::refreshAllRules);
        return mapper.toDto(savedRule);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Rule rule = ruleRepository.findByIdOrThrow(id);
        ruleRepository.deleteById(id);
        log.info("Правило \"{}\" было удалено", rule.getName());
        afterCommitManager.executeAfterCommit(refreshService::refreshAllRules);
    }



}