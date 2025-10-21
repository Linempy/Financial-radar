package com.financeRadar.manticore.service;

import com.financeRadar.manticore.dto.RuleCreateDto;
import com.financeRadar.manticore.dto.RuleUpdateDto;
import com.financeRadar.manticore.dto.RuleViewDto;
import com.financeRadar.manticore.entity.Rule;
import com.financeRadar.manticore.mapper.RuleMapper;
import com.financeRadar.manticore.repository.RuleRepository;
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

    private final RuleRepository repository;
    private final RuleMapper mapper;

    @Override
    @Transactional
    public String create(RuleCreateDto dto) {
        Rule rule = mapper.toEntity(dto);
        Rule savedRule = repository.save(rule);
        return savedRule.getId().toString();
    }

    @Override
    @Transactional(readOnly = true)
    public RuleViewDto get(Long id) {
        Rule rule = repository.findByIdOrThrow(id);
        return mapper.toDto(rule);
    }

    @Override
    @Transactional
    public RuleViewDto update(Long id, RuleUpdateDto dto) {
        Rule rule = repository.findByIdOrThrow(id);
        mapper.update(rule, dto);
        Rule savedRule = repository.save(rule);
        return mapper.toDto(savedRule);
    }
}