package ru.yandex.practicum.filmorate.repository.film_like;

public interface FilmLikeRepository {
    void addLikeFilm(long film_id, long user_id);

    void deleteLikeFilm(long film_id, long user_id);

    int countLikeFilm(long film_id);

    boolean isLikeFilm(long film_id, long user_id);
}
