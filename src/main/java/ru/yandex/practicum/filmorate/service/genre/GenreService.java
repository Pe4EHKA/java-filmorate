package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreService {
    Genre getGenre(long genreId);

    Collection<Genre> getAllGenres();
}
