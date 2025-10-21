package com.financeRadar.manticore.mapper;

import com.financeRadar.manticore.dto.RuleCreateDto;
import com.financeRadar.manticore.dto.RuleUpdateDto;
import com.financeRadar.manticore.dto.RuleViewDto;
import com.financeRadar.manticore.dto.redis.RuleRedisDto;
import com.financeRadar.manticore.entity.Rule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Маппер для преобразования DTO в сущность и наоборот, обновления сущности
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RuleMapper {

    Rule toEntity(RuleCreateDto dto);

    RuleViewDto toDto(Rule rule);

    void update(@MappingTarget Rule rule, RuleUpdateDto dto);

    RuleRedisDto toRedisDto(Rule rule);

    default List<RuleRedisDto> toRedisDtos(List<Rule> rules) {
        return rules.stream()
                .map(this::toRedisDto)
                .toList();
    }
}