# Electronics Store 

## Overview
This project implements a RESTful API for an electronics store's checkout system, built with **Spring Boot 3.5**, **PostgreSQL** for persistent storage, and **Redis** for in-memory caching. It supports admin operations (product management, discount deals, pagination) and customer operations (basket management, receipt calculation, filtering, pagination) with safe concurrent usage and comprehensive automated tests.

- **Swagger UI**: [http://localhost:8088/swagger-ui.html](http://localhost:8088/swagger-ui.html) (local) or [https://altech.celestialhighway.xyz/swagger-ui.html](https://altech.celestialhighway.xyz/swagger-ui.html) (deployed)
- **OpenAPI Spec**: [http://localhost:8088/v3/api-docs](http://localhost:8088/v3/api-docs) (local) or [https://altech.celestialhighway.xyz/v3/api-docs](https://altech.celestialhighway.xyz/v3/api-docs) (deployed)

## Requirements
- [Docker](https://docs.docker.com/get-docker/)
- [Java 17](https://adoptium.net/)
- [Gradle](https://gradle.org/install/)
- [Spring Boot 3.5](https://spring.io/projects/spring-boot)

## Architecture
- **Layered Architecture**: Separates concerns into controllers, services, and repositories for modularity and maintainability.
- **Persistence**: PostgreSQL stores products, baskets, and discounts; Redis caches frequently accessed data (e.g., product details, basket state).
- **Concurrency**: Uses JPA optimistic locking (`@Version`) for stock updates and Spring transactions to ensure atomicity and prevent partial updates.
- **Extensibility**: Implements a factory pattern for discounts, allowing new deal types to be added without modifying existing code. Services use interfaces for flexibility.
- **Security**: Role-based access control (ROLE_ADMIN for admin endpoints, ROLE_USER for customer endpoints) using Spring Security.

## API Endpoints
The API supports admin and customer operations with pagination, stock management, and discount application. All endpoints are documented in Swagger UI.


## Persistence and Concurrency
- **PostgreSQL**: Stores products (`products` table), baskets (`baskets` and `basket_items` tables), and discounts (`discounts` table). Uses JPA with Hibernate for ORM.
- **Redis**: Caches product details and basket state to reduce database load. Cache is invalidated on product or basket updates.
- **Concurrency**:
  - Stock updates use JPA’s `@Version` for optimistic locking to prevent race conditions.
  - Basket operations (add/remove) are wrapped in `@Transactional` methods to ensure atomicity.
  - Example: Adding a product to a basket checks stock, decrements it, and updates the basket in a single transaction. If any step fails, the transaction rolls back.


## Testing
- **Coverage**: 94% (measured with JaCoCo)
- **Test Types**:
  - **Unit Tests**: 
  - **Integration Tests**: 

## Setup and Running
### Prerequisites
- Docker
- Java 17
- Gradle
- PostgreSQL
- Redis

### Build the Project
```bash
./gradlew clean build
```

### Build and Run Locally
```bash
./gradlew bootRun
```
Application runs on `http://localhost:8088`.

### Docker Usage
#### Build Docker Image
```bash
docker build -t my-springboot-app:latest .
```

#### Run Docker Container
```bash
docker run -p 8088:8088 my-springboot-app:latest
```

### Docker Compose
Start all services (Spring Boot, PostgreSQL, Redis, Grafana):
```bash
docker-compose up
```
Stop services:
```bash
docker-compose down
```

## Running Tests
### Locally
```bash
./gradlew test
```

### Specific Test Case
```bash
./gradlew test --tests BasketServiceTest
```

## Environment Variables
See `.env`:
```
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/your_db
SPRING_DATASOURCE_USERNAME=your_user
SPRING_DATASOURCE_PASSWORD=your_password
REDIS_HOST=localhost
REDIS_PORT=6379
```

## CI/CD 
- **Jenkins**: [https://jenkins.celestialhighway.xyz](https://jenkins.celestialhighway.xyz) automates build, test, and deployment. Guest account: `guest` / `Guest123`


## Deployment
- **Deployed Host**: [https://altech.celestialhighway.xyz](https://altech.celestialhighway.xyz) (port 8088)
- **Access**: Secured via Cloudflare tunneling. Local deployment via Docker is sufficient for evaluation.


- For further details, review the source code and comments in the repository.