package com.redthread.puppiesapi.controller;

import com.redthread.puppiesapi.dto.LoginRequest;
import com.redthread.puppiesapi.dto.LoginResponse;
import com.redthread.puppiesapi.dto.UserDTO;
import com.redthread.puppiesapi.exception.LoginFailureException;
import com.redthread.puppiesapi.model.User;
import com.redthread.puppiesapi.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterANewUser() {
        UserDTO userDTO = new UserDTO("John Doe", "john.doe@example.com", "password");
        User user = new User();
        when(authService.createUser(any(UserDTO.class))).thenReturn(user);

        ResponseEntity<User> response = authController.register(userDTO);

        assertEquals(ResponseEntity.ok(user), response);
    }

    @Test
    void shouldDoLogin() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password");
        LoginResponse loginResponse = new LoginResponse("token");
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        assertEquals(ResponseEntity.ok(loginResponse), response);
    }
    @Test

    void shouldFailLoginIfUserDoesNotExists() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password");
        when(authService.login(any(LoginRequest.class))).thenThrow(new LoginFailureException("User not found"));

        RuntimeException exception = assertThrows(LoginFailureException.class, () -> {
            authController.login(loginRequest);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldFailLoginIfPasswordIsInvalid() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password");
        when(authService.login(any(LoginRequest.class))).thenThrow(new LoginFailureException("Invalid password"));

        RuntimeException exception = assertThrows(LoginFailureException.class, () -> {
            authController.login(loginRequest);
        });

        assertEquals("Invalid password", exception.getMessage());
    }
}