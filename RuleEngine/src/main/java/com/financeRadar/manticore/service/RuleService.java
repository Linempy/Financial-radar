package com.financeRadar.manticore.service;

import com.financeRadar.manticore.dto.RuleCreateDto;
import com.financeRadar.manticore.dto.RuleUpdateDto;
import com.financeRadar.manticore.dto.RuleViewDto;
import com.financeRadar.manticore.entity.Rule;
import jakarta.validation.Valid;

/**
 * RuleService — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 19.10.2025
 */
public interface RuleService {
    String create(RuleCreateDto dto);

    RuleViewDto get(Long id);

    RuleViewDto update(Long id, RuleUpdateDto dto);
}