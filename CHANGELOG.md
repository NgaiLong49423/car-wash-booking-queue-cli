## [0.2.0] - 2026-06-26

### Docs

* Finalized `docs/requirements/SRS.md` as the primary Software Requirements Specification for the project.
* Finalized `docs/requirements/PRD.md` as the Product Requirements Document derived from the finalized SRS.
* Standardized the requirements documentation structure under `docs/requirements/`.
* Defined `SRS.md` as the source of truth when there is any conflict between `SRS.md` and `PRD.md`.

### Added

* Added `.agent/skills/srs-to-github-issues/SKILL.md` to support decomposing requirements documents into professional GitHub Issue drafts.
* Added `.agent/skills/srs-to-github-issues/SKILL.VN.md` as a Vietnamese human-readable reference for the issue decomposition skill.
* Updated GitHub Issue templates to support Source Trace, Project Metadata, Relationships, Acceptance Criteria, and Suggested Branch.

### Changed

* Standardized the GitHub Issue creation workflow as draft-first: issue drafts should be created and reviewed before real GitHub Issues are created.
