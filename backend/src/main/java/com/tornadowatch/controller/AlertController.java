package com.tornadowatch.controller;

import com.tornadowatch.entity.Alert;
import com.tornadowatch.entity.AlertSeverity;
import com.tornadowatch.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private AlertRepository alertRepository;

    @GetMapping
    public ResponseEntity<List<Alert>> getAlerts(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) AlertSeverity severity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Alert> alerts = alertRepository.findAlertsWithFilters(region, severity, startDate, endDate);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlert(@PathVariable Long id) {
        return alertRepository.findById(id)
            .map(alert -> ResponseEntity.ok(alert))
            .orElse(ResponseEntity.notFound().build());
    }
}