# [Simulation][FR-04] Implement Simulation Time Settings

## Tóm tắt
Triển khai chức năng thiết lập thời gian mô phỏng toàn cục bao gồm ngày hiện tại (`currentDate`) và buổi hiện tại (`currentPeriod`), kèm kiểm tra tính hợp lệ của dữ liệu thời gian đầu vào (FR-26).

## Source Trace
- PRD: docs/requirements/PRD.md (FR-04, FR-26)
- SRS/Spec: docs/requirements/SRS.md (FR-04, FR-26, UC-10, Mục 3.1, Mục 6.6)

## Mục tiêu
Cung cấp biến thời gian mô phỏng thống nhất cho toàn bộ hệ thống, giúp tính toán booking window và kích hoạt buổi chính xác.

## Phạm vi
- Tạo biến trạng thái toàn cục lưu trữ: `currentDate` (String dạng `YYYY-MM-DD` hoặc đối tượng Date tương ứng) và `currentPeriod` (Enum hoặc String nhận các giá trị: `MORNING`, `AFTERNOON`, `EVENING`).
- Kiểm tra dữ liệu ngày và buổi nhập vào (ngày đúng định dạng ngày tháng hợp lệ, buổi thuộc danh mục quy định) (Tích hợp FR-26).
- Thiết lập trạng thái kích hoạt mặc định của buổi hiện tại (Activated hoặc Not Activated) dựa trên file `periods.txt`.
- Triển khai chức năng thay đổi ngày và buổi hiện tại từ Admin Menu.
- Lưu trữ/nạp cấu hình ngày và buổi hiện tại từ file `periods.txt`.

## Không nằm trong phạm vi
- Không lấy thời gian hệ thống thực tế của hệ điều hành làm mốc kiểm tra nghiệp vụ; mọi logic kiểm tra thời gian bắt buộc phải đối chiếu qua thời gian mô phỏng.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Định dạng ngày nhập vào phải là ngày hợp lệ (dạng `YYYY-MM-DD`).
- Buổi phục vụ bắt buộc phải là một trong ba giá trị: `MORNING`, `AFTERNOON`, `EVENING`.

## Grouping Justification
- **Grouped FRs**: FR-04 (Thiết lập ngày/buổi hiện tại), một phần FR-26 (Validate định dạng ngày/buổi).
- **Lý do gộp**: Logic kiểm tra định dạng ngày `YYYY-MM-DD` và kiểm tra giá trị buổi hợp lệ (FR-26) là điều kiện ràng buộc bắt buộc khi Admin thay đổi cấu hình thời gian mô phỏng (FR-04). Gộp chung vào cùng một workflow để đảm bảo an toàn dữ liệu.

## Acceptance Criteria
- [ ] Thiết lập ngày hiện tại thành công với định dạng `YYYY-MM-DD` hợp lệ (Ví dụ: `2026-06-26`).
- [ ] Thiết lập buổi hiện tại thành công chỉ với 3 giá trị cố định: `MORNING`, `AFTERNOON`, `EVENING` (không phân biệt chữ hoa thường khi nhập).
- [ ] Từ chối cập nhật và báo lỗi nếu ngày nhập sai định dạng hoặc buổi không hợp lệ.
- [ ] Thay đổi thời gian mô phỏng thành công sẽ tự động ghi nhận lại xuống file `periods.txt`.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: XS
- Story Points: 1
- Estimation Reason: Chỉ bao gồm thiết lập các biến toàn cục mô phỏng và kiểm tra định dạng chuỗi đơn giản, thời gian thực thi rất nhanh.
- Priority: ➡️ Medium (ID: 17b09fd9)
- Priority Reason: Cần thiết để làm mốc tính toán khoảng cách ngày cho booking window ở issue đặt lịch tiếp theo.
- Start Date: TBD
- Target Date: TBD

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🟠 priority-medium

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/001-foundation-custom-data-structures.md, .agent/report/github-issue-drafts/002-persistence-file-storage.md
- Blocking: .agent/report/github-issue-drafts/007-booking-creation-validation.md, .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/simulation-time`

## Ghi chú cho người thực hiện
- Nên viết hàm kiểm tra định dạng ngày bằng biểu thức chính quy (Regex) hoặc parse thử để xác định tính hợp lệ của ngày trước khi lưu.
