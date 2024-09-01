package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"filmId", "userId"})
@Builder
public class Like {
    @NotNull
    private Long filmId;

    @NotNull
    private Long userId;
}
