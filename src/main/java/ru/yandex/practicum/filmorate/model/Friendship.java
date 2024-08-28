package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"userId", "friendId"})
@Builder
public class Friendship {
    @NotNull
    private Long userId;

    @NotNull
    private Long friendId;

    @NotNull
    private Boolean accepted;
}
