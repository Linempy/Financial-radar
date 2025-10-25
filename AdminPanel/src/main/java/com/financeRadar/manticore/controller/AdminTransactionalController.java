package com.financeRadar.manticore.controller;

import com.financeRadar.manticore.dto.TransactionViewDto;
import com.financeRadar.manticore.service.TransactionalFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * AdminTransactionalController — контроллер для транзакций на админ панели
 *
 * @author bozya
 * @since 23.10.2025
 */
@Controller
@RequestMapping("/admin/transactions")
@RequiredArgsConstructor
public class AdminTransactionalController {
    private final TransactionalFacadeService transactionalFacadeService;

    @GetMapping
    public String getTransactions(Model model) {
        List<TransactionViewDto> transactions = transactionalFacadeService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "transactions";
    }

    @GetMapping("/{id}")
    public String getTransactionCard(@PathVariable String id, Model model) {
        TransactionViewDto transaction = transactionalFacadeService.getTransactionById(id);
        model.addAttribute("transaction", transaction);
        return "transaction-card";
    }

}
