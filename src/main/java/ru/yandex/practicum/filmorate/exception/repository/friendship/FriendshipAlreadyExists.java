package ru.yandex.practicum.filmorate.exception.repository.friendship;

public class FriendshipAlreadyExists extends RuntimeException {
    public static final String FRIENDSHIP_ALREADY_EXISTS = "Invite for friendship from " +
            "userId: %d to userId: %d already exists";

    public FriendshipAlreadyExists(String message) {
        super(message);
    }
}
