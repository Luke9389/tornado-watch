package com.tornadowatch.repository;

import com.tornadowatch.entity.Alert;
import com.tornadowatch.entity.AlertSeverity;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertRepositoryCustom {
    List<Alert> findAlertsWithFilters(String region, AlertSeverity severity, LocalDateTime startDate, LocalDateTime endDate);
}