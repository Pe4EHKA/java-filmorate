package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.annotation.Marker;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private long seq = 0;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Request for all films");
        return films.values();
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Request to create film: {}", film);
        if (film == null) {
            throw new ValidationException("Film is null");
        }

        log.debug("Creating film: {}", film);
        film.setId(generateNextId());
        films.put(film.getId(), film);
        log.debug("Film created: {}", film);
        return film;
    }

    @PutMapping
    @Validated({Marker.OnUpdate.class})
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Request to update film: {}", film);
        if (film == null) {
            throw new ValidationException("Film is null");
        }

        Film oldFilm = films.get(film.getId());
        if (oldFilm == null) throw new ValidationException("Film not found");
        log.debug("Updating film: {}", oldFilm);
        if (film.getName() != null) oldFilm.setName(film.getName());
        if (film.getDescription() != null) oldFilm.setDescription(film.getDescription());
        if (film.getReleaseDate() != null) oldFilm.setReleaseDate(film.getReleaseDate());
        if (oldFilm.getDuration() != null) oldFilm.setDuration(film.getDuration());
        log.debug("Film updated: {}", film);
        return oldFilm;
    }

    private Long generateNextId() {
        return ++seq;
    }
}
