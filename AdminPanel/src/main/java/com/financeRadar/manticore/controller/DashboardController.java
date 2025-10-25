package com.financeRadar.manticore.controller;

import com.financeRadar.manticore.dto.DashboardStatsDto;
import com.financeRadar.manticore.dto.TransactionViewDto;
import com.financeRadar.manticore.service.DashboardService;
import com.financeRadar.manticore.service.TransactionalFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Контроллер для главной страницы (Dashboard)
 *
 * @author bozya
 * @since 25.10.2025
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String redirectToDashboard() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        model.addAttribute("stats", stats);
        return "dashboard";
    }
}
