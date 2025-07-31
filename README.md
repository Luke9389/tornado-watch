# Tornado Watch Application

A practice project to check out Spring Boot. full-stack tornado alert system with React + TypeScript frontend and Spring Boot backend.

### Frontend Features  
- **React + TypeScript**: Modern component-based architecture
- **Alert Viewer**: Card-based alert display with severity color coding
- **Advanced Filters**: Real-time filtering by region, severity, date range
- **Responsive Design**: Mobile-friendly layout
- **API Integration**: Full integration with backend alert service

### Backend Features
- **Alert Entity**: Complete with severity, region, timestamps
- **Public API Endpoints**:
  - `GET /api/alerts` - List all alerts with optional filters
  - `GET /api/alerts/{id}` - Get specific alert
- **Advanced Filtering**: By region, severity, date range using dynamic JPQL
- **Mock Data**: 6 realistic tornado alerts loaded on startup
- **Security**: Public access to alert endpoints, protected auth endpoints

## Development Setup

### Prerequisites
- Java 17+
- Node.js 16+
- PostgreSQL running on localhost:5432
- Database: `tornadowatch` with user `tornado_user`

### Running the Application

#### 1. Start Backend (Terminal 1)
```bash
cd backend
mvn spring-boot:run
```
Backend runs on: http://localhost:8080

#### 2. Start Frontend (Terminal 2)  
```bash
cd frontend
npm start
```
Frontend runs on: http://localhost:3000

### API Examples
```bash
# Get all alerts
curl http://localhost:8080/api/alerts

# Filter by severity
curl "http://localhost:8080/api/alerts?severity=HIGH"

# Filter by region
curl "http://localhost:8080/api/alerts?region=Oklahoma"

# Filter by date range
curl "http://localhost:8080/api/alerts?startDate=2025-07-31T00:00:00&endDate=2025-07-31T23:59:59"
```

## Next: Sprint 3 - Subscriptions + Alert Notifier

Ready to implement:
- User subscription management
- Alert filtering preferences  
- Background notification system
- Email/SMS alert delivery

## Testing

```bash
# Backend tests
cd backend && mvn test

# Frontend tests
cd frontend && npm test
```