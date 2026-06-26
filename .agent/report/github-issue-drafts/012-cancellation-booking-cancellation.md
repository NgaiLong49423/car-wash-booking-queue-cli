# [Cancellation][FR-18] Implement Booking Cancellation and Queue Promotion

## Tóm tắt
Triển khai nghiệp vụ hủy booking hợp lệ cho Khách hàng (hủy booking WAITING của chính mình) và Admin (hủy booking WAITING hoặc SERVING bất kỳ). Tự động điều phối đẩy booking ưu tiên cao nhất từ Waitlist lên Main Queue để lấp đầy slot trống khi có hủy booking trong Main Queue.

## Source Trace
- PRD: docs/requirements/PRD.md (FR-18)
- SRS/Spec: docs/requirements/SRS.md (FR-18, UC-05, Mục 3.10)

## Mục tiêu
Cho phép khách hàng giải phóng lịch hẹn khi thay đổi kế hoạch và Admin giải phóng slot đang rửa xe, đồng thời tự động hóa tối ưu công suất hàng chờ của tiệm.

## Phạm vi
- Triển khai kiểm tra quyền hủy booking:
  - Nếu actor là Customer: chỉ được phép hủy booking của chính mình (`currentCustomer`) khi đang ở trạng thái `WAITING`.
  - Nếu actor là Admin/Nhân viên: cho phép hủy booking bất kỳ đang ở trạng thái `WAITING` hoặc `SERVING`.
- Triển khai logic hủy booking (FR-18):
  - Chuyển trạng thái booking sang `CANCELLED`.
  - Nếu booking bị hủy nằm trong Main Queue của buổi hiện tại: loại bỏ booking ra khỏi Main Queue. Ngay lập tức kiểm tra hàng chờ phụ (Waitlist), nếu có khách đang chờ, lấy booking ưu tiên cao nhất ra khỏi Waitlist (MyPriorityQueue.poll) đưa vào cuối hàng chờ chính (MyQueue.enqueue).
  - Nếu booking bị hủy nằm trong Waitlist hoặc booking tương lai: loại bỏ booking ra khỏi danh sách chờ tương ứng.
  - Nếu booking bị hủy đang ở trạng thái `SERVING`: giải phóng vị trí rửa xe, không ghi history, không cộng loyalty. Nếu booking `SERVING` đã có trạng thái `paymentStatus = PAID`, giữ nguyên trạng thái thanh toán hiện tại, không hoàn tiền. Thực hiện kiểm tra Waitlist theo quy tắc thời gian còn lại của buổi để kéo booking ưu tiên cao nhất từ Waitlist lên cuối Main Queue nếu đủ điều kiện.
- Ghi thay đổi trạng thái mới xuống file `bookings.txt`.

## Không nằm trong phạm vi
- Không cho phép hủy booking đã có trạng thái hoàn tất `COMPLETED`.
- Không xử lý nghiệp vụ đối soát tài chính hay hoàn trả tiền thực tế.
- Hủy booking `SERVING` không tự động chuyển booking khác sang trạng thái `SERVING`.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Xe trong Waitlist khi được kéo lên Main Queue sẽ tuân thủ nguyên tắc FIFO.
- Logic kiểm tra thời gian còn lại của buổi phục vụ áp dụng tương tự như khi complete booking khi hủy xe đang rửa (`SERVING`).

## Grouping Justification
- **Grouped FRs**: Không gộp với FR khác. Đây là Issue độc lập giải quyết trọn vẹn yêu cầu nghiệp vụ hủy booking (FR-18).

## Acceptance Criteria
- [ ] Khách hàng chỉ hủy được booking của chính mình khi trạng thái là `WAITING`. Báo lỗi nếu cố hủy booking của người khác hoặc booking `SERVING`/`COMPLETED`.
- [ ] Admin hủy được booking bất kỳ ở trạng thái `WAITING` hoặc `SERVING`.
- [ ] Từ chối hủy booking đã `COMPLETED` cho cả Customer và Admin.
- [ ] Khi hủy booking trong Main Queue, hệ thống tự động kéo booking ưu tiên cao nhất từ Waitlist (nếu có) vào cuối Main Queue.
- [ ] Khi Admin hủy booking đang `SERVING` đã `PAID`, booking chuyển sang `CANCELLED`, trạng thái thanh toán giữ nguyên là `PAID`, giải phóng vị trí rửa và thực hiện tính toán thời gian còn lại của buổi để kéo thêm xe từ Waitlist nếu đủ điều kiện.
- [ ] Cập nhật file `bookings.txt` thành công ngay sau khi hủy.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: M
- Story Points: 3
- Estimation Reason: Cần xử lý logic loại bỏ phần tử ở giữa hàng chờ chính (MyQueue) và điều phối kéo xe từ Waitlist (Priority Queue Max Heap) vào cuối Queue chính trong các trường hợp hủy khác nhau.
- Priority: ⬆️ High (ID: 06f6c1b8)
- Priority Reason: Đảm bảo hàng chờ hoạt động đúng khi có biến động khách hàng hủy lịch hẹn.
- Start Date: TBD
- Target Date: TBD

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🔴 priority-high

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/007-booking-creation-validation.md, .agent/report/github-issue-drafts/008-queue-period-activation.md
- Blocking: .agent/report/github-issue-drafts/014-undo-booking-rollback.md, .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/booking-cancellation`

## Ghi chú cho người thực hiện
- In thông báo rõ ràng cho Admin biết khách hàng nào trong Waitlist vừa được tự động đẩy lên slot chính sau khi hủy thành công.
