package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRegisterThenGetMainPage() {
        var expectedUser = new User(1, "testFullName", "testEmail", "testPassword");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(expectedUser));

        var model = new ConcurrentModel();
        var view = userController.register(expectedUser, model);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/cinema/index");
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    public void whenUserRegisterThenGetErrorPageWithMessage() {
        var user = new User(1, "testFullName", "testEmail", "testPassword");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        var expectedException = new RuntimeException("Пользователь с email " + user.getEmail() + " уже существует");
        when(userService.save(userArgumentCaptor.capture())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = userController.register(user, model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }
}