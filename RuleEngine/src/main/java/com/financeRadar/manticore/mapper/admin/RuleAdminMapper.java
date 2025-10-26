package com.financeRadar.manticore.mapper.admin;

import com.financeRadar.manticore.dto.admin.RuleAdminViewDto;
import com.financeRadar.manticore.entity.rule.Rule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования Rule в AdminViewDto
 *
 * @author bozya
 * @since 25.10.2025
 */
@Component
public class RuleAdminMapper {

    public RuleAdminViewDto toAdminViewDto(Rule rule) {
        if (rule == null) {
            return null;
        }

        return new RuleAdminViewDto(
                rule.getId(),
                rule.getName(),
                rule.getDescription(),
                rule.getEnabled(),
                rule.getRuleType(),
                rule.getPriority(),
                rule.getExpression(),
                rule.getVersion(),
                rule.getCreatedAt(),
                rule.getUpdatedAt()
        );
    }

    public List<RuleAdminViewDto> toAdminViewDtoList(List<Rule> rules) {
        return rules.stream()
                .map(this::toAdminViewDto)
                .collect(Collectors.toList());
    }
}
