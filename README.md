# Assignment Project

## Requirements

- [Docker](https://docs.docker.com/get-docker/) installed
- [Java 17](https://adoptium.net/) or higher
- [Spring Boot 3.5](https://spring.io/projects/spring-boot) (project uses Spring 3.5)
- [Gradle](https://gradle.org/install/) build tool

## Overview

This project provides a RESTful API for an electronic store.

- Supports basket management for shopping.
- Implements user roles: **user** and **admin** for access control.
- Uses **PostgreSQL** as the main database.
- Uses **Redis** for in-memory caching.
- Integrates **Grafana** for monitoring and metrics.

---

## API Documentation (Swagger)

- After starting the application, access Swagger UI at:
  ```
  http://localhost:8088/swagger
  ```
  or, if deployed, visit:
  ```
  https://altech.celestialhighway.xyz/swagger
  ```
  This is your deployment host.

- The OpenAPI specification is available at `/swagger` or `/docs`.

---

## Docker Usage

### Build the Docker Image

```bash
docker build -t assignment-app .
```

### Run the Docker Container (using Dockerfile)

```bash
docker run -p 8088:8088 assignment-app
```
The application will be available at `http://localhost:8088/`.

---

## Docker Compose

If you have a `docker-compose.yml` file, you can start all services:

```bash
docker-compose up
```
By default, the app will be exposed on port 8088.

**Services included:**
- Application (Spring Boot)
- PostgreSQL database
- Redis cache
- Grafana for metrics

To stop services:

```bash
docker-compose down
```

### Run Tests with Docker Compose

```bash
docker-compose run app ./gradlew test
```
Replace `app` with your service name in `docker-compose.yml`.

---

## Running Unit Tests

### Locally

```bash
# Gradle example
./gradlew test
```

### In Docker (using Dockerfile)

```bash
docker run assignment-app ./gradlew test
```

### In Docker Compose

```bash
docker-compose run app ./gradlew test
```
Replace `app` with your service name.

---
# Assignment Project


---

## CI/CD with Jenkins

This project uses Jenkins for continuous integration and deployment.

- **Jenkins Host:**  
  [https://jenkins.celestialhighway.xyz/](https://jenkins.celestialhighway.xyz/)
- **Deployment Host:**  
  Application is deployed at [https://altech.celestialhighway.xyz/](https://altech.celestialhighway.xyz/) on port **8088**.

- Jenkins automates build, test, and deployment processes.  
  You can view build status, logs, and pipeline details on the Jenkins dashboard.

---

## Deployment Note

This application is deployed on a home server machine for demo purposes.  
Access to the deployment host is tunneled through **Cloudflare** for secure connectivity.

---

## Notes

- Configure environment variables as needed (see `.env.example`).
- For more details, check the source code and comments.
