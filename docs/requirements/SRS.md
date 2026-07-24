> **Document:** Software Requirements Specification (SRS)
> **Version:** 4.0.0
> **Status:** Active / Canonical
> **Last updated:** 2026-07-21

# AutoWash Priority Booking Engine

## 1. Purpose and Scope

This SRS defines the active requirements for the AutoWash Priority Booking Engine. The product is a menu-driven Java CLI that simulates a car wash with limited service capacity. It stores data in pipe-delimited text files, loads records into custom in-memory data structures, and saves state after material business operations.

This document is the source of truth when it conflicts with the [PRD](PRD.md) or older documentation.

### 1.1 Glossary

| Term | Meaning |
| --- | --- |
| Main queue | Guaranteed service slots processed in FIFO order. |
| Waitlist | Overflow queue ordered by tier and booking creation time. |
| Current period | Simulated date and period configured by an administrator. |
| Future booking | A booking for a period that has not yet been activated. |
| Booking window | Maximum days a customer may book ahead for their membership tier. |

### 1.2 Actors

| Actor | Access |
| --- | --- |
| Customer | Service catalog, personal profile, own booking creation/active bookings/history, and cancellation of own waiting booking. |
| Administrator | Customer/vehicle/package management, simulation settings, booking operations, period activation, history, and undo. |

The application has no real login or authorization. Customer identification by ID and the Admin menu are interaction models only.

## 2. Business Rules

### 2.1 Service periods and capacity

| Period | Hours | Main slots | Waitlist slots | Total minutes |
| --- | --- | ---: | ---: | ---: |
| `MORNING` | 07:00–12:00 | 10 | 3 | 300 |
| `AFTERNOON` | 13:00–17:00 | 10 | 3 | 240 |
| `EVENING` | 18:00–21:00 | 5 | 2 | 180 |

A main-queue placement must satisfy both the slot limit and remaining service minutes. At most one booking may be `SERVING` at one time.

### 2.2 Membership and loyalty

| Tier | Booking window | Qualification |
| --- | ---: | --- |
| `MEMBER` | 7 days | Default |
| `SILVER` | 10 days | 5 visits or VND 2,000,000 spend |
| `GOLD` | 12 days | 15 visits or VND 6,000,000 spend |
| `PLATINUM` | 14 days | 30 visits or VND 15,000,000 spend |

- Higher tiers have higher waitlist priority: `PLATINUM`, `GOLD`, `SILVER`, then `MEMBER`.
- An earlier booking wins when memberships are equal.
- One loyalty point is earned per VND 1,000 spent.
- Visit count, spend, points, and tier are recalculated from eligible `COMPLETED` bookings in the previous 365 days.

### 2.3 Booking allocation and activation

- A booking must reference an existing customer, an owned vehicle, and an active wash package.
- A current booking for an activated period goes to the main queue when both slot and time are available; otherwise it can enter the waitlist when capacity remains.
- A booking for an unactivated current/future period remains a future booking after total capacity and time validation.
- A period can be activated only once, for the configured simulated date and period. Activation allocates future bookings by priority to the main queue and waitlist.
- When a main-queue place becomes available, the system considers waitlist entries by priority and may skip an entry that cannot fit in the remaining period time.

### 2.4 Status, payment, cancellation, and undo

| Item | Rule |
| --- | --- |
| Booking states | `WAITING`, `SERVING`, `COMPLETED`, `CANCELLED` |
| Payment states | `UNPAID`, `PAID`; methods are `NONE`, `CASH`, or `BANKING` |
| Completion | Requires a `SERVING` and `PAID` booking; creates history and recalculates loyalty. |
| Customer cancellation | Allowed only for the customer's own `WAITING` booking. |
| Admin cancellation | Allowed for `WAITING` or `SERVING`, but never for `COMPLETED`. |
| Undo | Reverses only the most recent completion, restores the booking to its pre-completion state, removes history, recalculates loyalty, and restores any recorded waitlist promotion. |

## 3. Functional Requirements

| ID | Requirement | Acceptance condition |
| --- | --- | --- |
| FR-01 | Customer management | Admin can add, list, search, update, and conditionally delete customers. |
| FR-02 | Vehicle management | Admin can manage vehicles; plates are unique and owners must exist. |
| FR-03 | Wash-package management | Admin can manage, search, and sort packages by price or duration. |
| FR-04 | Simulation settings | Admin can configure a valid current date and one valid period. |
| FR-05 | Booking creation | Customer or Admin can create a booking for a valid customer, vehicle, package, date, and period. |
| FR-06 | Booking window | The applicable tier limit is enforced for every booking actor. |
| FR-07 | Period validation | Bookings use only `MORNING`, `AFTERNOON`, or `EVENING`. |
| FR-08 | Current-period allocation | Activated current-period bookings enter main queue or waitlist only when eligible. |
| FR-09 | Future booking | Unactivated/future bookings are stored only when total capacity and minutes allow. |
| FR-10 | Period activation | Future bookings are allocated by priority when the current period is activated. |
| FR-11 | Duplicate activation prevention | A date/period activates at most once and records its state. |
| FR-12 | Main queue view | Admin can inspect main-queue bookings in FIFO order. |
| FR-13 | Waitlist view | Admin can inspect priority-ordered waitlist bookings. |
| FR-14 | Personal active bookings | Customer sees only own `WAITING` and `SERVING` bookings. |
| FR-15 | Process next booking | The next main-queue booking changes from `WAITING` to `SERVING` only if none is serving. |
| FR-16 | Payment confirmation | Admin can record `CASH` or `BANKING` payment for the serving booking. |
| FR-17 | Booking completion | Completion updates status, history, loyalty, undo data, and eligible promotion. |
| FR-18 | Cancellation | Authorized cancellation updates status and performs eligible promotion. |
| FR-19 | Loyalty recalculation | Recalculate visits, spend, points, and tier from qualifying completed history. |
| FR-20 | History view | Customers see personal history; Admin can view global or filtered history. |
| FR-21 | Latest completion undo | Undo restores all valid consequences of the latest completion. |
| FR-22 | Data loading | Load data from text files at startup. |
| FR-23 | Data saving | Save changed state to the relevant files after material operations. |
| FR-24 | Generated IDs | Generate incremental IDs for customers, vehicles, packages, and bookings. |
| FR-25 | Seed data | Populate empty or missing storage with sample records. |
| FR-26 | Input validation | Reject malformed or inconsistent user input with a clear message. |

## 4. Use-Case Summary

| Use case | Primary actor | Outcome |
| --- | --- | --- |
| UC-01–UC-02 | Customer | View services and personal profile. |
| UC-03 | Customer / Admin | Create a validated booking and show its allocation. |
| UC-04–UC-06 | Customer | View active bookings, cancel a permitted booking, and view personal history. |
| UC-07–UC-09 | Admin | Manage customers, vehicles, and wash packages. |
| UC-10–UC-12 | Admin | Configure simulation time, activate a period, and inspect period bookings. |
| UC-13–UC-15 | Admin | Process the next booking, confirm payment, and complete service. |
| UC-16–UC-17 | Admin | View history and undo the latest completion. |

## 5. Data Requirements

| Entity/file | Required fields |
| --- | --- |
| `customers.txt` | ID, name, unique phone, tier, points, total spend, visit count |
| `vehicles.txt` | ID, unique license plate, owner customer ID |
| `services.txt` | ID, name, price, duration, `ACTIVE`/`INACTIVE` status |
| `bookings.txt` | ID, customer ID, vehicle ID, service ID, date, period, booking/payment status, payment method, creation time |
| `periods.txt` | Simulated date, period, activation status |
| `history.txt` | Booking/customer/vehicle/service details, completion time, paid amount, earned points |

Records are delimited by `|`. Queue and waitlist are runtime structures reconstructed from bookings and period activation state; they are not persisted as separate files.

## 6. Non-Functional Requirements

| ID | Requirement |
| --- | --- |
| NFR-01 | Business rules, custom data structures, and status transitions must behave correctly. |
| NFR-02 | The menu-based CLI must provide clear English prompts, confirmations, and errors. |
| NFR-03 | Changes must be persisted and entity uniqueness/references must be protected. |
| NFR-04 | Packages and custom structures must remain readable and maintainable for learning and review. |
| NFR-05 | The solution must remain within academic CLI scope: no DBMS or complex UI frameworks. |

## 7. Architecture Constraints

- Java 8+ and a NetBeans/Ant-compatible project structure.
- Core logic uses project-defined `MyLinkedList`, `MyQueue`, `MyPriorityQueue`, `MyStack`, and `MyMap`.
- Plain Java file I/O provides persistence.
- The application is a single-process, monolithic CLI and does not implement security, auditing, or web/mobile delivery.

## 8. Traceability and History

- [PRD](PRD.md) provides the high-level product view.
- [Technical Decisions](../architecture/TECH_STACK.md) explains selected implementation constraints.
- The original Vietnamese detailed SRS is preserved as [SRS.vi.md](../old/SRS.vi.md).
