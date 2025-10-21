package com.financeRadar.manticore.controller;

import com.financeRadar.manticore.dto.RuleCreateDto;
import com.financeRadar.manticore.dto.RuleUpdateDto;
import com.financeRadar.manticore.dto.RuleViewDto;
import com.financeRadar.manticore.service.rule.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * REST-контроллера для чтения, записи, удаления, обновления правил
 *
 * @author Linempy
 * @since 19.10.2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rules")
public class RuleController {

    private final RuleService service;

    @PostMapping
    public ResponseEntity<Void> createRule(@RequestBody @Valid RuleCreateDto dto) {
        String ruleId = service.create(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ruleId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RuleViewDto> RuleViewDto(@PathVariable Long id) {
        RuleViewDto result = service.get(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RuleViewDto> updateRule(
            @PathVariable Long id,
            @RequestBody @Valid RuleUpdateDto dto) {
        RuleViewDto result = service.update(id, dto);
        return ResponseEntity.ok(result);
    }

}