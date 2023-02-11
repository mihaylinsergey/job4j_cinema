package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
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
                .map(x -> new FilmDto(
                        x.getId(),
                        x.getName(),
                        x.getDescription(),
                        x.getYear(),
                        x.getMinimalAge(),
                        x.getDurationInMinutes(),
                        genreRepository.findById(x.getGenreId()).get().getName(),
                        x.getFileId()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        var film = filmRepository.findById(id).get();
        return Optional.ofNullable(new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getYear(),
                film.getMinimalAge(),
                film.getDurationInMinutes(),
                genreRepository.findById(film.getGenreId()).get().getName(),
                film.getFileId()
        ));
    }
}
