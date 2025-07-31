package com.tornadowatch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tornadowatch.dto.LoginRequest;
import com.tornadowatch.dto.RegisterRequest;
import com.tornadowatch.entity.Role;
import com.tornadowatch.entity.User;
import com.tornadowatch.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_Success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        // Verify user was saved to database
        var savedUser = userRepository.findByUsername("testuser");
        assert savedUser.isPresent();
        assert savedUser.get().getRole() == Role.PUBLIC_USER;
    }

    @Test
    void registerUser_DuplicateUsername_ShouldFail() throws Exception {
        // Create existing user
        User existingUser = new User();
        existingUser.setUsername("testuser");
        existingUser.setPassword(passwordEncoder.encode("password"));
        existingUser.setRole(Role.PUBLIC_USER);
        userRepository.save(existingUser);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("newpassword");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already taken"));
    }

    @Test
    void loginUser_ValidCredentials_ShouldReturnJWT() throws Exception {
        // Create test user
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("testpass"));
        testUser.setRole(Role.PUBLIC_USER);
        userRepository.save(testUser);

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("eyJ"))); // JWT tokens start with "eyJ"
    }

    @Test
    void loginUser_InvalidUsername_ShouldFail() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("testpass");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void loginUser_InvalidPassword_ShouldFail() throws Exception {
        // Create test user
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("correctpass"));
        testUser.setRole(Role.PUBLIC_USER);
        userRepository.save(testUser);

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpass");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}