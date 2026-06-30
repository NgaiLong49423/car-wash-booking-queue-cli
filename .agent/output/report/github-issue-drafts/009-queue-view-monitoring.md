# [View][FR-12/13/14] Implement Queue Monitoring and Customer Bookings Views

## Tóm tắt
Triển khai các giao diện xem và theo dõi hàng chờ bao gồm: xem hàng chờ chính (FR-12), xem hàng chờ phụ (FR-13) của buổi hiện tại đang hoạt động, và xem danh sách các booking chưa kết thúc của riêng khách hàng (FR-14) trên giao diện CLI.

## Source Trace
- PRD: docs/requirements/PRD.md (FR-12, FR-13, FR-14)
- SRS/Spec: docs/requirements/SRS.md (FR-12, FR-13, FR-14, UC-04, UC-12, Mục 9.4)

## Mục tiêu
Cung cấp thông tin trực quan cho khách hàng và nhân viên về vị trí xếp hàng phục vụ, giúp theo dõi vận hành tiệm dễ dàng.

## Phạm vi
- Triển khai chức năng xem hàng chờ buổi hiện tại (đã kích hoạt):
  - Hiển thị danh sách xe đang nằm trong **Main Queue** theo thứ tự FIFO (duyệt từ đầu đến cuối hàng đợi - FR-12).
  - Hiển thị danh sách xe đang nằm trong **Waitlist** theo đúng thứ tự ưu tiên trong Max Heap (FR-13).
- Triển khai chức năng xem booking chưa kích hoạt (booking tương lai) của buổi chưa kích hoạt.
- Triển khai chức năng xem booking cá nhân của khách hàng: chỉ lọc các booking đang ở trạng thái `WAITING` hoặc `SERVING` của `currentCustomer` (FR-14).

## Không nằm trong phạm vi
- Không cho phép chỉnh sửa trạng thái booking hay thông tin thanh toán trong các màn hình xem/tra cứu này.
- Không in lịch sử rửa xe đã hoàn tất (đã có issue xem lịch sử riêng).

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Chỉ hiển thị các booking chưa kết thúc (`WAITING` và `SERVING`) trong chức năng xem booking của khách hàng.
- Sắp xếp hiển thị của Waitlist phải tuân thủ chính xác mức độ ưu tiên của Max Heap (hạng thành viên cao hơn đứng trước, cùng hạng đặt trước đứng trước).

## Grouping Justification
- **Grouped FRs**: FR-12 (Xem hàng chờ chính), FR-13 (Xem hàng chờ phụ), và FR-14 (Xem booking của khách hàng).
- **Lý do gộp**: Cả 3 yêu cầu này đều thuộc nhóm chức năng truy vấn dữ liệu đọc (Read-only queries) phục vụ hiển thị trạng thái xếp hàng hiện tại. Việc gộp chúng giúp lập trình viên triển khai tập trung các lớp tiện ích vẽ bảng (render GUI console) và giảm thiểu trùng lặp code hiển thị.

## Acceptance Criteria
- [ ] Xem hàng chờ buổi hiện tại hiển thị đầy đủ thông tin: Mã booking, Biển số xe, Tên dịch vụ, Hạng thành viên, Trạng thái booking, và phân loại rõ ràng mục "Slot chính" và mục "Hàng chờ phụ".
- [ ] Chức năng xem booking của tôi chỉ hiển thị đúng các booking `WAITING` hoặc `SERVING` của riêng khách hàng đang đăng nhập bằng ID.
- [ ] Hiển thị sức chứa dưới dạng phân số "số chỗ đã dùng / tổng sức chứa" (Ví dụ: `MORNING: 12/13 slots`).

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: S
- Story Points: 2
- Estimation Reason: Tập trung chủ yếu vào logic lọc và định dạng văn bản hiển thị dữ liệu từ RAM/file. Không thay đổi trạng thái nghiệp vụ nên rủi ro thấp.
- Priority: ➡️ Medium (ID: 17b09fd9)
- Priority Reason: Giúp người dùng và nhà phát triển kiểm chứng trực quan kết quả phân bổ hàng chờ của kích hoạt buổi ở issue trước.
- Start Date: 2026-07-02
- Target Date: 2026-07-05

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🟠 priority-medium

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/008-queue-period-activation.md
- Blocking: .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/queue-history-views`

## Ghi chú cho người thực hiện
- Sử dụng căn lề chữ (printf định dạng bảng) trong CLI để thông tin hàng chờ hiển thị đẹp mắt và chuyên nghiệp.
- Khi in Waitlist, lưu ý in đúng thứ tự ưu tiên của Max Heap (có thể lấy bản sao của Heap rồi thực hiện poll liên tiếp để hiển thị đúng thứ tự ưu tiên từ cao xuống thấp).
