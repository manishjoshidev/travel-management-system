# Architecture Guide

Detailed architecture documentation for Pikndel system.

---

## Table of Contents

1. [System Architecture](#system-architecture)
2. [Technology Stack](#technology-stack)
3. [Layered Architecture](#layered-architecture)
4. [Data Flow](#data-flow)
5. [Database Design](#database-design)
6. [Security Architecture](#security-architecture)
7. [Scalability](#scalability)
8. [Design Patterns](#design-patterns)

---

## System Architecture

### High-Level Overview

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENT LAYER                          │
│  (Web Browser, Mobile App, Third-party Integrations)    │
└────────────────────────┬────────────────────────────────┘
                         │
                    HTTP/HTTPS
                         │
┌────────────────────────▼────────────────────────────────┐
│               API GATEWAY / LOAD BALANCER                │
│                  (Nginx / HAProxy)                       │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                  REST API SERVERS                        │
│            (Spring Boot Applications)                    │
│  ┌──────────────────────────────────────────────────┐   │
│  │  Authentication & Authorization (JWT)            │   │
│  │  ┌──────────────────────────────────────────┐    │   │
│  │  │  Controllers (REST Endpoints)            │    │   │
│  │  ├──────────────────────────────────────────┤    │   │
│  │  │  Services (Business Logic)               │    │   │
│  │  ├──────────────────────────────────────────┤    │   │
│  │  │  Repositories (Data Access)              │    │   │
│  │  └──────────────────────────────────────────┘    │   │
│  └──────────────────────────────────────────────────┘   │
└────────────────────────┬────────────────────────────────┘
                         │
            ┌────────────┴──────────────┐
            │                           │
            │                           │
┌───────────▼────────────┐   ┌─────────▼─────────────┐
│    MySQL Database      │   │  Cache (Optional)     │
│  (Primary Data Store)  │   │  (Redis)              │
└────────────────────────┘   └───────────────────────┘
```

---

## Technology Stack

### Backend Framework

- **Spring Boot 3.5.6**: Web framework with embedded Tomcat
- **Spring Security 6.5.x**: Authentication and authorization
- **Spring Data JPA 3.5.6**: ORM and data persistence

### Database

- **MySQL 8.0**: Relational database
- **Hibernate 6.6.2**: ORM mapping
- **HikariCP**: Connection pooling

### Security

- **JJWT 0.12.3**: JWT token generation and validation
- **BCrypt**: Password encryption
- **Spring Security OAuth2**: Resource server configuration

### Build & Deployment

- **Maven 3.9+**: Build automation
- **Java 17 LTS**: Programming language
- **Docker**: Containerization

### Additional Libraries

- **Lombok 1.18.30**: Annotation processing for boilerplate
- **Jackson**: JSON serialization/deserialization
- **Jakarta Persistence**: Standard JPA API

---

## Layered Architecture

### 4-Tier Architecture

```
┌─────────────────────────────────────────┐
│    1. PRESENTATION LAYER                │
│    (REST Controllers)                   │
│                                         │
│  UserController                         │
│  OrderController                        │
│  CourierController                      │
│  AuthController                         │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│    2. BUSINESS LOGIC LAYER              │
│    (Services)                           │
│                                         │
│  UserService                            │
│  OrderService                           │
│  CourierService                         │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│    3. DATA ACCESS LAYER                 │
│    (Repositories)                       │
│                                         │
│  UserRepository                         │
│  OrderRepository                        │
│  CourierRepository                      │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│    4. DATABASE LAYER                    │
│    (MySQL)                              │
│                                         │
│  CRUD Operations                        │
│  Transactions                           │
│  Relationships                          │
└─────────────────────────────────────────┘
```

### Cross-Cutting Concerns

```
┌───────────────────────────────────────────────┐
│         SECURITY & AUTHENTICATION             │
│  ┌─────────────────────────────────────────┐  │
│  │  JWT Authentication Filter              │  │
│  │  Security Config                        │  │
│  │  Authority Validation                   │  │
│  └─────────────────────────────────────────┘  │
└───────────────────────────────────────────────┘
                       ▲
                       │
┌──────────────────────┴──────────────────────┐
│   LOGGING & MONITORING                     │
│   ERROR HANDLING & EXCEPTION MAPPING       │
│   VALIDATION & BUSINESS RULES              │
└────────────────────────────────────────────┘
```

---

## Data Flow

### User Registration Flow

```
┌──────────────────┐
│  Client Request  │
│ POST /register   │
└────────┬─────────┘
         │
         ├─ Email: user@example.com
         ├─ Password: pass123
         └─ Name: John Doe
         │
         ▼
┌─────────────────────────────┐
│  UserController.register()  │
│                             │
│ 1. Validate input          │
│ 2. Check email uniqueness  │
└────────┬────────────────────┘
         │
         ▼
┌──────────────────────────────┐
│  UserService.registerUser()  │
│                              │
│ 1. Hash password (BCrypt)    │
│ 2. Create User entity        │
│ 3. Save to database          │
│ 4. Return user details       │
└────────┬─────────────────────┘
         │
         ▼
┌────────────────────────────┐
│  UserRepository.save()     │
│                            │
│ Hibernate ORM              │
│ ↓                          │
│ INSERT INTO user ...       │
└────────┬───────────────────┘
         │
         ▼
┌────────────────────────────┐
│  Database Persisted        │
│  ✓ User created            │
│  ✓ Response sent to client │
└────────────────────────────┘
```

### Authentication & Authorization Flow

```
┌──────────────────────────────┐
│  Client Request              │
│  Authorization: Bearer JWT   │
└────────┬─────────────────────┘
         │
         ▼
┌──────────────────────────────────────┐
│  JwtAuthenticationFilter             │
│                                      │
│ 1. Extract JWT from Authorization   │
│ 2. Validate signature (HMAC-SHA512)  │
│ 3. Check expiration                  │
│ 4. Extract claims (username, role)   │
└────────┬─────────────────────────────┘
         │
         ├─ Token valid? ─┐
         │                └─► Yes
         ▼                    │
    Invalid                   ▼
       │             ┌──────────────────┐
       │             │ Set SecurityCtx  │
       │             │ - Principal      │
       │             │ - Authorities    │
       │             │ - Credentials    │
       │             └────────┬─────────┘
       │                      │
       │                      ▼
       │             ┌──────────────────────┐
       │             │  Check @PreAuthorize │
       │             │  Role validation     │
       │             └────────┬─────────────┘
       │                      │
       │                      ├─ Authorized? ─┐
       │                      │                └─► Yes
       │                      ▼                    │
       │                 Forbidden             Permission
       │                    │                 Granted
       │                    │                    │
       └────┬───────────────┴────────────────────┘
            │
            ▼
    Response to Client
    (401 or 403 or 200)
```

---

## Database Design

### Entity-Relationship Diagram

```
┌─────────────────────────────────────────┐
│             USER                        │
├─────────────────────────────────────────┤
│ user_id (PK)                            │
│ name                                    │
│ email (UNIQUE)                          │
│ phone (UNIQUE)                          │
│ password (HASHED)                       │
│ role (ENUM)                             │
│ created_at                              │
└─────────────┬──────────────────┬────────┘
              │ 1:N             │ 1:N
         ┌────▼────┐       ┌────▼──────┐
         │ ORDERS  │       │ COURIER   │
         │         │       │           │
    ┌────┴─────────────┐   │ courier_id│
    │ order_id (PK)    │   │ (PK)      │
    │ user_id (FK)     │   │ user_id   │
    │ status           │   │ (FK)      │
    │ delivery_addr    │   │ status    │
    │ total_amount     │   └────┬──────┘
    │ created_at       │        │
    └────┬─────────────┘        │ 1:N
         │                      │
         │ 1:N         ┌────────▼───────────┐
         │             │ DELIVERY_          │
    ┌────▼──────────┐  │ ASSIGNMENTS       │
    │ ORDER_ITEMS  │  │                    │
    │              │  │ assignment_id (PK) │
    │ item_id (PK) │  │ order_id (FK)      │
    │ order_id (FK)│  │ courier_id (FK)    │
    │ item_name    │  │ status             │
    │ quantity     │  │ assigned_at        │
    │ price        │  └────────────────────┘
    └──────────────┘

    ┌──────────────────────────┐
    │ ADDRESSES                │
    │                          │
    │ address_id (PK)          │
    │ user_id (FK) ──────┐ 1:N │
    │ address_type       └──────► USER
    │ street                     │
    │ city                       │
    │ state                      │
    │ zip_code                   │
    └──────────────────────────┘
```

### Key Relationships

| Relationship                  | From       | To            | Type | Notes                       |
| ----------------------------- | ---------- | ------------- | ---- | --------------------------- |
| User → Orders                 | user_id    | order_id      | 1:N  | One user has many orders    |
| User → Courier                | user_id    | user_id       | 1:1  | User becomes courier        |
| User → Addresses              | user_id    | address_id    | 1:N  | User has multiple addresses |
| Order → OrderItems            | order_id   | item_id       | 1:N  | Order contains items        |
| Order → DeliveryAssignments   | order_id   | assignment_id | 1:N  | Order can have assignments  |
| Courier → DeliveryAssignments | courier_id | assignment_id | 1:N  | Courier handles assignments |

---

## Security Architecture

### Authentication Pipeline

```
Request
  │
  ├─ No JWT Token
  │    │
  │    └─► Is endpoint public? ─── Yes ──► Allow
  │         │
  │         └─ No ──► 401 Unauthorized
  │
  ├─ JWT Token Present
  │    │
  │    ├─ Extract from Authorization Header
  │    │    │
  │    │    └─► JWT format: "Bearer <token>"
  │    │
  │    ├─ Validate Signature
  │    │    │
  │    │    ├─ Valid ──► Continue
  │    │    │
  │    │    └─ Invalid ──► 401 Unauthorized
  │    │
  │    ├─ Check Expiration
  │    │    │
  │    │    ├─ Not Expired ──► Continue
  │    │    │
  │    │    └─ Expired ──► 401 Unauthorized
  │    │
  │    ├─ Extract Claims
  │    │    │
  │    │    └─► username, role, issued_at, exp
  │    │
  │    ├─ Set SecurityContext
  │    │    │
  │    │    └─► Principal, Authorities
  │    │
  │    └─► Continue to Handler
  │
  └─ Authorization Check
       │
       ├─ Check @PreAuthorize roles
       │    │
       │    ├─ Authorized ──► Execute Handler
       │    │
       │    └─ Not Authorized ──► 403 Forbidden
       │
       └─► Response
```

### JWT Token Structure

```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3MzQzNDU2MzAsImV4cCI6MTczNDQzMjAzMH0...

└─ HEADER ──┴─ PAYLOAD ──┴─ SIGNATURE ──┘

HEADER:
{
  "alg": "HS512",
  "typ": "JWT"
}

PAYLOAD:
{
  "sub": "user@example.com",      // Username
  "role": "USER",                 // User role
  "iat": 1734345630,              // Issued at
  "exp": 1734432030               // Expiration (24h later)
}

SIGNATURE:
HMACSHA512(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  "ThisIsAVerySecureSecretKeyWith256BitsForJWT"
)
```

### Password Hashing

```
Plain Password
  │
  ├─ User: "password123"
  │
  ▼
BCrypt Encoder
  │
  ├─ Salt generation (random)
  ├─ Hash iterations
  ├─ Strength: 10 (default)
  │
  ▼
Hashed Password (Database)
  │
  └─ $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWDeS86E36DY1AUm
     ├─ $2a$ ─ BCrypt version
     ├─ $10$ ─ Cost/strength
     ├─ Next 22 chars ─ Salt
     └─ Remaining ─ Hashed password

During Login:
─────────────
1. User submits: "password123"
2. Read stored hash from DB: "$2a$10$N9qo8..."
3. BCrypt.matches("password123", "$2a$10$...")
4. ✓ Match ─► Token generated
5. ✗ No match ─► 401 Unauthorized
```

---

## Scalability

### Horizontal Scaling

```
┌────────────────────────────────────────────────────┐
│              Load Balancer (Nginx)                 │
├────────────────────────────────────────────────────┤
│  Round-robin / Least connections / IP hash        │
└──────────────┬──────────────────┬──────────────────┘
               │                  │
       ┌───────▼────────┐  ┌──────▼────────┐
       │ API Server 1   │  │ API Server 2  │
       │ Spring Boot    │  │ Spring Boot   │
       │ :8080          │  │ :8080         │
       └───────┬────────┘  └──────┬────────┘
               │                  │
               └──────────┬───────┘
                          │
                    ┌─────▼─────┐
                    │ MySQL DB  │
                    │ Replicated│
                    └───────────┘
```

### Caching Strategy

```
Request
  │
  ├─ Check Redis Cache
  │    │
  │    ├─ Cache Hit ──► Return cached data
  │    │
  │    └─ Cache Miss
  │         │
  │         ▼
  │    Query Database
  │         │
  │         ├─ Get data
  │         ├─ Store in Redis (TTL: 1h)
  │         │
  │         └─► Return data
  │
  └─► Response to Client

Cache Invalidation:
───────────────────
On Create/Update/Delete:
- Clear related cache entries
- OR use TTL-based expiration
- OR use event-based invalidation
```

### Database Optimization

```
Optimization Techniques:
───────────────────────

1. Indexing:
   - CREATE INDEX idx_user_email ON user(email);
   - CREATE INDEX idx_order_user_id ON orders(user_id);
   - CREATE INDEX idx_order_status ON orders(status);

2. Connection Pooling:
   - HikariCP: 20 max connections
   - 5 minimum idle connections
   - Connection timeout: 30s

3. Query Optimization:
   - Use Spring Data JPA derived queries
   - Avoid N+1 problem with @Fetch
   - Pagination for large datasets

4. Batch Operations:
   - Batch size: 20
   - Order inserts and updates
```

---

## Design Patterns

### Service Layer Pattern

```
Controller
    │
    ├─ Receives HTTP request
    ├─ Validates input
    │
    ▼
Service Layer
    │
    ├─ Business logic
    ├─ Validation rules
    ├─ Transaction management
    ├─ Exception handling
    │
    ▼
Repository Layer
    │
    ├─ Data access
    ├─ Query execution
    │
    ▼
Database
    │
    └─ Persistence
```

### Dependency Injection

```java
// Constructor Injection (Preferred)
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;  // Auto-injected
    private final UserService userService;
}

// Benefits:
// - Immutability
// - Easier testing
// - No circular dependencies
// - Clear dependencies
```

### Repository Pattern

```
Repository Interface
    │
    ├─ Abstracts database access
    ├─ Methods: save(), findById(), findAll(), delete()
    │
    ▼
JpaRepository (Spring Data)
    │
    ├─ Auto-implements common methods
    ├─ Supports derived queries
    ├─ Dynamic method generation
    │
    ▼
Database
```

### Data Transfer Object (DTO)

```
Entity (Database)
┌─────────────┐
│ - userId    │
│ - email     │
│ - password  │
│ - address   │
│ - phone     │
│ - createdAt │
└─────────────┘
       │
       └─ Mapper (MapStruct/Manual)
              │
              ▼
     DTO (API Response)
     ┌─────────────┐
     │ - userId    │
     │ - email     │
     │ - name      │
     │ - role      │
     │ (No password, address, etc.)
     └─────────────┘
```

### Exception Handling

```
Request
  │
  ├─ Try: Execute business logic
  │
  ├─ Catch: Custom Exceptions
  │    │
  │    ├─ UserNotFoundException ──► 404
  │    ├─ InvalidCredentialsException ──► 401
  │    ├─ InsufficientPermissionException ──► 403
  │    └─ BusinessLogicException ──► 400
  │
  ├─ Global Exception Handler
  │    │
  │    └─ Convert to HTTP Response
  │
  └─► Client Response (with proper status code)
```

---

## Performance Considerations

### Response Times Target

| Operation          | Target  | Actual |
| ------------------ | ------- | ------ |
| Authentication     | < 100ms | ~50ms  |
| User Registration  | < 200ms | ~80ms  |
| Order Creation     | < 300ms | ~150ms |
| Order List         | < 500ms | ~200ms |
| Courier Assignment | < 200ms | ~100ms |

### Database Queries

**Optimized**:

```sql
-- Use indexes
SELECT * FROM orders WHERE user_id = 1 AND status = 'PENDING';
CREATE INDEX idx_order_user_status ON orders(user_id, status);

-- Limit results
SELECT * FROM orders LIMIT 20;

-- Joins on indexed columns
SELECT o.*, u.name FROM orders o
JOIN user u ON o.user_id = u.user_id
WHERE o.status = 'PENDING';
```

**Avoid**:

```sql
-- Full table scans
SELECT * FROM orders WHERE status LIKE '%PENDING%';

-- N+1 queries (use joins)
SELECT * FROM orders;  -- Query 1
For each order:
  SELECT * FROM user WHERE id = order.user_id;  -- Query N

-- Unnecessary fields
SELECT * FROM user;  -- Get only needed fields
```

---

## Future Enhancements

### Scalability

- [ ] Message Queue (Kafka) for async operations
- [ ] Microservices architecture
- [ ] API Gateway (Kong)
- [ ] Service mesh (Istio)

### Performance

- [ ] Redis caching layer
- [ ] CDN for static content
- [ ] Database sharding
- [ ] Read replicas

### Features

- [ ] Real-time tracking (WebSocket)
- [ ] Notifications (Email/SMS)
- [ ] Analytics dashboard
- [ ] Advanced search (Elasticsearch)

---

**Last Updated**: December 16, 2025
