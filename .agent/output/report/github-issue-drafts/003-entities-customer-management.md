# [Entities][FR-01] Implement Customer Profile Management

## Tóm tắt
Triển khai chức năng quản lý khách hàng bao gồm xem danh sách, tìm kiếm, thêm mới với mã khách hàng sinh tự động (FR-24), cập nhật thông tin và xóa khách hàng kèm kiểm tra validate đầu vào (FR-26).

## Source Trace
- PRD: docs/requirements/PRD.md (FR-01, FR-24, FR-26)
- SRS/Spec: docs/requirements/SRS.md (FR-01, FR-24, FR-26, UC-07, Mục 3.3, Mục 3.11)

## Mục tiêu
Cho phép Admin cập nhật hồ sơ khách hàng và phục vụ làm cơ sở liên kết cho các thực thể Xe và Booking trong hệ thống.

## Phạm vi
- Tạo lớp thực thể `Customer` với các thuộc tính: mã khách hàng (`id`), họ tên (`name`), số điện thoại (`phoneNumber`), hạng thành viên (`tier`), điểm tích lũy (`loyaltyPoints`), tổng chi tiêu (`totalSpent`), số lần rửa xe (`visitCount`).
- Tự động sinh mã khách hàng tăng dần theo định dạng `C001, C002...` dựa trên ID lớn nhất hiện tại khi thêm mới (Tích hợp FR-24).
- Triển khai chức năng thêm khách hàng mới với hạng mặc định là `MEMBER`, các chỉ số tích lũy bằng 0, kèm kiểm tra số điện thoại không trùng và không để trống (Tích hợp FR-26).
- Triển khai chức năng cập nhật Họ tên và Số điện thoại của khách hàng theo ID.
- Triển khai chức năng tìm kiếm khách hàng bằng duyệt tuyến tính trên `MyLinkedList` theo mã khách hàng, tên hoặc số điện thoại.
- Triển khai chức năng xóa khách hàng theo ID kèm kiểm tra tính toàn vẹn dữ liệu.

## Không nằm trong phạm vi
- Không cho phép Admin hoặc người dùng tự nhập mã khách hàng tùy ý khi thêm mới.
- Admin không được phép chỉnh sửa thủ công hạng thành viên, điểm loyalty, tổng chi tiêu và số lần rửa của khách hàng. Các trường này chỉ được tính tự động.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Số điện thoại của khách hàng phải là duy nhất (không được trùng lặp).
- **Ràng buộc xóa**: Hệ thống phải từ chối xóa khách hàng nếu khách hàng đó đã có xe sở hữu, có booking (bất kể trạng thái) hoặc lịch sử rửa xe (`history.txt`) để bảo vệ tính nhất quán lịch sử.

## Grouping Justification
- **Grouped FRs**: FR-01 (Quản lý khách hàng), một phần FR-24 (Auto ID cho Customer), và một phần FR-26 (Validate thông tin khách hàng).
- **Lý do gộp**: Việc sinh mã khách hàng tự động (FR-24) và kiểm tra trùng lặp số điện thoại/họ tên không rỗng (FR-26) là một phần không thể tách rời của quy trình tạo mới hồ sơ khách hàng. Việc gộp chung giúp tính năng Customer CRUD được triển khai đầy đủ và kiểm thử độc lập dễ dàng hơn.

## Acceptance Criteria
- [ ] Thêm khách hàng mới thành công sinh mã dạng `CXXX` tăng dần (Ví dụ `C001`, `C002`).
- [ ] Từ chối thêm mới hoặc cập nhật nếu số điện thoại trùng với khách hàng khác đã có trong danh sách.
- [ ] Tìm kiếm khách hàng theo mã, tên (không phân biệt hoa thường) hoặc số điện thoại trả về chính xác danh sách khách hàng khớp.
- [ ] Từ chối xóa khách hàng có ID đã liên kết với ít nhất một xe trong `vehicles.txt`, hoặc một booking trong `bookings.txt`, hoặc một lượt sử dụng dịch vụ trong `history.txt`. Hiển thị thông báo lỗi rõ ràng.
- [ ] Cho phép xóa khách hàng nếu hoàn toàn không có liên kết nào; sau khi xóa, cập nhật file `customers.txt` lập tức.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: M
- Story Points: 3
- Estimation Reason: Cần triển khai các ràng buộc kiểm tra khóa ngoại chéo (ở Vehicles, Bookings và History) trước khi xóa, và kiểm tra tính duy nhất của số điện thoại trên danh sách động.
- Priority: ➡️ Medium (ID: 17b09fd9)
- Priority Reason: Cần thiết trước khi làm tính năng quản lý xe và đặt lịch booking.
- Start Date: 2026-06-27
- Target Date: 2026-07-01

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🟠 priority-medium

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/001-foundation-custom-data-structures.md, .agent/report/github-issue-drafts/002-persistence-file-storage.md
- Blocking: .agent/report/github-issue-drafts/004-entities-vehicle-management.md, .agent/report/github-issue-drafts/007-booking-creation-validation.md, .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/customer-management`

## Ghi chú cho người thực hiện
- Sử dụng phương thức so sánh chuỗi cẩn thận để tránh lỗi khoảng trắng đầu cuối (trim).
- Khi kiểm tra liên kết dữ liệu để xóa, duyệt qua các danh sách liên quan trong RAM và tìm kiếm theo `customerId`.
