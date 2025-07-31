package com.tornadowatch.repository;

import com.tornadowatch.entity.Alert;
import com.tornadowatch.entity.AlertSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>, AlertRepositoryCustom {
    
    List<Alert> findByRegion(String region);
    List<Alert> findBySeverity(AlertSeverity severity);
    List<Alert> findByIssuedAtBetween(LocalDateTime start, LocalDateTime end);
}