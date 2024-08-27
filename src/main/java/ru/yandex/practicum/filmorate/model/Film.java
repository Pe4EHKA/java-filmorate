package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.Marker;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValidation;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder
@EqualsAndHashCode(of = {"id", "name"})
public class Film {
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class)
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;

    @ReleaseDateValidation
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;

    @NotNull(message = "У фильма должен быть указан рейтинг MPA")
    private Mpa mpa;

    private LinkedHashSet<Genre> genres;
}
