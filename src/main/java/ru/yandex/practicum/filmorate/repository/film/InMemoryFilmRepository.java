package ru.yandex.practicum.filmorate.repository.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmRepository implements FilmRepository {

    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> filmLikes = new HashMap<>();
    private long seq = 0;

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Get all films");
        return films.values();
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        log.info("Get film by id: {}", id);
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film createFilm(Film film) {
        log.debug("Creating film: {}", film);
        film.setId(generateNextId());
        films.put(film.getId(), film);
        log.debug("Film created: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Film oldFilm = getFilmById(film.getId())
                .orElseThrow(() -> new NotFoundException("Film with id " + film.getId() + " not found"));
        log.debug("Updating film: {}", oldFilm);
        if (film.getName() != null) oldFilm.setName(film.getName());
        if (film.getDescription() != null) oldFilm.setDescription(film.getDescription());
        if (film.getReleaseDate() != null) oldFilm.setReleaseDate(film.getReleaseDate());
        if (oldFilm.getDuration() != null) oldFilm.setDuration(film.getDuration());
        log.debug("Film updated: {}", film);
        return oldFilm;
    }

    @Override
    public LinkedHashSet<Genre> getFilmGenres(long filmId) {
        return new LinkedHashSet<>();
    }

    @Override
    public void addFilmGenres(long filmId, Collection<Genre> genres) {

    }

    private Long generateNextId() {
        return ++seq;
    }
}
