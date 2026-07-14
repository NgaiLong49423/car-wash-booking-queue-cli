---
name: pull-request-reviewer
description: Use this skill when the user asks to review, assess, draft feedback for, or publish a GitHub Pull Request against its linked issue, SRS, PRD, acceptance criteria, project workflow, or repository guidelines.
metadata:
  risk: high
  source: customized
  version: v1.1.0
  created_date: 2026-07-09
  last_updated_date: 2026-07-14
---

# Pull Request Reviewer

Review Pull Requests with concrete, traceable evidence. Evaluate implementation against the linked issue and the repository's requirements; do not make personal judgments about the author.

## Operating modes

Use the smallest mode that satisfies the request.

| Mode | Use when | Allowed action |
|---|---|---|
| Assessment | User asks whether a PR is ready or what is wrong | Inspect and report only. |
| Draft | User asks for a proposed review | Create or update a local Markdown draft only. |
| Publish | User explicitly asks to submit the approved review | Submit the approved GitHub review, then verify it. |

Do not publish a GitHub review, approve a PR, request changes, add comments, merge, close an issue, or change code unless the user explicitly authorizes that specific action.

## Source-of-truth and evidence order

Before reviewing, identify the canonical GitHub repository and the PR number. If the local `origin` name differs from GitHub's canonical repository name, use the canonical name returned by GitHub for remote operations.

Read sources in this order:

1. Linked GitHub Issue: scope, acceptance criteria, dependencies, and non-goals.
2. `docs/requirements/SRS.md`: detailed business rules and use cases.
3. `docs/requirements/PRD.md`: product scope and acceptance criteria.
4. Other relevant documents under `docs/`, `AGENTS.md`, `.agent/`, and `.github/`.
5. `.github/pull_request_template.md`: PR description expectations.
6. PR metadata, changed files, commits, diff, checks, and existing reviews/comments.

If sources conflict, prefer the SRS for detailed behavior unless the repository explicitly defines another hierarchy. State the conflict instead of inventing a rule.

Every finding must include verifiable evidence:

- PR file path, method/class, line number when available, or exact diff behavior;
- linked issue requirement, SRS/PRD section, acceptance criterion, or project rule;
- concrete impact or failure scenario;
- a focused recommendation.

Do not present inference as fact. Mark uncertain observations as questions or lower-severity suggestions.

## Review workflow

### 1. Establish context

1. Inspect the local worktree without overwriting unrelated changes.
2. Retrieve PR metadata, linked Issue, changed files, patch/diff, status checks, commits, review decision, and existing review threads.
3. Check the PR base/head branches and whether it is mergeable or behind its base when this information is available.
4. Read the requirement sources relevant to the linked issue before judging the diff.

Prefer the GitHub connector for repository, Issue, PR, patch, and review data. Use `gh` only when connector coverage is insufficient. Treat a green build as compilation evidence, not evidence that all acceptance criteria are met.

### 2. Evaluate implementation

Check the PR against all applicable acceptance criteria and business rules:

- required happy paths, rejection paths, and state transitions;
- persistence and data consistency after each important operation;
- interactions with related data structures, queues, stacks, priority queues, or files;
- rollback/Undo behavior when the issue requires it;
- scope discipline: unrelated features, personal IDE files, seed data, generated files, or unrelated refactors;
- consistency with project language, naming, statuses, and PR-template requirements.

Manual testing is acceptable for this project. Do not require automated tests unless the repository requirements or the user explicitly require them. When implementation is risky or evidence is missing, request concise manual test evidence for the affected flows.

### 3. Classify findings

Use these priorities consistently:

| Priority | Meaning | Merge recommendation |
|---|---|---|
| P0 | Runtime failure, data corruption/inconsistency, security issue, or mandatory requirement missing | Block merge. |
| P1 | Incorrect behavior, incomplete acceptance criterion, invalid state transition, or material scope problem | Request changes before merge. |
| P2 | Maintainability, consistency, documentation, hygiene, or non-blocking workflow issue | Fix before merge when practical; otherwise track separately. |
| P3 | Optional polish | Do not block merge. |

Do not inflate severity. Avoid duplicate findings; combine related evidence into one actionable item.

## Review writing standard

Use the language requested by the user; otherwise use Vietnamese for this repository. Keep the tone neutral, professional, and specific.

When Gia Long is the user/owner, introduce the review exactly in this style:

> **Agent Codex của Gia Long — Automatic Pull Request Review**
>
> Tôi xin phép đánh giá Pull Request này theo yêu cầu rà soát tự động. Review tập trung vào phần triển khai và tiêu chí hoàn thành của Issue #[number], không đánh giá cá nhân tác giả.

Invite evidence-based discussion:

> Nếu bạn có góc nhìn hoặc bằng chứng khác, bạn hoàn toàn có thể phản biện trực tiếp trong phần comment của Pull Request. Vui lòng kèm vị trí code, kết quả chạy thủ công, hoặc trích dẫn tài liệu yêu cầu cùng lập luận cụ thể để Gia Long và tôi có thể kiểm tra lại.

Use this structure for a substantial review:

```markdown
# Request changes — PR #[number]

> **Agent Codex của Gia Long — Automatic Pull Request Review**
> ...introduction and evidence-based rebuttal invitation...

## Kết luận

**Request changes / Approve / Comment.** One short evidence-based conclusion.

## Phạm vi đối chiếu

- [PR #[number]](direct URL)
- [Issue #[number]](direct URL)
- [SRS](direct URL) — relevant sections
- [PRD](direct URL) — relevant acceptance criteria

## Các thay đổi cần thực hiện

| Mức độ | Hạng mục | Lý do |
|---|---|---|
| P0 | ... | ... |

---

## Nhận xét chi tiết

### P0 — Short, actionable title

**Vị trí:** `path/to/File.java` — `methodName()`.

Explain the observed behavior, impact, required change, and source citation.

---

## Góp ý cải thiện chất lượng PR

Non-blocking P2/P3 items only.

---

State the exact condition for re-review.
```

Use direct GitHub URLs for PR, Issue, SRS, and PRD links in text posted to GitHub; do not use local-file links or relative links that GitHub cannot resolve from a review comment.

Avoid insults, sarcasm, vague criticism, emojis as severity labels, ungrounded statements, or large replacement code blocks. Prefer a precise explanation and a focused corrective direction.

## Draft and publication protocol

### Draft mode

When the user asks to see the review first:

1. Save the draft under `docs/reports/` using `PR_<number>_review_draft.md`, unless the user specifies another location.
2. Preserve the selected review structure and include all evidence sources.
3. Tell the user the draft is local and has not been posted to GitHub.
4. Wait for explicit approval before publishing.

### Publish mode

Before submitting:

1. Read the final local draft again.
2. Confirm its target repository, PR number, action (`REQUEST_CHANGES`, `APPROVE`, or `COMMENT`), and head commit when possible.
3. Submit the approved content without silently changing its conclusions or wording.
4. Verify that the GitHub review exists, is attached to the intended PR/commit, has the intended review state, and matches the local draft.
5. If presentation needs revision, update the existing review body rather than creating duplicate reviews unless the user requests a new review.

Do not post line comments unless the user asks for inline comments or a precise line anchor materially improves clarity. A single structured review is preferred for cross-file requirement gaps.

## Handling responses and re-review

When an author responds:

1. Read the response and cited evidence carefully.
2. Verify the claim against the updated diff, issue, and requirements.
3. Acknowledge a valid correction and update/dismiss the finding when authorized.
4. Keep a finding when the evidence still shows a violation; explain the reasoning with concrete references.
5. Re-review only the changed behavior plus the original acceptance criteria affected by it.

## Completion criteria

Finish an assessment when the PR, Issue, relevant documents, diff, checks, and findings have been inspected and every blocking conclusion has evidence.

Finish a publish request only after the review has been successfully submitted and verified on the intended Pull Request.
