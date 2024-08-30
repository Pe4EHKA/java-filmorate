package ru.yandex.practicum.filmorate.repository.film_like;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

public interface FilmLikeRepository {
    void addLikeFilm(long filmId, long userId);

    void deleteLikeFilm(long filmId, long userId);

    Collection<Like> getFilmLikes(long filmId);
}
