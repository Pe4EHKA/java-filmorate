package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.repository.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.Collection;

@Slf4j
@Service
public class MpaServiceImpl implements MpaService {
    private final MpaRepository mpaRepository;

    @Autowired
    public MpaServiceImpl(MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    @Override
    public Mpa getMpa(long mpaId) {
        log.debug("getMpa {}", mpaId);
        if (!mpaRepository.containsMpa(mpaId)) {
            log.warn("Mpa with id {} not found", mpaId);
            throw new MpaNotFoundException(String.format(MpaNotFoundException.MPA_NOT_FOUND, mpaId));
        }
        return mpaRepository.getMpa(mpaId)
                .orElseThrow(() -> new MpaNotFoundException(String.format(MpaNotFoundException.MPA_NOT_FOUND, mpaId)));
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        log.debug("getAllMpa");
        return mpaRepository.getAllMpa();
    }
}
