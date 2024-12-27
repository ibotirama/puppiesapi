package com.redthread.puppiesapi.service;

import com.redthread.puppiesapi.dto.LoginRequest;
import com.redthread.puppiesapi.dto.LoginResponse;
import com.redthread.puppiesapi.dto.UserDTO;
import com.redthread.puppiesapi.model.User;
import com.redthread.puppiesapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUser() {
        UserDTO userDTO = new UserDTO("John Doe", "john.doe@example.com", "password");
        User user = new User(userDTO.name(), userDTO.email(), "encodedPassword");
        when(passwordEncoder.encode(userDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = authService.createUser(userDTO);

        assertEquals(userDTO.name(), createdUser.getName());
        assertEquals(userDTO.email(), createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
    }

    @Test
    void shouldLogin() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password");
        User user = new User("John Doe", "john.doe@example.com", "encodedPassword");
        when(userRepository.findByUsername(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("token");

        LoginResponse loginResponse = authService.login(loginRequest);

        assertEquals("token", loginResponse.token());
    }

    @Test
    void shouldFailLoginIfUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password");
        when(userRepository.findByUsername(loginRequest.email())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldFailLoginIfThePasswordIsInvalid() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password");
        User user = new User("John Doe", "john.doe@example.com", "encodedPassword");
        when(userRepository.findByUsername(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Invalid password", exception.getMessage());
    }
}