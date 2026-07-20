# BÁO CÁO ĐỐI CHIẾU ISSUE, SRS, PRD VÀ CODE HIỆN TẠI

| Thuộc tính | Giá trị |
|---|---|
| Repository | `NgaiLong49423/car-wash-booking-queue-cli` |
| Nhánh/commit được kiểm tra | `origin/main` tại `01b976a2ebc1575809c7b3a0d51160a98d7ed8e2` |
| SRS | v3.0.4, cập nhật 2026-07-17 |
| PRD | v1.0.1, cập nhật 2026-07-17 |
| Ngày kiểm tra | 2026-07-18 |
| Phạm vi | Issue đã đóng, issue/PR liên quan, toàn bộ source Java, dữ liệu seed, README, CHANGELOG và quy trình Git |

## 1. Kết luận điều hành

Dự án hiện biên dịch được với Java 8 nhưng chưa đáp ứng đầy đủ SRS v3.0.4, PRD v1.0.1 và trạng thái issue trên GitHub.

- Issue #3, #4 và #5 đạt tương đối đầy đủ.
- Issue #1, #2, #6, #9 và #11 chỉ đạt một phần hoặc có lỗi tích hợp.
- Issue #7 đã đóng nhưng chức năng tạo booking hiện không còn trong ứng dụng.
- Issue #12 vẫn mở dù code service và CHANGELOG đã ghi nhận cancellation.
- README đánh dấu hoàn thành nhiều chức năng chưa tồn tại hoặc chưa thể sử dụng từ CLI.
- Không có file test nào được track trên `main`.
- Các lỗi chặn luồng chính là regression của Issue #7, Queue/Waitlist không được tái tạo đúng, activation bị trộn với simulation time, thiếu processing/payment và cấu trúc menu sai SRS.

Kết luận cuối: `main` phù hợp để tiếp tục phát triển nhưng chưa đủ điều kiện nghiệm thu bản hoàn chỉnh theo SRS/PRD.

## 2. Phát hiện nghiêm trọng

### 2.1. Critical - Issue #7 đã đóng nhưng tạo booking đã biến mất

SRS yêu cầu Customer và Admin tạo booking với các kiểm tra customer, vehicle ownership, service, booking window, period, capacity và tự sinh ID `BXXX`.

Code hiện tại không còn `createBooking(...)` và menu không có lựa chọn tạo booking. `BookingService` chỉ còn phương thức legacy `addBooking(...)` với các vấn đề:

- Booking ID do caller truyền vào, không tự sinh.
- Customer bị hard-code thành `C000`.
- Ngày bị hard-code thành `2026-07-10`.
- Period bị hard-code thành `MORNING`.
- Không kiểm tra ownership, booking window, capacity hay activation.
- Luôn enqueue vào Main Queue.

Lịch sử Git cho thấy `createBooking` từng xuất hiện trong commit `b6391f7`, nhưng không còn sau lần đồng bộ tại commit `b9dab38`. Đây là integration regression. Issue #7 cần được mở lại.

### 2.2. High - Queue và Waitlist được xây dựng sai

Khi khởi động, `Main.java` đưa tất cả booking `WAITING` của ngày/buổi hiện tại vào Main Queue:

- Không giới hạn 10 slot cho MORNING/AFTERNOON hoặc 5 slot cho EVENING.
- Không đưa phần dư vào Waitlist.
- Không sắp theo tier và created time khi tái tạo.
- Không tái tạo Waitlist từ dữ liệu file.
- `addToWaitlist(...)` tồn tại nhưng không có luồng ứng dụng bình thường gọi đến.

Hậu quả:

- Waitlist thường luôn rỗng sau khi chạy app.
- Promotion trong Completion/Cancellation không có dữ liệu thực để hoạt động.
- Queue monitoring có thể hiển thị dữ liệu sai sức chứa.
- Công thức remaining time đúng ở service nhưng dữ liệu Main Queue mà công thức dựa vào có thể sai.

### 2.3. High - Đổi period nhưng Queue vẫn giữ dữ liệu period cũ

Kiểm tra thủ công đã tái hiện:

1. Khởi động ở MORNING với B001/B002 trong Main Queue.
2. Đổi current period sang AFTERNOON.
3. Mở Main Queue.
4. Ứng dụng hiển thị `AFTERNOON: 2/13 slots` nhưng danh sách vẫn là B001/B002 của MORNING.

Queue chỉ được tạo một lần lúc startup và không được rebuild khi ngày/buổi mô phỏng thay đổi.

### 2.4. High - Current period và ACTIVATED bị gộp nhầm

SRS phân biệt:

- FR-04: thiết lập `currentDate/currentPeriod`.
- FR-10/11: kích hoạt buổi, ngăn kích hoạt trùng và phân bổ booking.

`SimulationService.setCurrentPeriod()` hiện đặt period được chọn thành `ACTIVATED` và các period khác thành `NOT_ACTIVATED`. Hàm không chạy giải thuật phân bổ booking và vẫn cho chọn qua lại. Điều này trộn Issue #6 với Issue #8 và làm sai trạng thái activation.

### 2.5. High - Luồng booking end-to-end không thể chạy

Luồng SRS:

```text
Create booking
-> Activate period
-> Main Queue/Waitlist
-> Process next booking
-> SERVING
-> Confirm payment
-> PAID
-> Complete
-> History + Loyalty + Waitlist promotion
```

CLI hiện không có:

- Create booking.
- Activate period đúng nghĩa.
- Process next booking trong menu.
- Confirm payment.
- Customer cancellation.
- History view.
- Undo.

`processNextBooking()` có trong service nhưng không được gọi và không kiểm tra đã có booking `SERVING`. Không có phương thức `confirmPayment`. Do đó Issue #11 đúng ở cấp service nhưng chưa chạy được từ đầu đến cuối.

## 3. Persistence và seed data

### 3.1. High - File rỗng không kích hoạt seed

`FileManager.loadData()` đặt `hasData = true` khi file tồn tại, kể cả file rỗng. Kiểm tra thực tế với ba file core rỗng cho kết quả app báo load thành công và sau khi thoát cả sáu file vẫn 0 byte. Điều này vi phạm Issue #2 và FR-22/25.

### 3.2. Medium - Tên dịch vụ seed sai yêu cầu

SRS và Issue #2 yêu cầu:

- `Basic Wash`
- `Premium Wash`
- `Interior Cleaning`

DataSeeder hiện tạo:

- `Standard Wash`
- `Premium Wash + Vacuum`
- `Wash + Polish`

### 3.3. High - Dữ liệu loyalty và history không nhất quán

Trên `origin/main`:

- C001 là PLATINUM, 100 points, 5 visits.
- C002 là GOLD, 50 points, 3 visits.
- C003 là SILVER, 20 points, 1 visit.
- Không có booking nào ở trạng thái `COMPLETED`.
- `history.txt` chứa B003 nhưng `bookings.txt` không có B003.

SRS yêu cầu loyalty được tính từ toàn bộ booking `COMPLETED`, vì vậy seed hiện vi phạm toàn vẹn dữ liệu. B003 trong history là orphan record.

### 3.4. Medium - Kết quả lưu file không được phản ánh chính xác

Các hàm ghi file nuốt exception và chỉ in lỗi, trong khi `saveData()` vẫn luôn in thông báo tất cả dữ liệu đã được lưu an toàn. Caller không nhận được trạng thái success/failure thực tế.

## 4. Đánh giá từng issue đã đóng

| Issue | Kết quả | Nhận xét |
|---|---|---|
| #1 Custom Data Structures | Một phần | Các cấu trúc tồn tại và không import Java Collection cho logic chính. Tuy nhiên booking-window lookup chưa được cấu hình/sử dụng; comment MyMap còn ghi 1/3/5/7 thay vì 7/10/12/14. |
| #2 Persistence/Seed | Không đạt đầy đủ | Đọc/ghi `|` có, nhưng file rỗng không seed, tên service sai và seed không nhất quán. |
| #3 Customer Management | Đạt | Auto ID, phone uniqueness, search, dependency checks và autosave có. |
| #4 Vehicle Management | Đạt | Auto ID, ownership, duplicate plate, lookup và autosave có. |
| #5 Service Management | Đạt | Validation, auto ID và Selection Sort tự cài đặt có. |
| #6 Simulation Time | Một phần | Validate và persist có, nhưng current period bị gộp với activation và Queue không rebuild. |
| #7 Booking Creation | Không đạt | Chức năng bị mất sau merge; legacy method không đáp ứng Acceptance Criteria. |
| #9 Queue Monitoring | Một phần | Bảng và snapshot heap có, nhưng dữ liệu Queue/Waitlist được khởi tạo sai; Customer View không dùng `currentCustomer`. |
| #11 Completion/Loyalty | Một phần | Logic SERVING/PAID, history, loyalty và remaining time đúng ở service; luồng CLI và Waitlist chưa đủ để chạy end-to-end. |

## 5. Phần Issue #11 và #12 đang đúng

`CompletionService` và `CancellationService` đã dùng công thức remaining time đúng SRS v3.0.4:

- Tính booking `COMPLETED`.
- Tính booking `SERVING`.
- Chỉ tính booking `WAITING` thật sự nằm trong Main Queue.
- Không tính Waitlist và Future Booking.
- Chỉ xét booking ưu tiên cao nhất.

CancellationService đúng ở cấp service về quyền customer/admin, từ chối cancel COMPLETED, giữ PAID khi hủy SERVING và không ghi history/loyalty khi hủy. Tuy nhiên Customer cancellation chưa được nối menu và Waitlist chưa được tái tạo đúng. Issue #12 nên tiếp tục mở cho tới khi integration hoàn chỉnh.

## 6. Giao diện và ngôn ngữ

### 6.1. High - Cấu trúc menu sai SRS

SRS yêu cầu Main Menu chỉ có Customer Menu, Admin Menu và Exit. Code hiện có một menu phẳng tám lựa chọn. Không có `currentCustomer`, không có giới hạn ba lần nhập ID, không có session Customer và không có Admin Menu 13 chức năng. Issue #27 phản ánh đúng lỗi này.

### 6.2. Medium - Output chưa 100% English

`ConsoleInputter.java` vẫn có các thông báo người dùng nhìn thấy bằng tiếng Việt, ví dụ:

```text
Lỗi: Vui lòng chỉ nhập số thực hợp lệ!
Lỗi: Dữ liệu không được để trống. Vui lòng nhập lại!
Lỗi: Menu không có lựa chọn nào!
```

Điều này vi phạm yêu cầu giao diện 100% English của SRS v3.0.3+.

## 7. Mâu thuẫn GitHub, README và CHANGELOG

- Issue #7 đóng nhưng tính năng không còn.
- Issue #9 đóng nhưng Queue/Waitlist integration còn lỗi.
- Issue #11 đóng nhưng không có đường CLI để đưa booking đến SERVING/PAID.
- Issue #12 mở nhưng CHANGELOG ghi cancellation đã được thêm.
- README đánh dấu hoàn thành Create, Activate, Process, Payment, Customer Cancel, History và Undo dù nhiều chức năng chưa có hoặc chưa truy cập được.
- README dùng URL clone cũ `NgaiLong49423/autowash-priority-booking-engine.git`; repository thật là `NgaiLong49423/car-wash-booking-queue-cli.git`.
- README hướng dẫn mở thư mục `App`, trong khi project thực tế là `AutoWashQueueCLI`.

SRS cũng có một điểm cần dọn: điều khoản chuẩn yêu cầu UI 100% English nhưng wireframe menu trong tài liệu vẫn ghi tiếng Việt. Điều khoản 100% English nên được ưu tiên và wireframe nên được dịch để tránh hiểu khác nhau.

## 8. Vệ sinh repository

Không có test file hoặc test directory nào được track trên `origin/main`.

Tuy nhiên `main` còn track các file private/generated:

- `.report1-work/...`
- `.agent/output/report/*.zip`
- `AutoWashQueueCLI/nbproject/private/private.properties`
- `AutoWashQueueCLI/nbproject/private/private.xml`

`nbproject/private/` đã nằm trong `.gitignore` nhưng vẫn được track do từng được commit.

## 9. Kết quả kiểm tra kỹ thuật

- Compile toàn bộ source: PASS.
- Compile với `--release 8`: PASS.
- Có ba cảnh báo unchecked trong `MyMap`.
- Không tạo hoặc thêm test file vào repository.
- File dữ liệu rỗng: FAIL, seed không chạy.
- Đổi MORNING sang AFTERNOON: FAIL, Queue vẫn giữ booking MORNING.
- Complete B001 seed: từ chối đúng vì WAITING, nhưng không có đường CLI để chuyển thành SERVING rồi PAID.
- Main Queue seed hiển thị hai booking; Waitlist rỗng.

## 10. Thứ tự sửa đề xuất

1. Mở lại và hoàn thành Issue #7 từ `main` mới nhất, khôi phục booking creation mà không kéo theo code ngoài phạm vi.
2. Hoàn thành Issue #8: tách simulation time khỏi activation, phân bổ và rebuild Queue/Waitlist đúng SRS.
3. Hoàn thành Issue #10: chỉ cho tối đa một SERVING và thêm payment confirmation.
4. Hoàn thành Issue #15/#27: Customer/Admin Menu và `currentCustomer`.
5. Nối Customer View và Customer Cancellation vào Customer Menu.
6. Sửa FileManager, DataSeeder và toàn vẹn seed data.
7. Hoàn thành History #13 và Undo #14.
8. Đồng bộ README, CHANGELOG, trạng thái issue và dọn file private/generated.

## 11. Lưu ý về phân công nhóm

Tài liệu `bao_cao_chia_cong_viec_v1.0.0.md` và phần phân công trong tài liệu phase được lập cho bốn người. Nhóm hiện còn ba người nên tên người phụ trách và cân bằng Story Point trong đó không còn là nguồn phân công hiện hành. Tuy nhiên các phần sau vẫn dùng được:

- Dependency giữa các issue.
- Thứ tự phase.
- Quy tắc `1 Issue = 1 Branch = 1 Pull Request`.
- Quy tắc branch phải tạo từ `main` mới nhất.
- Tên branch chuẩn theo từng issue.

Đối với Issue #7, tên nhánh chính thức theo tài liệu phase là:

```text
feature/007-booking-creation-validation
```
