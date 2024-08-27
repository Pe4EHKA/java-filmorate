package ru.yandex.practicum.filmorate.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreRepository {
    Collection<Genre> getAllGenres();

    Optional<Genre> getGenre(long genreId);

    boolean containsGenre(long genreId);
}
