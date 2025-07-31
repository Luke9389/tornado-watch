package com.tornadowatch.service;

import com.tornadowatch.entity.AuditLog;
import com.tornadowatch.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuditLogServiceTest {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @BeforeEach
    void setUp() {
        auditLogRepository.deleteAll();
    }

    @Test
    void logLoginAttempt_Success_ShouldCreateAuditRecord() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.1.1");
        request.addHeader("User-Agent", "Mozilla/5.0");

        auditLogService.logLoginAttempt("testuser", true, null, request);

        List<AuditLog> logs = auditLogRepository.findAll();
        assertEquals(1, logs.size());

        AuditLog log = logs.get(0);
        assertEquals("testuser", log.getUsername());
        assertEquals(AuditLogService.LOGIN_ATTEMPT, log.getEventType());
        assertTrue(log.isSuccess());
        assertEquals("192.168.1.1", log.getIpAddress());
        assertEquals("Mozilla/5.0", log.getUserAgent());
        assertNull(log.getFailureReason());
        assertNotNull(log.getCreatedAt());
    }

    @Test
    void logLoginAttempt_Failure_ShouldCreateAuditRecordWithReason() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.1.2");
        request.addHeader("User-Agent", "Chrome/91.0");

        auditLogService.logLoginAttempt("baduser", false, "Invalid password", request);

        List<AuditLog> logs = auditLogRepository.findAll();
        assertEquals(1, logs.size());

        AuditLog log = logs.get(0);
        assertEquals("baduser", log.getUsername());
        assertEquals(AuditLogService.LOGIN_ATTEMPT, log.getEventType());
        assertFalse(log.isSuccess());
        assertEquals("192.168.1.2", log.getIpAddress());
        assertEquals("Chrome/91.0", log.getUserAgent());
        assertEquals("Invalid password", log.getFailureReason());
        assertNotNull(log.getCreatedAt());
    }

    @Test
    void logRegistrationAttempt_Success_ShouldCreateAuditRecord() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("10.0.0.1");
        request.addHeader("User-Agent", "Safari/14.0");

        auditLogService.logRegistrationAttempt("newuser", true, null, request);

        List<AuditLog> logs = auditLogRepository.findAll();
        assertEquals(1, logs.size());

        AuditLog log = logs.get(0);
        assertEquals("newuser", log.getUsername());
        assertEquals(AuditLogService.REGISTRATION_ATTEMPT, log.getEventType());
        assertTrue(log.isSuccess());
        assertEquals("10.0.0.1", log.getIpAddress());
        assertEquals("Safari/14.0", log.getUserAgent());
        assertNull(log.getFailureReason());
    }

    @Test
    void logRegistrationAttempt_Failure_ShouldCreateAuditRecordWithReason() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("172.16.0.1");

        auditLogService.logRegistrationAttempt("duplicateuser", false, "Username already taken", request);

        List<AuditLog> logs = auditLogRepository.findAll();
        assertEquals(1, logs.size());

        AuditLog log = logs.get(0);
        assertEquals("duplicateuser", log.getUsername());
        assertEquals(AuditLogService.REGISTRATION_ATTEMPT, log.getEventType());
        assertFalse(log.isSuccess());
        assertEquals("172.16.0.1", log.getIpAddress());
        assertEquals("Username already taken", log.getFailureReason());
    }

    @Test
    void getClientIpAddress_XForwardedFor_ShouldExtractFirstIP() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "203.0.113.1, 192.168.1.1, 10.0.0.1");
        request.setRemoteAddr("127.0.0.1");

        auditLogService.logLoginAttempt("testuser", true, null, request);

        List<AuditLog> logs = auditLogRepository.findAll();
        assertEquals("203.0.113.1", logs.get(0).getIpAddress());
    }

    @Test
    void getClientIpAddress_XRealIP_ShouldUseXRealIPHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Real-IP", "203.0.113.2");
        request.setRemoteAddr("127.0.0.1");

        auditLogService.logLoginAttempt("testuser", true, null, request);

        List<AuditLog> logs = auditLogRepository.findAll();
        assertEquals("203.0.113.2", logs.get(0).getIpAddress());
    }

    @Test
    void getClientIpAddress_NoHeaders_ShouldUseRemoteAddr() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.1.100");

        auditLogService.logLoginAttempt("testuser", true, null, request);

        List<AuditLog> logs = auditLogRepository.findAll();
        assertEquals("192.168.1.100", logs.get(0).getIpAddress());
    }
}