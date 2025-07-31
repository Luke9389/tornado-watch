package com.tornadowatch.controller;

import com.tornadowatch.entity.Alert;
import com.tornadowatch.entity.AlertSeverity;
import com.tornadowatch.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlertRepository alertRepository;

    @BeforeEach
    void setUp() {
        alertRepository.deleteAll();
        
        Alert alert1 = new Alert();
        alert1.setTitle("Tornado Watch - Oklahoma");
        alert1.setDescription("Tornado watch for Oklahoma County");
        alert1.setSeverity(AlertSeverity.MODERATE);
        alert1.setRegion("Oklahoma");
        alert1.setIssuedAt(LocalDateTime.now().minusHours(1));
        alert1.setExpiresAt(LocalDateTime.now().plusHours(3));
        
        Alert alert2 = new Alert();
        alert2.setTitle("Tornado Warning - Kansas");
        alert2.setDescription("Tornado warning for Kansas");
        alert2.setSeverity(AlertSeverity.HIGH);
        alert2.setRegion("Kansas");
        alert2.setIssuedAt(LocalDateTime.now().minusMinutes(30));
        alert2.setExpiresAt(LocalDateTime.now().plusHours(2));
        
        alertRepository.save(alert1);
        alertRepository.save(alert2);
    }
    
    @Test
    void testGetAllAlerts() throws Exception {
        mockMvc.perform(get("/api/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].severity").exists())
                .andExpect(jsonPath("$[0].region").exists());
    }
    
    @Test
    void testGetAlertsByRegion() throws Exception {
        mockMvc.perform(get("/api/alerts?region=Oklahoma"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].region").value("Oklahoma"));
    }
    
    @Test
    void testGetAlertsBySeverity() throws Exception {
        mockMvc.perform(get("/api/alerts?severity=HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].severity").value("HIGH"));
    }
    
    @Test
    void testGetSingleAlert() throws Exception {
        Alert savedAlert = alertRepository.findAll().get(0);
        
        mockMvc.perform(get("/api/alerts/" + savedAlert.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedAlert.getId()))
                .andExpect(jsonPath("$.title").value(savedAlert.getTitle()));
    }
    
    @Test
    void testGetNonExistentAlert() throws Exception {
        mockMvc.perform(get("/api/alerts/999"))
                .andExpect(status().isNotFound());
    }
}