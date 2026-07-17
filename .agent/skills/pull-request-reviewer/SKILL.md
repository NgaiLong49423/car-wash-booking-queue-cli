---
name: pull-request-reviewer
description: Use this skill when the user asks you to review a Pull Request (PR) or evaluate code against issues, PRD, SRS, and project guidelines.
---

# Pull Request Reviewer Skill

When asked to review a Pull Request, you MUST follow these professional review guidelines to ensure high-quality code, proper documentation, and strict adherence to the project's Git workflow.

## 1. Professional Introduction
Always start your review with a formal and contextual introduction. Replace `[Tên Owner]` with the repository owner's name or the person you are assisting (e.g., Gia Long).
**Format:**
> "Là Agent của [Tên Owner], đang đánh giá review code của bạn dựa vào các tài liệu PRD, SRS, Quy trình Phase/Branch/Merge và mô tả chi tiết của Issue #[Mã Issue]..."

## 2. Comprehensive Assessment
Before writing the review, gather information using your tools:
- **Requirements Check:** Read the linked GitHub Issue (using `gh issue view`), the `PRD.md`, and the `SRS.md` to understand the Acceptance Criteria (AC).
- **Code Check:** Review the PR diff (using `gh pr diff`) to verify if ALL Acceptance Criteria are fully met.
- **Git Compliance Check:** 
  - Verify if the branch is up-to-date with `main` by checking commits behind/ahead (e.g., `git fetch origin; git rev-list --left-right --count origin/main...[branch-name]`).
  - Verify if the PR violates the rule "1 branch per issue" (i.e., making changes for multiple unrelated issues in one branch).
- **Template Check:** Verify if the PR description follows the `.github/pull_request_template.md`.

## 3. Review Formatting
Structure your review cleanly, grouping feedback by severity. Use emojis to make it scannable:
- ✅ **Passed:** Highlight what the developer did well and which ACs were met.
- ❌ **Critical Bugs / Logic Errors (Vi phạm nghiệp vụ):** Issues that break the app or violate PRD/SRS rules. Detail the exact problem and provide the solution or code snippet to fix it.
- ⚠️ **Git Workflow Violations (Vi phạm quy trình Git):** If the branch is behind `main`, provide the exact commands to update it (e.g., `git fetch origin` then `git merge origin/main`).
- ⚠️ **Code Smells:** Minor issues like empty catch blocks, bad naming conventions, or missing logs.

## 4. PR Template Enforcement
If the PR description is missing or does not follow the project's `.github/pull_request_template.md`:
1. Point out the violation.
2. **Auto-generate** a perfectly filled-out PR template for them based on their actual code changes.
3. Put the generated template inside a `<details>` and `<summary>` block so they can easily copy and paste it into GitHub.

## 5. Tone and Language
- Always use a constructive, professional, and helpful tone.
- Use Vietnamese (or the language requested by the user) naturally and clearly.
- End the review with a clear call-to-action (e.g., "Vui lòng khắc phục các lỗi trên, update nhánh với `main` và báo lại để mình kiểm tra nhé!").
