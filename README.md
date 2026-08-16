# Todo-List-API

Project Url : https://roadmap.sh/projects/todo-list-api

A secure RESTful Todo List API built with Java and Spring Boot. The application provides user authentication, authorization, Todo management, pagination, filtering, sorting, rate limiting, validation, and PostgreSQL persistence.

## Features

- User registration and login
- JWT-based authentication
- Refresh token mechanism
- Role-based authorization
- Secure Todo CRUD operations
- Ownership-based access control
- Pagination, filtering, and sorting
- API rate limiting using Bucket4j
- Request validation
- Centralized exception handling
- PostgreSQL persistence
- Unit testing

## Tech Stack

- **Java 21**
- **Spring Boot 3.2.5**
- **Spring Security**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **JWT**
- **Bucket4j**
- **ModelMapper**
- **Lombok**
- **Maven**
- **JUnit / Mockito**
- **Postman**

## Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
````

The project is organized into dedicated packages for:

```text
Config
Controller
Dto
Entity
Error
RateLimiting
Repository
Security
Service
```

## Authentication & Authorization

The API uses Spring Security with JWT-based authentication.

The authentication flow includes:

1. User registration
2. User login
3. Access token generation
4. Refresh token generation
5. Authentication of protected requests
6. Role-based authorization
7. Refreshing expired access tokens

Protected API requests use the JWT access token:

```http
Authorization: Bearer <access-token>
```

## Todo Management

Authenticated users can manage their Todo items through REST APIs.

Supported operations include:

* Create Todo
* Retrieve Todos
* Retrieve a Todo by ID
* Update Todo
* Delete Todo

Todo access is restricted based on ownership to prevent users from accessing or modifying another user's tasks.

## Pagination, Filtering & Sorting

The API supports pagination, filtering, and sorting for efficient retrieval of Todo records.

This allows clients to retrieve a specific page of results and control filtering and ordering of Todo data.

## Rate Limiting

API rate limiting is implemented using **Bucket4j** to protect endpoints from excessive requests and abuse.

Requests exceeding the configured limit are rejected with an appropriate HTTP response.

## Validation & Exception Handling

The application uses request validation to ensure incoming data meets the required constraints.

Centralized exception handling provides consistent error responses for scenarios such as:

* Invalid request data
* Unauthorized access
* Forbidden operations
* Resource not found
* Duplicate or conflicting data
* Rate limit exceeded

## Database

The application uses **PostgreSQL** for persistent data storage.

Spring Data JPA and Hibernate are used for database interaction and entity management.

Main entities include:

* User
* Todo
* Refresh Token

## Running Locally

### Prerequisites

Make sure you have the following installed:

* Java 21
* PostgreSQL
* Maven

### 1. Clone the repository

```bash
git clone https://github.com/Avni15Sharma/Todo-List-API.git
cd Todo-List-API/Todo-List
```

### 2. Create the PostgreSQL database

Create a PostgreSQL database named:

```text
todoList
```

### 3. Configure environment variables

The application uses environment variables for sensitive configuration.

Set the following variables:

```text
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

#### Windows PowerShell

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your-postgres-password"
$env:JWT_SECRET="your-jwt-secret"
```

#### Linux / macOS

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=your-postgres-password
export JWT_SECRET=your-jwt-secret
```

Do not commit database credentials or JWT secrets to the repository.

### 4. Run the application

Using the Maven wrapper:

**Windows**

```powershell
mvnw.cmd spring-boot:run
```

**Linux / macOS**

```bash
./mvnw spring-boot:run
```

The application will be available at:

```text
http://localhost:8082/api/v1
```

## Testing

Run the test suite using:

**Windows**

```powershell
mvnw.cmd test
```

**Linux / macOS**

```bash
./mvnw test
```

API endpoints can also be tested using Postman.

## Project Structure

```text
Todo-List/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/TaskTracker/Todo/List/
│   │   │       ├── Config/
│   │   │       ├── Controller/
│   │   │       ├── Dto/
│   │   │       ├── Entity/
│   │   │       ├── Error/
│   │   │       ├── RateLimiting/
│   │   │       ├── Repository/
│   │   │       ├── Security/
│   │   │       ├── Service/
│   │   │       └── TodoListApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## Future Improvements

* Dockerize the application
* Add Swagger/OpenAPI documentation
* Add integration tests
* Add Redis caching
* Add CI/CD pipeline

## Author

**Avni Sharma**

LinkedIn: [https://linkedin.com/in/avni-sharma15](https://linkedin.com/in/avni-sharma15)

```
```

