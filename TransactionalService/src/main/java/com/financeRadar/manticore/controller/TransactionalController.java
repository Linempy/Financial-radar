package com.financeRadar.manticore.controller;

import com.financeRadar.manticore.dto.TransactionalRiskCheckDto;
import com.financeRadar.manticore.service.TransactionalServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactional")
public class TransactionalController {

    private final TransactionalServiceImpl service;

    @PostMapping
    public ResponseEntity<Void> checkTransactional(@RequestBody @Valid TransactionalRiskCheckDto dto) {
        service.handleTransactionalOnFraud(dto);
        return ResponseEntity.ok().build();
    }
}