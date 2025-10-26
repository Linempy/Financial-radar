package com.financeRadar.manticore.config.headerFilter;

import com.financeRadar.manticore.repository.redis.TransactionRedisRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
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
@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class IdempotencyKeyFilter extends OncePerRequestFilter {

    private final TransactionRedisRepository transactionalRepository;

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
        log.info("Ключ является новым: {}", isNewKey);

        if (!isNewKey) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"status\":\"already_processed\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

//    private final IdempotencyService idempotencyService;
//    private final ObjectMapper objectMapper;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String idempotencyKey = httpRequest.getHeader("Idempotency-Key");
//
//        if (idempotencyKey == null) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        String requestHash = generateRequestHash(httpRequest);
//
//        boolean acquired = idempotencyService.(idempotencyKey, requestHash);
//
//        if (!acquired) {
//            // 🔥 Ключ уже существует - возвращаем сохраненный результат
//            Optional<Object> cachedResult = idempotencyService.getResult(idempotencyKey);
//
//            if (cachedResult.isPresent()) {
//                // Возвращаем сохраненный успешный результат
//                writeCachedResponse(httpResponse, cachedResult.get());
//                return;
//            } else {
//                // Запрос еще в обработке или с другим хешем
//                httpResponse.setStatus(409); // Conflict
//                httpResponse.getWriter().write("{\"error\": \"Request with same idempotency key is processing\"}");
//                return;
//            }
//        }
//
//        // 🔥 Новый ключ - продолжаем обработку
//        try {
//            chain.doFilter(request, response);
//        } catch (Exception e) {
//            idempotencyService.markAsFailed(idempotencyKey);
//            throw e;
//        }
//    }
//
//    private String generateRequestHash(HttpServletRequest request) throws IOException {
//        String method = request.getMethod();
//        String path = request.getRequestURI();
//        String query = request.getQueryString();
//        String bodyHash = hashRequestBody(request);
//
//        return method + ":" + path + ":" + query + ":" + bodyHash;
//    }

    private boolean isModifyingMethod(String method) {
        return Arrays.asList("POST", "PUT", "PATCH", "DELETE").contains(method);
    }

}