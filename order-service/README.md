# Order Service - Spring Boot Microservice

A Spring Boot microservice for managing customer orders using MongoDB.
The application implements JWT authentication and role-based authorization.

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data MongoDB
- Maven
- MongoDB
- Postman

## Project Structure

controller  → REST APIs  
service     → Business logic  
repository  → MongoDB interaction  
model       → Entity classes  
dto         → Request/Response objects  
security    → JWT authentication and filter  
exception   → Custom exception handling

## MongoDB Setup

1. Install MongoDB locally
2. Start MongoDB server
3. Default connection:

mongodb://localhost:27017/orderdb

form intellij
Run → OrderServiceApplication

## Application will start on
http://localhost:8080

## Generate Token:
Call the POST /auth/login API with valid username and password; the response will return a JWT token.

## Use Token:
Include the token in the request header for secured APIs:
Authorization: Bearer <JWT_TOKEN>