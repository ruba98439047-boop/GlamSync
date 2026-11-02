package com.glamsync;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.List;
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> data) {

        String name = data.get("name");
        String email = data.get("email");
        String password = data.get("password");

        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Please fill all fields"
                    ));
        }

        if (userRepository.existsByEmail(email.trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "message",
                            "Email already registered"
                    ));
        }

        User user = new User();

        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message",
                        "Registration successful!"
                ));
    }

    // LOGIN
@PostMapping("/login")
public ResponseEntity<?> login(
        @RequestBody Map<String, String> data,
        HttpServletRequest request,
        HttpServletResponse response) {

    String email = data.get("email");
    String password = data.get("password");

    if (email == null || email.isBlank()
            || password == null) {

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "message",
                        "Email and password are required"
                ));
    }

    User user = userRepository
            .findByEmail(email.trim())
            .orElse(null);

    if (user == null
            || !passwordEncoder.matches(
                    password,
                    user.getPassword())) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "message",
                        "Invalid login details"
                ));
    }

    Authentication authentication =
            UsernamePasswordAuthenticationToken.authenticated(
                    user.getEmail(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );

    SecurityContext context =
            SecurityContextHolder.createEmptyContext();

    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);

    HttpSessionSecurityContextRepository repository =
            new HttpSessionSecurityContextRepository();

    repository.saveContext(context, request, response);

    return ResponseEntity.ok(
            Map.of("message", "Login successful!")
    );
}

    // RESET PASSWORD - LOCAL LEARNING ONLY
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody Map<String, String> data) {

        String email = data.get("email");
        String newPassword = data.get("newPassword");

        if (email == null || email.isBlank()
                || newPassword == null
                || newPassword.length() < 8) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Enter your email and a password of at least 8 characters"
                    ));
        }

        User user = userRepository
                .findByEmail(email.trim())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message",
                            "Account not found"
                    ));
        }

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password reset successful!"
                )
        );
    }
}