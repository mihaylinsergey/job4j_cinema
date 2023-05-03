package ru.job4j.cinema.controller;

import  net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "/users/registration";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        String rsl;
        try {
            userService.save(user);
            rsl = "redirect:/cinema/index";
        } catch (Exception exception) {
            model.addAttribute("message", "Пользователь с email " + user.getEmail() + " уже существует");
            rsl = "errors/404";
        }
        return rsl;
    }

   @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        String rsl;
        var userOptional = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Почта или пароль введены неверно");
            rsl = "users/login";
        }
        var session = request.getSession();
        session.setAttribute("user", userOptional.get());
        rsl = "redirect:/cinema/index";
        return rsl;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
