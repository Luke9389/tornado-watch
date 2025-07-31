package com.tornadowatch.service;

import com.tornadowatch.entity.Alert;
import com.tornadowatch.entity.AlertSeverity;
import com.tornadowatch.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private AlertRepository alertRepository;

    @Override
    public void run(String... args) throws Exception {
        if (alertRepository.count() == 0) {
            initializeMockAlerts();
        }
    }

    private void initializeMockAlerts() {
        List<Alert> mockAlerts = Arrays.asList(
            createAlert("Tornado Watch - Oklahoma County", 
                       "Tornado watch issued for Oklahoma County. Conditions favorable for tornado development.", 
                       AlertSeverity.MODERATE, "Oklahoma", LocalDateTime.now().minusHours(2)),
            
            createAlert("Tornado Warning - Moore, OK", 
                       "Tornado warning in effect for Moore, Oklahoma. Take shelter immediately if in the warned area.", 
                       AlertSeverity.HIGH, "Oklahoma", LocalDateTime.now().minusMinutes(30)),
            
            createAlert("Severe Thunderstorm Watch - Kansas", 
                       "Severe thunderstorm watch with possibility of tornado activity across central Kansas.", 
                       AlertSeverity.LOW, "Kansas", LocalDateTime.now().minusHours(4)),
            
            createAlert("Tornado Watch - Texas Panhandle", 
                       "Tornado watch issued for the Texas Panhandle region including Amarillo area.", 
                       AlertSeverity.MODERATE, "Texas", LocalDateTime.now().minusHours(1)),
            
            createAlert("Enhanced Risk - Nebraska", 
                       "Enhanced risk of severe weather including possible tornadoes across eastern Nebraska.", 
                       AlertSeverity.LOW, "Nebraska", LocalDateTime.now().minusHours(6)),
            
            createAlert("Tornado Emergency - Joplin Area", 
                       "Tornado emergency declared for Joplin, Missouri area. Large, destructive tornado confirmed.", 
                       AlertSeverity.HIGH, "Missouri", LocalDateTime.now().minusMinutes(15))
        );

        alertRepository.saveAll(mockAlerts);
    }

    private Alert createAlert(String title, String description, AlertSeverity severity, String region, LocalDateTime issuedAt) {
        Alert alert = new Alert();
        alert.setTitle(title);
        alert.setDescription(description);
        alert.setSeverity(severity);
        alert.setRegion(region);
        alert.setIssuedAt(issuedAt);
        alert.setExpiresAt(issuedAt.plusHours(4));
        return alert;
    }
}