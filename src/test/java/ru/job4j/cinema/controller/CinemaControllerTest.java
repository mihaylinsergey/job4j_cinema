package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CinemaControllerTest {

    private FilmSessionService filmSessionService;
    private FilmService filmService;
    private HallService hallService;
    private TicketService ticketService;
    private CinemaController cinemaController;

    @BeforeEach
    public void initServices() {
        filmSessionService = mock(FilmSessionService.class);
        filmService = mock(FilmService.class);
        hallService = mock(HallService.class);
        ticketService = mock(TicketService.class);
        cinemaController = new CinemaController(filmSessionService, filmService, hallService, ticketService);
    }

    @Test
    public void whenRequestScheduleThenGetPageWithFilmSessions() {
        var filmSession1 = new FilmSessionDto(1, 1, "testFilm1", "testHall1", LocalDateTime.now(), LocalDateTime.now());
        var filmSession2 = new FilmSessionDto(2, 2, "testFilm2", "testHall2", LocalDateTime.now(), LocalDateTime.now());
        var expectedFilmSessions = List.of(filmSession1, filmSession2);
        when(filmSessionService.findAll()).thenReturn(expectedFilmSessions);

        var model = new ConcurrentModel();
        var view = cinemaController.getSchedule(model);
        var actualFilmSession = model.getAttribute("filmSessions");

        assertThat(view).isEqualTo("cinema/shedule");
        assertThat(actualFilmSession).isEqualTo(expectedFilmSessions);
    }

    @Test
    public void whenRequestLibraryThenGetPageWithFilms() {
        var filmDto1 = new FilmDto.Builder()
                .buildId(1)
                .buildName("testName1")
                .buildDescription("testDescription1")
                .buildYear(2023)
                .buildMinimalAge(5)
                .buildDurationInMinutes(100)
                .buildGenre("testGenre1")
                .buildFileId(1)
                .build();
        var filmDto2 = new FilmDto.Builder()
                .buildId(2)
                .buildName("testName2")
                .buildDescription("testDescription2")
                .buildYear(2022)
                .buildMinimalAge(18)
                .buildDurationInMinutes(90)
                .buildGenre("testGenre2")
                .buildFileId(2)
                .build();
        var expectedFilmsDto = List.of(filmDto1, filmDto2);
        when(filmService.findAll()).thenReturn(expectedFilmsDto);

        var model = new ConcurrentModel();
        var view = cinemaController.getLibrary(model);
        var actualFilmDto = model.getAttribute("filmDto");

        assertThat(view).isEqualTo("cinema/film_library");
        assertThat(actualFilmDto).isEqualTo(expectedFilmsDto);
    }

    @Test
    public void whenFindByIdThenGetIt() {
        var expectedFilmSession = new FilmSessionDto(1, 1, "testFilm", "testHall", LocalDateTime.now(), LocalDateTime.now());
        var expectedFilmDto = new FilmDto.Builder()
                .buildId(1)
                .buildName("testName1")
                .buildDescription("testDescription1")
                .buildYear(2023)
                .buildMinimalAge(5)
                .buildDurationInMinutes(100)
                .buildGenre("testGenre1")
                .buildFileId(1)
                .build();
        var expectedHall = new Hall(1, "testName", 2, 2, "testDescription");
        var expectedRows = List.of(1, 2);
        var expectedPlaces = List.of(1, 2);

        when(filmSessionService.getFilmSessionById(any(Integer.class))).thenReturn(Optional.of(expectedFilmSession));
        when(filmService.findById(any(Integer.class))).thenReturn(Optional.of(expectedFilmDto));
        when(hallService.findByName(any(String.class))).thenReturn(Optional.of(expectedHall));

        var model = new ConcurrentModel();
        var view = cinemaController.getById(model, expectedFilmSession.getId());
        var actualFilmSession = model.getAttribute("filmSession");
        var actualFilmDto = model.getAttribute("filmDto");
        var actualRows = model.getAttribute("rows");
        var actualPlaces = model.getAttribute("places");

        assertThat(view).isEqualTo("cinema/buy_ticket");
        assertThat(actualFilmSession).isEqualTo(expectedFilmSession);
        assertThat(actualFilmDto).isEqualTo(expectedFilmDto);
        assertThat(actualRows).isEqualTo(expectedRows);
        assertThat(actualPlaces).isEqualTo(expectedPlaces);
    }

    @Test
    public void whenFindByIdThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException("Сеанс не найден");
        when(filmSessionService.getFilmSessionById(any(Integer.class))).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = cinemaController.getById(model, any(Integer.class));
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenBuyTicketAndThenGetIt() {
        var expectedTicket = new Ticket(1, 1, 1, 1, 1);
        var ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.save(ticketArgumentCaptor.capture())).thenReturn(Optional.of(expectedTicket));

        var model = new ConcurrentModel();
        var view = cinemaController.buyTicket(expectedTicket, model);
        var actualTicket = ticketArgumentCaptor.getValue();

        assertThat(view).isEqualTo("cinema/success");
        assertThat(actualTicket).isEqualTo(expectedTicket);
    }

    @Test
    public void whenBuyTicketThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException("Не удалось приобрести билет на заданное место. "
               + "Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова.");
        when(ticketService.save(any(Ticket.class))).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = cinemaController.buyTicket(any(Ticket.class), model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }
}