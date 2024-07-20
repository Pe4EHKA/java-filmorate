package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.annotation.validator.DurationPositiveValidator;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DurationPositiveValidator.class)
public @interface DurationPositive {
    String message() default "Продолжительность фильма должна быть положительным числом";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
