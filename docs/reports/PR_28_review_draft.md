# Request changes — PR #28

> **Agent Codex của Gia Long — Automatic Pull Request Review**
>
> Tôi xin phép đánh giá Pull Request này theo yêu cầu rà soát tự động. Review tập trung vào phần triển khai và tiêu chí hoàn thành của Issue #11, không đánh giá cá nhân tác giả.
>
> Nếu bạn có góc nhìn hoặc bằng chứng khác, bạn hoàn toàn có thể phản biện trực tiếp trong phần comment của Pull Request. Vui lòng kèm vị trí code, kết quả chạy thủ công, hoặc trích dẫn tài liệu yêu cầu cùng lập luận cụ thể để Gia Long và tôi có thể kiểm tra lại.

## Kết luận

**Request changes.** CI build thành công, nhưng PR chưa đáp ứng các yêu cầu bắt buộc của Issue #11. Các phần cần hoàn thiện trước khi merge là tính nhất quán dữ liệu khi complete, tính lại loyalty, điều phối Waitlist và Undo.

## Phạm vi đối chiếu

- [PR #28 — Add booking completion loyalty feature](https://github.com/NgaiLong49423/car-wash-booking-queue-cli/pull/28)
- [Issue #11 — Completion / FR-17 / FR-19](https://github.com/NgaiLong49423/car-wash-booking-queue-cli/issues/11)
- [SRS: quy tắc Complete, Waitlist, Loyalty và Undo](https://github.com/NgaiLong49423/car-wash-booking-queue-cli/blob/main/docs/requirements/SRS.md) — mục 3.9.1, 3.9.2, 3.11, 3.13.
- [PRD: Acceptance criteria](https://github.com/NgaiLong49423/car-wash-booking-queue-cli/blob/main/docs/requirements/PRD.md) — AC 11–13, 18–20.

## Các thay đổi cần thực hiện

| Mức độ | Hạng mục | Lý do |
|---|---|---|
| P0 | Khởi tạo/sửa mô hình `customerStack` | Luồng complete hiện có thể dừng với `NullPointerException`. |
| P0 | Tính lại loyalty từ booking `COMPLETED` | Points, total spent, visit count và tier chưa được tính theo SRS. |
| P0 | Bổ sung điều phối Waitlist theo thời lượng buổi | Một phần bắt buộc của FR-17 hiện chưa được triển khai. |
| P1 | Chỉ thực hiện side-effect khi complete thành công | Input sai hoặc trạng thái sai vẫn có thể tạo history/cập nhật customer. |
| P1 | Hoàn tác toàn bộ tác động của complete | Undo chưa khôi phục Waitlist hoặc loyalty đúng cách. |
| P1 | Sửa `dequeueNodeByID()` hoặc tách Cancel ra khỏi PR | Queue chỉ xóa đúng khi booking đứng đầu. |

---

## Nhận xét chi tiết

### P0 — `customerStack` chưa được khởi tạo

**Vị trí:** `AutoWashQueueCLI/src/service/CustomerService.java` — field `customerStack`, constructor `CustomerService()`, method `updatePoint()`.

PR khai báo `private MyStack<Customer> customerStack;`, nhưng constructor chỉ khởi tạo `customerList`. Khi complete hợp lệ, `updatePoint()` gọi `customerStack.clear()` và `customerStack.push(c)`, dẫn đến `NullPointerException`.

Booking có thể đã được đổi sang `COMPLETED` và lưu vào `bookings.txt`, trong khi customer/history chưa được cập nhật xong. Điều này làm dữ liệu không nhất quán.

**Đề nghị:** khởi tạo stack nếu vẫn sử dụng. Tuy nhiên, nên ưu tiên sửa mô hình Undo theo nhận xét P1 bên dưới thay vì lưu trực tiếp `Customer` mutable vào stack.

**Căn cứ:** SRS 3.9.1 yêu cầu complete phải đồng bộ booking, history, loyalty, Undo và file lưu trữ.

### P0 — Loyalty chưa tuân thủ công thức FR-19

**Vị trí:** `AutoWashQueueCLI/src/app/Main.java` — `case 6`; `AutoWashQueueCLI/src/service/CustomerService.java` — `updatePoint()`.

Luồng hiện tại tính `newPoint = price / 1000` và gán trực tiếp vào `points`. Code không duyệt toàn bộ booking `COMPLETED`, không cập nhật `totalSpent`, không cập nhật `visitCount`, và xét tier theo ngưỡng points `2000/6000/15000`.

Theo SRS, loyalty phải được tính lại như sau:

- `visitCount`: số booking `COMPLETED` của customer;
- `totalSpent`: tổng giá trị các booking `COMPLETED`;
- `loyaltyPoints = totalSpent / 1000`;
- tier cao nhất đạt theo `visitCount` **hoặc** `totalSpent`.

Ví dụ: customer có nhiều booking hoàn tất sẽ bị ghi đè points bằng points của riêng dịch vụ mới nhất, thay vì tổng points tích lũy.

**Đề nghị:** bổ sung `recalculateLoyalty(customerId, bookings, services)` để tính và lưu đủ bốn chỉ số sau Complete và Undo.

**Căn cứ:** Issue #11; SRS 3.3, 3.11; PRD AC 18–19.

### P0 — Chưa kiểm tra Waitlist theo thời lượng còn lại

**Vị trí:** `AutoWashQueueCLI/src/service/BookingService.java` và `AutoWashQueueCLI/src/app/Main.java`.

Diff chưa thao tác `MyPriorityQueue`, chưa tính `usedMinutes` từ booking `COMPLETED`, chưa tính `remainingMinutes` theo `MORNING`/`AFTERNOON`/`EVENING`, và chưa đưa booking ưu tiên cao nhất từ Waitlist vào cuối Main Queue.

Đây là yêu cầu bắt buộc của FR-17. Do đó, hai tiêu chí “kéo booking khi đủ thời gian” và “không bỏ qua booking ưu tiên cao nhất khi không đủ thời gian” của Issue #11 chưa đạt.

**Đề nghị:** sau Complete, chỉ `peek` booking ưu tiên cao nhất. Chỉ `poll` và `enqueue` cuối Main Queue khi `remainingMinutes >= serviceDuration`; nếu không đủ thời gian thì giữ nguyên Waitlist và không xét booking phía sau. Booking được kéo cần được lưu trong bản ghi Undo.

**Căn cứ:** Issue #11; SRS 3.9.1 bước 6, 3.9.2; PRD AC 12–13.

### P1 — `completeBooking()` có thể tiếp tục side-effect khi thao tác thất bại

**Vị trí:** `AutoWashQueueCLI/src/service/BookingService.java` — `completeBooking()`; `AutoWashQueueCLI/src/app/Main.java` — `case 6`.

`completeBooking()` trả về biến `b` sau vòng lặp ngay cả khi không tìm thấy ID hoặc booking chưa ở trạng thái `SERVING` + `PAID`. `Main` chỉ kiểm tra `b != null`, nên có thể tạo history và cập nhật customer cho một thao tác bị từ chối, hoặc cho booking cuối danh sách.

**Đề nghị:** trả về `null` hoặc kết quả thất bại rõ ràng cho mọi nhánh không thành công. Chỉ tạo history, tính loyalty, lưu Undo và save sau khi trạng thái thực sự chuyển sang `COMPLETED`.

**Căn cứ:** SRS 3.9.1; PRD AC 11.

### P1 — Undo chưa rollback toàn bộ tác động của Complete

**Vị trí:** `AutoWashQueueCLI/src/service/BookingService.java` — `undoCompletion()`; `AutoWashQueueCLI/src/service/CustomerService.java` — `undoCustomer()`.

Stack chỉ lưu `Booking` vừa complete, không lưu booking đã được kéo từ Waitlist. Vì vậy Undo không thể đưa booking đó từ Main Queue về Waitlist. Đồng thời, `undoCustomer()` chỉ gán `c = prevstate` cho biến cục bộ; đối tượng trong `customerList` không được khôi phục. Stack còn lưu cùng tham chiếu customer đã bị sửa, không phải snapshot trước thay đổi.

**Đề nghị:** dùng một bản ghi Undo chứa booking đã complete và booking được kéo từ Waitlist (nếu có). Khi Undo, khôi phục queue/waitlist rồi gọi `recalculateLoyalty()` thay vì khôi phục points thủ công.

**Căn cứ:** Issue #11; SRS 3.13; PRD AC 19.

### P1 — `dequeueNodeByID()` chỉ xóa được booking ở đầu queue

**Vị trí:** `AutoWashQueueCLI/src/datastructure/MyQueue.java` — `dequeueNodeByID()`.

Logic nối node kế tiếp đang nằm trong nhánh `if (current == front)`. Khi booking cần xóa ở giữa hoặc cuối queue, method duyệt hết nhưng không xóa node. Queue vì thế có thể không khớp trạng thái booking sau Cancel.

**Đề nghị:** xử lý riêng ba trường hợp đầu/giữa/cuối queue; giảm `size` đúng một lần; cập nhật `rear` khi cần. Vì Cancel không phải trọng tâm của Issue #11, cũng có thể tách phần này sang PR của Issue #18.

---

## Góp ý cải thiện chất lượng PR

### P2 — Loại bỏ cấu hình IDE và dữ liệu thay đổi không phục vụ feature

**Vị trí:** `AutoWashQueueCLI/nbproject/private/private.properties`, `AutoWashQueueCLI/nbproject/private/private.xml`, và `AutoWashQueueCLI/data/*`.

`nbproject/private/*` chứa đường dẫn máy cá nhân và không phục vụ tính năng. PR cũng thay đổi dữ liệu mẫu (`B003`, `C003`, `history.txt`) nhưng các thay đổi này không cần thiết để triển khai nghiệp vụ Complete/Loyalty.

**Đề nghị:** hoàn nguyên các file private IDE; chỉ giữ fixture dữ liệu nếu cần cho demo và mô tả rõ mục đích.

### P2 — Chuẩn hóa trạng thái và thông báo CLI

**Vị trí:** `BookingService.java`, `Main.java`.

SRS dùng trạng thái chuẩn `WAITING`, `SERVING`, `COMPLETED`, `CANCELLED`, trong khi PR ghi `Completed` và `Cancelled`. Nên lưu nhất quán theo định dạng đặc tả để dữ liệu dễ kiểm tra. SRS v3.0.3 cũng yêu cầu CLI hiển thị tiếng Anh, nhưng PR thêm một số thông báo tiếng Việt.

---

Sau khi các điểm P0/P1 được xử lý, tôi sẵn sàng kiểm tra lại Pull Request.
