package com.financeRadar.manticore.config.context;

import com.financeRadar.manticore.logs.LokiLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Фильтра для создания correlationId транзакции
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CorrelationFilter extends OncePerRequestFilter {

    private final LokiLogger lokiLogger;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {


        if (!shouldAssignCorrelationId(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String correlationId = request.getHeader("X-Correlation-ID");
        boolean isNewCorrelationId = false;
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
            isNewCorrelationId = true;
        }

        CorrelationContext.setCorrelationId(correlationId);
        response.setHeader("X-Correlation-ID", correlationId);

        try {
            lokiLogger.logCorrelationAssigned(correlationId, isNewCorrelationId,
                    isNewCorrelationId ? "generated" : "header");
            log.info("Запрос был принят, correlationId: {}", correlationId);
            filterChain.doFilter(request, response);
        } finally {
            CorrelationContext.clear();
        }
    }

    private boolean shouldAssignCorrelationId(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();

        return "POST".equalsIgnoreCase(method) &&
                path.equals("/api/v1/transactions");
    }
}