package ru.yandex.practicum.filmorate.repository.film_like;

public interface FilmLikeRepository {
    void addLikeFilm(long filmId, long userId);

    void deleteLikeFilm(long filmId, long userId);

    int countLikeFilm(long filmId);

    boolean isLikeFilm(long filmId, long userId);
}
