# BorrowBox – User Registration and Authentication System

## Project Description
BorrowBox is a full-stack web application developed as part of an academic laboratory activity.
It implements a complete **user registration and authentication system** using Spring Boot, MySQL, and ReactJS.

The system allows users to securely register, log in, view their profile, and log out through a web interface.

---

## Technologies Used

### Backend
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL
- Maven

### Frontend
- ReactJS (Create React App)
- JavaScript (ES6)
- Fetch API

### Tools
- Git & GitHub
- Postman (API testing)
- VS Code

---

## Local Backend Configuration

Before starting the Spring Boot backend, set these environment variables for your local machine:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/borrowbox_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
$env:JWT_SECRET="use_a_long_random_secret_at_least_32_characters"
$env:APP_ADMIN_EMAIL="your_admin_email@example.com"
```

`APP_ADMIN_EMAIL`, `JWT_ISSUER`, and `JWT_EXP_MINUTES` are optional. If no admin exists yet, the next successful login is promoted to `ADMIN`. If `APP_ADMIN_EMAIL` is set, that user is promoted to `ADMIN` when they log in.

---

## Implemented Features

### Backend
- User Registration (`POST /api/auth/register`)
- User Login (`POST /api/auth/login`)
- Get Logged-in User (`GET /api/user/me`) – protected
- Password encryption using **BCrypt**
- MySQL database integration
- Spring Security (Basic Authentication)
- CORS configuration for frontend integration

### Frontend
- Register page
- Login page
- Dashboard/Profile page
- Logout functionality
- Backend API integration

---

## Folder Structure
