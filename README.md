# Pikndel - Logistics Delivery Management System

A Spring Boot 3.5.6 REST API application for managing logistics delivery operations with JWT-based authentication, order management, and courier tracking.

## Table of Contents

- [Project Overview](#project-overview)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [API Endpoints](#api-endpoints)
- [Setup & Installation](#setup--installation)
- [Configuration](#configuration)
- [Database Schema](#database-schema)
- [Running the Application](#running-the-application)
- [Testing Endpoints](#testing-endpoints)
- [Security](#security)
- [Contributing](#contributing)

---

## Project Overview

**Pikndel** is a comprehensive logistics delivery management system that enables:

- User registration and authentication with JWT tokens
- Order creation and tracking
- Courier assignment and status management
- Role-based access control (USER, DELIVERY_GUY, ADMIN)
- Real-time delivery status updates
- Address and delivery assignment management

### Key Features

✅ JWT-based authentication and authorization  
✅ Role-based access control (RBAC)  
✅ Order lifecycle management  
✅ Courier fleet management  
✅ MySQL database with JPA/Hibernate ORM  
✅ RESTful API design with proper HTTP status codes  
✅ CORS support for cross-origin requests  
✅ Stateless session management

---

## Technology Stack

### Backend Framework

- **Spring Boot**: 3.5.6
- **Spring Security**: OAuth2 Resource Server
- **Spring Data JPA**: 3.5.6
- **Hibernate**: 6.6.2 (ORM)

### Database

- **MySQL**: 8.0+
- **HikariCP**: Connection pooling

### Authentication & Security

- **JJWT**: 0.12.3 (JSON Web Tokens)
- **BCrypt**: Password encoding
- **Spring Security**: 6.5.x

### Build & Dependency Management

- **Maven**: 3.9+
- **Java**: 17 (LTS)

### Additional Libraries

- **Lombok**: 1.18.30 (Annotations)
- **Jakarta Persistence API**: 3.1.0
- **Jackson**: JSON processing

---

## Architecture

### Project Structure

```
Pikndel/
├── src/
│   ├── main/
│   │   ├── java/com/joshi/Pikndel/
│   │   │   ├── config/              # Security & configuration
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JWTFilter.java
│   │   │   │   ├── UserServiceDetailsImpl.java
│   │   │   │   └── JWTUtil.java
│   │   │   ├── controller/          # REST API endpoints
│   │   │   │   ├── UserController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   └── CourierController.java
│   │   │   ├── service/             # Business logic
│   │   │   │   ├── UserService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   └── CourierService.java
│   │   │   ├── repository/          # Data access layer
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── CourierRepository.java
│   │   │   ├── entity/              # JPA entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── Courier.java
│   │   │   │   ├── OrderItems.java
│   │   │   │   ├── DeliveryAssignments.java
│   │   │   │   ├── TrackingEvents.java
│   │   │   │   ├── Addresses.java
│   │   │   │   └── Payments.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   ├── OrderDto.java
│   │   │   │   └── OrderItemDto.java
│   │   │   ├── mapper/              # Entity mappers
│   │   │   │   └── OrderMapper.java
│   │   │   └── PikndelApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql
│   │       ├── docker-compose.yml
│   │       └── templates/
│   └── test/
│       └── java/...
└── pom.xml
```

### Layered Architecture

```
┌─────────────────────┐
│   REST Controllers  │
├─────────────────────┤
│   Service Layer     │
├─────────────────────┤
│   Repository Layer  │
├─────────────────────┤
│   Database (MySQL)  │
└─────────────────────┘
     │
     ├─ Security Config (JWT, Filters)
     ├─ DTOs (Data Transfer)
     └─ Entities (Domain Models)
```

---

## API Endpoints

### 1. Authentication Endpoints (`/api/auth`)

#### POST `/api/auth/login`

**Description**: Authenticate user and get JWT token

**Request**:

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response** (200 OK):

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3MzQzNDU2MzAsImV4cCI6MTczNDQzMjAzMH0...."
}
```

**Response** (400 Bad Request): Invalid credentials

---

### 2. User Endpoints (`/api/users`)

#### POST `/api/users/register`

**Description**: Register a new user (Public endpoint)

**Request**:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "password": "password123"
}
```

**Response** (200 OK):

```json
{
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "role": "USER"
}
```

---

#### GET `/api/users`

**Description**: Get all users (Public endpoint)

**Response** (200 OK):

```json
[
  {
    "userId": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "role": "USER"
  }
]
```

---

#### GET `/api/users/{id}`

**Description**: Get user by ID (Public endpoint)

**Response** (200 OK):

```json
{
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "role": "USER"
}
```

---

#### DELETE `/api/users/{id}`

**Description**: Delete user by ID (Public endpoint)

**Response** (204 No Content)

---

### 3. Order Endpoints (`/api/orders`)

**Requires JWT Authentication**

#### POST `/api/orders`

**Description**: Create a new order

**Headers**:

```
Authorization: Bearer <JWT_TOKEN>
```

**Request**:

```json
{
  "userId": 1,
  "items": [
    {
      "itemName": "Package 1",
      "quantity": 1,
      "price": 100.0
    }
  ],
  "deliveryAddress": "123 Main St, City"
}
```

**Response** (200 OK):

```json
{
  "orderId": 1,
  "userId": 1,
  "status": "PENDING",
  "createdAt": "2025-12-16T10:30:00",
  "deliveryAddress": "123 Main St, City",
  "totalAmount": 100.0
}
```

---

#### POST `/api/orders/{id}/assign`

**Description**: Assign courier to order (ADMIN/SYSTEM role required)

**Headers**:

```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Response** (200 OK):

```json
{
  "orderId": 1,
  "courierId": 5,
  "status": "ASSIGNED",
  "assignedAt": "2025-12-16T10:31:00"
}
```

---

### 4. Courier Endpoints (`/api/couriers`)

#### POST `/api/couriers`

**Description**: Create new courier (ADMIN role required)

**Headers**:

```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Query Parameters**:

- `userId` (required): User ID to assign as courier

**Response** (200 OK):

```json
{
  "courierId": 5,
  "userId": 3,
  "status": "AVAILABLE"
}
```

---

#### GET `/api/couriers`

**Description**: Get all couriers (Requires authentication)

**Response** (200 OK):

```json
[
  {
    "courierId": 5,
    "userId": 3,
    "status": "AVAILABLE"
  }
]
```

---

#### GET `/api/couriers/available`

**Description**: Get available couriers (Public endpoint)

**Response** (200 OK):

```json
[
  {
    "courierId": 5,
    "userId": 3,
    "status": "AVAILABLE"
  }
]
```

---

#### GET `/api/couriers/{id}`

**Description**: Get courier by ID (Requires authentication)

**Response** (200 OK):

```json
{
  "courierId": 5,
  "userId": 3,
  "status": "AVAILABLE"
}
```

---

#### PUT `/api/couriers/{id}/status`

**Description**: Update courier status (ADMIN/DELIVERY_GUY role required)

**Headers**:

```
Authorization: Bearer <JWT_TOKEN>
```

**Query Parameters**:

- `status` (required): New status (e.g., "AVAILABLE", "BUSY", "INACTIVE")

**Response** (200 OK):

```json
{
  "courierId": 5,
  "userId": 3,
  "status": "BUSY"
}
```

---

## Setup & Installation

### Prerequisites

- Java 17 (JDK)
- MySQL 8.0+
- Maven 3.9+
- Git

### Step 1: Clone Repository

```bash
git clone <repository-url>
cd Pikndel
```

### Step 2: Set Up MySQL Database

```bash
# Create database
CREATE DATABASE pikndel_db;
CREATE USER 'pikndel_user'@'localhost' IDENTIFIED BY 'pikndel_password';
GRANT ALL PRIVILEGES ON pikndel_db.* TO 'pikndel_user'@'localhost';
FLUSH PRIVILEGES;
```

### Step 3: Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/pikndel_db
spring.datasource.username=pikndel_user
spring.datasource.password=pikndel_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
jwt.secret=ThisIsAVerySecureSecretKeyWith256BitsForJWT
jwt.expiration=86400000

# Application Configuration
spring.application.name=Pikndel
```

### Step 4: Build Project

```bash
mvn clean install
```

---

## Configuration

### Security Configuration

**File**: `src/main/java/com/joshi/Pikndel/config/SecurityConfig.java`

- **CSRF**: Disabled (stateless API)
- **CORS**: Enabled with defaults
- **Session Management**: STATELESS (JWT-based)
- **Authentication Filter**: JWT filter added before UsernamePasswordAuthenticationFilter
- **Password Encoding**: BCrypt

### JWT Configuration

**File**: `src/main/java/com/joshi/Pikndel/util/JwtUtil.java`

- **Algorithm**: HMAC-SHA512
- **Key Length**: 256 bits (minimum required)
- **Secret Key**: "ThisIsAVerySecureSecretKeyWith256BitsForJWT"
- **Default Expiration**: 24 hours (86400000 ms)

**⚠️ IMPORTANT**: Change the JWT secret key in production!

---

## Database Schema

### User Table

```sql
CREATE TABLE user (
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  phone VARCHAR(15) UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('USER', 'DELIVERY_GUY', 'ADMIN') DEFAULT 'USER',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Courier Table

```sql
CREATE TABLE courier (
  courier_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  status VARCHAR(50) DEFAULT 'AVAILABLE',
  pick_up_address VARCHAR(255),
  delivery_address VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES user(user_id)
);
```

### Order Table

```sql
CREATE TABLE orders (
  order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  status VARCHAR(50) DEFAULT 'PENDING',
  total_amount DECIMAL(10, 2),
  delivery_address VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(user_id)
);
```

### Order Items Table

```sql
CREATE TABLE order_items (
  item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  item_name VARCHAR(255),
  quantity INT,
  price DECIMAL(10, 2),
  FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
```

### Delivery Assignments Table

```sql
CREATE TABLE delivery_assignments (
  assignment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  courier_id BIGINT NOT NULL,
  status VARCHAR(50),
  assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES orders(order_id),
  FOREIGN KEY (courier_id) REFERENCES courier(courier_id)
);
```

### Tracking Events Table

```sql
CREATE TABLE tracking_events (
  event_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  event_type VARCHAR(50),
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  details TEXT,
  FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
```

### Payments Table

```sql
CREATE TABLE payments (
  payment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  amount DECIMAL(10, 2),
  status VARCHAR(50) DEFAULT 'PENDING',
  payment_method VARCHAR(50),
  transaction_id VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
```

### Addresses Table

```sql
CREATE TABLE addresses (
  address_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  address_type VARCHAR(50),
  street VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(100),
  zip_code VARCHAR(10),
  FOREIGN KEY (user_id) REFERENCES user(user_id)
);
```

---

## Running the Application

### Development Mode

```bash
# Using Maven
cd Pikndel
mvn spring-boot:run

# Application will start on http://localhost:8080
```

### Production Build

```bash
# Create JAR package
mvn clean package

# Run JAR file
java -jar target/Pikndel-0.0.1-SNAPSHOT.jar
```

### With Docker Compose

```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f
```

---

## Testing Endpoints

### Using cURL

#### 1. Register User

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "password": "password123"
  }'
```

#### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

_Save the token for authenticated requests_

#### 3. Create Order (with JWT)

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "userId": 1,
    "items": [{"itemName": "Package", "quantity": 1, "price": 100}],
    "deliveryAddress": "123 Main St"
  }'
```

### Using Postman

1. Import the API collection
2. Set up environment variables for `BASE_URL` and `TOKEN`
3. Execute requests in sequence

### Using REST Client (VS Code)

Create `test.http`:

```
### Register
POST http://localhost:8080/api/users/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "password": "password123"
}

### Login
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}

### Create Order
POST http://localhost:8080/api/orders
Content-Type: application/json
Authorization: Bearer <TOKEN>

{
  "userId": 1,
  "items": [{"itemName": "Package", "quantity": 1, "price": 100}],
  "deliveryAddress": "123 Main St"
}
```

---

## Security

### Authentication Flow

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       ├─ 1. Register/Login Request
       │   (username, password)
       │
       ▼
┌──────────────────┐
│  AuthController  │
└──────┬───────────┘
       │
       ├─ 2. Validate Credentials
       │   (BCrypt comparison)
       │
       ▼
┌──────────────────┐
│   JwtUtil        │
└──────┬───────────┘
       │
       ├─ 3. Generate JWT Token
       │   (HMAC-SHA512, 24h expiry)
       │
       ▼
┌──────────────────┐
│   Client         │
│  (stores token)  │
└──────┬───────────┘
       │
       ├─ 4. Authenticated Request
       │   (+ JWT in Authorization header)
       │
       ▼
┌──────────────────────┐
│ JwtAuthenticationFilter
└──────┬───────────────┘
       │
       ├─ 5. Extract & Validate JWT
       │   (verify signature, expiry)
       │
       ▼
┌──────────────────┐
│  SecurityContext │
│  (set principal) │
└──────┬───────────┘
       │
       ├─ 6. Grant Access
       │   (if authorized)
       │
       ▼
┌──────────────────┐
│  @Controller     │
│  @Service        │
└──────────────────┘
```

### Role-Based Access Control

| Endpoint                      | USER      | DELIVERY_GUY | ADMIN     |
| ----------------------------- | --------- | ------------ | --------- |
| POST /api/users/register      | ✅ Public | ✅ Public    | ✅ Public |
| GET /api/users                | ✅ Public | ✅ Public    | ✅ Public |
| POST /api/auth/login          | ✅        | ✅           | ✅        |
| POST /api/orders              | ✅        | ❌           | ✅        |
| POST /api/orders/{id}/assign  | ❌        | ❌           | ✅        |
| PUT /api/couriers/{id}/status | ❌        | ✅           | ✅        |
| POST /api/couriers            | ❌        | ❌           | ✅        |

### Password Security

- Passwords are encoded using **BCrypt** with strength 10
- Original password is never stored in database
- Password comparison done using BCrypt matcher

### JWT Security

- Tokens are signed with **HMAC-SHA512**
- Minimum key length: 256 bits
- Token expiration: 24 hours (configurable)
- Tokens include: username, role, issued at, expiration

---

## Environment Variables (Optional)

Create a `.env` file in project root:

```env
DB_URL=jdbc:mysql://localhost:3306/pikndel_db
DB_USERNAME=pikndel_user
DB_PASSWORD=pikndel_password
JWT_SECRET=YOUR_SECURE_SECRET_KEY_HERE
JWT_EXPIRATION=86400000
SERVER_PORT=8080
```

Then update `application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}
server.port=${SERVER_PORT}
```

---

## Troubleshooting

### Issue: 401 Unauthorized

**Solution**:

- Ensure JWT token is included in `Authorization` header
- Check token expiration
- Verify token is valid (test login endpoint)
- Ensure correct role for the endpoint

### Issue: 403 Forbidden

**Solution**:

- User role doesn't have permission for endpoint
- Check `@PreAuthorize` annotation on controller method
- Verify user role in database

### Issue: Database Connection Error

**Solution**:

- Verify MySQL is running
- Check database credentials in `application.properties`
- Ensure database `pikndel_db` exists

### Issue: CORS Error

**Solution**:

- CORS is already enabled in SecurityConfig
- Verify request origin is allowed
- Check browser console for CORS error details

---

## Performance Optimization

### Database Queries

- Use repository methods for optimized queries
- Implement pagination for large datasets
- Add database indexes on frequently queried columns

### Caching

- Consider adding Spring Cache for frequently accessed data
- Cache user details after authentication

### API Response

- Use DTOs to return only necessary fields
- Implement gzip compression

---

## Future Enhancements

- [ ] WebSocket support for real-time tracking
- [ ] Notification system (Email/SMS)
- [ ] Admin dashboard
- [ ] Advanced analytics and reporting
- [ ] Multi-language support
- [ ] Payment gateway integration
- [ ] Rating and review system
- [ ] Mobile app support
- [ ] Geolocation tracking
- [ ] Route optimization

---

## License

This project is proprietary software. All rights reserved.

---

## Support & Contact

For issues or questions:

- Email: manishjoc108@gmail.com
  

---

## Version History

| Version | Date       | Changes                            |
| ------- | ---------- | ---------------------------------- |
| 1.0.0   | 2025-12-16 | Initial release with core features |

---

## Author

**Manish Joshi**  
Developer & Maintainer

---

**Last Updated**: December 16, 2025  
**Status**: ✅ Production Ready
