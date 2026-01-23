# Configuration Guide

Complete configuration reference for Pikndel application.

---

## Table of Contents

1. [Configuration Profiles](#configuration-profiles)
2. [Application Properties](#application-properties)
3. [Environment Variables](#environment-variables)
4. [Database Configuration](#database-configuration)
5. [Security Configuration](#security-configuration)
6. [Server Configuration](#server-configuration)
7. [Logging Configuration](#logging-configuration)
8. [Advanced Configuration](#advanced-configuration)

---

## Configuration Profiles

Spring Boot profiles allow different configurations for different environments.

### Available Profiles

```
development (dev)  ─── Local development
test              ─── Unit/Integration testing
production (prod) ─── Production deployment
```

### Activate Profile

**Application Properties**:

```properties
spring.profiles.active=production
```

**Environment Variable**:

```bash
SPRING_PROFILES_ACTIVE=production
```

**Command Line**:

```bash
java -jar app.jar --spring.profiles.active=production
```

**Maven**:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=production"
```

### Profile-Specific Files

```
src/main/resources/
├── application.properties          # Default (all profiles)
├── application-dev.properties      # Development profile
├── application-test.properties     # Test profile
└── application-prod.properties     # Production profile
```

---

## Application Properties

### Complete Configuration Reference

#### Server Configuration

```properties
# Port
server.port=8080

# Context Path
server.servlet.context-path=/

# Connection Timeout
server.tomcat.connection-timeout=20000ms

# Session Timeout
server.servlet.session.timeout=30m

# Compression
server.compression.enabled=true
server.compression.min-response-size=1024
```

#### Database Configuration

```properties
# Connection
spring.datasource.url=jdbc:mysql://localhost:3306/pikndel_db
spring.datasource.username=pikndel_user
spring.datasource.password=pikndel_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Connection Pooling (HikariCP)
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.auto-commit=true
```

#### JPA/Hibernate Configuration

```properties
# Schema Generation
spring.jpa.hibernate.ddl-auto=update

# Hibernate Options:
# - validate       : Only validate schema, no changes
# - create         : Create schema, fail if exists
# - create-drop    : Create on start, drop on shutdown
# - drop-and-create: Drop and recreate
# - update         : Update existing schema (recommended for prod)

# SQL Output
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Performance
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
spring.jpa.properties.hibernate.jdbc.fetch_size=50

# Logging
spring.jpa.properties.hibernate.generate_statistics=false
```

#### JWT Configuration

```properties
# Secret Key (must be 256 bits or more)
jwt.secret=ThisIsAVerySecureSecretKeyWith256BitsForJWT

# Token Expiration (milliseconds)
# 86400000 = 24 hours
# 604800000 = 7 days
# 2592000000 = 30 days
jwt.expiration=86400000

# Custom JWT Properties (if extending)
jwt.refresh-token-expiration=604800000
jwt.token-prefix=Bearer
jwt.header-string=Authorization
```

#### Spring Security Configuration

```properties
# Security Headers
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.same-site=strict

# CORS Configuration
# (Configured in SecurityConfig.java)
```

#### Logging Configuration

```properties
# Root Level
logging.level.root=INFO

# Application Package
logging.level.com.joshi.Pikndel=DEBUG

# Spring Framework
logging.level.org.springframework=INFO
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=DEBUG

# Database
logging.level.org.hibernate=INFO
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# File Output
logging.file.name=logs/pikndel.log
logging.file.max-size=10MB
logging.file.max-history=30
logging.file.total-size-cap=1GB

# Console Pattern
logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

# File Pattern
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

#### Application Metadata

```properties
spring.application.name=Pikndel
spring.application.version=1.0.0

# Info Endpoints (accessible via /actuator/info)
info.app.name=Pikndel Logistics Delivery System
info.app.description=A Spring Boot application for managing logistics delivery
info.app.version=1.0.0
info.company.name=Your Company
info.company.url=https://yourcompany.com
```

---

## Environment Variables

### Using .env File

Create `.env.local` (local development):

```bash
# Database
DB_URL=jdbc:mysql://localhost:3306/pikndel_dev
DB_USERNAME=pikndel_dev
DB_PASSWORD=dev_password

# JWT
JWT_SECRET=ThisIsAVerySecureSecretKeyWith256BitsForJWT
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
PROFILES_ACTIVE=dev

# Logging
LOG_LEVEL=DEBUG
```

### Reading Environment Variables

**Application Properties**:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}
server.port=${SERVER_PORT}
logging.level.root=${LOG_LEVEL}
```

### Docker Environment Variables

```bash
docker run -d \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/pikndel \
  -e SPRING_DATASOURCE_USERNAME=pikndel \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e JWT_SECRET=your_secret_key \
  -e SERVER_PORT=8080 \
  pikndel:1.0.0
```

---

## Database Configuration

### MySQL Connection

```properties
spring.datasource.url=jdbc:mysql://[HOST]:[PORT]/[DATABASE]?useSSL=false&serverTimezone=UTC

# Connection String Breakdown:
# jdbc:mysql://          - Driver protocol
# localhost:3306         - Host and port
# /pikndel_db           - Database name
# ?useSSL=false         - Disable SSL
# &serverTimezone=UTC   - Timezone configuration
```

### Connection Pooling (HikariCP)

**Default Values**:

```properties
minimumIdle=5                    # Min idle connections
maximumPoolSize=20               # Max connections
connectionTimeout=30000          # 30 seconds
idleTimeout=600000               # 10 minutes
maxLifetime=1800000              # 30 minutes
autoCommit=true                  # Auto-commit transactions
```

**For High Traffic**:

```properties
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10
```

**For Low Traffic**:

```properties
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=2
```

### Schema Migration

#### Automatic (Development)

```properties
spring.jpa.hibernate.ddl-auto=create-drop
```

#### Manual (Production)

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Then run migration scripts manually:

```sql
-- migration.sql
ALTER TABLE courier ADD COLUMN status VARCHAR(50) DEFAULT 'AVAILABLE';
CREATE INDEX idx_status ON courier(status);
```

---

## Security Configuration

### JWT Security

```properties
# Secret Key Generation
# Minimum 256 bits required for HS512

# Generate new key:
# Using OpenSSL:
openssl rand -base64 32

# Using Java:
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
keyGen.init(512);
SecretKey key = keyGen.generateKey();
String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
System.out.println(encodedKey);
```

### SSL/TLS Configuration

```properties
# Enable HTTPS
server.ssl.enabled=true

# Certificate
server.ssl.key-store=/path/to/keystore.jks
server.ssl.key-store-password=your_password
server.ssl.key-store-type=JKS
server.ssl.key-alias=tomcat

# Self-signed Certificate Generation
keytool -genkey -alias tomcat -storetype PKCS12 \
  -keyalg RSA -keysize 2048 \
  -keystore keystore.p12 \
  -validity 365
```

### CORS Configuration

**In SecurityConfig.java**:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults());  // Enable CORS with defaults
    return http.build();
}
```

**Custom CORS**:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://yourdomain.com"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    config.setAllowedHeaders(Collections.singletonList("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

---

## Server Configuration

### Tomcat Tuning

```properties
# Thread Pool
server.tomcat.threads.max=200
server.tomcat.threads.min-spare=10

# Connection
server.tomcat.max-connections=10000
server.tomcat.accept-count=100

# Request
server.tomcat.max-http-post-size=2097152

# Session
server.servlet.session.timeout=30m
server.servlet.session.persistent=false
```

### Compression

```properties
# Enable compression
server.compression.enabled=true

# Compress responses larger than 1KB
server.compression.min-response-size=1024

# Compression levels
server.compression.level=6  # 1-9 (higher = more CPU)

# Mime types to compress
server.compression.mimetypes=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json
```

---

## Logging Configuration

### Log Levels

```
TRACE   - Most detailed (framework internals)
DEBUG   - Development debugging information
INFO    - General informational messages
WARN    - Warning messages
ERROR   - Error conditions
FATAL   - System failure conditions
```

### Logback Spring Configuration

Create `logback-spring.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- File Appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/pikndel.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>logs/pikndel-%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>1GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Logger Configuration -->
    <logger name="com.joshi.Pikndel" level="DEBUG" />
    <logger name="org.springframework.security" level="DEBUG" />
    <logger name="org.hibernate" level="INFO" />

    <!-- Root Logger -->
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

---

## Advanced Configuration

### Actuator Endpoints

```properties
# Enable endpoints
management.endpoints.web.exposure.include=health,metrics,info,env

# Endpoint paths
management.endpoints.web.base-path=/actuator

# Health endpoint
management.endpoint.health.show-details=when-authorized
management.endpoint.health.show-components=when-authorized

# Metrics
management.metrics.enable.jvm=true
management.metrics.enable.system=true
```

**Available Endpoints**:

- `/actuator/health` - Application health
- `/actuator/metrics` - Metrics list
- `/actuator/metrics/{metric.name}` - Specific metric
- `/actuator/info` - Application info
- `/actuator/env` - Environment properties

### Custom Configuration Properties

Create custom property class:

```java
@Configuration
@ConfigurationProperties(prefix = "app.email")
@Setter
@Getter
public class EmailConfig {
    private String host;
    private int port;
    private String username;
    private String password;
}
```

Define in properties:

```properties
app.email.host=smtp.gmail.com
app.email.port=587
app.email.username=your-email@gmail.com
app.email.password=your-app-password
```

Use in service:

```java
@Service
public class EmailService {
    private final EmailConfig emailConfig;

    public void sendEmail(String to, String subject, String body) {
        // Use emailConfig.getHost(), etc.
    }
}
```

### Multi-Environment Secrets

Use Spring Cloud Config Server or AWS Secrets Manager:

```properties
# Using Spring Cloud Config
spring.cloud.config.uri=https://config-server:8888
spring.cloud.config.label=main
spring.cloud.config.name=pikndel
```

---

## Configuration Checklist

### Development

- [ ] Use H2 or local MySQL database
- [ ] Set `ddl-auto=create-drop`
- [ ] Enable SQL logging
- [ ] Set log level to DEBUG
- [ ] Disable HTTPS
- [ ] Use default JWT secret (fine for dev)

### Testing

- [ ] Use H2 in-memory database
- [ ] Set `ddl-auto=create-drop`
- [ ] Use test profile
- [ ] Mock external services
- [ ] Clear database between tests

### Production

- [ ] Set `ddl-auto=validate`
- [ ] Use secure JWT secret (256+ bits)
- [ ] Enable HTTPS/TLS
- [ ] Set log level to INFO
- [ ] Use environment variables for secrets
- [ ] Configure database replicas
- [ ] Enable connection pooling
- [ ] Set up monitoring
- [ ] Configure backups

---

## Troubleshooting Configuration

### Property Not Being Loaded

**Solution**:

```properties
# Check property name (case-sensitive)
spring.datasource.url=...  # Correct
spring.DataSource.URL=...   # Wrong (case matters)

# Reload properties in IDE
- Delete target/
- Rebuild project
- Restart application
```

### Environment Variables Not Working

**Solution**:

```bash
# Verify variable is set
echo $SPRING_DATASOURCE_URL  # Linux/Mac
echo %SPRING_DATASOURCE_URL%  # Windows

# Use __ instead of . for complex names
SPRING_DATASOURCE_URL=...
SPRING_DATASOURCE_USERNAME=...  # Not SPRING_DATASOURCE.USERNAME

# Restart application after setting env vars
```

### Database Connection Failed

**Solution**:

```bash
# Verify MySQL running
mysql -u username -p

# Check connection string
jdbc:mysql://localhost:3306/dbname

# Verify credentials
mysql -u pikndel_user -p pikndel_db
```

---

**Last Updated**: December 16, 2025
