package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id", "name"})
@Builder
public class Genre {
    @NotNull
    private Long id;

    @NotNull
    private String name;
}
