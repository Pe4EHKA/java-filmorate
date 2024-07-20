package ru.yandex.practicum.filmorate.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.DurationPositive;

import java.time.Duration;

public class DurationPositiveValidator implements ConstraintValidator<DurationPositive, Duration> {
    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext constraintValidatorContext) {
        return duration.isPositive();
    }
}
