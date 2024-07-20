package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Set<ConstraintViolation<User>> violations;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @BeforeEach
    void init() {
        user = User.builder()
                .email("m@yandex.ru")
                .login("Pe4EHKA")
                .name("Valerij")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    @DisplayName("Create user")
    public void shouldCreateUser() {
        violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Create user with empty email")
    public void shouldThrowExceptionUserWithEmptyEmail() {
        user.setEmail("");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("Электронная почта не может быть пустой", violation.getMessage());
    }

    @Test
    @DisplayName("Create user with wrong email")
    public void shouldThrowExceptionUserWithWrongEmail() {
        user.setEmail("yandex.ru");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("Почта должна содержать символ @", violation.getMessage());
    }

    @Test
    @DisplayName("Create user with blank login")
    public void shouldThrowExceptionUserWithBlankLogin() {
        user.setLogin(" ");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("login", violation.getPropertyPath().toString());
        assertEquals("Логин не может быть пустым и содержать пробелы", violation.getMessage());
    }

    @Test
    @DisplayName("Create user with birthday in future")
    public void shouldThrowExceptionUserWithBirthdayInFuture() {
        user.setBirthday(LocalDate.of(2300, 1, 1));
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("birthday", violation.getPropertyPath().toString());
        assertEquals("Дата рождения не может быть в будущем", violation.getMessage());
    }
}