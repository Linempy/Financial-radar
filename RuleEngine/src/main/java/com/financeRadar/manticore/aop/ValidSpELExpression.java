package com.financeRadar.manticore.aop;

import com.financeRadar.manticore.validator.SpELExpressionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = SpELExpressionValidator.class)
public @interface ValidSpELExpression {
    String message() default "Некорректное SpEL выражение";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
