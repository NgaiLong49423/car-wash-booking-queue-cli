# Technical Decisions

## Status

Accepted

## Purpose

This document records the main technical decisions for the project.

The project is intentionally designed as a small academic Java CLI application. The goal is to keep the implementation simple, understandable, and suitable for the course scope.

---

## Selected Technology Stack

| Area                       | Decision                                  |
| -------------------------- | ----------------------------------------- |
| Application Type           | Command Line Interface (CLI) application  |
| Programming Language       | Java                                      |
| JDK Version                | JDK 8                                     |
| IDE                        | NetBeans                                  |
| Architecture Style         | Monolithic Architecture                   |
| Data Storage               | Text files                                |
| Database Management System | Not used                                  |
| Web Framework              | Not used                                  |
| Mobile Application         | Not used                                  |
| External API Integration   | Not used unless explicitly required later |

---

## Architecture Decision

The project uses a **monolithic architecture**.

This means the application is implemented as one single deployable program. The user interface, business logic, and data handling are part of the same application codebase.

This architecture is selected because:

* The project scope is small.
* The application is a CLI-based academic project.
* The team does not need distributed services or complex deployment.
* It is easier to understand, implement, test, and maintain within the course timeline.

---

## Design Approach

The project should keep a simple and clear code organization.

Recommended structure:

```text
AutoWashQueueCLI/
├── src/
│   ├── app/           # CLI entry point and menus
│   ├── datastructure/ # Custom list, queue, heap, stack, map
│   ├── model/
│   ├── service/
│   └── util/
├── data/              # Pipe-delimited persistent files
└── test/              # Reproducible benchmarks and tests
```

Suggested responsibility:

| Layer / Package | Responsibility                                                                       |
| --------------- | ------------------------------------------------------------------------------------ |
| `model`         | Store domain objects such as Customer, Booking, Service, Queue, and related entities |
| `service`       | Handle business rules and application logic                                          |
| `util`          | Handle file utilities, validation, formatting, and shared helpers                   |
| `util`          | Store helper functions and shared utilities                                          |
| `app`           | Start the CLI application and handle menu navigation                                 |

This structure is not a microservice or enterprise architecture. It is only a simple package organization to keep the code clean.

---

## Design Pattern Usage

The project should not force complex design patterns.

Allowed simple patterns:

| Pattern            | Usage                                                                  |
| ------------------ | ---------------------------------------------------------------------- |
| Service Layer      | Use for business logic such as booking, queue, loyalty, and undo rules |
| Repository Pattern | Use for isolating file read/write logic                                |
| DTO                | Optional, only if needed to transfer simple data between layers        |
| Singleton          | Avoid unless there is a clear reason                                   |

Patterns should only be used when they make the code easier to understand.

Do not introduce unnecessary complexity.

---

## Constraints

The project must not be converted into:

* A web application
* A mobile application
* A Spring Boot application
* A microservices system
* A database-driven system

Any change to these constraints must be approved first.

---

## Source of Truth

Functional requirements and business rules are defined in:

```text
docs/requirements/SRS.md
```

Product-level overview is defined in:

```text
docs/requirements/PRD.md
```

If this technical decision document conflicts with `SRS.md`, the conflict must be reviewed before implementation.
