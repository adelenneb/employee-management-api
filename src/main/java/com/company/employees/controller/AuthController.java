package com.company.employees.controller;

import com.company.employees.config.JwtService;
import com.company.employees.entity.User;
import com.company.employees.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String username = request.username().trim();
        String rawPassword = request.password();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Login failed: user not found [{}]", username);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
                });

        try {
            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                log.warn("Login failed: bad password for [{}]", username);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
        } catch (IllegalArgumentException ex) { // e.g., stored password not a valid BCrypt hash
            log.error("Stored password hash is invalid for user [{}]", username, ex);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(Map.of("token", token));
    }

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {
    }
}
