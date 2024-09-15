

# Book and Author Management System API

## Overview

This project is a Spring Boot application designed to manage books and authors. It provides endpoints to create, read, update, and delete (CRUD) book and author records. The project uses PostgreSQL as the main database and H2 in-memory database for unit and integration testing.

## Features

- **Book Management**
    - Create, update, and delete books
    - Retrieve book details and list all books
    - Partial updates to existing books

- **Author Management**
    - Create, update, and delete authors
    - Retrieve author details and list all authors
    - Partial updates to existing authors

## Technologies Used

- **Spring Boot**: Framework for building Java-based web applications
- **PostgreSQL**: Relational database management system
- **H2 Database**: In-memory database used for unit and integration testing
- **JUnit**: Testing framework used for unit and integration tests
- **Maven**: Build automation tool

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL

### Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/abdullahsn10/springboot-books-api
   ```

2. **Configure PostgreSQL:**

   Make sure PostgreSQL is installed and running. Create a database named `book_author_db` and update the database connection details in `src/main/resources/application.properties`.

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/book_author_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build and run the application:**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The application will be accessible at `http://localhost:8080`.

### Testing

The application uses H2 as an in-memory database for testing purposes. To run the tests, use:

```bash
mvn test
```

### API Endpoints

#### Book Management

- **Create/Update Book**
    - `PUT /books/{isbn}`
- **Get All Books**
    - `GET /books`
- **Get Book by ISBN**
    - `GET /books/{isbn}`
- **Partial Update Book**
    - `PATCH /books/{isbn}`
- **Delete Book**
    - `DELETE /books/{isbn}`

#### Author Management

- **Create Author**
    - `POST /authors`
- **Get All Authors**
    - `GET /authors`
- **Get Author by ID**
    - `GET /authors/{id}`
- **Full Update Author**
    - `PUT /authors/{id}`
- **Partial Update Author**
    - `PATCH /authors/{id}`
- **Delete Author**
    - `DELETE /authors/{id}`

### Contributing

Feel free to contribute to this project by submitting issues, pull requests, or suggestions. Follow the standard GitHub workflow for contributions:

1. Fork the repository
2. Create a new branch (`git checkout -b feature-branch`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature-branch`)
5. Create a new Pull Request



