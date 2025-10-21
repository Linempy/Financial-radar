package com.financeRadar.manticore.service;

import com.financeRadar.manticore.dto.TransactionalCreateDto;

/**
 * Интерфейс для взаимодействия с транзакциями
 *
 * @author Linempy
 * @since 18.10.2025
 */
public interface TransactionalService {

    void createWithChechRisk(TransactionalCreateDto dto);
}