
![CI](https://github.com/adelenneb/employee-management-api/actions/workflows/ci.yml/badge.svg)

# Employee Management API

Enterprise-grade REST API built with **Java 21**, **Spring Boot**, **Spring Security (JWT)** and **MySQL**, fully containerized using **Docker** and **Docker Compose**.

---

## 🚀 Run the application with Docker

### Prerequisites

* Docker
* Docker Compose (v2+)

### Start the stack

From the project root directory:

```bash
docker compose up --build
```

* API will be available at:
  👉 `http://localhost:8080`
* MySQL runs in a Docker container (internal hostname: `mysql`)

---

## 📖 API Documentation (Swagger / OpenAPI)

Once the application is running, open:

👉 `http://localhost:8080/swagger-ui/index.html`

Swagger provides interactive documentation for all available endpoints.

---

## 🔐 Authentication (JWT)

### Login endpoint

```
POST /api/auth/login
```

### Example request body

```json
{
  "username": "admin",
  "password": "admin123"
}
```

### Response

On success, the API returns a **JWT token**:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## 🔑 Using JWT with Swagger

1. Open Swagger UI
2. Click **Authorize 🔒** (top right)
3. Enter the token using the Bearer format:

```
Bearer <your-jwt-token>
```

4. Click **Authorize** and close the dialog

Swagger will now automatically send the `Authorization` header for secured endpoints.

---

## 🧪 Testing secured endpoints

After authentication, you can test:

* `POST /api/employees`
* `GET /api/employees`
* `PUT /api/employees/{id}`
* `DELETE /api/employees/{id}`

Access is controlled using **JWT-based authentication**.

---

## 🐳 Docker architecture

* Spring Boot API runs in its own container
* MySQL runs in a separate container
* Containers communicate over a Docker network
* Database data is persisted using Docker volumes

---

## ✅ Key features

* Java 21 & Spring Boot 3
* RESTful API design
* JWT Authentication & Authorization
* Global exception handling
* Swagger / OpenAPI documentation
* Docker & Docker Compose support

---

## 📌 Notes

* No local MySQL installation is required
* All infrastructure runs inside Docker containers
* Ideal for local development and demonstrations

---

## CI/CD

GitHub Actions runs on pushes and pull requests to `main` and `develop` using Java 21. The workflow checks out the code, sets up Temurin JDK with Maven wrapper and dependency caching, runs the test suite with the H2-backed `test` profile via `./mvnw -B -ntp test`, and then builds the Docker image from the project Dockerfile (image is not pushed).
