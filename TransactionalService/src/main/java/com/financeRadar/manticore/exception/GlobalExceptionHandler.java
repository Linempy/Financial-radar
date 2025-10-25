package com.financeRadar.manticore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений
 *
 * TODO: дописать что обрабатывает
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<Map<String, Object>> handleDataValidation(DataValidationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Data Validation Error");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(org.springframework.data.redis.RedisConnectionFailureException.class)
    public ResponseEntity<Map<String, Object>> handleRedisException(
            org.springframework.data.redis.RedisConnectionFailureException ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Redis Connection Error");
        errorResponse.put("message", "Ошибка подключения к Redis в транзакционном сервисе");

        log.error("Ошибка соединения Redis: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler(org.springframework.kafka.KafkaException.class)
    public ResponseEntity<Map<String, Object>> handleKafkaException(
            org.springframework.kafka.KafkaException ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Kafka Processing Error");
        errorResponse.put("message", "Ошибка обработки сообщения Kafka в транзакционном сервисе");

        log.error("Сбой обработки Kafka: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Неожиданная ошибка: ", ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Transactional Service Internal Error");
        errorResponse.put("message", "Внутренняя ошибка транзакционного сервиса");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}