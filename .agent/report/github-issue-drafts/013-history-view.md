# [History][FR-20] Implement Global and Customer History Reports

## Tóm tắt
Triển khai tính năng xem lịch sử rửa xe đã hoàn tất (FR-20) cho Admin (xem toàn bộ hoặc lọc theo khách hàng) và cho Khách hàng (chỉ xem lịch sử của chính mình).

## Source Trace
- PRD: docs/requirements/PRD.md (FR-20)
- SRS/Spec: docs/requirements/SRS.md (FR-20, UC-06, UC-16, Mục 3.12)

## Mục tiêu
Cung cấp báo cáo đối soát và kiểm tra lịch sử phục vụ của trạm rửa xe cho Admin và Khách hàng.

## Phạm vi
- Triển khai chức năng xem lịch sử rửa xe: hiển thị danh sách các booking ở trạng thái `COMPLETED` nạp từ `history.txt`.
- Nếu actor là Customer: tự động lọc và chỉ hiển thị lịch sử các lần rửa xe đã hoàn tất của riêng khách hàng đó (`currentCustomer`).
- Nếu actor là Admin/Nhân viên: cho phép xem toàn bộ lịch sử rửa xe của tiệm, hoặc lọc theo mã khách hàng cụ thể được nhập vào.
- Định dạng hiển thị các bản ghi: mã booking, biển số xe, tên dịch vụ, thời điểm hoàn tất, số tiền thanh toán, điểm loyalty tích lũy nhận được.

## Không nằm trong phạm vi
- Không cho phép cập nhật, sửa đổi hoặc xóa các dòng bản ghi lịch sử trong màn hình báo cáo này.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Lịch sử rửa xe chỉ ghi nhận các booking đã chuyển sang trạng thái `COMPLETED` thành công.
- Các booking bị hủy khi đang `SERVING` hoặc `WAITING` không được đưa vào lịch sử rửa xe hoàn tất.

## Grouping Justification
- **Grouped FRs**: Không gộp với FR khác. Đây là Issue độc lập giải quyết trọn vẹn yêu cầu nghiệp vụ xem lịch sử rửa xe (FR-20).

## Acceptance Criteria
- [ ] Khách hàng xem lịch sử chỉ thấy đúng các lượt rửa xe đã `COMPLETED` của chính mình.
- [ ] Admin xem được toàn bộ danh sách lịch sử rửa xe trong file `history.txt`.
- [ ] Admin có thể lọc lịch sử thành công theo mã khách hàng cụ thể.
- [ ] Hiển thị đầy đủ thông tin số tiền thanh toán và điểm tích lũy của từng lượt.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: S
- Story Points: 2
- Estimation Reason: Logic đọc dữ liệu từ file `history.txt` và hiển thị/lọc cơ bản trên giao diện CLI, độ phức tạp thấp.
- Priority: ➡️ Medium (ID: 17b09fd9)
- Priority Reason: Tính năng báo cáo thống kê phục vụ kiểm tra, có thể hoàn thành sau các luồng nghiệp vụ chính.
- Start Date: 2026-07-06
- Target Date: 2026-07-08

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🟠 priority-medium

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/011-completion-booking-completion.md
- Blocking: .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/history-reports`

## Ghi chú cho người thực hiện
- Nên thiết kế bảng hiển thị gọn gàng, hỗ trợ tính tổng doanh thu hiển thị ở cuối bảng lịch sử đối với Admin.
