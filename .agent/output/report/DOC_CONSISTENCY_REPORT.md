# Documentation Consistency Report

## Summary
- Repository: autowash-priority-booking-engine
- Audit date: 2026-07-17
- Overall status: Needs cleanup
- Critical issues: 3
- Major issues: 0
- Minor issues: 0
- Info notes: 0

## Files Reviewed
| File | Role | Status |
|---|---|---|
| `docs/requirements/SRS.md` | Requirements | Contradiction inside itself |
| `docs/requirements/PRD.md` | Project requirements | Contradiction |
| `.agent/output/report/github-issue-drafts/011-completion-booking-completion.md` | Issue Draft | Contradiction |
| `docs/old/*` | Archive | Ignored |

## Findings
| ID | Severity | Area | Problem | Evidence | Recommended Fix | Owner Decision Needed |
|---|---|---|---|---|---|---|
| 1 | Critical | Requirements | `SRS.md` tự mâu thuẫn | Trong cùng `SRS.md`, Mục 3.9.2 đã cập nhật gồm 3 trạng thái, nhưng ở Line 552 và Line 1318 vẫn ghi: *Tính lại `usedMinutes` của buổi hiện tại bằng tổng thời gian dịch vụ của các booking `COMPLETED`* | Sửa Line 552 và 1318 trong `SRS.md` để đồng nhất với Mục 3.9.2 | No |
| 2 | Critical | Requirements | `PRD.md` mâu thuẫn `SRS.md` | `PRD.md` ở Line 245 ghi: `usedMinutes = tổng serviceDuration của các booking cùng ngày, cùng period, trạng thái COMPLETED` | Cập nhật công thức tính thời gian còn lại trong `PRD.md` giống với `SRS.md v3.0.4` | No |
| 3 | Critical | Agent Drafts | Draft 011 sai logic | File issue draft `011-completion-booking.md` ở Line 26 ghi `usedMinutes` chỉ tính `COMPLETED` | Sửa lại file issue draft 011 | No |

## Cross-Document Checks
| Check | Result | Notes |
|---|---|---|
| Requirement Drift | Failed | `PRD.md` và các phần khác của `SRS.md` chưa được update đồng bộ |

## Suggested Fix Order
1. Critical issues: Cập nhật `SRS.md` (các dòng còn sót), `PRD.md`, và `011-completion-booking-completion.md`

## Files Suggested for Update
| File | Reason |
|---|---|
| `docs/requirements/SRS.md` | Còn sót dòng 552 và 1318 chưa được sửa. |
| `docs/requirements/PRD.md` | Cập nhật logic toán học ở Line 245. |
| `.agent/output/report/github-issue-drafts/011-completion-booking-completion.md` | Cập nhật logic ở Line 26. |

## Final Recommendation
Needs documentation cleanup first. Các file tài liệu vẫn còn mâu thuẫn do lần sửa trước mới chỉ cập nhật ở Mục 3.9.2 của SRS.md mà quên các vị trí khác.
