package com.joshi.tmsapplication.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.joshi.tmsapplication.repository.UserRepository;
import com.joshi.tmsapplication.dto.LoginRequest;
import com.joshi.tmsapplication.dto.LoginResponse;
import com.joshi.tmsapplication.entity.User;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> {
                    log.info("User found: {}", user.getEmail());
                    log.info("Provided password: {}", request.getPassword());
                    log.info("Stored hash: {}", user.getPassword());
                    
                    boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
                    log.info("Password matches: {}", matches);
                    return matches;
                })
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
                    log.info("Token generated for: {}", user.getEmail());
                    return ResponseEntity.ok(new LoginResponse(token));
                })
                .orElse(ResponseEntity.badRequest().build());
    }
}

