package com.redthread.puppiesapi.service;

import com.redthread.puppiesapi.dto.LoginRequest;
import com.redthread.puppiesapi.dto.LoginResponse;
import com.redthread.puppiesapi.dto.UserDTO;
import com.redthread.puppiesapi.exception.LoginFailureException;
import com.redthread.puppiesapi.model.User;
import com.redthread.puppiesapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User createUser(UserDTO userDTO) {
        User user = new User(userDTO.name(), userDTO.email(), passwordEncoder.encode(userDTO.password()));
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.email())
                .orElseThrow(() -> new LoginFailureException("User not found"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new LoginFailureException("Invalid password");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }
}