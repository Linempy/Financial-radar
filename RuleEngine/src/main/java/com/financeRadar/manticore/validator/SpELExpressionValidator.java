package com.financeRadar.manticore.validator;

import com.financeRadar.manticore.aop.ValidSpELExpression;
import com.financeRadar.manticore.dto.RuleCreateDto;
import com.financeRadar.manticore.dto.RuleUpdateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Валидатор для SpEL выражения
 * Используется в {@link RuleCreateDto} и {@link RuleUpdateDto}
 *
 * @author Linempy
 * @since 19.10.2025
 */
@Component
public class SpELExpressionValidator implements ConstraintValidator<ValidSpELExpression, String> {

    private final Set<String> ALLOWED_FUNCTIONS = Set.of(
            "redis.txCount", "redis.txSum", "redis.txAvg", "time.isNight"
    );

    @Override
    public boolean isValid(String expression, ConstraintValidatorContext context) {
        if (expression == null || expression.isBlank()) {
            return true;
        }

        try {
            Expression expr = new SpelExpressionParser().parseExpression(expression);

            if (containsDangerousMethods(expression)) {
                context.buildConstraintViolationWithTemplate("Выражение содержит запрещенные методы")
                        .addConstraintViolation();
                return false;
            }

            return true;

        } catch (ParseException e) {
            context.buildConstraintViolationWithTemplate("Синтаксическая ошибка в выражении: " + e.getMessage())
                    .addConstraintViolation();
            return false;
        }
    }

    private boolean containsDangerousMethods(String expression) {
        Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*(?=\\()");
        Matcher matcher = pattern.matcher(expression);

        while (matcher.find()) {
            String method = matcher.group();
            if (!ALLOWED_FUNCTIONS.contains(method) &&
                    !method.equals("tx") && !method.equals("ctx")) {
                return true;
            }
        }
        return false;
    }
}