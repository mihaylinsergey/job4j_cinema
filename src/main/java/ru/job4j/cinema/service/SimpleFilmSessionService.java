package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@ThreadSafe
@Service
public class SimpleFilmSessionService implements FilmSessionService {

    private final FilmSessionRepository filmSessionRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public SimpleFilmSessionService(FilmSessionRepository filmSessionRepository, FilmRepository filmRepository,
                                    HallRepository hallRepository) {
        this.filmSessionRepository = filmSessionRepository;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        return filmSessionRepository
                .findAll()
                .stream()
                .map(x -> new FilmSessionDto(
                        x.getId(),
                        x.getFilmId(),
                        filmRepository.findById(x.getFilmId()).get().getName(),
                        hallRepository.findById(x.getHallsId()).get().getName(),
                        x.getStartTime(),
                        x.getEndTime()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FilmSessionDto> getFilmSessionById(int id) {
        var filmSession = filmSessionRepository.getFileById(id).get();
        return Optional.ofNullable(new FilmSessionDto(
                filmSession.getId(),
                filmSession.getFilmId(),
                filmRepository.findById(filmSession.getFilmId()).get().getName(),
                hallRepository.findById(filmSession.getHallsId()).get().getName(),
                filmSession.getStartTime(),
                filmSession.getEndTime()
        ));
    }
}

