# Spring Boot Enterprise REST API Starter Kit

A production-ready, enterprise-grade REST API backend template built with Spring Boot 3, Java 21, Spring Security 6, JWT (JSON Web Tokens), Spring Data JPA, and OpenAPI 3 / Swagger.

This project is architected following Clean Architecture and MVC design principles. It serves as a comprehensive reference implementation demonstrating Object-Oriented Programming (OOP) concepts, advanced Java features, and modern software engineering design patterns.

---

## Architectural Principles and Design Patterns

### 1. Layered Architecture (MVC Data Flow)
The application enforces strict separation of concerns through clearly segregated layers:
* **Controller Layer (`web / controller`)**: Exposes REST endpoints, validates HTTP requests, and returns standardized responses.
* **Service Layer (`service`)**: Encapsulates business logic, transactional boundaries, and domain rules.
* **Repository Layer (`repository`)**: Manages persistence via Spring Data JPA with JPQL and automatic query derivation.
* **Domain / Entity Layer (`entity`)**: Encapsulates persistent state and relationship mappings.
* **DTO Layer (`dto`)**: Decouples internal database entities from external API contracts using immutable Java records.

### 2. Object-Oriented Programming (OOP) Concepts
* **Inheritance**: The abstract class `BaseAuditableEntity` serves as a `@MappedSuperclass` for all entities (`User`, `Post`, `VerificationToken`, `RefreshToken`). Common fields (`id`, `createdAt`, `updatedAt`, `deleted`, `version`) are defined once and inherited everywhere.
* **Abstraction**: Service interfaces (`AuthService`, `UserService`, `PostService`, `GenericService`) decouple business contracts from concrete implementations.
* **Polymorphism**: Strategy-driven verification, custom exception handling hierarchy, and role-based authorization.
* **Encapsulation**: Strict use of access modifiers, getter/setter protection, and immutable records for data carriers.

### 3. Advanced Java 21 Features
* **Java Records**: Utilized across all Request and Response DTOs for immutability, thread-safety, and boilerplate reduction.
* **Streams and Functional Pipelines**: Functional processing for mapping collections, extracting claims, and filtering entities.
* **Optional Handling**: Explicit null-safety using `Optional.map()`, `flatMap()`, and `orElseThrow()`.
* **Standardized Exception Hierarchy**: Subclassed `ApiException` handled globally via `@RestControllerAdvice`.

### 4. Security Architecture
* **Stateless Authentication**: Completely stateless session management (`SessionCreationPolicy.STATELESS`) powered by Spring Security 6.
* **JWT (JSON Web Token)**: Uses JJWT 0.12 with HMAC-SHA256 digital signatures for access tokens.
* **Refresh Token Rotation**: Persistent refresh tokens enable secure session continuity without storing credentials.
* **Password Hashing**: Cryptographic hashing using `BCryptPasswordEncoder`.
* **Role-Based Access Control (RBAC)**: Fine-grained method security using `@PreAuthorize`.

---

## Project Directory Structure

```text
springboot_starter_kit/
├── .mvn/
│   └── wrapper/
│       ├── maven-wrapper.jar
│       └── maven-wrapper.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── example/
    │   │           └── starterapi/
    │   │               ├── StarterApiApplication.java
    │   │               ├── auth/
    │   │               │   ├── controller/
    │   │               │   │   └── AuthController.java
    │   │               │   ├── dto/
    │   │               │   │   ├── AuthResponse.java
    │   │               │   │   ├── LoginRequest.java
    │   │               │   │   ├── RefreshTokenRequest.java
    │   │               │   │   ├── RegisterRequest.java
    │   │               │   │   ├── ResendVerificationRequest.java
    │   │               │   │   └── VerifyEmailResponse.java
    │   │               │   ├── entity/
    │   │               │   │   ├── RefreshToken.java
    │   │               │   │   ├── TokenType.java
    │   │               │   │   └── VerificationToken.java
    │   │               │   ├── repository/
    │   │               │   │   ├── RefreshTokenRepository.java
    │   │               │   │   └── VerificationTokenRepository.java
    │   │               │   └── service/
    │   │               │       ├── AuthService.java
    │   │               │       └── AuthServiceImpl.java
    │   │               ├── common/
    │   │               │   ├── controller/
    │   │               │   │   └── HealthController.java
    │   │               │   ├── entity/
    │   │               │   │   └── BaseAuditableEntity.java
    │   │               │   ├── exception/
    │   │               │   │   ├── ApiException.java
    │   │               │   │   ├── BadRequestException.java
    │   │               │   │   ├── ErrorResponse.java
    │   │               │   │   ├── ForbiddenException.java
    │   │               │   │   ├── GlobalExceptionHandler.java
    │   │               │   │   ├── ResourceNotFoundException.java
    │   │               │   │   └── UnauthorizedException.java
    │   │               │   ├── response/
    │   │               │   │   ├── ApiResponse.java
    │   │               │   │   └── PagedResponse.java
    │   │               │   └── service/
    │   │               │       └── GenericService.java
    │   │               ├── config/
    │   │               │   ├── AuditConfig.java
    │   │               │   ├── DataInitializer.java
    │   │               │   ├── OpenApiConfig.java
    │   │               │   └── WebMvcConfig.java
    │   │               ├── post/
    │   │               │   ├── controller/
    │   │               │   │   └── PostController.java
    │   │               │   ├── dto/
    │   │               │   │   ├── CreatePostRequest.java
    │   │               │   │   ├── PostResponse.java
    │   │               │   │   └── UpdatePostRequest.java
    │   │               │   ├── entity/
    │   │               │   │   ├── Post.java
    │   │               │   │   └── PostStatus.java
    │   │               │   ├── repository/
    │   │               │   │   └── PostRepository.java
    │   │               │   └── service/
    │   │               │       ├── PostService.java
    │   │               │       └── PostServiceImpl.java
    │   │               ├── security/
    │   │               │   ├── CurrentUser.java
    │   │               │   ├── SecurityConfig.java
    │   │               │   ├── jwt/
    │   │               │   │   ├── JwtAuthenticationEntryPoint.java
    │   │               │   │   ├── JwtAuthenticationFilter.java
    │   │               │   │   └── JwtTokenProvider.java
    │   │               │   └── service/
    │   │               │       ├── CustomUserDetails.java
    │   │               │       └── CustomUserDetailsService.java
    │   │               └── user/
    │   │                   ├── controller/
    │   │                   │   └── UserController.java
    │   │                   ├── dto/
    │   │                   │   ├── UpdateProfileRequest.java
    │   │                   │   └── UserProfileResponse.java
    │   │                   ├── entity/
    │   │                   │   ├── Role.java
    │   │                   │   └── User.java
    │   │                   ├── repository/
    │   │                   │   └── UserRepository.java
    │   │                   └── service/
    │   │                       ├── UserService.java
    │   │                       └── UserServiceImpl.java
    │   └── resources/
    │       ├── application.yml
    │       ├── static/
    │       └── templates/
    └── test/
        └── java/
            └── com/
                └── example/
                    └── starterapi/
                        └── StarterApiApplicationTests.java
```

---

## Prerequisites

Before running the project, verify that **Java 21** (or Java 22) is installed:

```bash
java -version
```

Note: You do **not** need to install Maven separately. The included Maven Wrapper (`mvnw` / `mvnw.cmd`) automatically provisions the correct Maven build environment.

---

## Step-by-Step Instructions to Run

### Step 1: Navigate to Project Directory
Open your terminal (PowerShell, Command Prompt, or Bash) and change directory into the project root:

```bash
cd "e:\Starter Kit\springboot_starter_kit"
```

### Step 2: Build the Application
Run the build command using the Maven Wrapper:

* On Windows (PowerShell or Command Prompt):
  ```cmd
  .\mvnw.cmd clean package -DskipTests
  ```

* On Linux or macOS:
  ```bash
  ./mvnw clean package -DskipTests
  ```

### Step 3: Run the Application
You can run the application directly through Maven or as an executable JAR file.

* Option A: Using Maven Spring Boot Plugin:
  ```cmd
  .\mvnw.cmd spring-boot:run
  ```

* Option B: Using the Packaged JAR:
  ```cmd
  java -jar target/starter-api-1.0.0.jar
  ```

### Step 4: Verify Server is Running
Open your browser or run the following curl command:
```bash
curl http://localhost:8080/api/v1/health
```

Expected Response:
```json
{
  "success": true,
  "message": "Service is healthy and running",
  "data": {
    "status": "UP",
    "version": "1.0.0",
    "service": "Spring Boot Starter Kit API"
  }
}
```

---

## Developer Portals and Tooling

### 1. Interactive Swagger UI Documentation
Open the following URL in any browser to inspect and execute all API requests interactively:
* URL: `http://localhost:8080/swagger-ui.html`
* Features: Complete schema viewer, Try-it-out runner, and Bearer Token authentication injection.

### 2. In-Memory Database Console (H2 Console)
The application runs on an embedded in-memory database with zero installation required. You can inspect tables and records directly in your browser:
* URL: `http://localhost:8080/h2-console`
* JDBC URL: `jdbc:h2:mem:starterkitdb`
* User Name: `sa`
* Password: `password`

### 3. Pre-Configured Administrator Account
On application startup, a default administrator account is automatically seeded for immediate testing:
* Email: `admin@example.com`
* Username: `admin`
* Password: `admin123`
* Role: `ROLE_ADMIN`

---

## API Reference

### Standard API Response Format

All API endpoints return data wrapped inside a consistent generic structure:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { ... },
  "timestamp": "2026-10-07T08:30:00Z"
}
```

In case of error, the standardized error structure is returned:

```json
{
  "timestamp": "2026-10-07T08:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/auth/register",
  "validationErrors": {
    "email": "Email must be a valid email address"
  }
}
```

---

### Authentication Endpoints (`/api/v1/auth`)

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/v1/auth/register` | Public | Register a new user account |
| POST | `/api/v1/auth/login` | Public | Authenticate user and issue JWT Access and Refresh tokens |
| GET | `/api/v1/auth/verify-email?token={token}` | Public | Verify user account email using verification token |
| POST | `/api/v1/auth/refresh-token` | Public | Obtain new access token using valid refresh token |
| POST | `/api/v1/auth/resend-verification` | Public | Resend verification token to registered email |

#### Example: Register Request
`POST http://localhost:8080/api/v1/auth/register`
```json
{
  "name": "John Doe",
  "username": "johndoe",
  "email": "johndoe@example.com",
  "password": "Password123!"
}
```

#### Example: Login Request
`POST http://localhost:8080/api/v1/auth/login`
```json
{
  "usernameOrEmail": "johndoe@example.com",
  "password": "Password123!"
}
```

#### Example: Login Response
```json
{
  "success": true,
  "message": "Authentication successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "4a7f92e1-8f3b-4c22-b5b6-71d3e8e2fa51",
    "tokenType": "Bearer",
    "expiresInMs": 86400000,
    "user": {
      "id": 2,
      "name": "John Doe",
      "username": "johndoe",
      "email": "johndoe@example.com",
      "role": "ROLE_USER",
      "verified": false,
      "createdAt": "2026-10-07T08:35:12.124Z"
    }
  }
}
```

---

### User Endpoints (`/api/v1/users`)

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/v1/users/me` | Bearer Token | Retrieve currently authenticated user profile |
| PUT | `/api/v1/users/me` | Bearer Token | Update currently authenticated user profile |
| GET | `/api/v1/users` | Bearer Token (Admin) | Paginated list of all users in the system |

---

### Post / Resource Endpoints (`/api/v1/posts`)

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/v1/posts` | Bearer Token | Create a new blog post |
| GET | `/api/v1/posts` | Public | Retrieve paginated published posts (supports `search`, `page`, `size`, `sort`) |
| GET | `/api/v1/posts/{id}` | Public | Retrieve single post by numerical ID (increments views) |
| GET | `/api/v1/posts/slug/{slug}` | Public | Retrieve single post by unique URL slug |
| GET | `/api/v1/posts/my-posts` | Bearer Token | Retrieve all posts authored by current user |
| PUT | `/api/v1/posts/{id}` | Bearer Token (Author/Admin) | Update title, content, or status of an existing post |
| DELETE | `/api/v1/posts/{id}` | Bearer Token (Author/Admin) | Soft delete a post (`deleted = true`) |

#### Example: Create Post Request
`POST http://localhost:8080/api/v1/posts`  
Header: `Authorization: Bearer <your_access_token>`
```json
{
  "title": "Mastering Spring Boot and Clean Architecture",
  "content": "In this guide, we explore how to design enterprise systems with Spring Boot, JPA, and JWT.",
  "status": "PUBLISHED"
}
```

#### Example: Query Posts with Pagination and Search
`GET http://localhost:8080/api/v1/posts?search=Spring&page=0&size=5&sort=createdAt,desc`

---

## Step-by-Step Testing Workflow via cURL

### 1. Check Service Health
```bash
curl -X GET http://localhost:8080/api/v1/health
```

### 2. Login as Pre-seeded Admin
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"admin@example.com","password":"admin123"}'
```
*Note: Copy the `accessToken` from the response for subsequent requests.*

### 3. Fetch User Profile
```bash
curl -X GET http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer <YOUR_ACCESS_TOKEN>"
```

### 4. Create a New Post
```bash
curl -X POST http://localhost:8080/api/v1/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <YOUR_ACCESS_TOKEN>" \
  -d '{"title":"My First Enterprise Post","content":"Detailed post content goes here...","status":"PUBLISHED"}'
```

### 5. Fetch All Posts (Public)
```bash
curl -X GET "http://localhost:8080/api/v1/posts?page=0&size=10"
```

---

## License
This project is open-source and released under the Apache 2.0 License.
