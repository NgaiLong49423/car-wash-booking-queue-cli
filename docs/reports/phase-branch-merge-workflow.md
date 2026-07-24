> **Document:** Issue-Based Phase, Branch, and Merge Workflow
> **Version:** 1.0.1
> **Status:** Completed process reference

# Issue-Based Development Workflow

## Core Principle

```text
1 GitHub Issue = 1 Branch = 1 Pull Request = 1 Merge to main
```

This approach keeps work isolated, makes reviews traceable, and helps keep `main` runnable.

## Working Rules

1. Create one focused branch for each feature, fix, documentation change, or task.
2. Start each branch from the latest `main`.
3. Keep an issue within its approved scope.
4. Build and test locally before opening a pull request.
5. Merge only after review and after resolving conflicts on the feature branch.
6. Update local `main` before starting the next dependency-sensitive issue.

## Standard Workflow

```bash
git checkout main
git pull origin main

git checkout -b feature/<issue-number>-<short-name>
# implement and test
git add <files>
git commit -m "feat(scope): concise description"
git push origin feature/<issue-number>-<short-name>
```

Create a pull request, request review, merge after approval, then update `main` again.

## Branch Names

Use lowercase kebab-case:

```text
feature/<issue-number>-<short-name>
fix/<issue-number>-<short-name>
docs/<short-name>
refactor/<short-name>
test/<short-name>
build/<short-name>
chore/<short-name>
```

Examples:

```text
feature/007-booking-creation-validation
fix/queue-waitlist-promotion
docs/update-requirements
```

## Suggested Delivery Sequence

| Phase | Focus |
| --- | --- |
| 0–2 | Project initialization, custom data structures, file storage, and seed data. |
| 3–4 | Customer, vehicle, package management, and simulation settings. |
| 5–6 | Booking validation, period activation, and queue monitoring. |
| 7–10 | Processing/payment, cancellation, completion/loyalty, and undo. |
| 11–12 | History reporting and end-to-end CLI integration. |

## Pull Request Checklist

- [ ] The project compiles and the relevant flow runs.
- [ ] The change satisfies the linked issue and does not expand its scope unexpectedly.
- [ ] Manual tests cover the normal path and relevant edge cases.
- [ ] No temporary files, generated outputs, or unrelated edits are included.
- [ ] Documentation is updated when behavior, setup, or requirements change.
- [ ] The pull request describes the linked issue, implementation summary, test method, and known limitations.

See the repository-wide [Contributing Guide](../../CONTRIBUTING.md) for the definitive branch and commit conventions. The original Vietnamese workflow is preserved at [`../old/phase-branch-merge-workflow.vi.md`](../old/phase-branch-merge-workflow.vi.md).
