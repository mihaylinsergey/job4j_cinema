package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.service.*;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileControllerTest {

    private FileService fileService;

    private FileController fileController;
    private MultipartFile testFileDto;

    @BeforeEach
    public void initServices() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
        testFileDto = new MockMultipartFile("test", new byte[] {1, 2});
    }

    @Test
    public void whenFindFileDtoByIdThenGetIt() throws IOException {
        var fileDto = new FileDto(testFileDto.getName(), testFileDto.getBytes());
        var expectedFileDto = ResponseEntity.ok(fileDto.getContent());
        when(fileService.getFileById(any(Integer.class))).thenReturn(Optional.of(fileDto));

        var actualFileDto = fileController.getById(any(Integer.class));

        assertThat(actualFileDto).isEqualTo(expectedFileDto);
    }

    @Test
    public void whenFindFileDtoByIdThenGetNotFound() {
        var expectedFileDto = ResponseEntity.notFound().build();
        when(fileService.getFileById(any(Integer.class))).thenReturn(Optional.empty());

        var actualFileDto = fileController.getById(any(Integer.class));

        assertThat(actualFileDto).isEqualTo(expectedFileDto);
    }
}