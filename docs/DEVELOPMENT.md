# Development Guide

Guide for developers setting up local development environment and contributing to Pikndel.

---

## Table of Contents

1. [Local Development Setup](#local-development-setup)
2. [Project Structure](#project-structure)
3. [Development Workflow](#development-workflow)
4. [Code Conventions](#code-conventions)
5. [Testing](#testing)
6. [Debugging](#debugging)
7. [Common Tasks](#common-tasks)
8. [Troubleshooting](#troubleshooting)

---

## Local Development Setup

### Prerequisites

- **Java 17**: Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.java.net/)
- **Maven 3.9+**: Download from [Apache Maven](https://maven.apache.org/download.cgi)
- **MySQL 8.0+**: Download from [MySQL](https://www.mysql.com/downloads/mysql/)
- **Git**: Download from [Git](https://git-scm.com/)
- **IDE**: IntelliJ IDEA or VS Code (recommended)

### Step 1: Clone Repository

```bash
git clone <repository-url>
cd Pikndel
```

### Step 2: Set Up MySQL

```bash
# Start MySQL service (if not already running)
# On Windows: services.msc -> Start MySQL service
# On Linux: sudo systemctl start mysql
# On Mac: brew services start mysql

# Connect to MySQL
mysql -u root -p

# Create database
CREATE DATABASE pikndel_dev;
CREATE USER 'pikndel_dev'@'localhost' IDENTIFIED BY 'dev_password';
GRANT ALL PRIVILEGES ON pikndel_dev.* TO 'pikndel_dev'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Step 3: Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# Database (Development)
spring.datasource.url=jdbc:mysql://localhost:3306/pikndel_dev
spring.datasource.username=pikndel_dev
spring.datasource.password=dev_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate (Development - auto-create schema)
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT
jwt.secret=ThisIsAVerySecureSecretKeyWith256BitsForJWT
jwt.expiration=86400000

# Logging (Development)
logging.level.root=INFO
logging.level.com.joshi=DEBUG
logging.level.org.springframework=DEBUG
logging.level.org.hibernate=DEBUG

# Application name
spring.application.name=Pikndel
```

### Step 4: Build Project

```bash
cd Pikndel
mvn clean install
```

### Step 5: Run Application

```bash
mvn spring-boot:run
```

Application starts on `http://localhost:8080`

### Step 6: Verify Setup

```bash
# Health check
curl http://localhost:8080/api/users
```

---

## Project Structure

```
Pikndel/
├── src/
│   ├── main/
│   │   ├── java/com/joshi/Pikndel/
│   │   │   ├── PikndelApplication.java          # Main entry point
│   │   │   │
│   │   │   ├── config/                          # Configuration
│   │   │   │   ├── SecurityConfig.java          # Spring Security setup
│   │   │   │   ├── JWTUtil.java                 # JWT token utilities
│   │   │   │   ├── JWTFilter.java               # JWT filter
│   │   │   │   └── UserServiceDetailsImpl.java   # User details service
│   │   │   │
│   │   │   ├── controller/                      # REST endpoints
│   │   │   │   ├── UserController.java          # User management
│   │   │   │   ├── OrderController.java         # Order management
│   │   │   │   ├── CourierController.java       # Courier management
│   │   │   │   └── AuthController.java          # Authentication
│   │   │   │
│   │   │   ├── service/                         # Business logic
│   │   │   │   ├── UserService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   └── CourierService.java
│   │   │   │
│   │   │   ├── repository/                      # Data access
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── CourierRepository.java
│   │   │   │
│   │   │   ├── entity/                          # JPA entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── Courier.java
│   │   │   │   └── ... (other entities)
│   │   │   │
│   │   │   ├── dto/                             # Data transfer objects
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   └── OrderDto.java
│   │   │   │
│   │   │   ├── mapper/                          # Entity mappers
│   │   │   │   └── OrderMapper.java
│   │   │   │
│   │   │   └── util/                            # Utilities
│   │   │       ├── JwtUtil.java
│   │   │       └── JwtAuthenticationFilter.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties            # Main config
│   │       ├── application-dev.properties        # Dev profile
│   │       ├── application-test.properties       # Test profile
│   │       ├── data.sql                          # Sample data
│   │       └── docker-compose.yml
│   │
│   └── test/
│       └── java/com/joshi/Pikndel/
│           └── PikndelApplicationTests.java
│
├── docs/                                        # Documentation
│   ├── API.md
│   ├── DEPLOYMENT.md
│   └── DEVELOPMENT.md
│
├── pom.xml                                      # Maven configuration
├── README.md                                    # Project overview
├── HELP.md
└── mvnw, mvnw.cmd                               # Maven wrapper
```

---

## Development Workflow

### 1. Create Feature Branch

```bash
git checkout -b feature/your-feature-name
```

### 2. Make Changes

- Edit files
- Follow code conventions (see below)
- Compile: `mvn clean compile`

### 3. Test Changes

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserControllerTest

# Run specific test method
mvn test -Dtest=UserControllerTest#testRegisterUser
```

### 4. Commit Changes

```bash
git add .
git commit -m "feat: add courier status update feature"
```

### 5. Push Branch

```bash
git push origin feature/your-feature-name
```

### 6. Create Pull Request

On GitHub:

1. Go to Pull Requests tab
2. Click "New Pull Request"
3. Select your branch
4. Write description
5. Submit PR

---

## Code Conventions

### Naming Conventions

**Classes**: PascalCase

```java
public class UserController { }
public class OrderService { }
```

**Methods**: camelCase

```java
public void createOrder() { }
private String generateToken() { }
```

**Variables**: camelCase

```java
String userName;
int orderCount;
```

**Constants**: UPPER_SNAKE_CASE

```java
private static final String JWT_HEADER = "Authorization";
private static final int MAX_ATTEMPTS = 5;
```

### Code Style

#### Imports

```java
// Organize imports: java, javax, org, com
import java.util.*;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joshi.Pikndel.entity.User;
import com.joshi.Pikndel.repository.UserRepository;
```

#### Methods

```java
// Maximum 3-4 parameters
public Order createOrder(Long userId, OrderDto dto, String ipAddress) {
    // Logic here
}

// Use meaningful names
private boolean isValidEmail(String email) { }
private Optional<User> findUserByEmail(String email) { }
```

#### Comments

```java
/**
 * Create a new order for the user.
 *
 * @param userId the user ID
 * @param dto the order data transfer object
 * @return the created order
 * @throws IllegalArgumentException if user not found
 */
public Order createOrder(Long userId, OrderDto dto) {
    // Implementation
}

// Inline comments for complex logic
int total = 0;
// Calculate total with tax (10%)
for (OrderItem item : items) {
        total += item.getPrice() * item.getQuantity() * 1.1;
    }
```

### Logging

```java
@Slf4j
@Service
public class UserService {

    public void registerUser(User user) {
        log.debug("Registering user: {}", user.getEmail());

        // Validation
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("User registration failed: email already exists");
            throw new IllegalArgumentException("Email already registered");
        }

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getId());
    }
}
```

---

## Testing

### Unit Tests

```java
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUser() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.registerUser(user);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(user);
    }
}
```

### Integration Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRegisterEndpoint() throws Exception {
        String json = "{\"name\": \"John\", \"email\": \"john@example.com\", " +
                      "\"phone\": \"1234567890\", \"password\": \"pass123\"}";

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }
}
```

### Run Tests

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

---

## Debugging

### Enable Debug Logging

Set in `application.properties`:

```properties
logging.level.com.joshi.Pikndel=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate=DEBUG
```

### Debug in IDE

**IntelliJ IDEA**:

1. Set breakpoint (click on line number)
2. Run -> Debug 'Application'
3. Application pauses at breakpoint
4. Use Debug panel to inspect variables

**VS Code**:

1. Install Debugger for Java
2. Create `.vscode/launch.json`:

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot App",
      "request": "launch",
      "mainClass": "com.joshi.Pikndel.PikndelApplication",
      "cwd": "${workspaceFolder}/Pikndel"
    }
  ]
}
```

### Remote Debugging

```bash
# Start app with debug port
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n \
  -jar target/Pikndel-0.0.1-SNAPSHOT.jar
```

---

## Common Tasks

### Add New Entity

1. **Create entity class** in `entity/`:

```java
@Entity
@Data
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    private Long orderId;
    private BigDecimal amount;
    private LocalDateTime issuedDate;
}
```

2. **Create repository**:

```java
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByOrderId(Long orderId);
}
```

3. **Create service**:

```java
@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public Invoice createInvoice(Long orderId, BigDecimal amount) {
        Invoice invoice = Invoice.builder()
            .orderId(orderId)
            .amount(amount)
            .issuedDate(LocalDateTime.now())
            .build();
        return invoiceRepository.save(invoice);
    }
}
```

4. **Create controller**:

```java
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(invoiceService.createInvoice(request.getOrderId(), request.getAmount()));
    }
}
```

### Add New Endpoint

1. Add method to existing controller
2. Add @RequestMapping annotation
3. Implement business logic via service
4. Add proper error handling
5. Write tests

### Update Database Schema

**For Development** (auto-update via Hibernate):

- Change entity fields
- Restart application
- Hibernate will update schema automatically

**For Production** (manual migration):

- Create SQL migration script
- Test on staging database
- Apply with approval

---

## Troubleshooting

### Issue: Application won't start

**Solutions**:

```bash
# Check for compilation errors
mvn clean compile

# Check for running instance on port 8080
lsof -i :8080

# Kill existing process
kill -9 <PID>

# Start fresh
mvn spring-boot:run
```

### Issue: Tests fail locally

**Solutions**:

```bash
# Run with verbose output
mvn test -X

# Clean and rebuild
mvn clean test

# Run single test to isolate issue
mvn test -Dtest=UserServiceTest#testRegisterUser
```

### Issue: Database connection errors

**Solutions**:

```bash
# Verify MySQL is running
mysql -u root -p

# Check credentials in application.properties
# Test connection
mysql -u pikndel_dev -p -h localhost pikndel_dev

# Check database exists
mysql -u root -p -e "SHOW DATABASES;"
```

### Issue: Port 8080 already in use

**Solutions** (Windows):

```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill process
taskkill /PID <PID> /F

# Or use different port
java -jar -Dserver.port=8081 target/Pikndel-0.0.1-SNAPSHOT.jar
```

**Solutions** (Linux/Mac):

```bash
lsof -i :8080
kill -9 <PID>
```

---

## IDE Setup

### IntelliJ IDEA

1. Open project: File -> Open -> Select Pikndel directory
2. Trust Maven project
3. Wait for indexing complete
4. Run -> Edit Configurations
5. Add new Spring Boot configuration
6. Main class: `com.joshi.Pikndel.PikndelApplication`
7. Run

### VS Code

1. Install extensions:

   - Extension Pack for Java
   - Spring Boot Extension Pack
   - REST Client

2. Open workspace: File -> Open Folder -> Select Pikndel

3. Create `.vscode/launch.json`:

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot App",
      "request": "launch",
      "mainClass": "com.joshi.Pikndel.PikndelApplication"
    }
  ]
}
```

4. Press F5 to start debugging

---

## Performance Profiling

### JProfiler Integration

Add to VM options:

```
-agentpath:/path/to/jprofiler/bin/jprofilerti=port=8849
```

### Java Flight Recorder

```bash
java -XX:+UnlockCommercialFeatures \
  -XX:+FlightRecorder \
  -XX:StartFlightRecording=duration=60s,filename=recording.jfr \
  -jar target/Pikndel-0.0.1-SNAPSHOT.jar
```

---

**Last Updated**: December 16, 2025
