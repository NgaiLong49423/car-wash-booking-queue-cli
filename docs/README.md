# Project Documentation

This directory contains the maintained English documentation for the AutoWash Priority Booking Engine.

## Structure

```text
docs/
├── architecture/       # Technical decisions and architecture constraints
├── requirements/       # Current PRD and canonical SRS
├── reports/            # Course reports and the delivery workflow
└── old/                # Superseded specifications and Vietnamese source copies
```

## Documentation Map

| Document | Purpose | Authority |
| --- | --- | --- |
| [PRD](requirements/PRD.md) | Product problem, scope, goals, and high-level requirements. | Product overview |
| [SRS](requirements/SRS.md) | Functional requirements, business rules, data model, and acceptance conditions. | **Source of truth** |
| [Technical Decisions](architecture/TECH_STACK.md) | Technology choices, architecture, and technical constraints. | Architecture reference |
| [Reports](reports/README.md) | Course reports and their naming convention. | Delivery reference |
| [Contributing Guide](../CONTRIBUTING.md) | Repository workflow, branches, commits, and pull requests. | Contribution process |

If the PRD conflicts with the SRS, follow the SRS.

## Archived Vietnamese Documents

The previous Vietnamese versions of maintained Markdown documents are preserved in [`old/`](old/):

- [`README.vi.md`](old/README.vi.md)
- [`PRD.vi.md`](old/PRD.vi.md)
- [`SRS.vi.md`](old/SRS.vi.md)
- [`reports-README.vi.md`](old/reports-README.vi.md)
- [`phase-branch-merge-workflow.vi.md`](old/phase-branch-merge-workflow.vi.md)

They are retained for historical reference and must not replace the current English SRS as the active requirement baseline.
