package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.Marker;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(of = {"email"})
@NotNull(groups = Marker.OnUpdate.class)
public class User {
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class)
    private Long id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Почта должна содержать символ @")
    private String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    @NotNull(message = "День рождения не может быть пустым полем")
    private LocalDate birthday;
}
