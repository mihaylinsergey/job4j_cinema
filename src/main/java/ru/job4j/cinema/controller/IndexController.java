package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@ThreadSafe
@Controller
@RequestMapping("/cinema")
public class IndexController {

    @GetMapping({"/", "/index"})
    public String getIndex() {
        return "cinema/index";
    }
}
