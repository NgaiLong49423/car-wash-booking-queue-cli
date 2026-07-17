# Request changes — PR #29

> **Agent Codex của Gia Long — Automatic Pull Request Review**
>
> Tôi xin phép đánh giá Pull Request này theo yêu cầu rà soát tự động. Review tập trung vào phần triển khai và tiêu chí hoàn thành của Issue #13, không đánh giá cá nhân tác giả.
>
> Nếu bạn có góc nhìn hoặc bằng chứng khác, bạn hoàn toàn có thể phản biện trực tiếp trong phần comment của Pull Request. Vui lòng kèm vị trí code, kết quả chạy thủ công, hoặc trích dẫn tài liệu yêu cầu cùng lập luận cụ thể để Gia Long và tôi có thể kiểm tra lại.

## Kết luận

**Request changes.** Phần báo cáo toàn cục và lọc theo mã khách hàng đã đáp ứng luồng Admin của FR-20/UC-16, và CI build đang xanh. Tuy nhiên, PR chưa triển khai luồng Customer chỉ xem lịch sử của chính mình; đồng thời dữ liệu mẫu mới tạo ra các bản ghi history không tương ứng với booking `COMPLETED`. Hai điểm này khiến Issue #13 chưa đạt đầy đủ và có nguy cơ làm sai tính toàn vẹn dữ liệu sau khi merge.

## Phạm vi đối chiếu

- [PR #29](https://github.com/NgaiLong49423/car-wash-booking-queue-cli/pull/29)
- [Issue #13](https://github.com/NgaiLong49423/car-wash-booking-queue-cli/issues/13)
- [SRS — FR-20, UC-06, UC-16](https://github.com/NgaiLong49423/car-wash-booking-queue-cli/blob/main/docs/requirements/SRS.md)
- [PRD — FR-20 và quy tắc history](https://github.com/NgaiLong49423/car-wash-booking-queue-cli/blob/main/docs/requirements/PRD.md)
- [Quy tắc đóng góp](https://github.com/NgaiLong49423/car-wash-booking-queue-cli/blob/main/CONTRIBUTING.md)

## Các thay đổi cần thực hiện

| Mức độ | Hạng mục | Lý do |
|---|---|---|
| P0 | Đồng bộ lại dữ liệu `history.txt` với các booking đã `COMPLETED` | PR tạo history cho `B001` và `B002` trong khi dữ liệu booking tại base của PR đang là `WAITING`; `B003` cũng không có booking tương ứng. Đây là dữ liệu lịch sử không hợp lệ và có thể gây trùng/sai đối soát khi luồng complete được tích hợp. |
| P1 | Triển khai luồng Customer History dựa trên `currentCustomer` | Issue #13 và UC-06 yêu cầu Customer tự động chỉ xem history của chính mình. PR hiện chỉ yêu cầu nhập Customer ID tùy ý từ menu chung. |

---

## Nhận xét chi tiết

### P0 — Seed history không phản ánh booking đã hoàn tất

**Vị trí:** `AutoWashQueueCLI/data/history.txt`.

PR thay nội dung file bằng các history cho `B001`, `B002`, `B003`. Tuy nhiên, tại commit base của PR, `bookings.txt` có `B001` và `B002` ở trạng thái `WAITING`, còn không có `B003`. Vì vậy, dữ liệu mới không thể hiện “các booking đã hoàn tất” như FR-20 yêu cầu.

SRS UC-16 nêu rõ chỉ hiển thị booking `COMPLETED`; PRD cũng quy định Complete mới ghi history, còn booking bị hủy/không hoàn tất không được ghi history. Giữ mock data này sau khi merge sẽ làm báo cáo có giao dịch không có thật, đồng thời có thể tạo bản ghi trùng khi Issue completion sau này ghi history cho cùng Booking ID.

**Đề nghị:** Không commit history giả vào dữ liệu chạy chung, hoặc bổ sung một bộ seed nhất quán: mỗi bản ghi history phải có booking tương ứng ở trạng thái `COMPLETED` và dữ liệu customer/service/loyalty khớp nhau. Nếu cần fixture tạm thời cho demo, hãy tách rõ fixture đó khỏi dữ liệu mặc định của ứng dụng.

### P1 — Chưa có lịch sử tự giới hạn theo Customer đang đăng nhập

**Vị trí:** `AutoWashQueueCLI/src/app/Main.java` — submenu `HISTORY REPORTS`; `AutoWashQueueCLI/src/service/HistoryService.java` — `displayCustomerHistory(...)`.

`displayCustomerHistory(...)` lọc đúng theo Customer ID được truyền vào, nhưng `Main` lấy ID bằng `ConsoleInputter.getStr("Enter customer ID")`. Nhánh này nằm trong menu quản trị chung và không có `currentCustomer` hay Customer Menu. Do đó, người dùng ở vai Customer không có luồng “Xem lịch sử của tôi”, và nếu được dùng submenu này thì vẫn có thể nhập ID của người khác.

Điều này chưa đạt acceptance criterion của Issue #13: Customer chỉ thấy các lượt `COMPLETED` của chính mình. Nó cũng trái với SRS UC-06: hệ thống phải lấy ID của `currentCustomer`, sau đó tự lọc history của khách đó.

**Đề nghị:** Khi luồng Customer Menu/currentCustomer đã sẵn sàng, thêm mục “View my wash history” gọi hàm lọc bằng ID của `currentCustomer` mà không hỏi nhập ID. Giữ việc nhập Customer ID chỉ cho nhánh Admin theo UC-16. Nếu PR này thật sự bị block bởi Issue tích hợp menu, cần thu hẹp mô tả/scope của PR thành chỉ UC-16 và tạo follow-up rõ ràng cho UC-06.

---

## Góp ý cải thiện chất lượng PR

### P2 — Tiêu đề PR chưa theo Conventional Commits

`CONTRIBUTING.md` yêu cầu tiêu đề PR cùng chuẩn với commit message. Tiêu đề hiện tại `[History][FR-20] Implement Global and Customer History Reports` không theo dạng `<type>(<scope>): <description>`.

**Đề nghị:** Đổi thành `feat(history): implement global and customer history reports`.

### P2 — Cập nhật tài liệu/changelog chưa được thể hiện

PR checklist đánh dấu đã cập nhật documentation nếu cần, nhưng diff không có thay đổi tài liệu hoặc `CHANGELOG.md`. Đây là tính năng/menu mới, trong khi `CONTRIBUTING.md` yêu cầu cập nhật tài liệu khi thay đổi usage hoặc hành vi dự án.

**Đề nghị:** Cập nhật `CHANGELOG.md` và, nếu menu sử dụng được xem là tài liệu người dùng, bổ sung hướng dẫn menu tương ứng. Nếu quyết định không cần cập nhật, bỏ dấu tích trong PR checklist để phản ánh đúng bằng chứng hiện có.

### P3 — Cân nhắc tổng doanh thu trong báo cáo toàn cục

Issue #13 ghi chú nên hiển thị tổng doanh thu ở cuối bảng history cho Admin. Đây không phải acceptance criterion bắt buộc, nên không chặn merge; tuy nhiên nó phù hợp với mục tiêu đối soát doanh thu của FR-20.

---

## Điều kiện để review lại

Vui lòng cập nhật PR để: (1) sửa hoặc tách fixture history không hợp lệ, và (2) triển khai/điều chỉnh scope rõ ràng cho luồng Customer theo `currentCustomer`. Sau đó cung cấp kết quả chạy tay cho: lịch sử rỗng, global history, lọc Admin có/không có dữ liệu, và Customer chỉ thấy đúng dữ liệu của mình.
