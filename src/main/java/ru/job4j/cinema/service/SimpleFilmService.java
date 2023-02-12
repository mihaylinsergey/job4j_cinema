package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmDto.*;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FileRepository;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@ThreadSafe
@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final FileRepository fileRepository;

    public SimpleFilmService(FilmRepository filmRepository, GenreRepository genreRepository, FileRepository fileRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmRepository
                .findAll()
                .stream()
                .map(x -> new Builder()
                                .buildId(x.getId())
                                .buildName(x.getName())
                                .buildDescription(x.getDescription())
                                .buildYear(x.getYear())
                                .buildMinimalAge(x.getMinimalAge())
                                .buildDurationInMinutes(x.getDurationInMinutes())
                                .buildGenre(genreRepository.findById(x.getGenreId()).get().getName())
                                .buildFileId(x.getFileId())
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        var film = filmRepository.findById(id).get();
        return Optional.ofNullable(new Builder()
                .buildId(film.getId())
                .buildName(film.getName())
                .buildDescription(film.getDescription())
                .buildYear(film.getYear())
                .buildMinimalAge(film.getMinimalAge())
                .buildDurationInMinutes(film.getDurationInMinutes())
                .buildGenre(genreRepository.findById(film.getGenreId()).get().getName())
                .buildFileId(film.getFileId())
                .build()
        );
    }
}
