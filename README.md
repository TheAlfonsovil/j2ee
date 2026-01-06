# J2EE Enterprise Application Demo

🚀 **Full-stack enterprise application showcasing advanced J2EE concepts with Spring Boot, Hibernate, Angular, and PostgreSQL**

## 📋 Project Overview

This is a production-ready enterprise application built for a job interview, demonstrating proficiency in:
- **Backend**: J2EE, Spring Boot 3.x, Hibernate/JPA, Spring Security
- **Frontend**: Angular 17, TypeScript, Reactive Programming
- **Database**: PostgreSQL with Flyway migrations
- **Security**: JWT authentication with refresh tokens, role-based access control
- **Architecture**: RESTful API, layered architecture, DTO pattern, exception handling

## ✨ Key Features

### 🔐 Security & Authentication
- JWT-based authentication with access and refresh tokens
- BCrypt password hashing
- Role-based authorization (USER, ADMIN)
- HTTP-only secure token storage
- Automatic token refresh mechanism
- Protected routes with Angular guards
- CORS configuration for cross-origin requests

### 📊 Backend Highlights (Spring Boot)
- **Spring Security** with custom authentication filters
- **Hibernate/JPA** with entity auditing (@CreatedBy, @CreatedDate, etc.)
- **Flyway** database migrations for version control
- **MapStruct** for DTO-entity mapping
- **Global Exception Handling** with @RestControllerAdvice
- **Bean Validation** (@Valid, custom validators)
- **Transactional Services** with @Transactional
- **OpenAPI/Swagger** documentation (accessible at `/swagger-ui.html`)
- **Spring Actuator** for monitoring and health checks
- **Connection Pooling** with HikariCP
- **Audit Logging** for tracking user actions
- **Multiple Profiles** (dev, prod) for environment-specific configs

### 🎨 Frontend Highlights (Angular)
- **Standalone Components** (modern Angular architecture)
- **Route Guards** (authGuard, adminGuard, loginGuard)
- **HTTP Interceptors** for automatic JWT injection and token refresh
- **Reactive Programming** with RxJS Observables
- **Type-safe Models** with TypeScript interfaces
- **Role-based UI** rendering (different views for USER vs ADMIN)
- **Paginated Data Tables** for users and audit logs
- **Responsive Design** with modern CSS
- **Error Handling** with user-friendly messages
- **Auto-redirect** to login when unauthenticated

### 📈 Admin Dashboard Features
- User management with pagination
- Complete audit log viewer
- System statistics and analytics
- Real-time user activity tracking
- Filtering and sorting capabilities

## 🏗️ Architecture

```
j2ee/
├── backend/                          # Spring Boot Application
│   ├── src/main/java/com/interview/j2ee/
│   │   ├── config/                   # Configuration classes
│   │   │   ├── SecurityConfig.java   # Spring Security + JWT config
│   │   │   ├── OpenAPIConfig.java    # Swagger documentation
│   │   │   └── AuditingConfig.java   # JPA auditing
│   │   ├── controller/               # REST Controllers
│   │   │   ├── AuthController.java   # Login, logout, refresh
│   │   │   ├── UserController.java   # User endpoints
│   │   │   └── AdminController.java  # Admin-only endpoints
│   │   ├── dto/                      # Data Transfer Objects
│   │   ├── entity/                   # JPA Entities
│   │   │   ├── User.java            # User entity with auditing
│   │   │   ├── RefreshToken.java    # JWT refresh tokens
│   │   │   └── AuditLog.java        # Activity tracking
│   │   ├── exception/                # Custom exceptions & handlers
│   │   ├── mapper/                   # MapStruct mappers
│   │   ├── repository/               # Spring Data JPA repositories
│   │   ├── security/                 # JWT & Security components
│   │   │   ├── JwtTokenProvider.java
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── UserPrincipal.java
│   │   └── service/                  # Business logic layer
│   ├── src/main/resources/
│   │   ├── application.yml           # Main configuration
│   │   ├── application-dev.yml       # Development profile
│   │   ├── application-prod.yml      # Production profile
│   │   └── db/migration/             # Flyway SQL migrations
│   └── pom.xml                       # Maven dependencies
│
├── frontend/                         # Angular Application
│   ├── src/app/
│   │   ├── components/
│   │   │   ├── login/               # Login component
│   │   │   └── dashboard/           # Dashboard with role-based views
│   │   ├── guards/                  # Route guards
│   │   ├── interceptors/            # HTTP interceptors
│   │   ├── models/                  # TypeScript interfaces
│   │   ├── services/                # Angular services
│   │   │   ├── auth.service.ts     # Authentication service
│   │   │   └── admin.service.ts    # Admin operations
│   │   ├── app.routes.ts           # Application routing
│   │   └── app.config.ts           # App configuration
│   └── package.json                # Node dependencies
│
└── docker-compose.yml              # PostgreSQL + pgAdmin setup
```

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.1
- **ORM**: Hibernate 6.x / JPA
- **Database**: PostgreSQL 16
- **Migration**: Flyway
- **Security**: Spring Security 6.x + JWT (jjwt 0.12.x)
- **Documentation**: SpringDoc OpenAPI 3
- **Build Tool**: Maven
- **Java Version**: 17

### Frontend
- **Framework**: Angular 17
- **Language**: TypeScript 5.2
- **HTTP Client**: Angular HttpClient
- **State Management**: RxJS BehaviorSubject
- **Styling**: CSS3 (custom design)

### DevOps & Tools
- **Containerization**: Docker & Docker Compose
- **Database Admin**: pgAdmin 4
- **API Testing**: Swagger UI (built-in)

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Node.js 18+ & npm
- Docker & Docker Compose
- Maven 3.6+

### 1. Start PostgreSQL Database

```bash
# Start PostgreSQL and pgAdmin containers
docker-compose up -d

# Verify containers are running
docker ps
```

**Database Connection Details:**
- Host: `localhost`
- Port: `5432`
- Database: `j2eedb`
- Username: `admin`
- Password: `admin123`

**pgAdmin Access:**
- URL: http://localhost:5050
- Email: admin@admin.com
- Password: admin

### 2. Run Backend (Spring Boot)

```bash
cd backend

# Build the project
mvn clean install

# Run with dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or run the JAR directly
java -jar target/j2ee-demo-1.0.0.jar
```

**Backend will start on:** http://localhost:8080

**Important Endpoints:**
- API Base: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/api-docs`
- Health Check: `http://localhost:8080/actuator/health`

### 3. Run Frontend (Angular)

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

**Frontend will start on:** http://localhost:4200

### 4. Login Credentials

The database is automatically initialized with two users:

**Regular User:**
- Username: `user`
- Password: `1234`
- Role: USER (Limited dashboard access)

**Administrator:**
- Username: `admin`
- Password: `1234`
- Role: ADMIN (Full dashboard access)

## 📚 API Documentation

### Authentication Endpoints

#### POST `/api/auth/login`
Login with username and password
```json
{
  "username": "admin",
  "password": "1234"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "a1b2c3d4-...",
  "tokenType": "Bearer",
  "id": 2,
  "username": "admin",
  "email": "admin@example.com",
  "fullName": "Admin User",
  "roles": ["ROLE_ADMIN"]
}
```

#### POST `/api/auth/refresh`
Refresh access token using refresh token
```json
{
  "refreshToken": "a1b2c3d4-..."
}
```

#### POST `/api/auth/logout`
Logout and invalidate tokens (requires authentication)

### User Endpoints

#### GET `/api/users/me`
Get current authenticated user details (requires authentication)

### Admin Endpoints (ADMIN role required)

#### GET `/api/admin/users`
Get paginated list of all users
- Query params: `page`, `size`, `sortBy`, `sortDir`

#### GET `/api/admin/users/{id}`
Get user by ID

#### GET `/api/admin/audit-logs`
Get paginated audit logs
- Query params: `page`, `size`, `username`, `action`

## 🎯 Key Implementation Details

### 1. JWT Authentication Flow
1. User sends credentials to `/api/auth/login`
2. Backend validates credentials and generates JWT access token (24h) and refresh token (7 days)
3. Frontend stores tokens in localStorage
4. HTTP interceptor automatically adds JWT to all requests
5. On 401 error, interceptor automatically refreshes token
6. On logout, tokens are invalidated in backend

### 2. Database Migrations
Flyway automatically runs SQL migrations on startup:
- `V1__Create_users_table.sql` - Creates users and refresh_tokens tables
- `V2__Create_audit_log_table.sql` - Creates audit logging table
- Default users are inserted with BCrypt hashed passwords

### 3. Entity Auditing
All entities extending `AuditableEntity` automatically track:
- `createdAt` - When entity was created
- `updatedAt` - When entity was last modified
- `createdBy` - Username who created the entity
- `updatedBy` - Username who last modified the entity

### 4. Role-Based Access Control
- **USER role**: Can only view their own profile
- **ADMIN role**: Can view all users, audit logs, and statistics
- Angular guards prevent unauthorized route access
- Spring Security @PreAuthorize annotations protect endpoints

### 5. Exception Handling
Global exception handler provides consistent error responses:
- `ResourceNotFoundException` → 404
- `BadRequestException` → 400
- `BadCredentialsException` → 401
- `AccessDeniedException` → 403
- Validation errors → 400 with field details

## 📊 Database Schema

### users
- `id` - Primary key
- `username` - Unique username
- `password` - BCrypt hashed password
- `role` - USER or ADMIN
- `email`, `first_name`, `last_name`
- `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`
- Audit fields (created_at, updated_at, created_by, updated_by)

### refresh_tokens
- `id` - Primary key
- `token` - Unique UUID token
- `user_id` - Foreign key to users
- `expiry_date` - Token expiration timestamp

### audit_log
- `id` - Primary key
- `user_id` - Foreign key to users (nullable)
- `username` - Username who performed action
- `action` - Action type (LOGIN, LOGOUT, etc.)
- `entity_type`, `entity_id` - Related entity info
- `details` - Additional information
- `ip_address` - User's IP address
- `created_at` - Timestamp

## 🔍 Testing the Application

### 1. Test Authentication
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"1234"}'

# Use the returned token
export TOKEN="<access_token>"

# Get current user
curl http://localhost:8080/api/users/me \
  -H "Authorization: Bearer $TOKEN"
```

### 2. Test Admin Endpoints
```bash
# Get all users (admin only)
curl http://localhost:8080/api/admin/users?page=0&size=10 \
  -H "Authorization: Bearer $TOKEN"

# Get audit logs (admin only)
curl http://localhost:8080/api/admin/audit-logs \
  -H "Authorization: Bearer $TOKEN"
```

### 3. Access Swagger UI
Navigate to http://localhost:8080/swagger-ui.html to:
- View all API endpoints
- Test endpoints interactively
- See request/response schemas

## 🎓 Interview Talking Points

### J2EE & Spring Boot Expertise
1. **Dependency Injection**: Demonstrated through @Autowired, @RequiredArgsConstructor (Lombok)
2. **AOP**: Transaction management with @Transactional
3. **Security**: Comprehensive Spring Security configuration with JWT
4. **Data Access**: Spring Data JPA repositories with custom queries
5. **REST API Design**: RESTful endpoints following best practices
6. **Configuration Management**: Multiple profiles, externalized configuration

### Hibernate & JPA
1. **Entity Relationships**: @ManyToOne, @OneToMany with cascade operations
2. **Auditing**: @CreatedDate, @CreatedBy, @LastModifiedDate, @LastModifiedBy
3. **Lazy Loading**: Optimized fetch strategies
4. **Indexes**: Database indexes on frequently queried columns
5. **Migrations**: Version-controlled schema with Flyway

### Advanced Features
1. **DTO Pattern**: Separation of API layer from domain model using MapStruct
2. **Exception Handling**: Global @RestControllerAdvice for consistent error responses
3. **Validation**: Bean Validation API (@Valid, @NotBlank, etc.)
4. **Pagination**: Pageable support for large datasets
5. **Logging**: SLF4J with structured logging
6. **Monitoring**: Spring Actuator endpoints
7. **API Documentation**: OpenAPI 3.0 specification

### Frontend Skills
1. **Modern Angular**: Standalone components, signals-ready architecture
2. **TypeScript**: Strong typing, interfaces, enums
3. **Reactive Programming**: RxJS operators, observables
4. **Guards & Interceptors**: Authentication flow, automatic token refresh
5. **State Management**: BehaviorSubject for user state
6. **Responsive Design**: Mobile-friendly CSS

## 📝 Future Enhancements (Optional)

- [ ] Unit & Integration Tests (JUnit 5, Mockito, Spring Test)
- [ ] Redis caching for improved performance
- [ ] Email notifications (Spring Mail)
- [ ] Password reset functionality
- [ ] Two-factor authentication (2FA)
- [ ] Rate limiting for API endpoints
- [ ] WebSocket support for real-time notifications
- [ ] File upload/download capabilities
- [ ] Advanced search and filtering
- [ ] Export to CSV/PDF functionality
- [ ] Docker multi-stage builds
- [ ] CI/CD pipeline configuration
- [ ] Kubernetes deployment manifests

## 📄 License

This project is created for interview demonstration purposes.

## 👤 Author

Interview Candidate - Showcasing enterprise Java development skills

---

**Built with ❤️ using Spring Boot, Angular, and PostgreSQL**
