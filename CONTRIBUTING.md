# Contributing Guide

Thank you for contributing to this project.

This document defines the rules for working with branches, commits, pull requests, documentation, and project workflow. All contributors and agents should follow these rules to keep the repository clean, consistent, and easy to maintain.

---

## 1. General Rules

* Keep changes small, clear, and reviewable.
* Do not commit temporary files, build outputs, IDE-specific files, or local configuration files.
* Do not modify unrelated files in the same commit.
* Update documentation when your change affects setup, usage, requirements, or project behavior.
* Make sure the project builds and runs before pushing changes.
* Do not rewrite existing requirements or business rules unless the change is intentional and approved.

---

## 2. Branch Naming Rules

Create a new branch for each feature, bug fix, documentation update, or task.

Branch format:

```text
<type>/<short-kebab-case-name>
```

Allowed branch prefixes:

```text
feature/    # New feature
fix/        # Bug fix
docs/       # Documentation update
refactor/   # Code restructuring without behavior change
test/       # Test-related changes
build/      # Build configuration or dependency changes
chore/      # Maintenance or setup tasks
```

Examples:

```text
feature/booking-window-validation
fix/customer-data-loading
docs/update-srs
refactor/booking-service
test/booking-queue-tests
build/update-dependencies
chore/project-setup
```

Branch names must use lowercase kebab-case.

---

## 3. Commit Message Rules

This project uses Conventional Commits.

Commit format:

```text
<type>(<scope>): <description>
```

Examples:

```text
feat(booking): add booking window validation
fix(queue): prevent duplicate waitlist promotion
docs(requirements): finalize SRS and PRD documentation
refactor(customer): simplify customer lookup logic
test(booking): add booking validation test cases
chore(git): update gitignore rules
```

Allowed commit types:

```text
feat      # New feature
fix       # Bug fix
docs      # Documentation-only change
style     # Formatting only, no logic change
refactor  # Code restructuring without behavior change
perf      # Performance improvement
test      # Add or update tests
build     # Build system or dependency changes
chore     # Maintenance task
ops       # Deployment, CI/CD, or infrastructure change
```

Commit description rules:

* Use lowercase at the beginning.
* Do not end with a period.
* Use a clear action phrase.
* Avoid vague messages such as `update`, `fix bug`, or `changes`.

Bad examples:

```text
update
fix bug
done
```

Good examples:

```text
docs(requirements): finalize SRS and PRD documentation
feat(booking): add priority booking validation
fix(file): handle missing customer data file
```

---

## 4. Pull Request Rules

Each pull request should be focused on one clear purpose.

Before opening a pull request:

* Make sure the branch is up to date.
* Make sure the project builds and runs.
* Remove debug code, temporary comments, and unused files.
* Fill in the pull request template.
* Link related issues when applicable.

Pull request title should follow the same style as commit messages.

Examples:

```text
feat(booking): implement booking creation flow
docs(requirements): finalize SRS and PRD documentation
fix(queue): correct waitlist promotion rule
```

---

## 5. Issue Rules

When creating or working on GitHub Issues:

* Use the correct issue template.
* Write clear acceptance criteria.
* Link the issue to related requirements when possible.
* Keep each issue focused and small enough to implement.
* If an issue is too large, split it into smaller issues or create an Epic/Parent issue.

Issue titles should be written in professional English.

Example:

```text
[Booking][FR-05/FR-06] Implement Booking Creation With Window Validation
```

---

## 6. Documentation Rules

Update documentation when changing:

* Requirements
* Business rules
* Setup instructions
* Usage instructions
* Project structure
* GitHub workflow
* Agent workflow

Important documentation paths:

```text
docs/requirements/SRS.md
docs/requirements/PRD.md
docs/requirements/README.md
CHANGELOG.md
README.md
AGENTS.md
```

If `SRS.md` and `PRD.md` conflict, `SRS.md` is treated as the source of truth.

---

## 7. Changelog Rules

Use `CHANGELOG.md` to record meaningful project changes.

Do not record every tiny edit. Record changes that affect:

* Features
* Requirements
* Documentation structure
* Project workflow
* GitHub templates
* Agent workflow
* Release milestones

Example:

```md
## [0.2.0] - 2026-06-26

### Docs

- Finalized `docs/requirements/SRS.md` as the primary Software Requirements Specification.
- Finalized `docs/requirements/PRD.md` as the Product Requirements Document derived from the finalized SRS.
```

---

## 8. Agent Rules

AI agents must follow the same contribution rules as human contributors.

Agents must not:

* Create branches unless explicitly requested.
* Create commits unless explicitly requested.
* Push code unless explicitly requested.
* Open pull requests unless explicitly requested.
* Create real GitHub Issues unless explicitly requested.
* Sync GitHub Project fields unless explicitly requested.
* Invent requirements, labels, project fields, or business rules.

When decomposing requirements into GitHub Issues, agents must use:

```text
.agent/skills/srs-to-github-issues/SKILL.md
```

Issue drafts must be written to:

```text
.agent/report/github-issue-drafts/
```

---

## 9. Checklist Before Push

Before pushing code, check:

* [ ] The project builds successfully.
* [ ] The project runs without obvious errors.
* [ ] No temporary files or build artifacts are committed.
* [ ] Commit messages follow Conventional Commits.
* [ ] Related documentation has been updated.
* [ ] Related issues are linked when applicable.
* [ ] The change is focused and does not include unrelated edits.

---

## 10. Summary

Use this file as the contribution rulebook for the project.

* `README.md` explains what the project is.
* `CHANGELOG.md` records what has changed.
* `CONTRIBUTING.md` explains how to contribute.
* `AGENTS.md` defines repository-level rules for AI agents.
* `.github/ISSUE_TEMPLATE/` defines issue creation forms.
* `.github/pull_request_template.md` defines the pull request form.
