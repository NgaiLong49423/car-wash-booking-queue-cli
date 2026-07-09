# [Queue][FR-10/11] Implement Service Period Activation

## Tóm tắt
Triển khai tính năng kích hoạt buổi rửa xe hiện tại (FR-10), ngăn chặn kích hoạt lại một buổi đã được kích hoạt trước đó (FR-11), lấy toàn bộ booking tương lai đã đặt trước của buổi đó, sắp xếp theo thứ tự độ ưu tiên và phân bổ vào Main Queue (FIFO) cùng Waitlist (Priority Queue).

## Source Trace
- PRD: docs/requirements/PRD.md (FR-10, FR-11)
- SRS/Spec: docs/requirements/SRS.md (FR-10, FR-11, UC-11, Mục 3.7)

## Mục tiêu
Thiết lập hàng chờ phục vụ thực tế cho buổi hiện tại, phân bổ vị trí công bằng dựa trên độ ưu tiên của hạng thành viên và thứ tự đặt trước của khách hàng.

## Phạm vi
- Triển khai chức năng Kích hoạt buổi phục vụ hiện tại (đối chiếu trùng khớp `currentDate` và `currentPeriod`).
- Ngăn chặn kích hoạt lại một buổi đã ở trạng thái `ACTIVATED` trong file `periods.txt` (Tích hợp FR-11).
- Lọc toàn bộ các booking tương lai thuộc ngày và buổi hiện tại đang ở trạng thái `WAITING` trong danh sách booking của bộ nhớ RAM.
- Sắp xếp danh sách booking tương lai lọc được theo thứ tự độ ưu tiên giảm dần:
  1. Hạng thành viên của khách hàng cao hơn được ưu tiên trước (Platinum > Gold > Silver > Member).
  2. Nếu cùng hạng thành viên, booking nào được tạo trước (`createdAt` nhỏ hơn) được ưu tiên trước.
- Tiến hành phân bổ danh sách đã sắp xếp vào cấu trúc dữ liệu:
  - Đổ tối đa bằng số giới hạn slot chính của buổi vào lớp `MyQueue` (Main Queue).
  - Đổ phần còn lại tối đa bằng số giới hạn slot chờ phụ của buổi vào lớp `MyPriorityQueue` (Waitlist - cài đặt bằng Max Heap).
- Ghi nhận trạng thái kích hoạt của buổi hiện tại xuống file `periods.txt` và cập nhật lại trạng thái các booking trong RAM (và ghi file `bookings.txt`).

## Không nằm trong phạm vi
- Không xử lý phân bổ cho các booking của ngày khác hoặc buổi khác ngày hiện tại mô phỏng.
- Không tự động thay đổi mốc thời gian mô phỏng sau khi kích hoạt.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Mỗi buổi (ngày + period) chỉ được kích hoạt duy nhất một lần. Trạng thái kích hoạt được lưu trữ bền vững.
- Giới hạn phân bổ slot của buổi phục vụ (Sức chứa slot chính, Sức chứa slot chờ phụ):
  - MORNING: 10 slot chính, 3 slot chờ phụ.
  - AFTERNOON: 10 slot chính, 3 slot chờ phụ.
  - EVENING: 5 slot chính, 2 slot chờ phụ.
- Trong điều kiện dữ liệu hợp lệ, hệ thống không phát sinh booking dư thừa vì tổng sức chứa đã được kiểm tra khi tạo booking. Nếu phát hiện dữ liệu vượt sức chứa do lỗi file hoặc dữ liệu thủ công, hệ thống không phân bổ phần dư và hiển thị lỗi dữ liệu.

## Grouping Justification
- **Grouped FRs**: FR-10 (Kích hoạt buổi rửa xe) và FR-11 (Ngăn kích hoạt trùng buổi).
- **Lý do gộp**: Ràng buộc ngăn kích hoạt trùng buổi (FR-11) là điều kiện kiểm tra (precondition check) trực tiếp nằm ngay đầu luồng hoạt động của chức năng kích hoạt buổi (FR-10). Do đó, hai yêu cầu này thuộc cùng một workflow nghiệp vụ và bắt buộc phải xử lý chung để tránh xáo trộn hàng đợi phục vụ.

## Acceptance Criteria
- [ ] Chỉ cho phép kích hoạt buổi phục vụ trùng khớp với ngày mô phỏng và buổi mô phỏng hiện tại.
- [ ] Báo lỗi và không cho phép kích hoạt lại buổi phục vụ đã được kích hoạt trước đó (được kiểm chứng qua file `periods.txt`).
- [ ] Khi kích hoạt, các booking tương lai được phân bổ chính xác: booking của Platinum luôn được ưu tiên vào slot chính trước Member; nếu cùng hạng Platinum, xe đặt lịch trước được vào slot chính trước.
- [ ] Trạng thái kích hoạt của buổi phục vụ được lưu lại trong file `periods.txt` thành công (Ví dụ dòng dữ liệu lưu: `2026-06-26|MORNING|ACTIVATED`).

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: M
- Story Points: 3
- Estimation Reason: Chứa thuật toán sắp xếp theo độ ưu tiên kép phức tạp và logic phân phối tài nguyên vào 2 cấu trúc dữ liệu khác nhau (FIFO Queue và Max Heap Priority Queue), kèm theo ghi nhận trạng thái vào file `periods.txt`.
- Priority: ⬆️ High (ID: 06f6c1b8)
- Priority Reason: Quyết định thứ tự phục vụ và trạng thái vận hành của tiệm, là tiền đề để nhân viên bắt đầu xử lý xe.
- Start Date: 2026-07-02
- Target Date: 2026-07-05

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🔴 priority-high

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/007-booking-creation-validation.md
- Blocking: .agent/report/github-issue-drafts/009-queue-view-monitoring.md, .agent/report/github-issue-drafts/010-process-service-processing.md, .agent/report/github-issue-drafts/012-cancellation-booking-cancellation.md, .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/period-activation`

## Ghi chú cho người thực hiện
- Đảm bảo in ra màn hình console danh sách kết quả phân bổ sau khi kích hoạt (bao nhiêu xe vào slot chính, bao nhiêu xe vào waitlist) để Admin dễ theo dõi.
