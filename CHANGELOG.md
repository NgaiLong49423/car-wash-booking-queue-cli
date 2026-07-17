## [Unreleased]

### Added

* Implemented Global and Customer History Reports (Issue #13). Total Revenue calculation is included in the Global Report.

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
