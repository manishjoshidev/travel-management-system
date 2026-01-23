# API Documentation

Complete API reference for Pikndel Logistics Delivery System.

## Base URL

```
http://localhost:8080
```

## Authentication

All protected endpoints require a JWT token in the `Authorization` header:

```
Authorization: Bearer <JWT_TOKEN>
```

### Getting a JWT Token

**Endpoint**: `POST /api/auth/login`

1. Register a user (if not already registered)
2. Call login endpoint with email and password
3. Extract the `token` from response
4. Include in all subsequent requests

---

## API Reference

### Authentication

#### POST /api/auth/login

Login user and get JWT token.

**Request**:

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Success Response (200)**:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

**Error Response (400)**:

```json
{
  "error": "Invalid email or password"
}
```

---

### User Management

#### POST /api/users/register

Register a new user.

**Request**:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "password": "password123"
}
```

**Success Response (201)**:

```json
{
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "role": "USER",
  "createdAt": "2025-12-16T10:00:00"
}
```

**Validation**:

- Email must be unique
- Phone must be unique
- Password minimum 6 characters

---

#### GET /api/users

Get all users (paginated).

**Query Parameters**:

- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)

**Response (200)**:

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

#### GET /api/users/{id}

Get user by ID.

**Path Parameters**:

- `id` (required): User ID

**Response (200)**:

```json
{
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "role": "USER"
}
```

**Error Response (404)**:

```json
{
  "error": "User not found"
}
```

---

#### DELETE /api/users/{id}

Delete user by ID.

**Path Parameters**:

- `id` (required): User ID

**Response (204)**: No Content

**Error Response (404)**:

```json
{
  "error": "User not found"
}
```

---

### Order Management

#### POST /api/orders

Create a new order. **Requires Authentication**

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
      "itemName": "Electronics",
      "quantity": 2,
      "price": 500.0
    },
    {
      "itemName": "Books",
      "quantity": 3,
      "price": 50.0
    }
  ],
  "deliveryAddress": "123 Main Street, New York, NY 10001"
}
```

**Success Response (201)**:

```json
{
  "orderId": 1,
  "userId": 1,
  "status": "PENDING",
  "totalAmount": 1150.0,
  "itemCount": 2,
  "deliveryAddress": "123 Main Street, New York, NY 10001",
  "createdAt": "2025-12-16T10:30:00"
}
```

**Validation**:

- User must exist
- At least one item required
- Item price must be positive

---

#### POST /api/orders/{id}/assign

Assign courier to order. **Requires ADMIN role**

**Headers**:

```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Path Parameters**:

- `id` (required): Order ID

**Response (200)**:

```json
{
  "orderId": 1,
  "courierId": 5,
  "status": "ASSIGNED",
  "courier": {
    "courierId": 5,
    "name": "Delivery Person",
    "phone": "9876543210"
  },
  "assignedAt": "2025-12-16T10:31:00"
}
```

**Error Response (403)**:

```json
{
  "error": "Insufficient permissions. ADMIN role required."
}
```

---

### Courier Management

#### POST /api/couriers

Create new courier. **Requires ADMIN role**

**Headers**:

```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Query Parameters**:

- `userId` (required): User ID to assign as courier

**Response (201)**:

```json
{
  "courierId": 5,
  "userId": 3,
  "status": "AVAILABLE",
  "createdAt": "2025-12-16T11:00:00"
}
```

---

#### GET /api/couriers

Get all couriers. **Requires Authentication**

**Headers**:

```
Authorization: Bearer <JWT_TOKEN>
```

**Response (200)**:

```json
[
  {
    "courierId": 5,
    "userId": 3,
    "status": "AVAILABLE",
    "name": "Delivery Person"
  }
]
```

---

#### GET /api/couriers/available

Get available couriers (public endpoint).

**Response (200)**:

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

#### GET /api/couriers/{id}

Get courier by ID. **Requires Authentication**

**Path Parameters**:

- `id` (required): Courier ID

**Response (200)**:

```json
{
  "courierId": 5,
  "userId": 3,
  "status": "AVAILABLE"
}
```

---

#### PUT /api/couriers/{id}/status

Update courier status. **Requires ADMIN or DELIVERY_GUY role**

**Headers**:

```
Authorization: Bearer <JWT_TOKEN>
```

**Path Parameters**:

- `id` (required): Courier ID

**Query Parameters**:

- `status` (required): New status (AVAILABLE, BUSY, INACTIVE)

**Response (200)**:

```json
{
  "courierId": 5,
  "userId": 3,
  "status": "BUSY",
  "updatedAt": "2025-12-16T11:30:00"
}
```

---

## HTTP Status Codes

| Code | Meaning      | Description                       |
| ---- | ------------ | --------------------------------- |
| 200  | OK           | Successful GET, POST, PUT request |
| 201  | Created      | Successful resource creation      |
| 204  | No Content   | Successful DELETE request         |
| 400  | Bad Request  | Invalid request data              |
| 401  | Unauthorized | Missing or invalid JWT token      |
| 403  | Forbidden    | Insufficient permissions          |
| 404  | Not Found    | Resource not found                |
| 500  | Server Error | Internal server error             |

---

## Error Responses

All error responses follow this format:

```json
{
  "timestamp": "2025-12-16T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "User email already exists",
  "path": "/api/users/register"
}
```

---

## Rate Limiting

Currently no rate limiting implemented. Future versions will include:

- Request throttling
- Per-endpoint limits
- IP-based limiting

---

## Pagination

List endpoints support pagination:

**Query Parameters**:

- `page`: Page number (0-indexed, default: 0)
- `size`: Items per page (default: 20, max: 100)
- `sort`: Sort criteria (optional)

**Example**:

```
GET /api/users?page=0&size=20&sort=name,asc
```

---

## Testing with cURL

### 1. Register User

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

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Save the token**:

```bash
export TOKEN="eyJhbGciOiJIUzUxMiJ9..."
```

### 3. Create Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "userId": 1,
    "items": [
      {"itemName": "Package", "quantity": 1, "price": 100}
    ],
    "deliveryAddress": "123 Main St"
  }'
```

### 4. List Couriers

```bash
curl -X GET http://localhost:8080/api/couriers/available
```

---

## Webhooks

Future implementation for real-time updates. Endpoints will notify external services of:

- Order creation
- Order assignment
- Delivery completion
- Payment status

---

## Version History

| Version | Date       | Changes             |
| ------- | ---------- | ------------------- |
| 1.0.0   | 2025-12-16 | Initial API release |

---

**Last Updated**: December 16, 2025
