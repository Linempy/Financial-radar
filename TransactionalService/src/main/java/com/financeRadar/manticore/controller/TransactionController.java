package com.financeRadar.manticore.controller;

import com.financeRadar.manticore.dto.RequestContext;
import com.financeRadar.manticore.dto.TransactionCreateDto;
import com.financeRadar.manticore.service.transactions.TransactionServiceImpl;
import com.financeRadar.manticore.validate.TransactionValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для взаимодействия с транзакциями
 *
 * @author Linempy
 * @since 18.10.2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionServiceImpl service;
    private final TransactionValidator validator;

    @PostMapping
    public ResponseEntity<Void> createTransactionWithCheckRisk(
            @RequestBody @Valid TransactionCreateDto dto,
            HttpServletRequest request) {
        validator.validate(dto);
        RequestContext context = new RequestContext(request.getRemoteAddr(), request.getHeader("User-Agent"));
        service.createWithCheckRisk(dto, context);
        return ResponseEntity.ok().build();
    }
}