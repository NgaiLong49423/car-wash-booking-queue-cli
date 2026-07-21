# [Logic][v4.0.0] Upgrade Business Logic to v4.0.0 (Time Capacity, Waitlist Loop, 365-day Loyalty)

## Tóm tắt
Nâng cấp hệ thống để đáp ứng phiên bản Business Logic v4.0.0. Các thay đổi bao gồm:
1. Nâng cấp kiểm tra sức chứa: bổ sung kiểm tra quỹ thời gian tổng của buổi.
2. Nâng cấp Waitlist Promotion: duyệt qua toàn bộ Priority Queue thay vì chỉ dừng lại ở phần tử đầu tiên nếu không đủ thời gian.
3. Nâng cấp tính toán Loyalty: chỉ tính toán dựa trên các booking `COMPLETED` trong vòng 365 ngày gần nhất.

## Source Trace
- PRD: docs/requirements/PRD.md (v2.0.0)
- SRS: docs/requirements/SRS.md (v4.0.0, FR-08, FR-09, FR-17, FR-19, Mục 3.1, 3.6, 3.7, 3.9, 3.11)

## Mục tiêu
Đảm bảo hệ thống điều phối booking chính xác theo ràng buộc thời gian thực tế, tối ưu năng suất Waitlist và thiết lập cơ chế hết hạn hạng thành viên thực tế.

## Phạm vi
- Cập nhật logic tạo booking (`FR-08`, `FR-09`):
  - Khi kiểm tra Waitlist hoặc slot chính, phải kiểm tra xem việc thêm booking này vào có làm vượt tổng quỹ thời gian của buổi (`periodTotalMinutes`) không.
- Cập nhật logic hoàn tất booking (`FR-17`):
  - Khi kéo xe từ Waitlist lên (Promotion), nếu xe ưu tiên cao nhất có `serviceDuration` dài hơn thời gian còn lại, hệ thống không bỏ cuộc mà tiếp tục xét các xe ưu tiên thấp hơn xem có xe nào vừa vặn để kéo lên không.
- Cập nhật logic tính toán Loyalty (`FR-19`):
  - Lọc danh sách booking `COMPLETED` của khách hàng, chỉ lấy các booking có ngày <= 365 ngày so với ngày hệ thống hiện tại.
  - Tính tổng chi tiêu, số lần rửa, điểm và hạng dựa trên danh sách đã lọc.

## Không nằm trong phạm vi
- Không viết chức năng chạy background job (batch) thực sự, việc tự động tính toán diễn ra ngay lúc cần hiển thị hoặc tính toán sau các tác vụ booking.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Tổng thời lượng phục vụ cố định của các buổi: Sáng (300 phút), Chiều (240 phút), Tối (180 phút).

## Acceptance Criteria
- [ ] Tính năng đặt lịch (Booking) từ chối thành công các booking làm vượt tổng quỹ thời gian của buổi, dù slot đếm số xe vẫn còn.
- [ ] Tính năng kéo Waitlist (Promotion) có thể bỏ qua xe ưu tiên 1 (nếu xe 1 yêu cầu quá nhiều thời gian) để kéo xe ưu tiên 2 (nếu xe 2 vừa vặn thời gian còn lại).
- [ ] Loyalty của khách hàng (Điểm, Hạng, Chi tiêu, Lần rửa) chỉ phản ánh các giao dịch hoàn tất trong vòng 365 ngày gần nhất so với ngày hệ thống hiện tại.
