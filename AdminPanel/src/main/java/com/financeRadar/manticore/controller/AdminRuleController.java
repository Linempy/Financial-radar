package com.financeRadar.manticore.controller;

import com.financeRadar.manticore.dto.RuleCreateDto;
import com.financeRadar.manticore.dto.RuleUpdateDto;
import com.financeRadar.manticore.dto.admin.RuleAdminViewDto;
import com.financeRadar.manticore.entity.rule.RuleType;
import com.financeRadar.manticore.service.admin.AdminRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Контроллер для управления правилами в админ-панели
 *
 * @author bozya
 * @since 25.10.2025
 */
@Controller
@RequestMapping("/admin/rules")
@RequiredArgsConstructor
public class AdminRuleController {

    private final AdminRuleService adminRuleService;

    @GetMapping
    public String getRules(Model model) {
        List<RuleAdminViewDto> rules = adminRuleService.getAllRules();
        model.addAttribute("rules", rules);
        return "rules";
    }

    @GetMapping("/{id}")
    public String getRuleDetails(@PathVariable("id") Long id, Model model) {
        RuleAdminViewDto rule = adminRuleService.getRuleById(id);
        model.addAttribute("rule", rule);
        return "rule-card";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ruleCreateDto", new RuleCreateDto("", "", true, 1, "", RuleType.THRESHOLD));
        model.addAttribute("ruleTypes", RuleType.values());
        return "rule-create";
    }

    @PostMapping
    public String createRule(@Valid @ModelAttribute RuleCreateDto dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ruleTypes", RuleType.values());
            return "rule-create";
        }

        try {
            String ruleId = adminRuleService.createRule(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Правило успешно создано");
            return "redirect:/admin/rules/" + ruleId;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при создании правила: " + e.getMessage());
            model.addAttribute("ruleTypes", RuleType.values());
            return "rule-create";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        RuleAdminViewDto rule = adminRuleService.getRuleById(id);

        RuleUpdateDto updateDto = new RuleUpdateDto(
                rule.name(),
                rule.description(),
                rule.priority(),
                rule.enabled(),
                rule.expression(),
                rule.ruleType()
        );

        model.addAttribute("ruleId", id);
        model.addAttribute("ruleUpdateDto", updateDto);
        model.addAttribute("ruleTypes", RuleType.values());
        return "rule-edit";
    }

    @PostMapping("/{id}")
    public String updateRule(@PathVariable("id") Long id,
                             @Valid @ModelAttribute RuleUpdateDto dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ruleId", id);
            model.addAttribute("ruleTypes", RuleType.values());
            return "rule-edit";
        }

        try {
            adminRuleService.updateRule(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Правило успешно обновлено");
            return "redirect:/admin/rules/" + id;
        } catch (Exception e) {
            model.addAttribute("ruleId", id);
            model.addAttribute("errorMessage", "Ошибка при обновлении: " + e.getMessage());
            model.addAttribute("ruleTypes", RuleType.values());
            return "rule-edit";
        }
    }

    @PostMapping("/{id}/toggle")
    public String toggleStatus(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            adminRuleService.toggleRuleStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Статус правила изменён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/admin/rules";
    }

    @PostMapping("/{id}/delete")
    public String deleteRule(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            adminRuleService.deleteRule(id);
            redirectAttributes.addFlashAttribute("successMessage", "Правило удалено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении: " + e.getMessage());
        }
        return "redirect:/admin/rules";
    }
}
