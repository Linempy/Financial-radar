package com.financeRadar.manticore.config.headerFilter;

import com.financeRadar.manticore.entity.model.IdempotencyResult;
import com.financeRadar.manticore.service.idempotencyKey.IdempotencyService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * Фильтр по {@code Idempotency-Key} для предотвращения дубликата транзакции
 *
 * @author Linempy
 * @since 18.10.2025
 */
@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class IdempotencyKeyFilter implements Filter {

    private final IdempotencyService idempotencyService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String idempotencyKey = httpRequest.getHeader("Idempotency-Key");

        if (shouldCheckIdempotency(httpRequest, idempotencyKey)) {
            IdempotencyResult result = idempotencyService.checkAndReserveKey(idempotencyKey);

            if (result.isProcessing()) {
                writeProcessingResponse(httpResponse);
                return;
            } else if (result.isCompleted()) {
                writeCachedResponse(httpResponse, result.getCachedResponse());
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void writeProcessingResponse(HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().write("""
            {
                "status": "processing",
                "message": "Request is being processed"
            }
            """);
    }

    private void writeCachedResponse(HttpServletResponse response, String cachedResponse) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().write(cachedResponse);
    }

    private boolean isModifyingMethod(String method) {
        return Objects.equals("POST", method);
    }

    private boolean shouldCheckIdempotency(HttpServletRequest request, String idempotencyKey) {
        return idempotencyKey != null && !idempotencyKey.isBlank() &&
                (isModifyingMethod(request.getMethod()));
    }

}