package com.financeRadar.manticore.service.admin;

import com.financeRadar.manticore.dto.RuleCreateDto;
import com.financeRadar.manticore.dto.RuleUpdateDto;
import com.financeRadar.manticore.dto.admin.RuleAdminViewDto;
import com.financeRadar.manticore.entity.Rule;
import com.financeRadar.manticore.mapper.admin.RuleAdminMapper;
import com.financeRadar.manticore.repository.RuleRepository;
import com.financeRadar.manticore.service.rule.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с правилами в админ-панели
 *
 * @author bozya
 * @since 25.10.2025
 */
@Service
@RequiredArgsConstructor
public class AdminRuleService {

    private final RuleService ruleService;
    private final RuleRepository ruleRepository;
    private final RuleAdminMapper ruleAdminMapper;

    /**
     * Получить все правила
     */
    @Transactional(readOnly = true)
    public List<RuleAdminViewDto> getAllRules() {
        List<Rule> rules = ruleRepository.findAll();
        return ruleAdminMapper.toAdminViewDtoList(rules);
    }

    /**
     * Получить правило по ID
     */
    @Transactional(readOnly = true)
    public RuleAdminViewDto getRuleById(Long id) {
        Rule rule = ruleRepository.findByIdOrThrow(id);
        return ruleAdminMapper.toAdminViewDto(rule);
    }

    /**
     * Создать новое правило
     */
    @Transactional
    public String createRule(RuleCreateDto dto) {
        return ruleService.create(dto);
    }

    /**
     * Обновить правило
     */
    @Transactional
    public void updateRule(Long id, RuleUpdateDto dto) {
        ruleService.update(id, dto);
    }

    /**
     * Удалить правило
     */
    @Transactional
    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
    }

    /**
     * Переключить статус правила
     */
    @Transactional
    public void toggleRuleStatus(Long id) {
        Rule rule = ruleRepository.findByIdOrThrow(id);
        rule.setEnabled(!rule.getEnabled());
        ruleRepository.save(rule);
    }
}
