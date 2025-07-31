# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Purpose:
Learn some new technologies with this practice repo.
Build a full-stack application that lets users:

 - View tornado alerts for a region (mocked data)

 - Subscribe for notifications based on filters (region, severity)

 - Submit reports or sightings (optionally with photo uploads)

 - Internal users can log in to triage reports (admin dashboard)

This project touches on:

 - Backend APIs with Java + Spring Boot

 - Frontend with React + TypeScript

 - Database with PostgreSQL + JPA (ORM)

 - Cloud-readiness via Docker + GitHub Actions

 - Monitoring/logging tools (Sentry, Prometheus)

 - Security/compliance practices (JWT auth, audit logging)



## Development Progress

Sprint 1: Authentication + Internal Role System
    •    Login system (JWT)
    •    Role-based auth: public vs internal
    •    Spring Security config
    •    Audit logging of logins
Sprint 2: Public-Facing Tornado Alert Viewer
    •    Fetch alert data from mock endpoint or local JSON
    •    Filter by region/severity/date
    •    Build UI in React + TypeScript
Sprint 3: Subscriptions + Alert Notifier
    •    Users can subscribe to alerts
    •    Backend stores filters per user
    •    (Mock) background job sends alerts based on new data
    •    Use CRON or Spring Scheduling
Sprint 4: Report Submission & Triage Dashboard
    •    Users submit tornado sightings
    •    Internal users review, approve, and archive reports
    •    Admin dashboard UI + backend filtering/sorting
Sprint 5: Monitoring, Logging & Deployment
    •    Sentry for client + backend errors
    •    Prometheus + Grafana metrics for backend health
    •    Dockerize app with multi-container setup (backend, frontend, db)
    •    GitHub Actions for CI: test, build Docker image, deploy


## Tech Stack Overview (Matches Pano’s)
| Layer | Tech |
|---|---|
| Frontend | React + TypeScript + CSS Modules or Tailwind |
| Backend | Java + Spring Boot (Spring Web, Spring Security, Spring Data JPA) |
| Database | PostgreSQL + ORM (Hibernate) |
| Auth | JWT-based login (public vs internal roles) |
| CI/CD | GitHub Actions |
| Cloud-Ready | Docker + Docker Compose (Bonus: Deploy to GCP or Fly.io) |
| Monitoring | Sentry (frontend), Prometheus + Grafana (backend) |
| Security | Password hashing, RBAC, audit logs, validation |


## Project Overview

Spring Boot 3.5.4 application using Java 17, PostgreSQL, and JWT-based authentication. The codebase implements a tornado watch backend service with user authentication and role-based access control.

## Development Commands

### Build and Test
```bash
# Clean and compile
mvn clean compile

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AuthControllerTest

# Run specific test method
mvn test -Dtest=AuthControllerTest#testLoginSuccess

# Start application
mvn spring-boot:run

# Package application
mvn clean package
```

### Database Setup
Requires PostgreSQL running on localhost:5432 with database `tornadowatch` and user `tornado_user`. Connection details in `application.properties`.

## Architecture Overview

**Package Structure:**
- `com.tornadowatch.controller` - REST endpoints
- `com.tornadowatch.service` - Business logic (minimal, mostly in controllers)
- `com.tornadowatch.repository` - JPA repositories
- `com.tornadowatch.entity` - JPA entities
- `com.tornadowatch.dto` - Request/response DTOs
- `com.tornadowatch.config` - Security configuration
- `com.tornadowatch.filter` - JWT authentication filter
- `com.tornadowatch.util` - JWT utilities

**Key Design Patterns:**
- JWT-based stateless authentication with 24-hour expiration
- Spring Security filter chain with whitelisted public endpoints (`/auth/register`, `/auth/login`)
- BCrypt password hashing
- Role-based access using `PUBLIC_USER` and `INTERNAL_USER` enum
- Standard Spring Boot Repository pattern with JpaRepository
- Integration testing with `@SpringBootTest` and MockMvc

**Authentication Flow:**
1. Registration/Login → JWT token generation
2. Subsequent requests → JWT filter validation → Security context setup
3. Protected endpoints require valid Bearer token in Authorization header

**Database Integration:**
- JPA with Hibernate DDL auto-update
- PostgreSQL with show-sql enabled for debugging
- User entity with unique username constraint

## Current Limitations

- Business logic currently in controllers instead of dedicated service layer
- Minimal validation on DTOs (no `@Valid` annotations)
- String-based error responses instead of structured error objects
- JWT secret hardcoded in JwtUtil (should be externalized)
- Role enum not fully integrated with Spring Security authorities

## Test Patterns

Tests use `@SpringBootTest` with `@AutoConfigureTestMvc` for integration testing. Each test class includes `@BeforeEach` setup for database cleanup and uses `@Transactional` with `@DirtiesContext` for test isolation.
