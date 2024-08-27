package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.repository.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.Collection;

@Slf4j
@Service
public class GenreDbService implements GenreService {
    private final GenreRepository genreRepository;

    @Autowired
    public GenreDbService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre getGenre(long genreId) {
        log.debug("getGenre: genreId={}", genreId);
        if (!genreRepository.containsGenre(genreId)) {
            log.warn("Genre with id {} not found", genreId);
            throw new GenreNotFoundException(String.format(GenreNotFoundException.GENRE_NOT_FOUND, genreId));
        }
        return genreRepository.getGenre(genreId)
                .orElseThrow(() -> new GenreNotFoundException(String
                        .format(GenreNotFoundException.GENRE_NOT_FOUND, genreId)));
    }

    @Override
    public Collection<Genre> getAllGenres() {
        log.debug("Get all genres");
        return genreRepository.getAllGenres();
    }
}
