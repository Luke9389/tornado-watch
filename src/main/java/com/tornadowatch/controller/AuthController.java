package com.tornadowatch.controller;

import com.tornadowatch.dto.LoginRequest;
import com.tornadowatch.dto.RegisterRequest;
import com.tornadowatch.entity.Role;
import com.tornadowatch.entity.User;
import com.tornadowatch.repository.UserRepository;
import com.tornadowatch.service.AuditLogService;
import com.tornadowatch.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) {
            auditLogService.logLoginAttempt(loginRequest.getUsername(), false, "User not found", request);
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            auditLogService.logLoginAttempt(loginRequest.getUsername(), false, "Invalid password", request);
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        auditLogService.logLoginAttempt(loginRequest.getUsername(), true, null, request);
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            auditLogService.logRegistrationAttempt(request.getUsername(), false, "Username already taken", httpRequest);
            return ResponseEntity.badRequest().body("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.PUBLIC_USER);

        userRepository.save(user);
        auditLogService.logRegistrationAttempt(request.getUsername(), true, null, httpRequest);

        return ResponseEntity.ok("User registered successfully");
    }
}
