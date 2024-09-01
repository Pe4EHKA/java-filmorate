package ru.yandex.practicum.filmorate.exception.repository.friendship;

public class FriendshipNotFoundException extends RuntimeException {
    public static final String FRIENDSHIP_NOT_FOUND = "Invite for friendship from userId: %d to userId: %d not found";

    public FriendshipNotFoundException(String message) {
        super(message);
    }
}
