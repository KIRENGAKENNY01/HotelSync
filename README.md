# Hotel Management System

A monolithic REST API built with **Spring Boot 3.2** and **Java 21** that handles hotel bookings end-to-end — from room browsing through payment confirmation — with JWT-secured endpoints and automated email notifications.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Persistence | Spring Data JPA + PostgreSQL |
| Email | Spring Mail (JavaMailSender) |
| API Docs | SpringDoc OpenAPI / Swagger UI |
| Build | Maven |
| Utilities | Lombok |

---

## Booking Flow

```
1. Register / Login          → receive JWT token
2. Customer: POST /api/bookings          → booking created as PENDING
3. Admin:    PUT  /api/admin/bookings/{id}/confirm
                             → booking becomes CONFIRMED
                             → invoice generated & emailed to customer (includes Billing ID)
4. Customer: POST /api/billing/{id}/pay  → bill marked PAID, room locked
                             → payment receipt emailed
5. Admin (optional): DELETE /api/admin/bookings/{id}
                             → booking CANCELLED, room freed, cancellation email sent
```

---

## Project Structure

```
src/main/java/com/hotelmanagement/
├── auth/               # Registration, login, JWT issuance
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── billing/            # Invoice generation and payment
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── booking/            # Booking lifecycle management
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── hotel/              # Hotel and room management
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── notification/       # Email notifications
│   └── service/
├── security/           # JWT filter, UserDetailsService, SecurityConfig
├── common/             # Shared exceptions, base entity, configs
└── HotelManagementApplication.java
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 14+
- An SMTP account (e.g. Gmail) for email notifications

### 1. Clone the repository

```bash
git clone <repository-url>
cd Hotel-Management-System
```

### 2. Configure environment variables

Copy `.env.example` to `.env` and fill in your values:

```bash
cp .env.example .env
```

| Variable | Description |
|---|---|
| `DB_URL` | PostgreSQL JDBC URL (e.g. `jdbc:postgresql://localhost:5432/hotel_db`) |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key for signing JWT tokens |
| `JWT_EXPIRATION` | Token expiry in milliseconds (e.g. `86400000` for 24 h) |
| `MAIL_HOST` | SMTP host (e.g. `smtp.gmail.com`) |
| `MAIL_PORT` | SMTP port (e.g. `587`) |
| `MAIL_USERNAME` | Sender email address |
| `MAIL_PASSWORD` | Sender email password / app password |

### 3. Create the database

```sql
CREATE DATABASE hotel_db;
```

### 4. Build and run

```bash
./mvnw spring-boot:run
```

The application starts on **http://localhost:8080** by default.

---

## API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

Authenticate by clicking **Authorize 🔒** and entering `Bearer <your-jwt-token>`.

---

## API Endpoints

### Authentication — `/api/auth`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Login and receive JWT |

### Customer — Hotels & Rooms

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/hotels` | Customer | List all hotels |
| GET | `/api/hotels/{id}` | Customer | Get hotel details |
| GET | `/api/hotels/{hotelId}/rooms` | Customer | List rooms in a hotel |

### Customer — Bookings

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/bookings` | Customer | Create a booking (status: PENDING) |
| GET | `/api/bookings` | Customer | Get own bookings |
| DELETE | `/api/bookings/{id}` | Customer | Cancel own booking |

### Customer — Billing

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/billing` | Customer | View own bills |
| POST | `/api/billing/{id}/pay` | Customer | Pay a bill (locks the room) |

### Admin — Hotels & Rooms

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/admin/hotels` | Admin | Create a hotel |
| PUT | `/api/admin/hotels/{id}` | Admin | Update a hotel |
| DELETE | `/api/admin/hotels/{id}` | Admin | Delete a hotel |
| POST | `/api/admin/hotels/{hotelId}/rooms` | Admin | Add a room |
| PUT | `/api/admin/rooms/{id}` | Admin | Update a room |
| DELETE | `/api/admin/rooms/{id}` | Admin | Delete a room |

### Admin — Bookings

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/admin/bookings` | Admin | List all bookings |
| PUT | `/api/admin/bookings/{id}/confirm` | Admin | Confirm booking → generates invoice & emails customer |
| DELETE | `/api/admin/bookings/{id}` | Admin | Cancel any booking |

### Admin — Billing

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/admin/billing` | Admin | View all billing records |

### Admin — Analytics

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/admin/dashboard` | Admin | System-wide statistics |

---

## Email Notifications

The system sends HTML emails at key lifecycle events:

| Event | Trigger | Content |
|---|---|---|
| **Invoice** | Admin confirms booking | Billing ID, Booking ID, hotel, room, dates, amount due, payment status |
| **Payment Receipt** | Customer pays bill | Booking ID, hotel, room, dates, amount paid, PAID status |
| **Cancellation** | Booking cancelled | Hotel, room details, cancellation notice |

---

## Roles

| Role | Capabilities |
|---|---|
| `CUSTOMER` | Browse hotels/rooms, create/cancel own bookings, pay own bills |
| `ADMIN` | Full access — manage hotels, rooms, confirm/cancel any booking, view all billing |

---

## License

This project is for educational purposes.
