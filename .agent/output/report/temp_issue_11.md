# [Completion][FR-17/19] Implement Booking Completion and Loyalty Recalculation

## Tóm tắt
Triển khai nghiệp vụ hoàn tất booking đang phục vụ (`SERVING` & `PAID` -> `COMPLETED` - FR-17), tự động ghi nhận lịch sử vào `history.txt`, tính toán lại loyalty và nâng hạng thành viên cho khách hàng (FR-19). Đồng thời, tính toán thời gian còn lại của buổi phục vụ để điều phối kéo xe ưu tiên cao nhất từ Waitlist lên Main Queue.

## Source Trace
- PRD: docs/requirements/PRD.md (FR-17, FR-19)
- SRS/Spec: docs/requirements/SRS.md (FR-17, FR-19, UC-15, Mục 3.3, Mục 3.9.1, Mục 3.9.2, Mục 3.11)

## Mục tiêu
Hoàn thành quy trình rửa xe khép kín, tự động hóa cập nhật quyền lợi tích lũy của khách hàng và tối ưu năng suất phục vụ của tiệm bằng cách bổ dung xe từ hàng chờ phụ nếu thời gian cho phép.

## Phạm vi
- Triển khai chức năng "Hoàn tất booking":
  - Kiểm tra xem có booking nào đang ở trạng thái `SERVING` và đã có trạng thái thanh toán `PAID` hay chưa. Nếu chưa thanh toán hoặc không có xe rửa, báo lỗi và từ chối.
  - Chuyển trạng thái booking sang `COMPLETED` (FR-17).
  - Tạo bản ghi lịch sử rửa xe và lưu vào file `history.txt` (gồm: mã booking, mã khách hàng, tên khách hàng, biển số xe, tên dịch vụ, thời điểm hoàn tất, số tiền thanh toán, điểm loyalty cộng thêm).
  - Đẩy bản ghi hoàn tác vào `MyStack` để phục vụ chức năng Undo. Bản ghi phải lưu thông tin booking vừa hoàn tất và mã booking được kéo từ Waitlist lên Main Queue (nếu có).
  - Triển khai hàm tính toán lại loyalty của khách hàng liên quan dựa trên các booking còn ở trạng thái `COMPLETED` (Tích hợp FR-19):
    - `visitCount` = số booking `COMPLETED` của khách hàng.
    - `totalSpent` = tổng giá dịch vụ của các booking `COMPLETED`.
    - `loyaltyPoints = totalSpent / 1000`.
    - `tier` = hạng thành viên cao nhất đạt điều kiện (Silver: $\ge 5$ lần rửa hoặc chi tiêu từ 2 triệu; Gold: $\ge 15$ lần rửa hoặc chi tiêu từ 6 triệu; Platinum: $\ge 30$ lần rửa hoặc chi tiêu từ 15 triệu).
    - Cập nhật thông tin khách hàng và ghi đè file `customers.txt`.
  - Triển khai logic kiểm tra Waitlist theo thời gian còn lại của buổi:
    - Tính tổng thời gian rửa xe đã sử dụng (`usedMinutes`) bằng tổng `serviceDuration` của các booking có trạng thái `COMPLETED`, `SERVING`, hoặc `WAITING` (chỉ tính xe `WAITING` đang nằm trong hàng chờ chính - Main Queue, không tính xe trong Waitlist) cùng ngày và cùng buổi phục vụ.
    - Tính thời gian còn lại của buổi phục vụ (`remainingMinutes = periodTotalMinutes - usedMinutes`).
    - Nếu Waitlist (MyPriorityQueue) không trống, lấy ra booking có độ ưu tiên cao nhất trong Waitlist để kiểm tra.
    - Nếu `remainingMinutes >= serviceDuration` của booking đó, rút booking ra khỏi Waitlist và xếp vào cuối hàng chờ chính (Main Queue). Đổi trạng thái booking thành `WAITING` (vị trí: Slot chính buổi hiện tại).
    - Nếu không đủ thời gian phục vụ, giữ nguyên Waitlist và không xét tiếp các booking phía sau.
- Ghi đè toàn bộ thay đổi dữ liệu xuống các file liên quan (`bookings.txt`, `history.txt`, `customers.txt`).

## Không nằm trong phạm vi
- Không tự ý cộng thêm hoặc bớt điểm loyalty thủ công độc lập; mọi thay đổi loyalty phải được tính toán lại từ danh sách các booking `COMPLETED`.
- Không hạ hạng thành viên theo chu kỳ thời gian định kỳ.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Tỷ lệ tích điểm: Mỗi 1,000 VND chi tiêu tích lũy được 1 điểm loyalty.
- Tổng thời lượng phục vụ cố định của các buổi:
  - Sáng (MORNING): 300 phút.
  - Chiều (AFTERNOON): 240 phút.
  - Tối (EVENING): 180 phút.
- Xét nâng hạng thành viên dựa trên Visit Count HOẶC Total Spent (đáp ứng một trong hai điều kiện).

## Grouping Justification
- **Grouped FRs**: FR-17 (Hoàn tất booking) và FR-19 (Tính toán lại loyalty).
- **Lý do gộp**: Tính toán lại điểm loyalty, tổng chi tiêu, visit count và xét hạng thành viên (FR-19) là tác vụ hệ quả (post-action side effect) bắt buộc và tự động sau khi một booking chuyển sang trạng thái hoàn tất `COMPLETED` (FR-17). Việc gộp chung đảm bảo loyalty luôn nhất quán với lịch sử rửa xe thực tế trong mọi tình huống.

## Acceptance Criteria
- [ ] Từ chối hoàn tất booking nếu xe đang rửa (`SERVING`) chưa được thanh toán (`paymentStatus != PAID`).
- [ ] Chuyển trạng thái booking thành `COMPLETED` thành công, lưu bản ghi mới vào file `history.txt`.
- [ ] Loyalty của khách hàng được tính toán lại chính xác (Ví dụ: khách hàng hoàn tất dịch vụ giá 100,000 VND, điểm loyalty tăng thêm 100 điểm, chi tiêu tăng thêm 100,000 VND).
- [ ] Hệ thống tự động nâng hạng thành viên cho khách hàng lên Silver/Gold/Platinum lập tức khi thỏa mãn điều kiện nâng hạng.
- [ ] Hệ thống tự động kéo booking có độ ưu tiên cao nhất từ Waitlist lên cuối Main Queue nếu thời gian còn lại của buổi rửa xe đủ đáp ứng `serviceDuration` của dịch vụ đó.
- [ ] Nếu thời gian còn lại không đủ đáp ứng booking ưu tiên cao nhất, hệ thống từ chối kéo thêm bất kỳ booking nào khác phía sau kể cả khi các booking sau có thời gian rửa ngắn hơn.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: L
- Story Points: 5
- Estimation Reason: Logic giao dịch nghiệp vụ cốt lõi có độ phức tạp cao, tích hợp nhiều thao tác tính toán lại loyalty, nâng hạng thành viên, tính thời lượng còn lại của buổi phục vụ và tương tác đẩy/kéo giữa các cấu trúc dữ liệu (Waitlist Max Heap, Main Queue FIFO, Undo Stack).
- Priority: ⬆️ High (ID: 06f6c1b8)
- Priority Reason: Đóng luồng phục vụ rửa xe, trực tiếp cập nhật các chỉ số doanh thu và loyalty cho khách hàng thân thiết.
- Start Date: TBD
- Target Date: TBD

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🔴 priority-high

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/010-process-service-processing.md
- Blocking: .agent/report/github-issue-drafts/013-history-view.md, .agent/report/github-issue-drafts/014-undo-booking-rollback.md, .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/booking-completion`

## Ghi chú cho người thực hiện
- Đảm bảo in rõ thông tin điểm số cũ/mới và hạng thành viên cũ/mới của khách hàng lên màn hình CLI sau khi hoàn tất dịch vụ để nhân viên đối soát.
