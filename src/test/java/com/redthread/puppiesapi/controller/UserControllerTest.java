package com.redthread.puppiesapi.controller;

import com.redthread.puppiesapi.model.User;
import com.redthread.puppiesapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRetrieveUserProfile() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserProfile(userId);

        assertEquals(ResponseEntity.ok(user), response);
    }

    @Test
    void shouldReturnNotFoundIfTheUserDoesNotExist() {
        Long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserProfile(userId);

        assertEquals(ResponseEntity.notFound().build(), response);
    }
}