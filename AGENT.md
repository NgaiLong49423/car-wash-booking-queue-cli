# AGENTS.md

## Project Context

This repository contains the AutoWash Priority Booking Engine project.

The canonical requirements documents are located in:

- `docs/requirements/PRD.md`
- `docs/requirements/SRS.md`

Use `docs/requirements/PRD.md` for product-level overview.
Use `docs/requirements/SRS.md` for detailed requirements, business rules, actors, use cases, functional requirements, non-functional requirements, constraints, and edge cases.

If `PRD.md` and `SRS.md` conflict, treat `docs/requirements/SRS.md` as the source of truth.

## Repository Structure

Important paths:

- `.agent/skills/` — local agent skills for this repository.
- `.agent/output/report/` — generated analysis reports and issue drafts.
- `.agent/output/report/github-issue-drafts/` — default output directory for drafted GitHub Issues.
- `.github/labels.yml` — allowed GitHub labels.
- `.github/ISSUE_TEMPLATE/` — GitHub Issue templates.
- `.github/pull_request_template.md` — Pull Request template.
- `docs/requirements/` — product and requirements documents.
- `docs/reports/` — project reports.
- `docs/architecture/` — technical decisions and architecture documentation.
- `AutoWashQueueCLI/` — application source code, data, and tests.

## General Agent Rules

- Read this file before making changes.
- Do not invent requirements, features, labels, project fields, business rules, actors, or technical constraints.
- Do not modify source code unless the user explicitly asks for implementation work.
- Do not create commits, branches, pull requests, or GitHub Issues unless the user explicitly requests that action.
- Prefer small, reviewable changes.
- If a requirement is unclear, ask the user before changing files or creating GitHub artifacts.

## Requirement-to-Issue Workflow

When asked to decompose requirements into GitHub Issues, use:

- `.agent/skills/srs-to-github-issues/SKILL.md`

The Vietnamese reference version is:

- `.agent/skills/srs-to-github-issues/SKILL.VN.md`

`SKILL.md` is the operational version for agents.
`SKILL.VN.md` is for human reference only unless the user explicitly asks to read it.

## Issue Draft Rules

Default mode is draft-only.

When drafting issues:

- Read `docs/requirements/PRD.md` and `docs/requirements/SRS.md`.
- Read `.github/labels.yml` before assigning labels.
- Read `.github/ISSUE_TEMPLATE/*.yml` before drafting issue bodies.
- Write issue drafts to `.agent/output/report/github-issue-drafts/`.
- Create or update `.agent/output/report/github-issue-drafts/ISSUE_INDEX.md`.
- Group issues by module or domain.
- Each issue must trace back to the source document, such as FR, UC, business rule, NFR, or PRD section.
- Issue titles must be written in professional English.
- Issue bodies should be written in Vietnamese unless the user requests otherwise.
- Suggested branch names must use lowercase kebab-case.

## GitHub Issue Creation Rules

Do not create real GitHub Issues unless the user explicitly requests GitHub creation mode.

When creating real GitHub Issues:

- Use only approved drafts by default.
- If the user specifies issue numbers or a range, create only the requested drafts.
- Ensure required labels exist on GitHub.
- If a label is missing but exists in `.github/labels.yml`, it may be created from `.github/labels.yml`.
- After issue creation, update `ISSUE_INDEX.md` with GitHub issue number, URL, creation date, and status.

## GitHub Project Sync Rules

Do not sync GitHub Project fields unless the user explicitly requests Project sync mode.

When Project sync is requested:

- Inspect the GitHub Project fields before editing.
- Confirm the correct owner, project number, field names, field IDs, and option IDs.
- Sync only fields that can be identified safely.
- Supported metadata fields include:
  - Type
  - Size
  - Story Points
  - Priority
  - Start date
  - Target date
  - Relationships

If any required project field, option, or relationship cannot be identified safely, stop and ask the user.

## Labels and Templates

Allowed labels are defined by:

- `.github/labels.yml`

Issue templates are defined by:

- `.github/ISSUE_TEMPLATE/`

Agents must not invent labels outside `.github/labels.yml`.

When assigning labels:

- Each issue must include at least one main type label.
- Priority labels must match the issue priority when priority is set.
- Additional labels must be selected only from `.github/labels.yml`.

## Issue Sizing Rules

Use separate fields for `Size` and `Story Points`.

Size estimates scope:

- `XS` — very small scope
- `S` — small scope
- `M` — moderate scope
- `L` — large scope
- `XL` — too large or epic-level scope

Story Points estimate effort, uncertainty, and risk:

- `1` — trivial
- `2` — easy
- `3` — moderate
- `5` — hard
- `8` — very hard
- `13` — too large or high-risk

If an implementation issue is `XL` or has `Story Points >= 13`, propose splitting it.
Epic or parent issues may remain large if they are only used to group child issues.

## Relationships

When useful, issue drafts should include:

- Parent
- Blocked by
- Blocking
- Security alert

Use relationships only when they are justified by the requirements, dependencies, or implementation order.

## Branch Rules

Agents must not create branches automatically.

Each issue may include a suggested branch name:

```text
<type>/<short-kebab-case-title>
