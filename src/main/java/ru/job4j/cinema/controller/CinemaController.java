package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;
import ru.job4j.cinema.service.TicketService;

import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Controller
@RequestMapping("/cinema")
public class CinemaController {

    private final FilmSessionService filmSessionService;
    private final FilmService filmService;

    private final HallService hallService;

    private final TicketService ticketService;

    public CinemaController(FilmSessionService filmSessionService, FilmService filmService, HallService hallService, TicketService ticketService) {
        this.filmSessionService = filmSessionService;
        this.filmService = filmService;
        this.hallService = hallService;
        this.ticketService = ticketService;
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        model.addAttribute("filmSessions", filmSessionService.findAll());
        return "cinema/shedule";
    }

    @GetMapping("/library")
    public String getLibrary(Model model) {
        model.addAttribute("filmDto", filmService.findAll());
        return "cinema/film_library";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var filmSessionOptional = filmSessionService.getFilmSessionById(id);
        System.out.println(filmSessionOptional.get());
        System.out.println(filmService.findById(id).get());
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Сеанс не найден");
            return "errors/404";
        }
        model.addAttribute("filmSession", filmSessionOptional.get());
        model.addAttribute("filmDto", filmService.findById(id).get());
        var hall = hallService.findByName(filmSessionOptional.get().getHalls()).get();
        List<Integer> rows = new ArrayList<>();
        List<Integer> places = new ArrayList<>();
        for (int i = 1; i <= hall.getRowCount(); i++) {
            rows.add(i);
        }
        for (int i = 1; i <= hall.getPlaceCount(); i++) {
            places.add(i);
        }
        model.addAttribute("rows", rows);
        model.addAttribute("places", places);
        return "cinema/buy_ticket";
    }

    @PostMapping("/buy")
    public String buyTicket(@ModelAttribute Ticket ticket, Model model) {
        var newTicket = ticketService.save(ticket);
        if (newTicket.isEmpty()) {
            model.addAttribute("message", "Не удалось приобрести билет на заданное место. "
                    + "Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова.");
            return "errors/404";
        }
        model.addAttribute("ticket", ticket);
        return "cinema/success";
    }
}