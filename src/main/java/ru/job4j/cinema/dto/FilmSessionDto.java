package ru.job4j.cinema.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class FilmSessionDto {

    private int id;
    private int filmId;
    private String film;
    private String halls;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public FilmSessionDto() {
    }

    public FilmSessionDto(int id, int filmId, String film, String halls, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.filmId = filmId;
        this.film = film;
        this.halls = halls;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getHalls() {
        return halls;
    }

    public void setHalls(String halls) {
        this.halls = halls;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilmSessionDto that = (FilmSessionDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FilmSessionDto{"
               + "id=" + id
               + ", filmId=" + filmId
               + ", film='" + film + '\''
               + ", halls='" + halls + '\''
               + ", startTime=" + startTime
               + ", endTime=" + endTime
               + '}';
    }
}
