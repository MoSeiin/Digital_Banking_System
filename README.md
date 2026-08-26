# 🏦 Digital Banking System

A complete, secure backend for a digital banking system, built with **Spring Boot 3** and **Java 17**.
This project simulates the core processes of a real banking system: JWT-based authentication, user and role management, account opening and management, card issuance and management, and financial transactions (deposit, withdrawal, transfer) — along with an admin panel for managing users and reports.

!\[Java](https://img.shields.io/badge/Java-17-orange)
!\[Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.16-brightgreen)
!\[PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
!\[JWT](https://img.shields.io/badge/Auth-JWT-yellow)
!\[License](https://img.shields.io/badge/License-MIT-lightgrey)

\---

## 📋 Table of Contents

* [About the Project](#-about-the-project)
* [Features](#-features)
* [Tech Stack](#-tech-stack)
* [Architecture \& Project Structure](#-architecture--project-structure)
* [User Roles](#-user-roles)
* [API Documentation](#-api-documentation)
* [Setup \& Installation](#-setup--installation)
* [Environment Variables](#-environment-variables)
* [Testing](#-testing)
* [Roadmap](#-roadmap)
* [License](#-license)

\---

## 📖 About the Project

**Digital Banking System** is a RESTful API for managing banking operations, designed following **Layered Architecture** principles (Controller → Service → Repository), with a focus on security and separation of concerns. This project was built to demonstrate the ability to design real-world backend systems, handle authentication/authorization, and work with a relational database.

## ✨ Features

* 🔐 **JWT-based authentication and authorization** with Role-Based Access Control (RBAC)
* 👥 **User management**: registration, login, admin approval/rejection/blocking of users, role changes, and password changes
* 💳 **Bank account management**: opening accounts, viewing personal accounts, closing accounts
* 💰 **Card management**: card issuance, blocking, and activation
* 💸 **Transaction operations**: deposit, withdrawal, and fund transfer between accounts with balance validation
* 📊 **Transaction history and filtering** (by date range, transaction type, etc.) using the Specification Pattern
* 🛠 **Admin panel**: user management, viewing all transactions, and transaction summary reports
* 📄 **Automatic API documentation** with Swagger / OpenAPI
* 🧩 **Automatic DTO ↔ Entity mapping** with MapStruct
* ⚠️ **Centralized error handling** (Global Exception Handler) with standardized error responses
* 🌱 **Data Seeder** for generating sample data (admin, users, accounts) on first run

## 🛠 Tech Stack

|Category|Technology|
|-|-|
|Language|Java 17|
|Framework|Spring Boot 3.5.16|
|Security|Spring Security + JWT (jjwt)|
|Database|PostgreSQL|
|ORM|Spring Data JPA / Hibernate|
|DTO Mapping|MapStruct|
|Validation|Spring Validation|
|API Documentation|springdoc-openapi (Swagger UI)|
|Build Tool|Maven|
|Boilerplate Reduction|Lombok|

## 🏗 Architecture \& Project Structure

The project follows a Layered Architecture:

```
src/main/java/ir/digitalbankingsystem/digital\\\_banking\\\_system/
├── config/          # Security config, JWT filter, Data Seeder
├── controller/      # REST API layer (Auth, Account, Card, Transaction, Admin, Update)
├── domain/          # Entities and Enums
├── dto/             # Data transfer objects (Request / Response)
├── exception/       # Custom exceptions + Global Exception Handler
├── mapper/          # MapStruct mappers
├── repository/      # Data access layer (Spring Data JPA)
├── service/         # Business logic
├── specification/   # Dynamic filtering for transactions (JPA Specification)
└── util/            # Helper utilities (JWT, UserDetails)
```

## 👤 User Roles

The system has three user roles, with access levels controlled via `@PreAuthorize`:

|Role|Permissions|
|-|-|
|**CUSTOMER**|View own accounts and cards, transfer funds, view own transaction history|
|**EMPLOYEE**|Open/close accounts, issue/block cards, record deposits and withdrawals|
|**ADMIN**|All EMPLOYEE permissions + user management (approve, reject, block, change role) and viewing overall reports|

## 📡 API Documentation

Once the project is running, full interactive API documentation is available via Swagger:

```
http://localhost:8080/swagger-ui.html
```

### Main Endpoints Overview

|Method|Endpoint|Description|Access|
|-|-|-|-|
|`POST`|`/auth/register`|Register a new user|Public|
|`POST`|`/auth/login`|Log in and receive a JWT|Public|
|`PUT`|`/auth/update/user/{userCode}`|Update user information|Authenticated|
|`PATCH`|`/auth/update/{userCode}/password`|Change password|Authenticated|
|`POST`|`/api/accounts/person/{personCode}/create`|Open a new account|ADMIN, EMPLOYEE|
|`GET`|`/api/accounts/my`|View own accounts|CUSTOMER, EMPLOYEE, ADMIN|
|`PATCH`|`/api/accounts/{accountNumber}/close`|Close an account|ADMIN, EMPLOYEE|
|`POST`|`/api/cards/account/{accountNumber}/issue`|Issue a new card|ADMIN, EMPLOYEE|
|`GET`|`/api/cards/my`|View own cards|CUSTOMER, ADMIN, EMPLOYEE|
|`PATCH`|`/api/cards/{cardNumber}/block`|Block a card|ADMIN, EMPLOYEE|
|`PATCH`|`/api/cards/{cardNumber}/activate`|Activate a card|ADMIN, EMPLOYEE|
|`POST`|`/api/transactions/deposit`|Deposit funds|ADMIN, EMPLOYEE|
|`POST`|`/api/transactions/withdraw`|Withdraw funds|ADMIN, EMPLOYEE|
|`POST`|`/api/transactions/transfer`|Transfer funds|CUSTOMER, ADMIN, EMPLOYEE|
|`GET`|`/api/transactions/my-history`|View own transaction history|Authenticated|
|`GET`|`/api/transactions/person/{personCode}/history`|View a user's transaction history|ADMIN, EMPLOYEE|
|`GET`|`/admin/users`|List all users|ADMIN|
|`PUT`|`/admin/user/{userCode}/approve`|Approve a user|ADMIN|
|`PUT`|`/admin/user/{userCode}/reject`|Reject a user|ADMIN|
|`PUT`|`/admin/user/{userCode}/block`|Block a user|ADMIN|
|`PUT`|`/admin/user/{userCode}/unblock`|Unblock a user|ADMIN|
|`PUT`|`/admin/user/{userCode}/role`|Change a user's role|ADMIN|
|`GET`|`/admin/transactions`|View all transactions|ADMIN|
|`GET`|`/admin/reports/transactions/summary`|Transaction summary report|ADMIN|

> The full list, along with sample requests/responses, is available in Swagger UI.

## ⚙️ Setup \& Installation

### Prerequisites

* Java 17+
* Maven 3.8+
* PostgreSQL 13+

### Installation Steps

```bash
# 1. Clone the repository
git clone https://github.com/MoSeiin/digital-banking-system.git
cd digital-banking-system

# 2. Create the database in PostgreSQL
createdb digital\\\_banking\\\_system

# 3. Set the environment variables (see below)

# 4. Run the project
./mvnw spring-boot:run
```

The project runs on port `8080` by default.

> ℹ️ On the first run, the `DataSeeder` automatically creates sample users and accounts (including an admin) in the database so you can start testing the API right away.

## 🔑 Environment Variables

Before running the project, set the following environment variables (or in a `.env` file):

|Variable|Description|Example|
|-|-|-|
|`JWT\\\_SECRET`|JWT signing key|A secure random string|
|`JWT\\\_EXPIRATION`|Token expiration time (milliseconds)|`86400000`|

Also update the database connection settings in `src/main/resources/application.yml` to match your environment (host, username, and password for PostgreSQL).

## 🧪 Testing

```bash
./mvnw test
```

## 🚧 Roadmap

* \[ ] Add more unit and integration tests
* \[ ] Rate limiting for sensitive endpoints
* \[ ] Dockerize the project (Dockerfile + docker-compose)
* \[ ] Add refresh token support
* \[ ] Implement notifications for transactions

\---

<div align="center">

Built with ❤️ and Spring Boot

</div>

