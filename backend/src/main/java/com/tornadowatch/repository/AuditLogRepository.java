package com.tornadowatch.repository;

import com.tornadowatch.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    List<AuditLog> findByUsernameOrderByCreatedAtDesc(String username);
    
    List<AuditLog> findByEventTypeOrderByCreatedAtDesc(String eventType);
    
    @Query("SELECT a FROM AuditLog a WHERE a.success = false AND a.createdAt >= :since ORDER BY a.createdAt DESC")
    List<AuditLog> findFailedAttemptsSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT a FROM AuditLog a WHERE a.username = :username AND a.success = false AND a.createdAt >= :since")
    List<AuditLog> findFailedLoginAttemptsByUserSince(@Param("username") String username, @Param("since") LocalDateTime since);
}