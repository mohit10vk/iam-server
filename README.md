# Identity & Access Management (IAM) System

A secure Identity and Access Management (IAM) system built with Spring Boot and Spring Security. This project provides user authentication and authorization using JWT (JSON Web Token), role-based access control (RBAC), and refresh token support.

## 🚀 Features

* User Registration
* User Login
* JWT Access Token Authentication
* Refresh Token Support
* Role-Based Access Control (Admin/User)
* BCrypt Password Encryption
* Spring Security Integration
* Secure REST APIs
* PostgreSQL Database
* Exception Handling
* Validation Support

---

## 🛠️ Tech Stack

* Java 17
* Spring Boot 3
* Spring Security
* Spring Data JPA
* PostgreSQL
* JWT (JJWT)
* Maven
* Lombok

---

## 📁 Project Structure

```text
src
 ├── config
 │    ├── SecurityConfig
 │    └── JwtAuthenticationFilter
 │
 ├── controller
 │    ├── AuthController
 │    └── UserController
 │
 ├── dto
 │
 ├── entity
 │    ├── User
 │    └── RefreshToken
 │
 ├── repository
 │
 ├── service
 │    └── impl
 │
 ├── security
 │    └── JwtSecurity
 │
 └── exception
```

---

## 🔐 Authentication Flow

1. User registers with email and password.
2. Password is encrypted using BCrypt before storing in the database.
3. User logs in using email and password.
4. Spring Security authenticates the user.
5. A JWT Access Token and Refresh Token are generated.
6. The client sends the Access Token in the `Authorization` header.
7. JWT is validated for every protected API request.
8. If the Access Token expires, a new one can be generated using the Refresh Token.

---

## 🔑 JWT Token Structure

The Access Token contains the following information:

```json
{
  "sub": "user@example.com",
  "role": "ROLE_USER",
  "iat": 1754280000,
  "exp": 1754283600
}
```

---

## 📌 API Endpoints

### Authentication

| Method | Endpoint              | Description                 |
| ------ | --------------------- | --------------------------- |
| POST   | `/user/register`      | Register a new user         |
| POST   | `/user/login`         | Login and generate JWT      |
| POST   | `/user/refresh-token` | Generate a new Access Token |

### User

| Method | Endpoint        | Description                   |
| ------ | --------------- | ----------------------------- |
| GET    | `/user/profile` | Access protected user profile |

---

## 🗄️ Database

Main entities used in the project:

* User
* RefreshToken

---

## 🔒 Security

* JWT Authentication
* BCrypt Password Encoding
* Stateless Authentication
* Role-Based Authorization
* Spring Security Filter Chain

---

## ▶️ Running the Project

### Clone Repository

```bash
git clone https://github.com/your-username/your-repository.git
```

### Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Run

```bash
mvn spring-boot:run
```

or run the main application class from your IDE.

---

## 📈 Future Enhancements

* Email Verification
* Forgot Password
* OTP Authentication
* OAuth2 Login (Google/GitHub)
* Account Lock after Multiple Failed Attempts
* Audit Logs
* Multi-Factor Authentication (MFA)

---

## 👨‍💻 Author

**Mohit Vishavkarma**

* Java Backend Developer
* Spring Boot Developer
* Passionate about Backend Development, Security, and Microservices.
