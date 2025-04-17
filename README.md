# Delivery Service API

## 📦 Description

This is a reactive Spring Boot application built with Java 21 and Spring WebFlux following the Hexagonal Architecture pattern. It allows customers to:

- View available delivery methods
- View available time slots based on delivery method
- Reserve a time slot (if not already reserved)

## 🧱 Architecture

The project follows a clean Hexagonal Architecture:

```
delivery_service/
├── domain/
│   ├── model/                 # Entities (DeliveryMethod, TimeSlot)
│   ├── port/                  # Interfaces (DeliveryService, TimeSlotRepository)
│   └── exception/             # Custom domain exceptions
│
├── application/
│   └── service/              # Business logic implementation (DeliveryServiceImpl)
│
├── infrastructure/
│   ├── web/                  # REST Controllers, DTOs, Mappers, Exception handling
│   ├── persistence/          # InMemory implementation of TimeSlotRepository
│   └── mapper/               # MapStruct mappers
```

## 🚀 How to Run

```bash
# Build and run the app
./mvnw clean spring-boot:run
```

The API will be available at:
```
http://localhost:8080/api/delivery
```

## 🧪 Test
```bash
./mvnw test
```

## 🔗 REST API Endpoints

### 1. Get available delivery methods
```http
GET /api/delivery/methods
```
**Response:**
```json
[
  "DRIVE",
  "DELIVERY",
  "DELIVERY_TODAY",
  "DELIVERY_ASAP"
]
```

### 2. Get time slots for a given method
```http
GET /api/delivery/slots/{method}
```
**Example:**
```http
GET /api/delivery/slots/DRIVE
```
**Response:**
```json
[
  {
    "id": "...",
    "method": "DRIVE",
    "date": "2025-04-17",
    "start": "09:00",
    "end": "10:00",
    "reserved": false
  },
  ...
]
```

### 3. Reserve a time slot
```http
POST /api/delivery/slots/{id}/reserve
```
**Response:**
```json
{
  "id": "...",
  "method": "DRIVE",
  "date": "2025-04-17",
  "start": "09:00",
  "end": "10:00",
  "reserved": true
}
```

## ⚠️ Error Handling (GlobalExceptionHandler)
All errors are returned in a standard format:

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid delivery method: EXPRESS_DRONE",
  "path": "/api/delivery/slots/EXPRESS_DRONE"
}
```

## ✅ Tech Stack
- Java 21
- Spring Boot 3.x
- Spring WebFlux (Reactive)
- MapStruct
- Hexagonal Architecture
- JUnit 5 + Mockito + StepVerifier
- SonarQube + JaCoCo for static code analysis and test coverage
