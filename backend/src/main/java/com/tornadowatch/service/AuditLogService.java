package com.tornadowatch.service;

import com.tornadowatch.entity.AuditLog;
import com.tornadowatch.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuditLogService {

    public static final String LOGIN_ATTEMPT = "LOGIN_ATTEMPT";
    public static final String REGISTRATION_ATTEMPT = "REGISTRATION_ATTEMPT";

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void logLoginAttempt(String username, boolean success, String failureReason, HttpServletRequest request) {
        AuditLog auditLog = new AuditLog(
            username,
            LOGIN_ATTEMPT,
            success,
            getClientIpAddress(request),
            request.getHeader("User-Agent")
        );
        
        if (!success && failureReason != null) {
            auditLog.setFailureReason(failureReason);
        }
        
        auditLogRepository.save(auditLog);
    }

    public void logRegistrationAttempt(String username, boolean success, String failureReason, HttpServletRequest request) {
        AuditLog auditLog = new AuditLog(
            username,
            REGISTRATION_ATTEMPT,
            success,
            getClientIpAddress(request),
            request.getHeader("User-Agent")
        );
        
        if (!success && failureReason != null) {
            auditLog.setFailureReason(failureReason);
        }
        
        auditLogRepository.save(auditLog);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            return xForwardedForHeader.split(",")[0].trim();
        }
        
        String xRealIpHeader = request.getHeader("X-Real-IP");
        if (xRealIpHeader != null && !xRealIpHeader.isEmpty()) {
            return xRealIpHeader;
        }
        
        return request.getRemoteAddr();
    }
}