# Deployment Guide

Complete guide for deploying Pikndel application to production.

---

## Table of Contents

1. [Pre-Deployment Checklist](#pre-deployment-checklist)
2. [Environment Setup](#environment-setup)
3. [Database Migration](#database-migration)
4. [Build Configuration](#build-configuration)
5. [Docker Deployment](#docker-deployment)
6. [Cloud Deployment](#cloud-deployment)
7. [Monitoring & Logging](#monitoring--logging)
8. [Backup & Recovery](#backup--recovery)

---

## Pre-Deployment Checklist

- [ ] All tests pass (`mvn test`)
- [ ] No compilation errors (`mvn clean compile`)
- [ ] Security vulnerabilities checked
- [ ] Environment variables configured
- [ ] Database backups taken
- [ ] SSL/TLS certificates prepared
- [ ] Load balancer configured
- [ ] Monitoring setup complete
- [ ] Incident response plan documented
- [ ] Team trained on deployment

---

## Environment Setup

### Production Environment Variables

Create `.env.production` file:

```env
# Server
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080
SERVER_SSL_ENABLED=true
SERVER_SSL_KEY_STORE=/path/to/keystore.jks
SERVER_SSL_KEY_STORE_PASSWORD=secure_password

# Database
SPRING_DATASOURCE_URL=jdbc:mysql://prod-db-server:3306/pikndel_db
SPRING_DATASOURCE_USERNAME=pikndel_prod_user
SPRING_DATASOURCE_PASSWORD=SECURE_PASSWORD_HERE
SPRING_DATASOURCE_MAX_ACTIVE=20
SPRING_DATASOURCE_MAX_IDLE=5

# JPA/Hibernate
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_SHOW_SQL=false

# JWT Security
JWT_SECRET=GENERATE_NEW_SECURE_256BIT_KEY
JWT_EXPIRATION=86400000

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_JOSHI=INFO
LOGGING_FILE=/var/log/pikndel/application.log

# Application
SPRING_APPLICATION_NAME=Pikndel
```

### Application Properties for Production

Create `application-production.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api
server.compression.enabled=true
server.compression.min-response-size=1024

# Database Configuration
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Logging
logging.level.root=INFO
logging.level.com.joshi=INFO
logging.file.name=/var/log/pikndel/application.log
logging.file.max-size=10MB
logging.file.max-history=30

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}

# Security
server.ssl.enabled=true
server.ssl.key-store=${SERVER_SSL_KEY_STORE}
server.ssl.key-store-password=${SERVER_SSL_KEY_STORE_PASSWORD}
```

---

## Database Migration

### Pre-Migration Tasks

1. **Backup Current Database**

```bash
mysqldump -u pikndel_prod_user -p pikndel_db > backup_$(date +%Y%m%d_%H%M%S).sql
```

2. **Verify Backup**

```bash
# Test restore from backup
mysql -u pikndel_prod_user -p < backup_20251216_120000.sql
```

### Migration Steps

1. **Set DDL to 'validate' in production** (no auto schema changes)

```properties
spring.jpa.hibernate.ddl-auto=validate
```

2. **Create Migration Scripts** for manual updates:

```sql
-- Migration script: V1_0_1__add_courier_status.sql
ALTER TABLE courier ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'AVAILABLE';
ALTER TABLE courier ADD INDEX idx_status (status);
```

3. **Apply Migrations**

```bash
# Test migrations on staging first
java -jar Pikndel-0.0.1-SNAPSHOT.jar --spring.profiles.active=staging

# Apply to production
java -jar Pikndel-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
```

---

## Build Configuration

### Maven Production Build

```bash
# Build with production profile
mvn clean package -Pproduction -DskipTests

# Build with specific version
mvn clean package -Dproject.version=1.0.0 -Pproduction -DskipTests

# Verify build
java -jar target/Pikndel-0.0.1-SNAPSHOT.jar --version
```

### JAR File Size Optimization

```bash
# Remove unnecessary dependencies
mvn clean package -Pproduction -DskipTests -Dmaven.compiler.parameters=true
```

### Build Verification

```bash
# Test run (local validation)
java -jar target/Pikndel-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=test \
  --server.port=8081 &

# Wait for startup
sleep 10

# Health check
curl http://localhost:8081/actuator/health

# Stop test instance
kill %1
```

---

## Docker Deployment

### Dockerfile

```dockerfile
# Multi-stage build
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Pproduction

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/Pikndel-*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD java -cp app.jar org.springframework.boot.loader.JarLauncher \
  org.springframework.boot.actuate.health.HealthEndpoint

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose (Production)

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: pikndel-db
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: pikndel_db
      MYSQL_USER: pikndel_user
      MYSQL_PASSWORD: pikndel_password
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql
    networks:
      - pikndel-network
    restart: unless-stopped

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: pikndel-app
    depends_on:
      - mysql
    environment:
      SPRING_PROFILES_ACTIVE: production
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/pikndel_db
      SPRING_DATASOURCE_USERNAME: pikndel_user
      SPRING_DATASOURCE_PASSWORD: pikndel_password
      JWT_SECRET: ${JWT_SECRET}
    ports:
      - "8080:8080"
    networks:
      - pikndel-network
    restart: unless-stopped

volumes:
  db_data:

networks:
  pikndel-network:
    driver: bridge
```

### Deploy with Docker

```bash
# Build image
docker build -t pikndel:1.0.0 .

# Tag for registry
docker tag pikndel:1.0.0 your-registry/pikndel:1.0.0

# Push to registry
docker push your-registry/pikndel:1.0.0

# Run container
docker run -d \
  --name pikndel \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=production \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-host:3306/pikndel_db \
  -e JWT_SECRET=your_secure_key \
  your-registry/pikndel:1.0.0
```

---

## Cloud Deployment

### AWS Deployment

#### Option 1: EC2 + RDS

```bash
# 1. Create EC2 Instance
aws ec2 run-instances \
  --image-id ami-0c55b159cbfafe1f0 \
  --instance-type t3.medium \
  --key-name your-key

# 2. Connect to instance
ssh -i your-key.pem ec2-user@your-instance-ip

# 3. Install Java
sudo yum update
sudo yum install -y java-17-amazon-corretto

# 4. Deploy JAR
scp -i your-key.pem target/Pikndel-*.jar ec2-user@your-instance-ip:/home/ec2-user/

# 5. Run application
ssh -i your-key.pem ec2-user@your-instance-ip
java -jar /home/ec2-user/Pikndel-*.jar \
  --spring.datasource.url=jdbc:mysql://rds-endpoint:3306/pikndel_db \
  --spring.datasource.username=admin \
  --spring.datasource.password=secure_password
```

#### Option 2: ECS Fargate

```yaml
# ECS Task Definition (task-definition.json)
{
  "family": "pikndel",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "containerDefinitions":
    [
      {
        "name": "pikndel",
        "image": "your-registry/pikndel:1.0.0",
        "portMappings":
          [{ "containerPort": 8080, "hostPort": 8080, "protocol": "tcp" }],
        "environment":
          [{ "name": "SPRING_PROFILES_ACTIVE", "value": "production" }],
        "logConfiguration":
          {
            "logDriver": "awslogs",
            "options":
              {
                "awslogs-group": "/ecs/pikndel",
                "awslogs-region": "us-east-1",
                "awslogs-stream-prefix": "ecs",
              },
          },
      },
    ],
}
```

```bash
# Register task definition
aws ecs register-task-definition --cli-input-json file://task-definition.json

# Create service
aws ecs create-service \
  --cluster pikndel-cluster \
  --service-name pikndel-service \
  --task-definition pikndel \
  --desired-count 2 \
  --launch-type FARGATE
```

### Azure Deployment

```bash
# 1. Create resource group
az group create --name pikndel-rg --location eastus

# 2. Create App Service
az appservice plan create \
  --name pikndel-plan \
  --resource-group pikndel-rg \
  --sku B2 --is-linux

# 3. Deploy JAR
az webapp create \
  --resource-group pikndel-rg \
  --plan pikndel-plan \
  --name pikndel-app \
  --runtime "JAVA|17"

# 4. Upload JAR
az webapp deployment source config-zip \
  --resource-group pikndel-rg \
  --name pikndel-app \
  --src target/Pikndel-*.jar
```

---

## Monitoring & Logging

### Application Monitoring

#### Spring Boot Actuator

Enable in `application-production.properties`:

```properties
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=when-authorized
management.metrics.enable.jvm=true
```

**Endpoints**:

- `GET /actuator/health` - Application health
- `GET /actuator/metrics` - Application metrics
- `GET /actuator/info` - Application info

#### Prometheus Integration

Add dependency to `pom.xml`:

```xml
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Access metrics: `GET /actuator/prometheus`

### Logging Configuration

```properties
# File rotation
logging.file.name=/var/log/pikndel/application.log
logging.file.max-size=10MB
logging.file.max-history=30
logging.file.total-size-cap=1GB

# Log levels
logging.level.root=INFO
logging.level.com.joshi=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Log Aggregation

**ELK Stack** or **Splunk** integration:

```properties
# Send logs to ELK
logging.config=classpath:logback-spring.xml
```

---

## Backup & Recovery

### Database Backup Strategy

**Daily Backup**:

```bash
#!/bin/bash
BACKUP_DIR="/backups/pikndel"
DATE=$(date +%Y%m%d_%H%M%S)
mysqldump -u pikndel_user -p pikndel_db | gzip > $BACKUP_DIR/pikndel_$DATE.sql.gz
```

**Cron Job** (run daily at 2 AM):

```cron
0 2 * * * /usr/local/bin/backup-pikndel.sh
```

### Recovery Procedure

```bash
# 1. Restore from backup
gunzip < /backups/pikndel/pikndel_20251216_020000.sql.gz | mysql -u pikndel_user -p

# 2. Verify data
mysql -u pikndel_user -p pikndel_db -e "SELECT COUNT(*) FROM user;"

# 3. Restart application
systemctl restart pikndel
```

---

## Performance Tuning

### Database Performance

```sql
-- Add indexes
CREATE INDEX idx_user_email ON user(email);
CREATE INDEX idx_order_user_id ON orders(user_id);
CREATE INDEX idx_order_status ON orders(status);
CREATE INDEX idx_courier_status ON courier(status);

-- Analyze tables
ANALYZE TABLE user, orders, courier;
```

### JVM Tuning

```bash
java -Xms512m -Xmx2048m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -jar Pikndel-0.0.1-SNAPSHOT.jar
```

---

## Rollback Procedure

### If Deployment Fails

```bash
# 1. Stop application
systemctl stop pikndel

# 2. Restore previous version
cp /opt/pikndel/releases/Pikndel-0.0.0.jar /opt/pikndel/Pikndel.jar

# 3. Restore database
mysql -u pikndel_user -p < /backups/pikndel/pre_migration_backup.sql

# 4. Restart
systemctl start pikndel

# 5. Verify
curl http://localhost:8080/actuator/health
```

---

## Security Hardening

- [ ] Enable HTTPS/TLS
- [ ] Set strong JWT secret (256-bit)
- [ ] Enable rate limiting
- [ ] Configure firewall rules
- [ ] Enable audit logging
- [ ] Use secrets manager (AWS Secrets Manager, Azure Key Vault)
- [ ] Regular security patches
- [ ] OWASP compliance check

---

**Last Updated**: December 16, 2025
