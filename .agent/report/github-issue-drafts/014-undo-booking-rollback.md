# [Undo][FR-21] Implement Undo Last Completed Booking

## Tóm tắt
Triển khai chức năng hoàn tác (Undo) giao dịch hoàn tất booking gần nhất (FR-21) sử dụng cấu trúc dữ liệu `MyStack` (LIFO). Khôi phục booking về trạng thái `SERVING`, xóa dòng lịch sử tương ứng trong `history.txt`, tính lại loyalty cho khách hàng và hoàn trả booking bị kéo lên slot chính (nếu có) quay về Waitlist.

## Source Trace
- PRD: docs/requirements/PRD.md (FR-21)
- SRS/Spec: docs/requirements/SRS.md (FR-21, UC-17, Mục 3.13)

## Mục tiêu
Cho phép nhân viên nhanh chóng khôi phục trạng thái hoạt động của xe gần nhất khi lỡ tay bấm nhầm nút hoàn tất rửa xe.

## Phạm vi
- Triển khai cấu trúc ngăn xếp hoàn tác (`MyStack`) lưu giữ các đối tượng bản ghi hoàn tác. Mỗi bản ghi hoàn tác chứa thông tin: ID của booking vừa hoàn tất và ID của booking được kéo từ Waitlist lên Main Queue (nếu có).
- Khi Admin chọn chức năng hoàn tác (FR-21):
  - Kiểm tra ngăn xếp hoàn tác có dữ liệu hay không. Nếu rỗng, báo lỗi và từ chối.
  - Lấy bản ghi hoàn tác gần nhất ở đỉnh ngăn xếp (`MyStack.pop`).
  - Chuyển booking đã hoàn tất trong bản ghi đó từ `COMPLETED` về trạng thái trước đó là `SERVING`, giữ nguyên trạng thái thanh toán `PAID`.
  - Xóa dòng lịch sử tương ứng của booking vừa hoàn tác khỏi file `history.txt`.
  - Gọi chức năng tính toán lại loyalty của khách hàng liên quan dựa trên các booking còn ở trạng thái `COMPLETED` (FR-19).
  - Kiểm tra xem bản ghi hoàn tác có chứa booking được kéo từ Waitlist lên Main Queue trong bước hoàn tất trước đó hay không. Nếu có:
    - Loại bỏ booking được kéo lên đó khỏi Main Queue.
    - Trả booking đó về Waitlist và gán trạng thái booking tương ứng trong RAM.
    - Sắp xếp lại Waitlist theo Priority Queue dựa trên hạng thành viên và thời gian tạo booking.
- Cập nhật toàn bộ thay đổi dữ liệu xuống các file (`bookings.txt`, `customers.txt`, `history.txt`).

## Không nằm trong phạm vi
- Không cho phép hoàn tác tùy ý các booking cũ hơn trong lịch sử; chỉ cho phép hoàn tác duy nhất 1 booking hoàn tất gần nhất nằm ở đỉnh Stack.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Khi trả booking về Waitlist, không cần khôi phục vị trí cũ tuyệt đối mà đưa lại vào Priority Queue và để Max Heap tự sắp xếp theo độ ưu tiên hiện hành.
- Trạng thái thanh toán của booking được hoàn tác quay về `SERVING` vẫn được giữ nguyên là `PAID`.

## Grouping Justification
- **Grouped FRs**: Không gộp với FR khác. Đây là Issue độc lập giải quyết trọn vẹn yêu cầu nghiệp vụ hoàn tác booking hoàn tất gần nhất (FR-21).

## Acceptance Criteria
- [ ] Báo lỗi và từ chối nếu Admin chọn hoàn tác khi Stack hoàn tác rỗng.
- [ ] Hoàn tác thành công chuyển đổi booking hoàn tất gần nhất từ `COMPLETED` về lại trạng thái `SERVING`.
- [ ] Bản ghi lịch sử rửa xe tương ứng bị loại bỏ hoàn toàn khỏi file `history.txt`.
- [ ] Loyalty của khách hàng liên quan được tính toán lại chính xác từ danh sách các booking `COMPLETED` còn lại (Visit count giảm 1, tổng chi tiêu giảm, hạng thành viên có thể bị hạ nếu không còn đủ điều kiện).
- [ ] Booking bị kéo từ Waitlist lên Main Queue trước đó bị loại bỏ khỏi Main Queue và đưa trở lại Waitlist đúng thứ tự ưu tiên của Priority Queue.
- [ ] Lưu thay đổi dữ liệu xuống file thành công lập tức.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: M
- Story Points: 3
- Estimation Reason: Logic rollback phức tạp liên quan đến tính nhất quán của nhiều cấu trúc dữ liệu đồng thời (Stack, Queue, Priority Queue) và đồng bộ lại loyalty từ đầu.
- Priority: ⬆️ High (ID: 06f6c1b8)
- Priority Reason: Chức năng khôi phục sai sót quan trọng của hệ thống, giúp sửa đổi lỗi vận hành tức thời.
- Start Date: TBD
- Target Date: TBD

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🔴 priority-high

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/011-completion-booking-completion.md, .agent/report/github-issue-drafts/012-cancellation-booking-cancellation.md
- Blocking: .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/undo-booking`

## Ghi chú cho người thực hiện
- Đảm bảo in thông tin trạng thái sau khi undo thành công để thông báo cho Admin biết xe nào đã quay lại trạng thái `SERVING` và xe nào đã bị trả lại `Waitlist`.
