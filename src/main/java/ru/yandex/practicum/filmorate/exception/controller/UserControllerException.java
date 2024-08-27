package ru.yandex.practicum.filmorate.exception.controller;

public class UserControllerException extends RuntimeException {
    public static final String MUTUAL_FRIENDS_AMONG_YOURSELF =
            "Пользовазователь с ID: %d не может запоросить близких друзей у самого себя";
    public static final String DELETE_YOURSELF_FROM_FRIENDS =
            "Пользователь с ID: %d не может удалить самого сея из друзей";
    public static final String ADD_YOURSELF = "Пользователь с ID: %d не может добавить самого себя в друзья";

    public UserControllerException(String message) {
        super(message);
    }
}
