> **Document:** Product Requirements Document (PRD)
> **Version:** 2.0.0
> **Status:** Active
> **Last updated:** 2026-07-21

# AutoWash Priority Booking Engine

## Product Overview

AutoWash Priority Booking Engine is a Java command-line application that simulates booking and service-queue operations for a car wash with limited capacity. It manages customers, vehicles, wash packages, bookings, service periods, payment status, history, and loyalty while using custom data structures and pipe-delimited text files instead of a database.

The product is an academic CSD201 project. Its main value is demonstrating how a FIFO queue, a max-heap priority queue, a stack, a linked list, and a map can support a coherent operational workflow.

## Problem Statement

A car wash can serve only a limited number of vehicles and minutes in each service period. Customers need to book in advance or during the active period. The system must preserve predictable first-in, first-out processing for confirmed service slots while allowing higher membership tiers to receive priority when capacity is scarce.

The system must also keep operational records consistent when bookings are completed, cancelled, promoted from the waitlist, or undone.

## Goals

- Accept and validate bookings for current and future service periods.
- Allocate bookings fairly under slot and service-time constraints.
- Give higher membership tiers priority in the waitlist.
- Provide a complete operational CLI flow from booking through payment and completion.
- Persist state in plain text files and restore it after restart.
- Demonstrate custom data-structure and algorithm implementations without a DBMS.

## Scope

### In scope

- Customer, vehicle, and wash-package management.
- Membership booking windows, service periods, slot capacity, and remaining-time validation.
- Future-booking storage and one-time period activation.
- FIFO main queue and priority waitlist allocation.
- Processing, payment confirmation, completion, cancellation, history, loyalty recalculation, and latest-completion undo.
- Pipe-delimited data persistence, generated IDs, input validation, and seed data.

### Out of scope

- Real authentication, authorization, payment gateways, refunds, audit logging, or multi-user concurrency.
- Web, mobile, or desktop graphical interfaces.
- SQL/NoSQL databases, license-plate recognition, email/SMS notifications, and advanced promotions.

## Users

| Actor | Responsibilities |
| --- | --- |
| Customer | Views services and profile, creates bookings for owned vehicles, views active bookings/history, and cancels own waiting bookings. |
| Administrator | Manages data and runs the operational flow: period configuration and activation, queue monitoring, payment, completion, cancellation, reporting, and undo. |

The roles are menu-level simulations, not secured accounts.

## Product Capabilities

| ID | Capability |
| --- | --- |
| FR-01–FR-03 | Manage customers, vehicles, and wash packages. |
| FR-04 | Configure the simulated current date and service period. |
| FR-05–FR-09 | Create and validate bookings for current or future periods. |
| FR-10–FR-13 | Activate a period once and inspect main-queue, waitlist, or future bookings. |
| FR-14 | Let customers view active personal bookings. |
| FR-15–FR-17 | Process, collect payment for, and complete the next booking. |
| FR-18 | Cancel permitted bookings and promote eligible waitlisted bookings. |
| FR-19–FR-21 | Recalculate loyalty, show history, and undo the latest completion. |
| FR-22–FR-26 | Load/save data, generate IDs, seed empty storage, and validate input. |

## Core Rules

### Service periods

| Period | Hours | Main slots | Waitlist slots | Service minutes |
| --- | --- | ---: | ---: | ---: |
| `MORNING` | 07:00–12:00 | 10 | 3 | 300 |
| `AFTERNOON` | 13:00–17:00 | 10 | 3 | 240 |
| `EVENING` | 18:00–21:00 | 5 | 2 | 180 |

### Membership benefits

| Tier | Maximum advance booking | Qualification |
| --- | ---: | --- |
| `MEMBER` | 7 days | Default tier |
| `SILVER` | 10 days | 5 visits or VND 2,000,000 spend |
| `GOLD` | 12 days | 15 visits or VND 6,000,000 spend |
| `PLATINUM` | 14 days | 30 visits or VND 15,000,000 spend |

Waitlist priority is `PLATINUM` > `GOLD` > `SILVER` > `MEMBER`; earlier creation time breaks ties. Loyalty is recalculated from completed bookings in the rolling 365-day window.

## Success Criteria

The product is successful when it can:

1. Reject invalid, over-window, over-capacity, or over-time bookings.
2. Process the main queue in FIFO order while enforcing priority ordering in the waitlist.
3. Persist changes after state-changing operations and reconstruct operational state after restart.
4. Keep booking, history, queue, and loyalty state consistent after completion, cancellation, promotion, and undo.
5. Run entirely as a Java CLI application using the repository's custom data structures.

## Related Documents

- [Canonical SRS](SRS.md)
- [Technical Decisions](../architecture/TECH_STACK.md)
- [Project README](../../README.md)
- [Archived Vietnamese PRD](../old/PRD.vi.md)
