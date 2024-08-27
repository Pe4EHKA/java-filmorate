package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"fromUserId", "toUserId"})
@Builder
public class Friendship {
    @NotNull
    private Long fromUserId;

    @NotNull
    private Long toUserId;

    @NotNull
    private Boolean accepted;
}
