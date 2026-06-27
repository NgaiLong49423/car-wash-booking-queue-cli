# [Entities][FR-03] Implement Service Configuration with Selection Sort

## Tóm tắt
Triển khai tính năng quản lý dịch vụ rửa xe bao gồm xem danh sách dịch vụ, tìm kiếm dịch vụ, thêm dịch vụ mới với mã dịch vụ tự sinh (FR-24), sắp xếp danh sách dịch vụ theo giá hoặc thời gian thực hiện bằng thuật toán sắp xếp tự viết, và kiểm tra tính hợp lệ của dữ liệu dịch vụ (FR-26).

## Source Trace
- PRD: docs/requirements/PRD.md (FR-03, FR-24, FR-26)
- SRS/Spec: docs/requirements/SRS.md (FR-03, FR-24, FR-26, UC-09, Mục 7.7)

## Mục tiêu
Cung cấp bảng giá dịch vụ và thời gian thi công cho tiệm rửa xe, cho phép khách hàng/nhân viên tra cứu và sắp xếp dịch vụ trực quan.

## Phạm vi
- Tạo lớp thực thể `Service` với các thuộc tính: mã dịch vụ (`id`), tên dịch vụ (`name`), giá tiền (`price`), thời gian thực hiện (`duration` - phút), trạng thái hoạt động (`status`).
- Tự động sinh mã dịch vụ tăng dần theo định dạng `S001, S002...` khi thêm mới (Tích hợp FR-24).
- Triển khai chức năng thêm dịch vụ mới với kiểm tra giá tiền và thời gian thi công phải lớn hơn 0, tên dịch vụ không rỗng (Tích hợp FR-26).
- Triển khai thuật toán sắp xếp chọn (**Selection Sort**) tự viết để sắp xếp danh sách hiển thị dịch vụ theo giá hoặc thời gian thực hiện (tăng dần hoặc giảm dần) trên LinkedList mà không dùng thư viện Java.

## Không nằm trong phạm vi
- Không sử dụng phương thức `Collections.sort()` hoặc `Arrays.sort()` có sẵn của Java.
- Không thay đổi trạng thái hoạt động của dịch vụ trong phạm vi issue này.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Giá tiền dịch vụ và thời gian rửa phải lớn hơn 0.
- Tên dịch vụ không được để trống.

## Grouping Justification
- **Grouped FRs**: FR-03 (Quản lý dịch vụ), một phần FR-24 (Auto ID cho Service), và một phần FR-26 (Validate thông tin dịch vụ).
- **Lý do gộp**: Mã ID tự động sinh (FR-24) và kiểm tra giá/thời gian lớn hơn 0 (FR-26) là điều kiện cần để tạo mới dịch vụ hợp lệ. Thuật toán sắp xếp hiển thị thuộc về tính năng xem danh sách dịch vụ nên được đưa vào cùng workflow để dễ kiểm thử.

## Acceptance Criteria
- [ ] Thêm dịch vụ mới thành công sinh mã `SXXX` tăng dần.
- [ ] Từ chối thêm dịch vụ nếu giá tiền $\le 0$ hoặc thời gian thực hiện $\le 0$.
- [ ] Chức năng sắp xếp hiển thị danh sách dịch vụ hoạt động đúng: giá tăng dần/giảm dần, thời gian tăng dần/giảm dần.
- [ ] Sắp xếp được viết bằng thuật toán Selection Sort (hoặc Insertion Sort) tự cài đặt, không gọi thư viện JDK.
- [ ] Lưu dịch vụ mới xuống file `services.txt` ngay lập tức.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: S
- Story Points: 2
- Estimation Reason: CRUD dịch vụ đơn giản kết hợp viết thuật toán sắp xếp cơ bản trên danh sách liên kết đơn tự định nghĩa.
- Priority: ➡️ Medium (ID: 17b09fd9)
- Priority Reason: Cần thiết để lựa chọn dịch vụ khi tạo booking.
- Start Date: 2026-06-27
- Target Date: 2026-07-01

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
`feature/service-management`

## Ghi chú cho người thực hiện
- Chú ý logic hoán đổi phần tử khi viết Selection Sort trên LinkedList để tránh mất liên kết node.
