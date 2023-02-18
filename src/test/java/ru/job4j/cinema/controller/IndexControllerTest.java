package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IndexControllerTest {

    @Test
    public void whenGetIndexAndGetStartPage() {
        var expectedUser = new User(1, "FullName", "Email", "Password");

        var model = new ConcurrentModel();
        var session = mock(HttpSession.class);
        when(session.getAttribute(any(String.class))).thenReturn(expectedUser);
        var view = new IndexController().getIndex(model, session);
        var actualUser = model.getAttribute("user");

        assertThat(view).isEqualTo("cinema/index");
        assertThat(actualUser).isEqualTo(expectedUser);
    }

}