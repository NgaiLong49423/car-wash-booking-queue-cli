## [0.7.0] - 2026-07-21

### Author

* Prepared by Ngô Gia Long (SE190732) and Agent.

### Changed

* **Business Logic (SRS & PRD):** Upgraded queue capacity logic to check not only slot limits but also total service duration (`periodTotalMinutes`) to prevent time overflow during the active period.
* **Waitlist Processing:** Upgraded the Waitlist promotion algorithm to iterate through the entire Priority Queue, ensuring available time slots are utilized even if the highest-priority booking is too long.
* **Loyalty Calculation:** Updated loyalty rules to only count `COMPLETED` bookings within the last 365 days, simulating a more realistic tier-reset business mechanism.
* Synchronized `PRD.md` (v2.0.0) and `README.md` to align with the new v4.0.0 business rules defined in `SRS.md`.

## [0.6.0] - 2026-07-20

### Author

* Prepared by Ngô Gia Long (SE190732) and Agent.

### Added

* **Feature 006 (Simulation Time):** Added functionality to set the simulated current date and service period (`MORNING`, `AFTERNOON`, `EVENING`) for testing time-dependent logic.
* **Feature 007 (Booking Creation):** Implemented booking creation with robust validation for customer limits (Booking Window based on Tier) and capacity checks.
* **Feature 008 (Period Activation):** Implemented service period activation to automatically allocate future bookings into the Main Queue and Waitlist (Max-Heap) based on priority.
* **Feature 010 (Service Processing):** Added the ability to process the next vehicle in the Main Queue and simulate payment confirmation.
* **Feature 013 (History Reports):** Added comprehensive UI reports for viewing global wash history and specific customer history.
* **Feature 014 (Undo):** Implemented an Undo feature using a custom Stack (`MyStack`) to revert the last completed booking and restore waitlist states.
* **Feature 015 (CLI Integration):** Integrated all subsystems into a unified CLI menu with hardened user input validation (regex for phone numbers and license plates).

### Fixed

* **Fix 027 (Menu Routing):** Corrected Main Menu routing to properly separate Customer and Admin roles and maintain correct authentication states.

## [0.5.0] - 2026-07-18

### Author

* Prepared by Ngô Gia Long (SE190732) and Agent.

### Added

* Added read-only queue monitoring for the active service period, including a FIFO view of the Main Queue and a priority-ordered view of the Waitlist.
* Added non-destructive queue snapshots so monitoring can display Main Queue and Max-Heap Waitlist contents without removing or reordering live bookings.
* Added formatted booking tables showing booking ID, license plate, wash service, membership tier, booking status, and used-versus-total slot capacity.
* Added views for future bookings by date and period, plus customer-specific active bookings filtered to `WAITING` and `SERVING` states.
* Added booking completion for `SERVING` and `PAID` bookings, including status transition to `COMPLETED`, wash-history creation, and persistent data updates.
* Added loyalty recalculation from all completed bookings, covering visit count, total spending, loyalty points, and highest eligible membership tier.
* Added Waitlist promotion after completion when the highest-priority booking fits within the remaining service-period time.
* Added customer cancellation for the customer's own `WAITING` bookings and administrator cancellation for `WAITING` or `SERVING` bookings.
* Added cancellation-aware queue coordination: removal from Main Queue or Waitlist, release of the current service position, and promotion of the highest-priority Waitlist booking when eligible.
* Added preservation of an existing `PAID` payment status when a `SERVING` booking is cancelled, without creating completion history or loyalty credit.

### Changed

* Updated remaining-time calculations to include completed bookings, the currently serving booking, and waiting bookings that occupy Main Queue slots, while excluding Waitlist and future bookings.
* Standardized newly added queue, completion, cancellation, and monitoring messages in English to follow the application's interface-language requirement.
* Synchronized documentation work with the latest `main` branch after the queue monitoring, completion, loyalty, cancellation, and Waitlist coordination features were integrated.

## [0.4.0] - 2026-07-11

### Author

* Prepared by Ngô Gia Long (SE190732) and Agent.

### Added

* Implemented persistent file storage (`util.FileManager`) with `|` separated `.txt` files, including automatic data directory initialization and auto-save capability (Issue #2).
* Implemented Customer Management (Issue #3) providing CRUD operations with auto-generated IDs, phone number validation, and strict foreign-key dependency checks before deletion.
* Implemented Vehicle Management (Issue #4) with auto-generated IDs, license plate validation (space and case insensitive), and strict customer ownership linkage.
* Implemented Service Management (Issue #5) providing CRUD for wash packages, price/duration validations, and defensive programming for user data entry.
* Added a custom Selection Sort algorithm to sort wash services by price or duration (Issue #5).
* Created a reusable `pull-request-reviewer` agent skill to automate and standardize Pull Request quality checks against SRS and Project rules.

## [0.3.0] - 2026-06-29

### Author

* Prepared by Ngô Gia Long (SE190732).

### Added

* Implemented and integrated custom data structures to serve as the application's foundational data layer, avoiding Java's built-in collections:
  * `MyLinkedList`: A custom singly linked list implementation for generic data storage.
  * `MyQueue`: A custom FIFO (First-In-First-Out) queue implementation.
  * `MyStack`: A custom LIFO (Last-In-First-Out) stack implementation.
  * `MyPriorityQueue`: A custom Max-Heap based priority queue implementation to manage prioritized bookings.
  * `MyMap`: A custom key-value association structure.
  * `Node`: A generic node class supporting the custom linked-list, queue, and stack implementations.

## [0.2.1] - 2026-06-27

### Author

* Prepared by Ngô Gia Long (SE190732).

### Project Management

* Finalized the official task assignment plan for the Car Wash Booking Queue CLI project.
* Balanced the project workload across 4 members using Story Points, dependency risk, module ownership, and previously completed planning work.
* Grouped the 15 GitHub Issues into 4 milestone-based delivery phases instead of using small per-issue deadlines.
* Added milestone-based Start Date and Target Date planning for the main GitHub Issues.
* Defined the following milestone schedule:
  * Milestone 1: Foundation & Data Ready — 2026-06-27 to 2026-07-01.
  * Milestone 2: Booking Core Ready — 2026-07-02 to 2026-07-05.
  * Milestone 3: Operation Flow Ready — 2026-07-06 to 2026-07-08.
  * Milestone 4: CLI Integration & Final QA — 2026-07-09 to 2026-07-11.
* Confirmed that GitHub Issues are used for detailed implementation tracking, while deadlines are managed through larger milestones.

### Docs

* Updated the project planning records to align task assignment, milestone deadlines, and GitHub Project tracking.
* Confirmed the task assignment structure between the work distribution report and the phase/branch/merge workflow.

### Changed

* Changed deadline planning from small isolated issue-level deadlines to larger milestone-based deadlines.
* Clarified that Issue #15 is treated as the final CLI integration and QA milestone involving all members.

## [0.2.0] - 2026-06-26

### Author

* Prepared by Ngô Gia Long (SE190732).

### Docs

* Finalized `docs/requirements/SRS.md` as the primary Software Requirements Specification for the project.
* Finalized `docs/requirements/PRD.md` as the Product Requirements Document derived from the finalized SRS.
* Standardized the requirements documentation structure under `docs/requirements/`.
* Defined `SRS.md` as the source of truth when there is any conflict between `SRS.md` and `PRD.md`.
* Added `docs/architecture/TECH_STACK.md` to document the project's selected technology stack and architecture decisions.

### Added

* Added `.agent/skills/srs-to-github-issues/SKILL.md` to support decomposing requirements documents into professional GitHub Issue drafts.
* Added `docs/architecture/TECH_STACK.md` to record the project technology stack, including JDK 8, NetBeans, monolithic architecture, and simple 3-layer code organization.
* Updated GitHub Issue templates to support Source Trace, Project Metadata, Relationships, Acceptance Criteria, and Suggested Branch.

### Changed

* Standardized the GitHub Issue creation workflow as draft-first: issue drafts should be created and reviewed before real GitHub Issues are created.
