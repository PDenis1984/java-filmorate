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
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User validUser;
    private User updatedUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        validUser = User.builder()
                .id(1L)
                .email("test@denis.com")
                .login("testlogin")
                .name("Test User")
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .build();

        updatedUser = User.builder()
                .id(1L)
                .email("updated@denis.com")
                .login("updatedlogin")
                .name("Updated User")
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .build();
    }

    @Test
    void createUserWithValidDataTest() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(validUser);

        String userJson = """
            {
                "email": "test@denis.com",
                "login": "testlogin",
                "name": "Test User",
                "dateOfBirth": "1980-01-01"
            }
            """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@denis.com"))
                .andExpect(jsonPath("$.login").value("testlogin"))
                .andExpect(jsonPath("$.name").value("Test User"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void getUserWithExistingIdTest() throws Exception {
        when(userService.getUser(1L)).thenReturn(validUser);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@denis.com"))
                .andExpect(jsonPath("$.login").value("testlogin"));

        verify(userService, times(1)).getUser(1L);
    }

    @Test
    void getUserWithNonExistingIdTest() throws Exception {
        when(userService.getUser(999L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(999L);
    }

    @Test
    void updateUserWithValidDataTest() throws Exception {
        when(userService.updateUser(any(User.class), eq(1L))).thenReturn(updatedUser);

        String updatedUserJson = """
            {
                "email": "updated@denis.com",
                "login": "updatedlogin",
                "name": "Updated User",
                "dateOfBirth": "1980-01-01"
            }
            """;

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("updated@denis.com"))
                .andExpect(jsonPath("$.login").value("updatedlogin"))
                .andExpect(jsonPath("$.name").value("Updated User"));

        verify(userService, times(1)).updateUser(any(User.class), eq(1L));
    }

    @Test
    void updateUserWithNonExistingIdTest() throws Exception {
        when(userService.updateUser(any(User.class), eq(999L))).thenReturn(validUser);

        String userJson = """
            {
                "email": "test@denis.com",
                "login": "testlogin",
                "name": "Test User",
                "dateOfBirth": "1980-01-01"
            }
            """;

        mockMvc.perform(put("/api/v1/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(any(User.class), eq(999L));
    }

    @Test
    void getAllUsersWhenUsersExistTest() throws Exception {
        List<User> users = List.of(validUser, updatedUser);
        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("test@denis.com"))
                .andExpect(jsonPath("$[1].id").value(1L))
                .andExpect(jsonPath("$[1].email").value("updated@denis.com"));

        verify(userService, times(1)).getUsers();
    }

    @Test
    void getAllUsersWhenNoUsersTest() throws Exception {
        when(userService.getUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userService, times(1)).getUsers();
    }

    @Test
    void createUserWithInvalidEmailTest() throws Exception {
        String invalidUserJson = """
            {
                "email": "invalid-email",
                "login": "validlogin",
                "name": "Test User",
                "dateOfBirth": "1980-01-01"
            }
            """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    void createUserWithEmptyEmailTest() throws Exception {
        String invalidUserJson = """
            {
                "email": "",
                "login": "validlogin",
                "name": "Test User",
                "dateOfBirth": "1980-01-01"
            }
            """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    void createUserWithEmptyLoginTest() throws Exception {
        String invalidUserJson = """
            {
                "email": "test@denis.com",
                "login": "",
                "name": "Test User",
                "dateOfBirth": "1980-01-01"
            }
            """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    void createUserWithLoginContainingSpacesTest() throws Exception {
        String invalidUserJson = """
            {
                "email": "test@denis.com",
                "login": "login with spaces",
                "name": "Test User",
                "dateOfBirth": "1980-01-01"
            }
            """;

        when(userService.createUser(any(User.class)))
                .thenThrow(new ValidationException("Логин не должен содержать пробелы"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());


        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void createUserWithFutureBirthdayTest() throws Exception {
        String futureDate = LocalDate.now().plusDays(1).toString();
        String invalidUserJson = String.format("""
            {
                "email": "test@denis.com",
                "login": "validlogin",
                "name": "Test User",
                "dateOfBirth": "%s"
            }
            """, futureDate);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    void updateUserWithInvalidDataTest() throws Exception {
        String invalidUserJson = """
            {
                "email": "invalid-email",
                "login": "",
                "name": "Test User",
                "dateOfBirth": "1980-01-01"
            }
            """;

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(any(User.class), anyLong());
    }

    @Test
    void createUserWithNullEmailTest() throws Exception {
        String invalidUserJson = """
            {
                "login": "validlogin",
                "name": "Test User",
                "dateOfBirth": "1980-01-01"
            }
            """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    void createUserWithNullLoginTest() throws Exception {
        String invalidUserJson = """
            {
                "email": "test@denis.com",
                "name": "Test User",
                "dateOfBirth": "1980-01-01"
            }
            """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(User.class));
    }
}