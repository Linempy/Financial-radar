package com.financeRadar.manticore.service.rule;

import com.financeRadar.manticore.dto.RuleCreateDto;
import com.financeRadar.manticore.dto.RuleUpdateDto;
import com.financeRadar.manticore.dto.RuleViewDto;
import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import com.financeRadar.manticore.entity.Rule;

import java.util.List;

/**
 * Интерфейс взаимодействия с сущностью {@link Rule}
 *
 * @author Linempy
 * @since 19.10.2025
 */
public interface RuleService {
    String create(RuleCreateDto dto);

    RuleViewDto get(Long id);

    RuleViewDto update(Long id, RuleUpdateDto dto);

    List<RuleRedisDto> getFromRedisOrDb();
}