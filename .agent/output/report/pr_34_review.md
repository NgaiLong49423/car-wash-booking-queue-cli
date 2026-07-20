<!-- antigravity-pr-review -->
Đây là kết quả review tự động cho PR #34, dựa trên diff mới nhất, Issue liên quan, tài liệu dự án và các kiểm tra hiện có.

### Bối cảnh review
- PR: feat(queue): implement queue monitoring views (#34)
- Base / head: `main` ← `feature/009-queue-view-monitoring`
- Issue liên kết: #9
- Đã đối chiếu: docs/requirements/PRD.md (FR-12, FR-13, FR-14), docs/requirements/SRS.md, Issue #9 và diff mới nhất
- CI: Không có cảnh báo lỗi build

### Đánh giá Acceptance Criteria
| Tiêu chí | Trạng thái | Bằng chứng |
|---|---|---|
| Hiển thị hàng chờ chính / chờ phụ (FR-12, FR-13) | Đạt | Đã tách riêng xem Main Queue và Waitlist. Thông tin in ra dạng bảng `printf` đầy đủ: Mã booking, Biển số xe, Dịch vụ, Hạng thành viên, Trạng thái. |
| In Waitlist đúng thứ tự ưu tiên (Max Heap) | Đạt | Hàm `snapshotInPriorityOrder()` trong `MyPriorityQueue` thực hiện xuất ra list bằng hàm `poll()` sau đó đẩy ngược lại `insert()` rất thông minh để giữ nguyên trạng thái Heap. |
| Xem booking của tôi (FR-14) | Đạt | Hàm `displayCustomerActiveBookings` lọc chuẩn xác điều kiện `WAITING` hoặc `SERVING` của riêng ID khách hàng. |
| Hiển thị sức chứa (Ví dụ: MORNING: 12/13 slots) | Đạt | Hàm `printCapacity` trong `BookingService` thực hiện chuẩn logic tính size của (Main Queue + Waitlist) trên tổng dung lượng của ca hiện tại. |

### ✅ Đã kiểm tra (Passed Checks)
- Không có lỗi Crash khi dữ liệu trống (`bookingQueue` rỗng).
- Các phương thức `snapshot()` của Queue và PriorityQueue đảm bảo truy vấn lấy dữ liệu để in (Read-only) mà không làm hỏng hay mất mát phần tử đang chờ phục vụ.
- Định dạng xuất dữ liệu ra Console (Table Format) rõ ràng, chuyên nghiệp.

### Findings (Các vấn đề phát hiện)

#### [Low] Dư thừa code (Dead Code / Scope Creep)
- File: `AutoWashQueueCLI/src/model/Booking.java`
- Line: 126, 160-174
- Evidence: Thay đổi class `Booking implements Comparable<Booking>` và thêm biến `priorityRank`.
- Problem: Hàng chờ ưu tiên của dự án hiện tại đang dùng lớp wrapper `WaitlistEntry` để xử lý Comparable mức độ ưu tiên. Thay đổi này trực tiếp trên `Booking` là code dư thừa (Dead code) không hề được dùng đến trong code hiển thị.
- Impact: Làm rối model Booking.
- Recommendation: Nên xóa bỏ implement Comparable và `priorityRank` trên class Booking để giữ code sạch (Clean code).
- Merge blocking: Không

#### [Nit] Vị trí Menu chưa tích hợp Customer Portal
- File: `AutoWashQueueCLI/src/app/Main.java`
- Line: 258-290
- Evidence: `displayCustomerActiveBookings` được gọi thông qua việc hỏi "Enter customer ID" ở SubMenu của Admin.
- Problem: Ở PR #29 trước đó, chúng ta đã có `Customer Portal` tự động lấy ID của khách hàng.
- Impact: Trải nghiệm khách hàng hơi thủ công do nhánh này chưa được đồng bộ với nhánh của PR 29.
- Recommendation: Sau khi merge PR này vào `main`, có thể di chuyển `case 4` (xem active booking) vào bên trong `Customer Portal` để khách hàng đăng nhập tự động.
- Merge blocking: Không

### Nguồn yêu cầu (Referenced Sources)
- Issue #9 — Acceptance Criteria: Xem hàng chờ buổi hiện tại hiển thị đầy đủ thông tin... Chức năng xem booking của tôi chỉ hiển thị... Hiển thị sức chứa dưới dạng phân số...
- PRD.md, Mục FR-12, FR-13, FR-14.

## Quyết định review
- Critical findings count: 0
- High findings count: 0
- Medium findings count: 0
- Low findings count: 1
- Nit count: 1
- Overall risk: Low
- Mandatory review decision:
- Kết quả: ĐỀ XUẤT SẴN SÀNG MERGE
- GitHub review state: COMMENT
- Lỗi chặn merge còn lại: 0
- Điều kiện tiếp theo: Có thể thực hiện Merge
- Quyền quyết định cuối cùng: Người duy trì repository

> Kết luận tự động: Pull Request này hiện không còn phát hiện chặn merge và có thể được người duy trì xem xét để merge. Đây không phải là phê duyệt chính thức. Một người có thẩm quyền vẫn phải review, approve và thực hiện merge.
