package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private Film film;
    private Set<ConstraintViolation<Film>> violations;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @BeforeEach
    void init() {
        film = Film.builder()
                .name("Avengers")
                .description("Hero movie")
                .releaseDate(LocalDate.of(2012, 4, 11))
                .duration(Duration.ofMinutes(137))
                .build();
    }

    @Test
    @DisplayName("Create film")
    public void shouldCreateFilm() {
        violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Create film without name")
    public void shouldThrowExceptionFilmWithoutName() {
        film.setName(null);
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Название фильма не может быть пустым", violation.getMessage());
    }

    @Test
    @DisplayName("Create film with long description")
    public void shouldThrowExceptionFilmWithLongDescription() {
        film.setDescription("a".repeat(201));
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("Максимальная длина описания — 200 символов", violation.getMessage());
    }

    @Test
    @DisplayName("Create film with wrong release date")
    public void shouldThrowExceptionFilmWithWrongReleaseDate() {
        film.setReleaseDate(LocalDate.of(1894, 4, 11));
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("releaseDate", violation.getPropertyPath().toString());
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", violation.getMessage());
    }

    @Test
    @DisplayName("Create film with negative duration")
    public void shouldThrowExceptionFilmWithNegativeDuration() {
        film.setDuration(Duration.ofMinutes(-1));
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("duration", violation.getPropertyPath().toString());
        assertEquals("Продолжительность фильма должна быть положительным числом", violation.getMessage());
    }
}