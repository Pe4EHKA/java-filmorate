package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> filmLikes = new HashMap<>();
    private long seq = 0;

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Get all films");
        return films.values();
    }

    @Override
    public Film getFilmById(long id) {
        log.info("Get film by id: {}", id);
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Film with id: %s not found".formatted(id));
        }
        log.info("Film with id: {} found and returned", id);
        return films.get(id);
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
        Film oldFilm = films.get(film.getId());
        if (oldFilm == null) throw new NotFoundException("Film not found");
        log.debug("Updating film: {}", oldFilm);
        if (film.getName() != null) oldFilm.setName(film.getName());
        if (film.getDescription() != null) oldFilm.setDescription(film.getDescription());
        if (film.getReleaseDate() != null) oldFilm.setReleaseDate(film.getReleaseDate());
        if (oldFilm.getDuration() != null) oldFilm.setDuration(film.getDuration());
        log.debug("Film updated: {}", film);
        return oldFilm;
    }

    @Override
    public void addLike(Film film, User user) {
        log.debug("Adding like to film: {}", film);
        Set<Long> likes = filmLikes.computeIfAbsent(film.getId(), k -> new HashSet<>());
        likes.add(user.getId());
        log.debug("Added like to film: {}", film);
        films.get(film.getId());
    }

    @Override
    public void removeLike(Film film, User user) {
        log.info("Removing like from film: {}", film);
        Set<Long> likes = filmLikes.computeIfAbsent(film.getId(), k -> new HashSet<>());
        likes.remove(user.getId());
        log.debug("Removed like from film: {}", film);
    }

    @Override
    public Collection<Film> getMostLikedFilms(int count) {
        log.debug("Get most liked films");
        return filmLikes.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().size(), Comparator.reverseOrder()))
                .limit(count)
                .map(entry -> films.get(entry.getKey()))
                .collect(Collectors.toList());
    }

    private Long generateNextId() {
        return ++seq;
    }
}
