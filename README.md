# Product API

A production-style **RESTful Product Management API** built with **Java 17 and Spring Boot**.

The application provides product and item management with **JWT authentication, refresh tokens, role-based authorization, validation, pagination, database indexing, centralized exception handling, Swagger/OpenAPI documentation, automated testing, and Docker support**.

---

## 🚀 Features

* RESTful Product Management APIs
* Product and Product Item management
* JWT-based authentication
* Access token and refresh token support
* Role-based authorization (`ADMIN`, `USER`)
* Jakarta Bean Validation
* Pagination using Spring Data `Page`
* MySQL database
* JPA/Hibernate entity relationships
* Database indexing for optimized queries
* Centralized exception handling
* Standardized error responses
* CORS configuration for frontend applications
* Swagger/OpenAPI documentation
* Unit testing with JUnit 5 and Mockito
* Spring Boot integration testing
* H2 database for tests
* Docker and Docker Compose support
* Production-specific configuration

---

## 🛠️ Tech Stack

| Technology        | Purpose                        |
| ----------------- | ------------------------------ |
| Java 17           | Programming language           |
| Spring Boot 4.1.1 | Application framework          |
| Spring Data JPA   | Data access                    |
| Hibernate         | ORM                            |
| MySQL 8           | Production database            |
| Spring Security   | Authentication & authorization |
| JWT               | Stateless authentication       |
| JUnit 5           | Testing                        |
| Mockito           | Mocking                        |
| H2                | Test database                  |
| SpringDoc OpenAPI | API documentation              |
| Maven             | Build & dependency management  |
| Docker            | Containerization               |
| Docker Compose    | Multi-container setup          |

---

## 🏗️ Architecture

The application follows a layered architecture to keep responsibilities separated and maintainable.

```text
                    Client
                      │
                      ▼
                 Controller
                      │
                      ▼
                   Service
                      │
                      ▼
                 Repository
                      │
                      ▼
                  Database
```

### Main Layers

**Controller**

* Handles HTTP requests and responses
* Performs request validation
* Defines REST endpoints
* Returns appropriate HTTP status codes

**Service**

* Contains business logic
* Coordinates application operations
* Communicates with repositories

**Repository**

* Uses Spring Data JPA
* Handles database operations
* Provides data access abstraction

**Entity**

* Represents database tables
* Defines relationships between entities

**DTO**

* Separates API request/response models from persistence entities
* Prevents direct exposure of database entities through APIs

**Security**

* Handles authentication
* Generates and validates JWTs
* Handles refresh tokens
* Provides role-based authorization
* Configures the authentication filter

**Exception Handling**

* Centralizes application exception handling
* Provides consistent API error responses
* Prevents internal exceptions from being exposed to clients

---

## 📁 Project Structure

```text
src/
├── main/
│   ├── java/com/zest/product_api/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── security/
│   │   └── service/
│   │
│   └── resources/
│       ├── application.properties
│       └── application-prod.properties
│
└── test/
    ├── java/com/zest/product_api/
    │   ├── ProductApiApplicationTests.java
    │   ├── ProductControllerIntegrationTest.java
    │   └── ProductServiceImplTest.java
    │
    └── resources/
        └── application.properties
```

---

# 🔐 Authentication & Authorization

The API uses **stateless JWT-based authentication**.

After successful login, the client receives:

* Access Token
* Refresh Token

The access token must be included in authenticated API requests:

```http
Authorization: Bearer <access-token>
```

Refresh tokens can be used to obtain a new access token when the access token expires.

### Roles

The application supports two roles:

#### ADMIN

Can:

* Create products
* Update products
* Delete products
* Read products
* Read product items

#### USER

Can:

* Read products
* Read product items

### Security Responses

Unauthenticated requests receive:

```text
401 Unauthorized
```

Authenticated users without sufficient permissions receive:

```text
403 Forbidden
```

---

# 📡 API Endpoints

### Base URL

```text
/api/v1
```

## Authentication

| Method | Endpoint               | Description                                         |
| ------ | ---------------------- | --------------------------------------------------- |
| `POST` | `/api/v1/auth/login`   | Authenticate user and receive access/refresh tokens |
| `POST` | `/api/v1/auth/refresh` | Obtain a new access token using a refresh token     |

---

## Products

| Method   | Endpoint                      | Authorization | Description                      |
| -------- | ----------------------------- | ------------- | -------------------------------- |
| `GET`    | `/api/v1/products`            | Authenticated | Get paginated products           |
| `GET`    | `/api/v1/products/{id}`       | Authenticated | Get product by ID                |
| `POST`   | `/api/v1/products`            | `ADMIN`       | Create a product                 |
| `PUT`    | `/api/v1/products/{id}`       | `ADMIN`       | Update a product                 |
| `DELETE` | `/api/v1/products/{id}`       | `ADMIN`       | Delete a product                 |
| `GET`    | `/api/v1/products/{id}/items` | Authenticated | Get items belonging to a product |

---

# 📄 Pagination

The product collection endpoint supports pagination using Spring Data's `Page`.

Example:

```http
GET /api/v1/products?page=0&size=10
```

Default values:

```text
page = 0
size = 10
```

This allows large product collections to be retrieved efficiently without loading all records into memory at once.

---

# 🗄️ Database

The application uses **MySQL 8** as its primary database.

### Main Entities

```text
Product
   │
   └── Item
```

A product can contain multiple items.

The relationship is represented using:

```text
item.product_id → product.id
```

### Database Indexing

An index is created on:

```text
item.product_id
```

This improves query performance when retrieving items associated with a specific product.

---

# ✅ Validation

Request DTOs use **Jakarta Bean Validation**.

Invalid request data is rejected before reaching the business logic.

Validation failures are handled by the centralized exception handling mechanism and returned as structured API error responses.

---

# ⚠️ Error Handling

The application uses centralized exception handling to provide consistent API responses.

For example, when a requested product does not exist, the API returns an appropriate HTTP error response rather than exposing internal exceptions or stack traces.

This provides a cleaner and safer API experience for clients.

---

# 🌐 CORS

CORS is configured to support local frontend development.

Allowed origins include:

```text
http://localhost:3000
http://localhost:5173
```

Supported HTTP methods include:

```text
GET
POST
PUT
DELETE
OPTIONS
```

---

# 📚 Swagger / OpenAPI

The API is documented using **Swagger/OpenAPI** through SpringDoc.

Once the application is running, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI can be used to explore and test the available API endpoints.

---

# 🧪 Testing

The project includes automated tests using **JUnit 5, Mockito, Spring Boot Test, and H2**.

Tests cover areas including:

* Authentication requirements
* Role-based authorization
* Product creation
* Product update
* Product deletion
* Product retrieval
* Service-layer behavior
* Validation and request processing
* Controller security behavior

### Test Types

**Unit Tests**

Service-layer business logic is tested using Mockito-based mocks.

**Integration Tests**

Spring Boot integration tests verify controller and application behavior.

**Test Database**

H2 is used as the test database to avoid depending on a local MySQL instance during automated testing.

### Run Tests

Windows:

```powershell
.\mvnw.cmd test
```

---

# 🐳 Docker

The project includes:

```text
Dockerfile
docker-compose.yml
```

Docker Compose defines:

* Product API service
* MySQL service
* Persistent MySQL volume
* MySQL health check

### Start with Docker Compose

```bash
docker compose up --build
```

The API will be available at:

```text
http://localhost:8080
```

---

# 💻 Running Locally

## Prerequisites

Make sure the following are installed:

* Java 17+
* Maven
* MySQL 8

---

## 1. Clone the Repository

```bash
git clone https://github.com/MohinMokashi7/product-api.git
cd product-api
```

---

## 2. Create the Database

Create the MySQL database:

```sql
CREATE DATABASE product_api_db;
```

---

## 3. Configure Database Credentials

Update the database configuration in:

```text
src/main/resources/application.properties
```

Configure your local MySQL username and password as required.

---

## 4. Run the Application

Using the Maven wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

---

# ⚙️ Production Configuration

Production-specific configuration is provided through:

```text
application-prod.properties
```

The production configuration supports forwarding HTTPS information when the application is deployed behind a reverse proxy or load balancer.

---

# 🔄 API Flow

A typical authenticated request follows this flow:

```text
Client
  │
  │ Login
  ▼
Auth Controller
  │
  ▼
JWT Authentication
  │
  ├── Access Token
  └── Refresh Token
          │
          ▼
   Authenticated Request
          │
          ▼
   Security Filter
          │
          ▼
      Controller
          │
          ▼
       Service
          │
          ▼
      Repository
          │
          ▼
        MySQL
```

---

# 🔒 Security Highlights

The application implements several security-related practices:

* Stateless JWT authentication
* Bearer token authentication
* Refresh token support
* Role-based endpoint authorization
* Authentication checks for protected endpoints
* Authorization checks for administrative operations
* Request validation
* Centralized exception handling
* CORS configuration

---

# 📌 API Summary

| Area             | Implementation                       |
| ---------------- | ------------------------------------ |
| Authentication   | JWT + Refresh Token                  |
| Authorization    | Role-based (`ADMIN`, `USER`)         |
| API Style        | REST                                 |
| Validation       | Jakarta Bean Validation              |
| Pagination       | Spring Data `Page`                   |
| ORM              | Hibernate / JPA                      |
| Database         | MySQL 8                              |
| Testing          | JUnit 5 + Mockito + Spring Boot Test |
| Test Database    | H2                                   |
| Documentation    | Swagger / OpenAPI                    |
| Containerization | Docker + Docker Compose              |
| Java Version     | Java 17                              |

---

# 👨‍💻 Repository

**GitHub:**
https://github.com/MohinMokashi7/product-api

---

## 📄 License

This project was developed as a backend engineering assignment demonstrating REST API development, authentication, authorization, persistence, testing, API documentation, and containerization using the Spring Boot ecosystem.
