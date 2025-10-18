package com.financeRadar.manticore.config.headerFilter;

import com.financeRadar.manticore.repository.redis.TransactionalRedisRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * Фильтр по {@code Idempotency-Key} для предотвращения дубликата транзакции
 *
 * @author Linempy
 * @since 18.10.2025
 */
@Component
@RequiredArgsConstructor
public class IdempotencyKeyFilter extends OncePerRequestFilter {

    private final TransactionalRedisRepository transactionalRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        if (!isModifyingMethod(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String idempotencyKey = request.getHeader("Idempotency-Key");

        if (idempotencyKey == null || idempotencyKey.trim().isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isNewKey = transactionalRepository.addIfAbsent(idempotencyKey);

        if (!isNewKey) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"status\":\"already_processed\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isModifyingMethod(String method) {
        return Arrays.asList("POST", "PUT", "PATCH", "DELETE").contains(method);
    }

}