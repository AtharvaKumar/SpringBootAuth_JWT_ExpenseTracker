# 🔐 Spring Boot JWT Authentication with Refresh Tokens

This project implements **JWT-based authentication** and **refresh token management** in a Spring Boot application. 
It uses **Spring Security**, **Spring Data JPA**, and **MySQL** to manage users, roles, and token lifecycle.

---

## 🚀 Features

- User **signup** with encrypted passwords (BCrypt).
- User **login** with JWT access token + refresh token.
- **Stateless authentication** (no HTTP session).
- Custom **JWT filter** integrated with Spring Security.
- Refresh token mechanism to get new access tokens.
- Role-based security with `UserRole` entity.

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot 3+
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL
- Lombok
- JJWT (JSON Web Token library)

---

## 📂 Project Structure & File Responsibilities

### 1. Authentication Layer (`com.mavenprojectdemo.auth`)

- **JwtAuthFilter**  
  Custom filter to intercept requests, extract JWT from headers, validate it, and set authentication.

- **SecurityConfig**  
  Configures Spring Security, disables CSRF, sets stateless session, configures permitted endpoints, 
  and adds `JwtAuthFilter` before `UsernamePasswordAuthenticationFilter`.

- **UserConfig**  
  Provides `BCryptPasswordEncoder` bean.

### 2. Controllers (`com.mavenprojectdemo.controller`)

- **AuthController**  
  Handles signup, creates user, and returns JWT + refresh token.

- **TokenController**  
  Handles login and refresh token logic.

### 3. Entities (`com.mavenprojectdemo.entities`)

- **UserInfo** → represents users.
- **UserRole** → represents user roles.
- **RefreshToken** → stores refresh tokens.

### 4. Repositories (`com.mavenprojectdemo.repository`)

- **UserRepository** → manages users.
- **RefreshTokenRepository** → manages refresh tokens.

### 5. DTOs (`com.mavenprojectdemo.request`, `com.mavenprojectdemo.response`)

- `AuthRequestDTO`, `RefreshTokenRequestDTO` → request objects.
- `JwtResponseDTO` → response with access + refresh tokens.
- `UserInfoDto` → extended signup model.

### 6. Services (`com.mavenprojectdemo.service`)

- **UserDetailsServiceImpl** → loads users and handles signup.  
- **CustomUserDetails** → adapts `UserInfo` into Spring Security’s `UserDetails`.  
- **JwtService** → generates and validates JWTs.  
- **RefreshTokenService** → manages refresh tokens.  

---

## 🔄 Code Execution Flow

### 1. User Signup (`/auth/v1/signup`)
1. Request hits `AuthController`.
2. User saved in DB with encoded password.
3. Access + refresh token generated.
4. Tokens returned in response.

### 2. User Login (`/auth/v1/login`)
1. Credentials authenticated via `AuthenticationManager`.
2. Access + refresh token generated.
3. Tokens returned in response.

### 3. Accessing Protected Endpoints
1. Request intercepted by `JwtAuthFilter`.
2. Token extracted and validated via `JwtService`.
3. User authenticated and added to `SecurityContextHolder`.
4. Controller executes if authentication is valid.

### 4. Refreshing Token (`/auth/v1/refreshToken`)
1. Refresh token validated in DB.
2. If not expired, new access token generated.
3. Response returned with new access token.

---

## ⚙️ Application Properties

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/authservice
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL8Dialect

server.port=9898
logging.level.org.springframework.security=DEBUG
```

---

## 📌 Example Requests

### Sign Up
```http
POST /auth/v1/signup
{
  "username": "john_doe",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com"
}
```

### Login
```http
POST /auth/v1/login
{
  "username": "john_doe",
  "password": "password123"
}
```

### Refresh Token
```http
POST /auth/v1/refreshToken
{
  "token": "2a1b-4cde-1234-5678"
}
```

---

## 📖 Summary of Execution Order
1. Signup/Login → Generates JWT + Refresh Token.  
2. Client stores tokens.  
3. Every request includes `Authorization: Bearer <accessToken>`.  
4. `JwtAuthFilter` validates token on each request.  
5. `SecurityContextHolder` holds authentication.  
6. Refresh token generates new access token when needed.  
