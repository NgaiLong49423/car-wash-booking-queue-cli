# [Entities][FR-02] Implement Vehicle Owner Linkage and Search

## Tóm tắt
Triển khai tính năng quản lý xe của khách hàng bao gồm liên kết xe mới (biển số) với khách hàng hợp lệ, sinh mã xe tự động (FR-24), kiểm tra dữ liệu đầu vào (FR-26), xem danh sách xe và tìm kiếm xe.

## Source Trace
- PRD: docs/requirements/PRD.md (FR-02, FR-24, FR-26)
- SRS/Spec: docs/requirements/SRS.md (FR-02, FR-24, FR-26, UC-08, Ràng buộc 11.1)

## Mục tiêu
Thiết lập phương tiện cho khách hàng phục vụ kiểm tra sở hữu khi đặt lịch rửa xe.

## Phạm vi
- Tạo lớp thực thể `Vehicle` với các thuộc tính: mã xe (`id`), biển số xe (`licensePlate`), mã khách hàng sở hữu (`customerId`).
- Tự động sinh mã xe tăng dần theo định dạng `V001, V002...` khi thêm xe (Tích hợp FR-24).
- Triển khai chức năng liên kết xe mới với khách hàng bằng cách kiểm tra mã khách hàng xem có tồn tại trong hệ thống hay không.
- Kiểm tra tính hợp lệ của biển số xe (không rỗng, không trùng) (Tích hợp FR-26).
- Triển khai chức năng xem danh sách xe của một khách hàng cụ thể.
- Triển khai chức năng tìm kiếm xe theo biển số.

## Không nằm trong phạm vi
- Không hỗ trợ xóa xe trong issue này (giữ nguyên dữ liệu liên kết hoặc kiểm tra nếu cần mở rộng sau).
- Không tự động cập nhật hoặc thay đổi thông tin khách hàng sở hữu xe sau khi xe đã được thêm.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Biển số xe không được để trống và không được trùng lặp trong hệ thống.
- Khách hàng sở hữu xe phải tồn tại trước trong danh sách khách hàng.

## Grouping Justification
- **Grouped FRs**: FR-02 (Quản lý xe), một phần FR-24 (Auto ID cho Vehicle), và một phần FR-26 (Validate thông tin xe).
- **Lý do gộp**: Việc sinh mã xe tự động (FR-24) và kiểm tra định dạng/trùng lặp biển số xe (FR-26) là bắt buộc phải thực hiện trong khi tạo mới thực thể Vehicle. Việc gộp chung giúp tính năng Vehicle CRUD được hoàn thiện và kiểm thử độc lập.

## Acceptance Criteria
- [ ] Thêm xe mới sinh mã dạng `VXXX` tăng dần bắt đầu bằng `V001`.
- [ ] Từ chối liên kết xe nếu `customerId` không tìm thấy trong danh sách khách hàng hiện có.
- [ ] Từ chối thêm xe nếu biển số bị trùng (không phân biệt ký tự hoa thường và khoảng trắng).
- [ ] Hiển thị chính xác danh sách các xe thuộc về một khách hàng cụ thể theo mã khách hàng.
- [ ] Lưu thành công thông tin xe mới xuống file `vehicles.txt` ngay sau khi thêm.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: S
- Story Points: 2
- Estimation Reason: Logic CRUD cơ bản cho một thực thể phụ có khóa ngoại liên kết tới Customer, độ phức tạp lập trình thấp.
- Priority: ➡️ Medium (ID: 17b09fd9)
- Priority Reason: Phương tiện là thành phần bắt buộc phải chọn khi đặt lịch booking.
- Start Date: 2026-06-27
- Target Date: 2026-07-01

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🟠 priority-medium

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/001-foundation-custom-data-structures.md, .agent/report/github-issue-drafts/002-persistence-file-storage.md, .agent/report/github-issue-drafts/003-entities-customer-management.md
- Blocking: .agent/report/github-issue-drafts/007-booking-creation-validation.md, .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/vehicle-management`

## Ghi chú cho người thực hiện
- Sử dụng hàm kiểm tra regex biển số cơ bản nếu cần để hạn chế người dùng nhập ký tự rác.
- Đảm bảo khi in danh sách xe, in kèm thông tin tên chủ sở hữu để dễ kiểm chứng dữ liệu.
