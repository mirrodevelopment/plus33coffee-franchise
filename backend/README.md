# ============================================================
# PLUS33 CAFÉ FRANÇAIS — FRANCHISE BACKEND
# ============================================================
# Spring Boot 3.4 · Java 21 · Maven

## Project Structure

```
backend/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/plus33/franchise/
    │   │   ├── FranchiseApplication.java          ← Spring Boot entry point
    │   │   ├── config/
    │   │   │   └── SecurityConfig.java            ← CORS + Auth rules
    │   │   ├── controller/
    │   │   │   ├── FranchiseApplicationController.java
    │   │   │   └── ContactInquiryController.java
    │   │   ├── dto/
    │   │   │   ├── ApiResponse.java                ← Generic wrapper
    │   │   │   ├── FranchiseApplicationRequest.java
    │   │   │   ├── FranchiseApplicationResponse.java
    │   │   │   └── ContactInquiryRequest.java
    │   │   ├── exception/
    │   │   │   └── GlobalExceptionHandler.java
    │   │   ├── model/
    │   │   │   ├── FranchiseApplication.java       ← JPA entity
    │   │   │   └── ContactInquiry.java
    │   │   ├── repository/
    │   │   │   ├── FranchiseApplicationRepository.java
    │   │   │   └── ContactInquiryRepository.java
    │   │   └── service/
    │   │       ├── FranchiseApplicationService.java
    │   │       ├── ContactInquiryService.java
    │   │       └── EmailNotificationService.java
    │   └── resources/
    │       ├── application.properties               ← Dev (H2)
    │       └── application-prod.properties          ← Prod (MySQL)
    └── test/
        ├── java/com/plus33/franchise/
        │   └── FranchiseApplicationTests.java
        └── resources/
            └── application-test.properties
```

## Running the Application

### Prerequisites
- Java 21 (`C:\Program Files\Microsoft\jdk-21.0.12.8-hotspot`)
- Maven 3.9.6 (`C:\tools\apache-maven-3.9.6`)

### Start (Development — H2 in-memory DB)
```powershell
cd "c:\Franchaise - Plus 33\backend"
$env:PATH += ";C:\tools\apache-maven-3.9.6\bin;C:\Program Files\Microsoft\jdk-21.0.12.8-hotspot\bin"
mvn spring-boot:run
```

### Build JAR
```powershell
mvn clean package -DskipTests
java -jar target/franchise-backend-1.0.0.jar
```

### Production (MySQL)
```powershell
java -jar target/franchise-backend-1.0.0.jar --spring.profiles.active=prod
```

## API Endpoints

### Public (no auth required)
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/applications | Submit franchise application (apply.html) |
| POST | /api/inquiries | Submit contact inquiry (index.html) |
| GET | /actuator/health | Health check |

### Admin (Basic Auth: admin / plus33admin)
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/applications | List all applications |
| GET | /api/applications/{id} | Get single application |
| GET | /api/applications/search?q=john | Search |
| GET | /api/applications/status/PENDING | Filter by status |
| PATCH | /api/applications/{id}/status | Update status |
| DELETE | /api/applications/{id} | Delete |
| GET | /api/applications/stats | Dashboard KPIs |
| GET | /api/applications/recent | Latest 10 |
| GET | /api/applications/analytics/model | Count by model |
| GET | /api/inquiries | All inquiries |

## H2 Console
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:plus33db`
- User: `sa` | Password: *(empty)*

## Email Configuration
Update `application.properties`:
```properties
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
```
Create App Password: Gmail → Settings → Security → 2-Step Verification → App Passwords

## Production MySQL Setup
```sql
CREATE DATABASE plus33_franchise CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'plus33user'@'localhost' IDENTIFIED BY 'strongpassword';
GRANT ALL PRIVILEGES ON plus33_franchise.* TO 'plus33user'@'localhost';
```
Then update `application-prod.properties` credentials.
