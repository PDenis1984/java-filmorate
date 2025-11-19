package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FilmControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;

    private Film validFilm;
    private Film updatedFilm;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();

        validFilm = Film.builder()
                .id(1L)
                .name("Test Film")
                .description("Test description")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(Duration.ofMinutes(120))
                .build();

        updatedFilm = Film.builder()
                .id(1L)
                .name("Updated Film")
                .description("Updated description")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(Duration.ofMinutes(150))
                .build();
    }

    @Test
    void createFilmWithValidDataTest() throws Exception {
        when(filmService.createFilm(any(Film.class))).thenReturn(validFilm);

        String filmJson = """
            {
                "name": "Test Film",
                "description": "Test description",
                "releaseDate": "1980-01-01",
                "duration": 290
            }
            """;

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Film"))
                .andExpect(jsonPath("$.description").value("Test description"));

        verify(filmService, times(1)).createFilm(any(Film.class));
    }

    @Test
    void getFilmWithExistingIdTest() throws Exception {
        when(filmService.getFilm(1L)).thenReturn(validFilm);

        mockMvc.perform(get("/api/v1/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Film"))
                .andExpect(jsonPath("$.description").value("Test description"));

        verify(filmService, times(1)).getFilm(1L);
    }

    @Test
    void getFilmWithNonExistingIdTest() throws Exception {
        when(filmService.getFilm(999L)).thenThrow(new FilmNotFoundException("Film not found"));

        mockMvc.perform(get("/api/v1/films/999"))
                .andExpect(status().isNotFound());

        verify(filmService, times(1)).getFilm(999L);
    }

    @Test
    void updateFilmWithValidDataTest() throws Exception {
        when(filmService.updateFilm(any(Film.class), eq(1L))).thenReturn(updatedFilm);

        String updatedFilmJson = """
            {
                "name": "Updated Film",
                "description": "Updated description",
                "releaseDate": "1980-01-01",
                "duration": 150
            }
            """;

        mockMvc.perform(put("/api/v1/films/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedFilmJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Film"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.duration").value(150));

        verify(filmService, times(1)).updateFilm(any(Film.class), eq(1L));
    }

    @Test
    void updateFilmWithNonExistingIdTest() throws Exception {
        when(filmService.updateFilm(any(Film.class), eq(999L))).thenReturn(validFilm);

        String filmJson = """
            {
                "name": "Test Film",
                "description": "Test description",
                "releaseDate": "1980-01-01",
                "duration": 120
            }
            """;

        mockMvc.perform(put("/api/v1/films/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isOk());

        verify(filmService, times(1)).updateFilm(any(Film.class), eq(999L));
    }

    @Test
    void getAllFilmsWhenFilmsExistTest() throws Exception {
        List<Film> films = List.of(validFilm, updatedFilm);
        when(filmService.getFilms()).thenReturn(films);

        mockMvc.perform(get("/api/v1/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Film"))
                .andExpect(jsonPath("$[1].id").value(1L))
                .andExpect(jsonPath("$[1].name").value("Updated Film"));

        verify(filmService, times(1)).getFilms();
    }

    @Test
    void getAllFilmsWhenNoFilmsTest() throws Exception {
        when(filmService.getFilms()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(filmService, times(1)).getFilms();
    }

    @Test
    void createFilmWithEmptyNameTest() throws Exception {
        String invalidFilmJson = """
            {
                "name": "",
                "description": "Test description",
                "releaseDate": "1980-01-01",
                "duration": 120
            }
            """;

        when(filmService.createFilm(any(Film.class)))
                .thenThrow(new ValidationException("Название фильма не должно быть пустым"));

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());


        verify(filmService, times(1)).createFilm(any(Film.class));
    }

    @Test
    void createFilmWithNullNameTest() throws Exception {
        String invalidFilmJson = """
            {
                "description": "Test description",
                "releaseDate": "1980-01-01",
                "duration": 120
            }
            """;
        when(filmService.createFilm(any(Film.class)))
                .thenThrow(new ValidationException("Название фильма не должно быть пустым"));

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());

        verify(filmService, times(1)).createFilm(any(Film.class));
    }

    @Test
    void createFilmWithTooLongDescriptionTest() throws Exception {
        String longDescription = "a".repeat(201);
        String invalidFilmJson = String.format("""
            {
                "name": "Test Film",
                "description": "%s",
                "releaseDate": "1980-01-01",
                "duration": 120
            }
            """, longDescription);

        when(filmService.createFilm(any(Film.class)))
                .thenThrow(new ValidationException("Описание не может превышать 200 символов"));

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());

        verify(filmService, times(1)).createFilm(any(Film.class));
    }

    @Test
    void createFilmWithInvalidReleaseDateTest() throws Exception {
        String invalidFilmJson = """
            {
                "name": "Test Film",
                "description": "Test description",
                "releaseDate": "1890-01-01",
                "duration": 120
            }
            """;

        when(filmService.createFilm(any(Film.class)))
                .thenThrow(new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года"));

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());

        verify(filmService, times(1)).createFilm(any(Film.class));
    }

    @Test
    void createFilmWithNegativeDurationTest() throws Exception {
        String invalidFilmJson = """
            {
                "name": "Test Film",
                "description": "Test description",
                "releaseDate": "1980-01-01",
                "duration": -120
            }
            """;

        when(filmService.createFilm(any(Film.class)))
                .thenThrow(new ValidationException("Продолжительность фильма должна быть положительной"));

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());

        verify(filmService, times(1)).createFilm(any(Film.class));
    }

    @Test
    void createFilmWithZeroDurationTest() throws Exception {
        String invalidFilmJson = """
            {
                "name": "Test Film",
                "description": "Test description",
                "releaseDate": "1980-01-01",
                "duration": 0
            }
            """;

        when(filmService.createFilm(any(Film.class)))
                .thenThrow(new ValidationException("Продолжительность фильма должна быть положительной"));

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());

        verify(filmService, times(1)).createFilm(any(Film.class));
    }

    @Test
    void updateFilmWithInvalidDataTest() throws Exception {

        Film existingFilm = Film.builder()
                .id(1L)
                .name("Existing Film")
                .description("Existing description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(Duration.ofMinutes(120))
                .build();

        when(filmService.updateFilm(any(Film.class), eq(1L)))
                .thenThrow(new ValidationException("Название фильма не может быть пустым"));

        String invalidFilmJson = """
        {
            "name": "",
            "description": "Test description",
            "releaseDate": "1980-01-01", 
            "duration": 120
        }
        """;

        mockMvc.perform(put("/api/v1/films/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());

        verify(filmService, times(1)).updateFilm(any(Film.class), eq(1L));
    }

    @Test
    void createFilmWithValidBoundaryDescriptionTest() throws Exception {
        String boundaryDescription = "a".repeat(200);
        String validFilmJson = String.format("""
            {
                "name": "Test Film",
                "description": "%s",
                "releaseDate": "1980-01-01",
                "duration": 120
            }
            """, boundaryDescription);

        when(filmService.createFilm(any(Film.class))).thenReturn(validFilm);

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFilmJson))
                .andExpect(status().isCreated());

        verify(filmService, times(1)).createFilm(any(Film.class));
    }

    @Test
    void createFilmWithEarliestValidReleaseDateTest() throws Exception {
        String validFilmJson = """
            {
                "name": "Test Film",
                "description": "Test description",
                "releaseDate": "1895-12-28",
                "duration": 120
            }
            """;

        when(filmService.createFilm(any(Film.class))).thenReturn(validFilm);

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFilmJson))
                .andExpect(status().isCreated());

        verify(filmService, times(1)).createFilm(any(Film.class));
    }
}