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

import java.time.Duration;
import java.util.HashSet;
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

    private final Set<String> ALLOWED_METHODS = Set.of(
            "count", "amountSum", "isNight", "isWeekend"
    );

    private final Set<String> ALLOWED_FIELDS = Set.of(
            "amount", "userId", "currency", "timestamp",
            "senderId", "createdAt", "userAgent"
    );

    private final Set<String> ALLOWED_DURATIONS = Set.of(
            "PT1M", "PT5M", "PT10M", "PT15M", "PT20M"
    );

    @Override
    public boolean isValid(String expression, ConstraintValidatorContext context) {
        if (expression == null || expression.isBlank()) {
            return true;
        }

        try {
            Expression expr = new SpelExpressionParser().parseExpression(expression);

            String dangerousMethods = getDangerousMethods(expression);
            if (!dangerousMethods.isEmpty()) {
                context.buildConstraintViolationWithTemplate("Запрещенные методы: " + dangerousMethods)
                        .addConstraintViolation();
                return false;
            }

            String dangerousFields = getDangerousFields(expression);
            if (!dangerousFields.isEmpty()) {
                context.buildConstraintViolationWithTemplate("Запрещенные поля: " + dangerousFields)
                        .addConstraintViolation();
                return false;
            }

            String invalidDurations = getInvalidDurations(expression);
            if (!invalidDurations.isEmpty()) {
                context.buildConstraintViolationWithTemplate("Недопустимые форматы времени: " + invalidDurations)
                        .addConstraintViolation();
                return false;
            }

            if (containsDangerousConstructs(expression)) {
                context.buildConstraintViolationWithTemplate("Выражение содержит потенциально опасные конструкции")
                        .addConstraintViolation();
                return false;
            }

            return true;

        } catch (ParseException e) {
            context.buildConstraintViolationWithTemplate("Синтаксическая ошибка: " + e.getMessage())
                    .addConstraintViolation();
            return false;
        }
    }

    private String getDangerousMethods(String expression) {
        Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*(?=\\()");
        Matcher matcher = pattern.matcher(expression);
        Set<String> dangerous = new HashSet<>();

        while (matcher.find()) {
            String method = matcher.group();
            if (!ALLOWED_METHODS.contains(method) && !isSystemKeyword(method)) {
                dangerous.add(method);
            }
        }
        return String.join(", ", dangerous);
    }

    private String getDangerousFields(String expression) {
        Pattern pattern = Pattern.compile("(?<!\\.)\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b(?!\\()");
        Matcher matcher = pattern.matcher(expression);
        Set<String> dangerous = new HashSet<>();

        while (matcher.find()) {
            String fieldName = matcher.group(1);

            if (ALLOWED_METHODS.contains(fieldName) ||
                    isSystemKeyword(fieldName) ||
                    isNumber(fieldName) ||
                    fieldName.equals("null")) {
                continue;
            }

            if (!ALLOWED_FIELDS.contains(fieldName)) {
                dangerous.add(fieldName);
            }
        }
        return String.join(", ", dangerous);
    }

    private String getInvalidDurations(String expression) {
        Pattern pattern = Pattern.compile("'([^']*)'");
        Matcher matcher = pattern.matcher(expression);
        Set<String> invalid = new HashSet<>();

        while (matcher.find()) {
            String quotedString = matcher.group(1);

            if (isPotentialDuration(quotedString) && !isValidDuration(quotedString)) {
                invalid.add("'" + quotedString + "'");
            }
        }
        return String.join(", ", invalid);
    }

    private boolean isPotentialDuration(String str) {
        return str.matches("P.*|PT.*");
    }

    private boolean isValidDuration(String durationStr) {
        try {
            Duration duration = Duration.parse(durationStr);

            return duration != null &&
                    !duration.isNegative() &&
                    duration.toDays() <= 7 &&
                    ALLOWED_DURATIONS.contains(durationStr);

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isSystemKeyword(String word) {
        return Set.of("true", "false", "null", "new", "instanceof",
                "and", "or", "not", "eq", "ne", "lt", "le", "gt", "ge").contains(word);
    }

    private boolean isNumber(String word) {
        return word.matches("-?\\d+(\\.\\d+)?");
    }

    private boolean containsDangerousConstructs(String expression) {
        Pattern dangerousPattern = Pattern.compile(
                "T\\([^)]*\\)|" +
                        "new\\s+[A-Z]|" +
                        "instanceof|" +
                        "\\.getClass\\(\\)|" +
                        "@[a-zA-Z]|" +
                        "java\\.|" +
                        "System\\.|" +
                        "Runtime\\.|" +
                        "\\$\\{",
                Pattern.CASE_INSENSITIVE
        );

        return dangerousPattern.matcher(expression).find();
    }
}