# Product Requirements Document (PRD)
# Car Wash Booking Queue CLI

## Thông tin tài liệu

| Thuộc tính | Giá trị |
|---|---|
| Tên tài liệu | Product Requirements Document (PRD) |
| Tên sản phẩm | Car Wash Booking Queue CLI |
| Nguồn chuyển đổi | SRS.md v3.0.2 |
| Ngày tạo | 2026-06-26 |
| Ngôn ngữ | Tiếng Việt |
| Nguyên tắc chuyển đổi | Chỉ rút gọn và tái cấu trúc nội dung đã có trong SRS; không thêm chức năng mới, không thêm giả định ngoài SRS |

---

## 1. Product Overview

**Car Wash Booking Queue CLI** là ứng dụng giao diện dòng lệnh (**CLI - Command Line Interface**, nghĩa là giao diện chạy bằng terminal) mô phỏng hoạt động đặt lịch và điều phối xếp hàng rửa xe trong một tiệm rửa xe.

Hệ thống tập trung vào việc quản lý khách hàng, xe, dịch vụ, lịch đặt rửa xe, hàng chờ theo từng buổi phục vụ, lịch sử rửa xe và cập nhật loyalty (**điểm, tổng chi tiêu, số lần rửa, hạng thành viên**) sau khi khách hoàn tất dịch vụ.

Dự án phục vụ mục tiêu học tập môn **CSD201 - Cấu trúc dữ liệu và giải thuật**, nên trọng tâm là xử lý nghiệp vụ bằng cấu trúc dữ liệu tự định nghĩa như **Queue**, **Priority Queue**, **Stack**, **List**, **Map** và thuật toán tìm kiếm, sắp xếp cơ bản.

---

## 2. Problem Statement

Một tiệm rửa xe có số lượng xe phục vụ giới hạn theo từng buổi vì bị giới hạn bởi thời gian, nhân lực và mặt bằng. Khi nhiều khách hàng đặt lịch vào cùng ngày/buổi, hệ thống cần quản lý:

- Ai được nhận vào slot chính.
- Ai nằm trong hàng chờ phụ.
- Booking nào được ưu tiên khi hàng chờ phụ được kéo lên slot chính.
- Thứ tự xe được xử lý tiếp theo.
- Lịch sử sau khi rửa xe được lưu lại như thế nào.
- Loyalty của khách hàng được tính lại sau booking hoàn tất hoặc sau thao tác nghiệp vụ liên quan.
- Undo (**hoàn tác**) booking hoàn tất gần nhất nếu nhân viên thao tác nhầm.

---

## 3. Goals

Hệ thống cần đạt các mục tiêu sau:

1. Tiếp nhận booking có giới hạn theo từng buổi cụ thể.
2. Phân biệt booking đặt ngay trong buổi hiện tại và booking đặt trước cho buổi tương lai.
3. Ưu tiên khách hàng hạng thành viên cao khi sắp xếp lịch và hàng chờ.
4. Giúp nhân viên quản lý thứ tự xe cần rửa tiếp theo.
5. Lưu trữ lịch sử rửa xe.
6. Tự động tính toán lại loyalty gồm điểm, tổng chi tiêu, số lần rửa và hạng thành viên.
7. Hỗ trợ Undo booking hoàn tất gần nhất bằng Stack.
8. Tải và lưu dữ liệu qua file text phẳng dùng dấu phân tách `|`.
9. Giữ phạm vi phù hợp với ứng dụng CLI phục vụ môn CSD201.

---

## 4. Scope

### 4.1 In Scope

Các chức năng nằm trong phạm vi:

- Quản lý thông tin khách hàng, xe của khách và dịch vụ rửa xe.
- Thiết lập ngày/buổi hiện tại và kích hoạt buổi rửa xe.
- Tạo booking rửa xe.
- Kiểm tra booking window theo hạng thành viên.
- Quản lý slot chính bằng Queue và hàng chờ phụ bằng Priority Queue theo từng buổi.
- Xử lý xe tiếp theo.
- Xác nhận thanh toán mô phỏng.
- Hoàn tất booking.
- Hủy booking và tự động điều phối hàng chờ phụ lên slot chính khi đủ điều kiện.
- Tính toán lại điểm tích lũy và xét hạng thành viên sau khi hoàn tất dịch vụ hoặc sau thao tác nghiệp vụ ảnh hưởng đến booking `COMPLETED`.
- Lưu lịch sử rửa xe.
- Hoàn tác booking hoàn tất gần nhất bằng Stack.
- Tải/lưu dữ liệu qua file text phẳng sử dụng dấu phân tách `|`.
- Tự động sinh ID tăng dần.
- Khởi tạo dữ liệu mẫu khi file rỗng hoặc chưa tồn tại.
- Kiểm tra dữ liệu đầu vào.

### 4.2 Out of Scope

Các nội dung ngoài phạm vi:

- Đăng nhập/đăng xuất thực tế.
- Phân quyền tài khoản bảo mật thực tế.
- Giao diện Web hoặc Mobile.
- Tương tác với hệ quản trị cơ sở dữ liệu như SQL Server, MySQL hoặc các DBMS khác.
- Thanh toán trực tuyến thực tế.
- Nhận diện biển số xe tự động.
- Gửi email, SMS hoặc thông báo tự động.
- Quản lý chương trình khuyến mãi nâng cao.
- Đổi điểm lấy quà.
- Hạ hạng định kỳ theo thời gian hoặc hết hạn điểm.
- Hoàn tiền hoặc đối soát thanh toán thực tế.
- Audit log (**nhật ký hoạt động chi tiết**) như ghi nhận tài khoản nào thay đổi dữ liệu nào.
- Nhiều vị trí rửa xe hoạt động song song; hệ thống chỉ mô phỏng đúng 1 vị trí rửa xe.

---

## 5. Actors

### 5.1 Customer

Customer là khách hàng sử dụng dịch vụ rửa xe. Khi vào Customer Menu, hệ thống yêu cầu nhập mã khách hàng hợp lệ để xác định `currentCustomer`.

Customer có thể:

- Xem danh sách dịch vụ.
- Xem thông tin cá nhân của chính mình.
- Tạo booking cho chính mình.
- Xem booking cá nhân đang `WAITING` hoặc `SERVING`.
- Xem lịch sử rửa xe cá nhân.
- Hủy booking của chính mình nếu booking đang `WAITING`.

### 5.2 Admin/Nhân viên

Admin/Nhân viên là người vận hành tiệm trong hệ thống CLI. Admin Menu có thể truy cập trực tiếp từ Main Menu mà không cần tài khoản hay mật khẩu.

Admin/Nhân viên có thể:

- Quản lý khách hàng.
- Quản lý xe.
- Quản lý dịch vụ.
- Thiết lập ngày và buổi hiện tại.
- Tạo booking cho khách hàng hợp lệ.
- Kích hoạt buổi rửa xe.
- Xem hàng chờ chính/phụ và booking theo buổi.
- Xử lý booking tiếp theo.
- Xác nhận thanh toán.
- Hoàn tất booking.
- Hủy booking `WAITING` hoặc `SERVING`.
- Xem lịch sử rửa xe.
- Undo booking hoàn tất gần nhất.

---

## 6. Functional Requirements

| Mã FR | Tên yêu cầu | Tóm tắt |
|---|---|---|
| FR-01 | Quản lý khách hàng | Admin thêm, xem, tìm kiếm, cập nhật và xóa khách hàng khi không có dữ liệu liên kết. |
| FR-02 | Quản lý xe của khách hàng | Admin liên kết xe với khách hàng, xem danh sách xe và tìm kiếm xe. |
| FR-03 | Quản lý dịch vụ rửa xe | Admin thêm, xem, tìm kiếm và sắp xếp dịch vụ theo giá hoặc thời gian thực hiện. |
| FR-04 | Thiết lập ngày và buổi hiện tại | Admin thay đổi `currentDate` và `currentPeriod` mô phỏng. |
| FR-05 | Tạo booking rửa xe | Customer hoặc Admin tạo lịch đặt rửa xe cho khách hàng, xe, dịch vụ, ngày và buổi cụ thể. |
| FR-06 | Kiểm tra booking window | Hệ thống kiểm tra ngày đặt có vượt giới hạn theo tier hay không. |
| FR-07 | Xác định buổi rửa xe | Hệ thống phân loại booking vào `MORNING`, `AFTERNOON` hoặc `EVENING`. |
| FR-08 | Xử lý booking trong buổi hiện tại | Booking của buổi hiện tại được đưa vào slot chính hoặc Waitlist nếu buổi đã kích hoạt; nếu chưa kích hoạt thì lưu như booking tương lai. |
| FR-09 | Xử lý booking cho buổi tương lai | Booking tương lai được lưu tạm sau khi kiểm tra tổng sức chứa buổi. |
| FR-10 | Kích hoạt buổi rửa xe | Admin kích hoạt buổi hiện tại để phân bổ booking tương lai vào Main Queue và Waitlist theo ưu tiên. |
| FR-11 | Ngăn kích hoạt trùng buổi | Mỗi ngày + buổi chỉ được kích hoạt một lần và trạng thái được lưu vào `periods.txt`. |
| FR-12 | Xem hàng chờ chính | Admin xem Main Queue của buổi hiện tại theo FIFO. |
| FR-13 | Xem hàng chờ phụ | Admin xem Waitlist của buổi hiện tại theo thứ tự ưu tiên. |
| FR-14 | Xem booking của khách hàng | Customer xem booking cá nhân chưa kết thúc gồm `WAITING` và `SERVING`. |
| FR-15 | Xử lý booking tiếp theo | Admin chuyển booking đầu Main Queue từ `WAITING` sang `SERVING`. |
| FR-16 | Xác nhận thanh toán booking | Admin xác nhận thanh toán cho booking đang `SERVING`. |
| FR-17 | Hoàn tất booking | Booking `SERVING` và `PAID` chuyển sang `COMPLETED`, ghi history, tính loyalty và kiểm tra Waitlist. |
| FR-18 | Hủy booking | Customer hủy booking của mình khi `WAITING`; Admin hủy booking bất kỳ khi `WAITING` hoặc `SERVING`. |
| FR-19 | Tính toán lại loyalty | Tính lại `visitCount`, `totalSpent`, `loyaltyPoints`, `tier` từ các booking `COMPLETED`. |
| FR-20 | Xem lịch sử rửa xe | Hiển thị danh sách các booking đã hoàn tất. |
| FR-21 | Hoàn tác booking hoàn tất gần nhất | Undo tác động của booking vừa complete gần nhất, gồm trạng thái, history, loyalty và Waitlist nếu có. |
| FR-22 | Tải dữ liệu từ file khi khởi động | Load dữ liệu từ file text trong thư mục `data` vào RAM. |
| FR-23 | Lưu dữ liệu sau thao tác quan trọng | Ghi dữ liệu RAM xuống file sau thao tác thay đổi dữ liệu. |
| FR-24 | Sinh mã tự động | Sinh ID tăng dần cho Customer, Vehicle, Service và Booking. |
| FR-25 | Khởi tạo dữ liệu mẫu | Tạo seed data khi file chưa tồn tại hoặc rỗng. |
| FR-26 | Kiểm tra dữ liệu đầu vào | Validate dữ liệu nhập trước khi xử lý nghiệp vụ. |

---

## 7. Business Rules

### 7.1 Service Periods

| Buổi | Khung thời gian | Slot chính | Slot chờ phụ |
|---|---:|---:|---:|
| MORNING | 07:00 - 12:00 | 10 | 3 |
| AFTERNOON | 13:00 - 17:00 | 10 | 3 |
| EVENING | 18:00 - 21:00 | 5 | 2 |

### 7.2 Queue và Waitlist

- Slot chính (**Main Queue**) dùng nguyên tắc FIFO (**First In First Out**, nghĩa là vào trước ra trước).
- Hàng chờ phụ (**Waitlist**) dùng Priority Queue (**hàng đợi ưu tiên**), ưu tiên theo tier cao hơn; nếu cùng tier thì booking tạo sớm hơn được ưu tiên hơn.

### 7.3 Tier

| Tier | Điều kiện |
|---|---|
| Member | Mặc định |
| Silver | Tối thiểu 5 lần rửa hoặc tổng chi tiêu từ 2,000,000 VND |
| Gold | Tối thiểu 15 lần rửa hoặc tổng chi tiêu từ 6,000,000 VND |
| Platinum | Tối thiểu 30 lần rửa hoặc tổng chi tiêu từ 15,000,000 VND |

### 7.4 Booking Window

| Tier | Số ngày đặt trước tối đa |
|---|---:|
| Member | 7 |
| Silver | 10 |
| Gold | 12 |
| Platinum | 14 |

Admin không được override booking window.

### 7.5 Booking hiện tại và booking tương lai

- Booking hiện tại là booking cho đúng `currentDate` và `currentPeriod`.
- Nếu buổi hiện tại đã kích hoạt, booking được đưa vào Main Queue nếu slot chính còn chỗ, hoặc Waitlist nếu slot chính đầy nhưng Waitlist còn chỗ.
- Nếu buổi hiện tại chưa kích hoạt, booking được lưu như booking tương lai.
- Booking tương lai chỉ kiểm tra tổng sức chứa của buổi; khi kích hoạt mới phân bổ vào Main Queue và Waitlist.

### 7.6 Activate Period

- Chỉ kích hoạt buổi trùng `currentDate` và `currentPeriod`.
- Mỗi buổi chỉ được kích hoạt một lần.
- Trạng thái kích hoạt lưu xuống `periods.txt`.
- Khi kích hoạt, hệ thống sắp xếp booking tương lai theo tier và thời gian tạo, sau đó phân bổ vào Main Queue và Waitlist.
- Nếu dữ liệu file bị lỗi hoặc bị sửa thủ công gây vượt sức chứa, hệ thống không phân bổ phần dư và hiển thị lỗi dữ liệu.

### 7.7 Booking Status

Luồng trạng thái hợp lệ:

```text
WAITING -> SERVING -> COMPLETED
WAITING -> CANCELLED
SERVING -> CANCELLED
COMPLETED -> SERVING   # chỉ khi Undo
```

Tại một thời điểm chỉ có tối đa 1 booking ở trạng thái `SERVING`.

### 7.8 Complete Booking

- Chỉ complete booking đang `SERVING` và có `paymentStatus = PAID`.
- Khi complete, booking chuyển sang `COMPLETED`, history được ghi, loyalty được tính lại, bản ghi Undo được đẩy vào Stack.
- Sau complete, hệ thống kiểm tra thời gian còn lại của buổi để quyết định có kéo booking ưu tiên cao nhất từ Waitlist lên Main Queue hay không.

### 7.9 Remaining Time

Hệ thống tính thời gian còn lại bằng dữ liệu booking đã hoàn tất trong cùng ngày và cùng buổi:

```text
usedMinutes = tổng serviceDuration của các booking cùng ngày, cùng period, trạng thái COMPLETED
remainingMinutes = periodTotalMinutes - usedMinutes
```

Khi kiểm tra Waitlist, hệ thống chỉ xét booking ưu tiên cao nhất. Nếu booking đó không đủ thời gian phục vụ, hệ thống không bỏ qua để xét booking phía sau.

### 7.10 Cancel Booking

- Customer chỉ được hủy booking của chính mình khi booking đang `WAITING`.
- Admin được hủy booking bất kỳ khi booking đang `WAITING` hoặc `SERVING`.
- Booking `COMPLETED` không được hủy.
- Hủy booking trong Main Queue có thể kéo booking ưu tiên cao nhất từ Waitlist lên Main Queue.
- Hủy booking `SERVING` không ghi history, không cộng loyalty, không tăng visit count, không tự động chuyển booking khác sang `SERVING`.
- Nếu booking `SERVING` đã `PAID`, hệ thống giữ nguyên trạng thái thanh toán và không xử lý hoàn tiền hoặc đối soát thực tế.

### 7.11 Loyalty

- Mỗi 1,000 VND chi tiêu tích lũy được 1 điểm loyalty.
- `visitCount` = số booking `COMPLETED` của khách hàng.
- `totalSpent` = tổng giá trị dịch vụ của các booking `COMPLETED`.
- `loyaltyPoints` = `totalSpent / 1000`.
- `tier` = hạng cao nhất đạt được dựa trên `visitCount` hoặc `totalSpent`.
- Loyalty không được chỉnh sửa thủ công độc lập.
- Loyalty được tính lại sau thao tác ảnh hưởng đến booking `COMPLETED`.

### 7.12 Undo

- Chỉ undo booking complete gần nhất.
- Undo dùng Stack theo LIFO (**Last In First Out**, nghĩa là vào sau ra trước).
- Booking `COMPLETED` quay về trạng thái trước đó, mặc định là `SERVING`, giữ `paymentStatus = PAID`.
- History tương ứng bị xóa hoặc vô hiệu hóa.
- Nếu complete trước đó kéo booking từ Waitlist lên Main Queue, Undo phải đưa booking đó trở lại Waitlist và sắp xếp lại theo Priority Queue.

### 7.13 File Storage

- Dữ liệu lưu trong file text phẳng trong thư mục `data`.
- Các trường phân tách bằng ký tự `|`.
- Save file sau thao tác quan trọng để giảm rủi ro mất dữ liệu.

---

## 8. Non-Functional Requirements

### NFR-01 — Tính đúng đắn

Hệ thống phải vận hành chính xác các quy tắc nghiệp vụ điều phối hàng chờ, booking window, loyalty và nâng hạng thành viên. Các cấu trúc dữ liệu tự cài đặt phải đảm bảo tính logic và không xảy ra lỗi tràn bộ nhớ khi chạy thử.

### NFR-02 — Tính dễ sử dụng trong CLI

- Menu CLI phân cấp trực quan, dễ thao tác.
- Có seed data tự động để sẵn sàng demo.
- Hiển thị rõ trạng thái xếp chỗ của booking sau khi tạo thành công.
- Hiển thị sức chứa buổi dưới dạng đã dùng / tổng.

### NFR-03 — Tính toàn vẹn dữ liệu

- Dữ liệu tự động ghi xuống file text sau thao tác quan trọng.
- Kiểm tra tính duy nhất của số điện thoại, biển số xe, mã thực thể.
- Từ chối xóa khách hàng nếu khách đã có dữ liệu liên kết như xe, booking hoặc lịch sử.

### NFR-04 — Khả năng bảo trì

Code và lớp cấu trúc dữ liệu tự cài đặt phải được tổ chức tách biệt, mạch lạc, có chú thích để dễ đọc hiểu, giải thích giải thuật và bảo trì.

### NFR-05 — Giới hạn phạm vi

Hệ thống giữ độ phức tạp vừa phải, không tích hợp thư viện UI phức tạp hoặc DBMS, tập trung vào mục tiêu cấu trúc dữ liệu và giải thuật của môn CSD201.

---

## 9. Data Requirements

Hệ thống quản lý các nhóm dữ liệu chính:

- Dữ liệu khách hàng: mã khách hàng, tên, số điện thoại, tier, loyalty points, total spent, visit count.
- Dữ liệu xe: mã xe, biển số, mã khách hàng sở hữu.
- Dữ liệu dịch vụ: mã dịch vụ, tên dịch vụ, giá tiền, thời gian rửa.
- Dữ liệu booking: mã booking, mã khách hàng, mã xe, mã dịch vụ, ngày, buổi, trạng thái booking, trạng thái thanh toán và thông tin liên quan.
- Dữ liệu lịch sử: các booking đã `COMPLETED`.
- Dữ liệu ngày/buổi hiện tại: `currentDate`, `currentPeriod` dùng cho mô phỏng.
- Dữ liệu hàng chờ: Main Queue, Waitlist, booking tương lai theo ngày/buổi.

---

## 10. Technical Constraints

- Ứng dụng là CLI chạy bằng menu trong terminal.
- Không sử dụng DBMS.
- Dữ liệu lưu bằng file text phẳng trong thư mục `data`.
- Hệ thống nạp dữ liệu vào RAM khi khởi động.
- Hệ thống sử dụng các cấu trúc dữ liệu tự định nghĩa:
  - `MyLinkedList` / List cho danh sách dữ liệu.
  - `MyQueue` cho slot chính.
  - `MyPriorityQueue` dùng Max Heap cho Waitlist.
  - `MyStack` cho Undo.
  - `MyMap` hoặc cấu trúc tra cứu đơn giản cho booking window.
- Sử dụng đọc/ghi file thuần Java như `BufferedReader`, `BufferedWriter`, `FileWriter`.
- Thuật toán liên quan gồm tìm kiếm tuyến tính, sắp xếp dịch vụ theo giá/thời gian và heap operations cho Priority Queue.

---

## 11. CLI Interface Summary

### 11.1 Main Menu

```text
Main Menu
1. Customer Menu
2. Admin Menu
0. Exit
```

### 11.2 Customer Menu

Customer nhập mã khách hàng hợp lệ để xác định `currentCustomer`. Nếu nhập sai tối đa 3 lần, hệ thống quay lại Main Menu.

```text
Customer Menu
1. Xem danh sách dịch vụ
2. Xem thông tin cá nhân
3. Tạo booking
4. Xem booking của tôi
5. Hủy booking của tôi
6. Xem lịch sử rửa xe của tôi
0. Quay lại
```

### 11.3 Admin Menu

```text
Admin Menu
1. Quản lý khách hàng
2. Quản lý xe
3. Quản lý dịch vụ
4. Thiết lập ngày/buổi hiện tại
5. Tạo booking cho khách
6. Kích hoạt buổi rửa xe
7. Xem hàng chờ / booking theo buổi
8. Xử lý booking tiếp theo
9. Xác nhận thanh toán
10. Hoàn tất booking
11. Hủy booking
12. Xem lịch sử
13. Hoàn tác booking hoàn tất gần nhất
0. Quay lại
```

---

## 12. Acceptance Criteria

Acceptance Criteria nghĩa là **tiêu chí chấp nhận**, dùng để kiểm tra chức năng đã đạt yêu cầu hay chưa. Các tiêu chí dưới đây được rút trực tiếp từ FR, UC và quy tắc nghiệp vụ trong SRS.

1. Booking chỉ được tạo khi khách hàng, xe, dịch vụ hợp lệ, xe thuộc đúng khách hàng, ngày đặt nằm trong booking window và buổi còn sức chứa.
2. Booking vượt booking window bị từ chối và không được ghi dữ liệu mới.
3. Admin không được override booking window hoặc sức chứa buổi.
4. Booking hiện tại của buổi đã kích hoạt được đưa vào Main Queue nếu còn slot chính, hoặc Waitlist nếu slot chính đầy nhưng Waitlist còn chỗ.
5. Booking tương lai chỉ được phân bổ vào Main Queue/Waitlist khi buổi được kích hoạt.
6. Mỗi ngày + buổi chỉ được kích hoạt một lần; trạng thái kích hoạt phải lưu vào `periods.txt`.
7. Khi kích hoạt buổi, booking tương lai được sắp xếp theo tier giảm dần; nếu cùng tier thì booking tạo sớm hơn được ưu tiên.
8. Main Queue phục vụ theo FIFO.
9. Waitlist phục vụ theo Priority Queue, ưu tiên tier cao hơn và thời gian tạo sớm hơn.
10. Hệ thống chỉ cho phép tối đa 1 booking `SERVING` tại một thời điểm.
11. Booking chỉ được complete khi đang `SERVING` và `paymentStatus = PAID`.
12. Complete booking phải ghi history, tính lại loyalty, đẩy bản ghi Undo vào Stack và kiểm tra Waitlist theo thời gian còn lại.
13. Khi Waitlist được kiểm tra theo thời gian còn lại, hệ thống chỉ xét booking ưu tiên cao nhất; nếu không đủ thời gian thì không xét booking phía sau.
14. Customer chỉ được hủy booking của chính mình khi booking đang `WAITING`.
15. Admin được hủy booking bất kỳ khi booking đang `WAITING` hoặc `SERVING`.
16. Booking `COMPLETED` không được hủy.
17. Hủy booking `SERVING` không ghi history, không cộng loyalty, không tự động chuyển booking khác sang `SERVING`.
18. Loyalty phải được tính lại từ toàn bộ booking `COMPLETED`, không chỉnh sửa thủ công độc lập.
19. Undo chỉ áp dụng cho booking complete gần nhất, khôi phục booking về `SERVING`, xử lý history, tính lại loyalty và khôi phục Waitlist nếu complete trước đó có kéo booking lên Main Queue.
20. Dữ liệu phải được load từ file khi khởi động và save xuống file sau thao tác quan trọng.
21. ID cho Customer, Vehicle, Service và Booking phải được sinh tự động, tăng dần và không trùng.
22. Seed data phải được tạo khi file chưa tồn tại hoặc rỗng.
23. Dữ liệu nhập phải được validate trước khi xử lý nghiệp vụ.

---

## 13. Implementation Notes for Agent

Các ghi chú này không thêm nghiệp vụ mới, chỉ nhắc lại giới hạn từ SRS:

- Ưu tiên SRS nếu có điểm khác nhau giữa PRD và SRS.
- Không tự thêm login thật, phân quyền bảo mật thật, DBMS, Web/Mobile UI, online payment, LPR, email/SMS hoặc khuyến mãi ngoài đặc tả.
- Không tự ý sửa quy tắc booking window, tier, Queue, Waitlist, Complete, Cancel, Undo và Loyalty.
- Nếu gặp điểm chưa rõ, đối chiếu mục **Vấn đề cần xác nhận** trong SRS hoặc hỏi người dùng trước khi code.

---

## 14. Known Confirmation Points from SRS

Các điểm SRS đã ghi là cần thống nhất khi triển khai:

1. Admin phải thêm khách hàng hoặc xe thành công qua FR-01/FR-02 trước khi tạo booking cho khách hàng đó; SRS không mô tả luồng con thêm trực tiếp trong quá trình tạo booking.
2. Admin chỉ quản lý thông tin hồ sơ cơ bản của khách hàng và không được thay đổi thủ công điểm tích lũy hoặc hạng thành viên; các giá trị này phải được cập nhật tự động sau hoàn tất booking hoặc undo booking.
