package com.redthread.puppiesapi.service;

import com.redthread.puppiesapi.model.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        user = new User();
        user.setUsername("jhondoe");
    }

    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken(user);
        assertNotNull(token);
    }

    @Test
    void shouldExtractUsername() {
        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);
        assertEquals(user.getUsername(), username);
    }

    @Test
    void shouldIsTokenValid() {
        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void shouldIsTokenExpired() {
        String token = jwtService.generateToken(user);
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void shouldExtractClaim() {
        String token = jwtService.generateToken(user);
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
        assertNotNull(expiration);
    }

    @Test
    void shouldGenerateTokenWithExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "admin");
        String token = jwtService.generateToken(extraClaims, user);
        assertNotNull(token);
    }
}