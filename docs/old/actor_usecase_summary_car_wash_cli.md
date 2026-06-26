# Tổng hợp phân tích Actor và Use Case cho Car Wash Booking Queue CLI

## 1. Mục đích tài liệu

Tài liệu này tổng hợp các quyết định đã chốt trong bước trừu tượng hóa ứng dụng **Car Wash Booking Queue CLI**.

Nội dung tập trung vào:

- App sinh ra cho những ai sử dụng.
- Mỗi nhóm người dùng có hành vi gì trong hệ thống.
- Use Case nên được viết theo mức độ nào.
- Các Use Case đặc biệt như **Tạo booking** và **Hủy booking** nên được mô hình hóa ra sao.

Tài liệu này có thể dùng để bổ sung vào SRSV2.md hoặc làm file nền trước khi viết Use Case chi tiết.

---

## 2. Quyết định 1 — Actor chính của hệ thống

### 2.1 Quyết định đã chọn

Chọn hướng **B — Hệ thống có 2 actor chính**:

1. **Customer — Khách hàng**
2. **Admin/Nhân viên — Người vận hành tiệm**

### 2.2 Lý do chọn

Hướng này cân bằng nhất cho app CLI môn CSD.

App không cần đăng nhập/phân quyền thật, nhưng vẫn cần mô phỏng rõ ai dùng hệ thống và mỗi người được làm gì. Nếu chỉ có một actor thì hệ thống quá đơn giản, khó thể hiện nghiệp vụ đặt lịch từ phía khách hàng. Nếu tách thành ba actor như Customer, Staff, Manager/Admin thì phạm vi bị lớn quá so với project CLI.

### 2.3 Cách hiểu chính thức

Hệ thống không triển khai đăng nhập và phân quyền bảo mật thật. Tuy nhiên, để mô phỏng nghiệp vụ và tổ chức menu CLI rõ ràng, hệ thống chia hành vi thành hai vai trò sử dụng:

- **Customer**: người sử dụng dịch vụ rửa xe.
- **Admin/Nhân viên**: người vận hành tiệm và quản lý dữ liệu trong hệ thống.

Người dùng có thể chọn menu tương ứng để thao tác theo vai trò.

Ví dụ menu tổng:

```text
1. Customer Menu
2. Admin Menu
0. Exit
```

---

## 3. Quyết định 2 — Hành vi của Customer

### 3.1 Quyết định đã chọn

Chọn hướng **B — Customer được tạo booking và hủy booking của mình**.

### 3.2 Customer được phép làm gì?

Customer có thể:

1. Xem danh sách dịch vụ.
2. Xem thông tin cá nhân.
3. Tạo booking cho xe của mình.
4. Xem danh sách booking của mình.
5. Hủy booking của mình nếu booking còn ở trạng thái `WAITING`.
6. Xem lịch sử rửa xe của mình.

### 3.3 Customer không được phép làm gì?

Customer không được:

- Quản lý khách hàng khác.
- Quản lý dịch vụ.
- Kích hoạt buổi rửa xe.
- Xử lý booking tiếp theo.
- Xác nhận thanh toán.
- Hoàn tất booking.
- Hoàn tác booking đã hoàn tất.
- Hủy booking của khách hàng khác.
- Hủy booking đang `SERVING`.
- Hủy booking đã `COMPLETED`.

### 3.4 Lý do chọn

Hướng này giúp Customer có hành vi thật sự rõ ràng, đặc biệt là thao tác **tạo booking** và **hủy booking của mình**.

Đồng thời, phạm vi vẫn không quá lớn vì Customer chỉ thao tác trên dữ liệu thuộc về chính họ.

### 3.5 Cách mô phỏng trong CLI

Vì hệ thống không có đăng nhập thật, khi vào Customer Menu, hệ thống có thể yêu cầu nhập mã khách hàng để xác định Customer hiện tại.

Ví dụ:

```text
Nhập mã khách hàng: C001
```

Sau đó hệ thống coi `C001` là khách hàng đang thao tác.

### 3.6 Menu Customer đề xuất

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

---

## 4. Quyết định 3 — Hành vi của Admin/Nhân viên

### 4.1 Quyết định đã chọn

Chọn hướng **B — Admin/Nhân viên quản lý dữ liệu và vận hành booking**.

### 4.2 Admin/Nhân viên được phép làm gì?

Admin/Nhân viên có thể:

1. Quản lý khách hàng.
2. Quản lý xe của khách hàng.
3. Quản lý dịch vụ rửa xe.
4. Thiết lập ngày và buổi hiện tại.
5. Kích hoạt buổi rửa xe.
6. Tạo booking cho khách hàng.
7. Xem hàng chờ chính và hàng chờ phụ.
8. Xử lý booking tiếp theo.
9. Xác nhận thanh toán.
10. Hoàn tất booking.
11. Hủy booking hợp lệ.
12. Xem lịch sử rửa xe.
13. Hoàn tác booking hoàn tất gần nhất.

### 4.3 Lý do chọn

Hướng này phù hợp nhất với app CLI vì Admin/Nhân viên vừa có quyền chuẩn bị dữ liệu, vừa có quyền vận hành tiệm.

Nếu Admin/Nhân viên chỉ vận hành booking mà không quản lý dữ liệu nền, hệ thống sẽ phụ thuộc quá nhiều vào dữ liệu mẫu. Nếu Admin/Nhân viên có toàn quyền sửa/xóa sâu mọi dữ liệu, phạm vi sẽ bị lớn và kéo theo nhiều ràng buộc phức tạp.

### 4.4 Menu Admin/Nhân viên đề xuất

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

## 5. Quyết định 4 — Mức độ viết Use Case

### 5.1 Quyết định đã chọn

Chọn hướng **B — Viết Use Case theo từng hành vi chính của actor**.

### 5.2 Use Case là gì?

**Use Case** nghĩa là **ca sử dụng**, tức là một tình huống người dùng tương tác với hệ thống để đạt một mục tiêu cụ thể.

Ví dụ:

- Customer tạo booking.
- Admin kích hoạt buổi rửa xe.
- Admin hoàn tất booking.
- Customer xem lịch sử rửa xe cá nhân.

### 5.3 Lý do chọn

Hướng này cân bằng nhất:

- Không quá tổng quát khiến tài liệu mơ hồ.
- Không quá chi tiết đến mức trùng với Functional Requirement.
- Dễ vẽ Use Case Diagram.
- Dễ map Use Case sang FR, test case và menu CLI.

### 5.4 Nguyên tắc viết Use Case

Mỗi Use Case nên đại diện cho một hành vi chính của actor.

Không nên tách quá nhỏ thành từng thao tác phụ như:

- Nhập mã khách hàng.
- Kiểm tra dữ liệu rỗng.
- Lưu file.
- In thông báo.

Các thao tác này nên nằm trong luồng xử lý của Use Case, không nên tách thành Use Case riêng.

---

## 6. Danh sách Use Case tạm chốt

| Mã UC | Tên Use Case | Actor chính |
|---|---|---|
| UC-01 | Xem danh sách dịch vụ | Customer |
| UC-02 | Xem thông tin cá nhân | Customer |
| UC-03 | Tạo booking | Customer, Admin/Nhân viên |
| UC-04 | Xem booking cá nhân | Customer |
| UC-05 | Hủy booking | Customer, Admin/Nhân viên |
| UC-06 | Xem lịch sử rửa xe cá nhân | Customer |
| UC-07 | Quản lý khách hàng | Admin/Nhân viên |
| UC-08 | Quản lý xe | Admin/Nhân viên |
| UC-09 | Quản lý dịch vụ | Admin/Nhân viên |
| UC-10 | Thiết lập ngày và buổi hiện tại | Admin/Nhân viên |
| UC-11 | Kích hoạt buổi rửa xe | Admin/Nhân viên |
| UC-12 | Xem hàng chờ và booking theo buổi | Admin/Nhân viên |
| UC-13 | Xử lý booking tiếp theo | Admin/Nhân viên |
| UC-14 | Xác nhận thanh toán | Admin/Nhân viên |
| UC-15 | Hoàn tất booking | Admin/Nhân viên |
| UC-16 | Xem lịch sử rửa xe | Admin/Nhân viên |
| UC-17 | Hoàn tác booking hoàn tất gần nhất | Admin/Nhân viên |

Ghi chú: Ban đầu có thể tách "Hủy booking của tôi" và "Hủy booking bất kỳ" thành hai UC riêng. Tuy nhiên, quyết định cuối cùng là dùng một UC chính **Hủy booking** và mô tả biến thể theo actor.

---

## 7. Quyết định 5 — Thiết kế UC Tạo booking

### 7.1 Quyết định đã chọn

Chọn hướng **C — Một UC chính "Tạo booking", có biến thể cho Customer và Admin/Nhân viên**.

### 7.2 Lý do chọn

Cả Customer và Admin/Nhân viên đều có thể tạo booking, nhưng luồng nghiệp vụ cốt lõi gần giống nhau:

1. Chọn khách hàng.
2. Chọn xe.
3. Chọn dịch vụ.
4. Chọn ngày và buổi.
5. Kiểm tra booking window.
6. Kiểm tra sức chứa.
7. Tạo booking nếu hợp lệ.

Nếu tách thành hai UC riêng, tài liệu sẽ bị lặp. Nếu chỉ viết một UC chung mà không mô tả biến thể, quyền thao tác sẽ bị mơ hồ.

Vì vậy, hướng tốt nhất là viết một UC chính và ghi rõ biến thể theo actor.

### 7.3 Mô tả chính thức đề xuất

#### UC-03 — Tạo booking

**Actor chính:** Customer hoặc Admin/Nhân viên.

**Mục tiêu:** Tạo lịch rửa xe cho một khách hàng, một xe, một dịch vụ, vào một ngày và buổi cụ thể.

**Biến thể theo actor:**

- Nếu actor là **Customer**, hệ thống chỉ cho phép tạo booking cho chính Customer hiện tại và chỉ được chọn xe thuộc Customer đó.
- Nếu actor là **Admin/Nhân viên**, hệ thống cho phép chọn khách hàng bất kỳ trong hệ thống và tạo booking thay cho khách đó.

**Quy tắc chung:**

Cả hai trường hợp đều phải kiểm tra:

- Khách hàng có tồn tại không.
- Xe có tồn tại không.
- Xe có thuộc đúng khách hàng không.
- Dịch vụ có tồn tại không.
- Ngày đặt có nằm trong booking window không.
- Buổi được chọn còn sức chứa không.
- Booking thuộc buổi hiện tại hay buổi tương lai.

**Kết quả mong đợi:**

Nếu hợp lệ, hệ thống tạo booking mới ở trạng thái `WAITING` và lưu dữ liệu. Nếu không hợp lệ, hệ thống từ chối và thông báo lý do.

---

## 8. Quyết định 6 — Thiết kế UC Hủy booking

### 8.1 Quyết định đã chọn

Chọn hướng **C — Một UC chính "Hủy booking", có biến thể cho Customer và Admin/Nhân viên**.

### 8.2 Lý do chọn

Customer và Admin/Nhân viên đều có thể hủy booking, nhưng quyền hủy khác nhau.

Nếu tách riêng thành hai UC, tài liệu sẽ rõ quyền nhưng bị lặp logic. Nếu viết một UC chung mà không mô tả ràng buộc theo actor, hệ thống sẽ dễ bị hiểu sai.

Vì vậy, hướng tốt nhất là viết một UC chính và ghi rõ biến thể theo actor.

### 8.3 Mô tả chính thức đề xuất

#### UC-05 — Hủy booking

**Actor chính:** Customer hoặc Admin/Nhân viên.

**Mục tiêu:** Hủy một booking hợp lệ và cập nhật lại trạng thái hàng chờ nếu việc hủy tạo ra slot trống.

**Luồng chung:**

1. Actor chọn chức năng hủy booking.
2. Hệ thống nhận mã booking cần hủy.
3. Hệ thống kiểm tra booking có tồn tại không.
4. Hệ thống kiểm tra actor có quyền hủy booking đó không.
5. Hệ thống kiểm tra trạng thái booking có được phép hủy không.
6. Nếu hợp lệ, booking chuyển sang trạng thái `CANCELLED`.
7. Nếu booking bị hủy nằm trong slot chính của buổi hiện tại, hệ thống kiểm tra hàng chờ phụ.
8. Nếu hàng chờ phụ có booking, hệ thống lấy booking có độ ưu tiên cao nhất đưa vào cuối hàng chờ chính.
9. Hệ thống lưu thay đổi xuống file.

**Ràng buộc theo actor:**

- Nếu actor là **Customer**, Customer chỉ được hủy booking của chính mình và chỉ khi booking đang ở trạng thái `WAITING`.
- Nếu actor là **Admin/Nhân viên**, Admin/Nhân viên được hủy booking ở trạng thái `WAITING` hoặc `SERVING` của bất kỳ khách hàng nào.
- Booking ở trạng thái `COMPLETED` không được hủy. Nếu cần sửa lỗi complete, hệ thống dùng chức năng hoàn tác booking hoàn tất gần nhất.

**Kết quả mong đợi:**

Booking hợp lệ được chuyển sang `CANCELLED`. Nếu việc hủy làm trống slot chính, hệ thống tự động bổ sung booking phù hợp từ hàng chờ phụ nếu có.

---

## 9. Bảng tổng hợp quyền theo actor

| Chức năng / Hành vi | Customer | Admin/Nhân viên |
|---|---:|---:|
| Xem danh sách dịch vụ | Có | Có |
| Xem thông tin cá nhân | Có | Có thể xem qua quản lý khách hàng |
| Tạo booking | Có, chỉ cho chính mình | Có, cho bất kỳ khách hợp lệ |
| Xem booking cá nhân | Có | Có thể xem booking hệ thống |
| Hủy booking | Có, chỉ booking của mình đang WAITING | Có, booking WAITING hoặc SERVING |
| Xem lịch sử cá nhân | Có | Có thể xem toàn bộ lịch sử |
| Quản lý khách hàng | Không | Có |
| Quản lý xe | Không | Có |
| Quản lý dịch vụ | Không | Có |
| Thiết lập ngày/buổi hiện tại | Không | Có |
| Kích hoạt buổi rửa xe | Không | Có |
| Xem hàng chờ | Không | Có |
| Xử lý booking tiếp theo | Không | Có |
| Xác nhận thanh toán | Không | Có |
| Hoàn tất booking | Không | Có |
| Hoàn tác booking hoàn tất gần nhất | Không | Có |

---

## 10. Gợi ý vị trí đưa vào SRSV2.md

Nếu đưa nội dung này vào SRSV2.md, nên đặt như sau:

### 10.1 Thêm vào mục 2 — Mô tả tổng quan

Bổ sung rõ phần actor:

- Customer.
- Admin/Nhân viên.
- Không có đăng nhập thật, chỉ mô phỏng vai trò qua menu CLI.

### 10.2 Thêm một mục mới sau phần Mô tả tổng quan

Có thể thêm mục:

```text
3. Use Case và hành vi người dùng
```

Nếu SRSV2 hiện đã có mục 3 là Quy tắc nghiệp vụ, có thể đặt là:

```text
2.8 Actor và hành vi người dùng
2.9 Danh sách Use Case tổng quan
```

Cách này ít làm xáo trộn số thứ tự tài liệu.

### 10.3 Không nên ghi thành "Cập nhật bổ sung"

Không nên ghi:

```text
Cập nhật bổ sung về actor...
```

Nên ghi thẳng thành nội dung chính thức của tài liệu:

```text
Hệ thống mô phỏng hai vai trò sử dụng chính...
```

---

## 11. Nội dung có thể viết tiếp sau bước này

Sau khi đã chốt actor và danh sách Use Case, bước tiếp theo nên làm là viết chi tiết từng UC theo mẫu thống nhất.

Mẫu đề xuất:

```text
UC-xx — Tên Use Case

Actor chính:
Mục tiêu:
Tiền điều kiện:
Hậu điều kiện:
Luồng chính:
Luồng thay thế / ngoại lệ:
Quy tắc nghiệp vụ liên quan:
Cấu trúc dữ liệu liên quan:
```

Giải thích nhanh:

- **Actor chính**: ai dùng chức năng này.
- **Tiền điều kiện**: điều kiện phải đúng trước khi UC bắt đầu.
- **Hậu điều kiện**: kết quả sau khi UC kết thúc.
- **Luồng chính**: các bước chạy bình thường.
- **Luồng thay thế / ngoại lệ**: các trường hợp lỗi hoặc rẽ nhánh.
- **Quy tắc nghiệp vụ liên quan**: các rule phải tuân thủ.
- **Cấu trúc dữ liệu liên quan**: Queue, Priority Queue, Stack, List, Map nào được dùng.

---

## 12. Quyết định 7 — Cách Customer vào hệ thống

### 12.1 Quyết định đã chọn

Chọn hướng **B — Khi vào Customer Menu, nhập mã khách hàng một lần**.

### 12.2 Cách hiểu chính thức

Khi người dùng chọn **Customer Menu**, hệ thống yêu cầu nhập mã khách hàng để xác định **Customer hiện tại**.

Nếu mã khách hàng hợp lệ, hệ thống cho phép Customer thực hiện các chức năng cá nhân như:

- Xem danh sách dịch vụ.
- Xem thông tin cá nhân.
- Tạo booking.
- Xem booking của tôi.
- Hủy booking của tôi.
- Xem lịch sử rửa xe của tôi.

Đây chỉ là cơ chế mô phỏng định danh trong CLI, không phải chức năng đăng nhập bảo mật.

### 12.3 Khái niệm currentCustomer

**currentCustomer** nghĩa là **khách hàng hiện tại đang thao tác trong Customer Menu**.

Sau khi Customer nhập mã hợp lệ, hệ thống lưu tạm Customer đó trong phiên menu hiện tại. Mọi chức năng trong Customer Menu chỉ được thao tác trên dữ liệu thuộc về currentCustomer.

Khi Customer quay lại Main Menu, phiên Customer kết thúc.

### 12.4 Luồng minh họa

```text
Main Menu
1. Customer Menu
2. Admin Menu
0. Exit

Chọn: 1
Nhập mã khách hàng: C001

Xin chào Nguyễn Văn A

Customer Menu
1. Xem danh sách dịch vụ
2. Xem thông tin cá nhân
3. Tạo booking
4. Xem booking của tôi
5. Hủy booking của tôi
6. Xem lịch sử rửa xe của tôi
0. Quay lại
```

### 12.5 Lý do chọn

Hướng này cân bằng nhất vì:

- Không cần làm đăng nhập thật.
- Không cần mật khẩu hoặc PIN.
- Không bắt Customer nhập lại mã khách hàng ở từng chức năng.
- Dễ viết Use Case vì các chức năng Customer đều hiểu là thao tác trên currentCustomer.
- Phù hợp với phạm vi app CLI môn CSD.

---

## 13. Quyết định 8 — Cách Admin/Nhân viên vào hệ thống

### 13.1 Quyết định đã chọn

Chọn hướng **A — Admin Menu vào thẳng, không cần xác định ai đang thao tác**.

### 13.2 Cách hiểu chính thức

Khi người dùng chọn **Admin Menu**, hệ thống cho phép truy cập trực tiếp vào các chức năng quản lý dữ liệu và vận hành tiệm.

Hệ thống không xác thực tài khoản Admin vì phạm vi project không bao gồm đăng nhập, phân quyền hoặc bảo mật tài khoản.

### 13.3 Luồng minh họa

```text
Main Menu
1. Customer Menu
2. Admin Menu
0. Exit

Chọn: 2

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

### 13.4 Ràng buộc phạm vi

Hệ thống không có:

- Username/password cho Admin.
- Mã nhân viên.
- Audit log — nhật ký thao tác, nghĩa là ghi lại ai đã thực hiện hành động nào.
- Phân quyền bảo mật thật.

Admin/Nhân viên chỉ là vai trò mô phỏng để truy cập nhóm chức năng quản lý và vận hành.

### 13.5 Lý do chọn

Hướng này phù hợp nhất với app CLI môn CSD vì trọng tâm của project là mô phỏng booking queue, priority queue, stack, list, file storage và luồng vận hành rửa xe, không phải xây dựng hệ thống tài khoản.

---

## 14. Quyết định 9 — Cấu trúc Main Menu

### 14.1 Quyết định đã chọn

Chọn hướng **A — Main Menu chỉ có Customer Menu và Admin Menu**.

### 14.2 Cách hiểu chính thức

Main Menu của hệ thống gồm hai nhánh chính:

1. **Customer Menu**
2. **Admin Menu**

Customer Menu dùng cho các thao tác cá nhân của khách hàng sau khi nhập mã khách hàng hợp lệ.

Admin Menu dùng cho các thao tác quản lý dữ liệu và vận hành tiệm, không yêu cầu đăng nhập thật.

### 14.3 Menu tổng đề xuất

```text
Main Menu
1. Customer Menu
2. Admin Menu
0. Exit
```

### 14.4 Luồng vào Customer Menu

```text
Main Menu
1. Customer Menu
2. Admin Menu
0. Exit

Chọn: 1
Nhập mã khách hàng: C001

Nếu mã hợp lệ:
→ Hiển thị Customer Menu

Nếu mã không hợp lệ:
→ Cho phép nhập lại theo giới hạn số lần thử
```

### 14.5 Luồng vào Admin Menu

```text
Main Menu
1. Customer Menu
2. Admin Menu
0. Exit

Chọn: 2
→ Hiển thị Admin Menu ngay
```

### 14.6 Lý do chọn

Hướng này nhất quán với quyết định hệ thống có 2 actor chính. Customer và Admin/Nhân viên có hành vi khác nhau nên menu cũng nên chia theo actor để dễ dùng, dễ demo và dễ map sang Use Case.

---

## 15. Quyết định 10 — Xử lý khi Customer nhập sai mã khách hàng

### 15.1 Quyết định đã chọn

Chọn hướng **B — Cho nhập lại tối đa 3 lần**.

### 15.2 Cách hiểu chính thức

Khi người dùng chọn Customer Menu, hệ thống yêu cầu nhập mã khách hàng.

Nếu mã khách hàng không tồn tại, hệ thống cho phép nhập lại tối đa 3 lần.

Nếu sau 3 lần vẫn không hợp lệ, hệ thống quay lại Main Menu.

### 15.3 Luồng minh họa

```text
Main Menu
1. Customer Menu
2. Admin Menu
0. Exit

Chọn: 1

Nhập mã khách hàng: C999
Không tìm thấy khách hàng. Vui lòng nhập lại. Còn 2 lần thử.

Nhập mã khách hàng: C888
Không tìm thấy khách hàng. Vui lòng nhập lại. Còn 1 lần thử.

Nhập mã khách hàng: C777
Không tìm thấy khách hàng. Quay lại Main Menu.
```

### 15.4 Lý do chọn

Hướng này cân bằng giữa trải nghiệm người dùng và độ đơn giản khi code:

- Không văng người dùng ra Main Menu ngay khi nhập nhầm.
- Không tạo vòng lặp nhập lại vô hạn.
- Dễ viết Use Case với luồng ngoại lệ rõ ràng.
- Dễ demo vì có giới hạn số lần thử cụ thể.

---

## 16. Quyết định 11 — Customer chọn xe khi tạo booking

### 16.1 Quyết định đã chọn

Chọn hướng **B — Hệ thống hiển thị danh sách xe của Customer rồi cho chọn**.

### 16.2 Cách hiểu chính thức

Khi Customer tạo booking, hệ thống chỉ hiển thị danh sách xe thuộc về **currentCustomer**.

Customer chọn một xe trong danh sách này.

Hệ thống không cho phép Customer tạo booking cho xe không thuộc quyền sở hữu của mình.

### 16.3 Luồng minh họa

```text
Xe của bạn:
1. V001 - 51A-12345
2. V002 - 59B-99999
3. V003 - 60C-88888

Chọn xe: 2
```

Hệ thống hiểu Customer chọn xe:

```text
V002 - 59B-99999
```

### 16.4 Lý do chọn

Hướng này tốt nhất vì:

- Customer không cần nhớ mã xe.
- Customer chỉ thấy xe thuộc về mình.
- Giảm nguy cơ chọn nhầm xe của khách hàng khác.
- Dễ demo trong CLI.
- Phù hợp với quyền của Customer đã chốt trước đó.

---

## 17. Quyết định 12 — Xử lý khi Customer chưa có xe

### 17.1 Quyết định đã chọn

Chọn hướng **A — Từ chối tạo booking và yêu cầu liên hệ Admin/Nhân viên thêm xe**.

### 17.2 Cách hiểu chính thức

Nếu Customer hiện tại chưa có xe nào trong hệ thống, hệ thống không cho phép tạo booking.

Hệ thống thông báo Customer cần liên hệ Admin/Nhân viên để thêm xe trước khi đặt lịch rửa xe.

### 17.3 Luồng minh họa

```text
Customer Menu
3. Tạo booking

Bạn chưa có xe nào trong hệ thống.
Vui lòng liên hệ Admin/Nhân viên để thêm xe trước khi tạo booking.
```

### 17.4 Lý do chọn

Hướng này nhất quán với phạm vi đã chốt:

- Customer không tự quản lý xe.
- Admin/Nhân viên là người quản lý xe của khách hàng.
- Booking phải gắn với một xe hợp lệ.
- Không tạo xe giả hoặc dữ liệu không thực tế.

---

## 18. Quyết định 13 — Customer chọn dịch vụ khi tạo booking

### 18.1 Quyết định đã chọn

Chọn hướng **B — Hệ thống hiển thị danh sách dịch vụ rồi cho chọn theo số thứ tự**.

### 18.2 Cách hiểu chính thức

Khi Customer tạo booking, hệ thống hiển thị danh sách dịch vụ hiện có kèm:

- Mã dịch vụ.
- Tên dịch vụ.
- Giá tiền.
- Thời gian thực hiện.

Customer chọn một dịch vụ theo số thứ tự trong danh sách.

Hệ thống chỉ cho phép tạo booking với dịch vụ hợp lệ đang tồn tại trong hệ thống.

### 18.3 Luồng minh họa

```text
Danh sách dịch vụ:
1. S001 - Rửa xe cơ bản - 100,000 VND - 30 phút
2. S002 - Rửa xe cao cấp - 200,000 VND - 45 phút
3. S003 - Rửa xe + phủ bóng - 350,000 VND - 60 phút

Chọn dịch vụ: 2
```

Hệ thống hiểu Customer chọn:

```text
S002 - Rửa xe cao cấp - 200,000 VND - 45 phút
```

### 18.4 Lý do chọn

Hướng này phù hợp nhất vì:

- Customer không cần nhớ mã dịch vụ.
- Dễ dùng và dễ demo.
- Hiển thị được giá tiền trước khi đặt.
- Hiển thị được thời gian thực hiện, yếu tố có thể ảnh hưởng đến việc hệ thống kiểm tra còn đủ thời gian để kéo booking từ waitlist lên sau khi complete.
- Tái sử dụng được danh sách service đang có trong hệ thống.

---

## 19. Quyết định 14 — Customer chọn ngày và buổi khi tạo booking

### 19.1 Quyết định đã chọn

Chọn hướng **B — Hiển thị danh sách ngày hợp lệ theo booking window, rồi chọn ngày**.

### 19.2 Cách hiểu chính thức

Khi Customer tạo booking, hệ thống dựa trên hạng thành viên của Customer hiện tại để sinh danh sách ngày hợp lệ.

Danh sách ngày hợp lệ được tính từ ngày hiện tại mô phỏng đến ngày xa nhất mà Customer được phép đặt trước theo booking window.

Customer chọn ngày từ danh sách này, sau đó chọn buổi rửa xe.

Hệ thống không hiển thị hoặc không cho chọn các ngày vượt quá booking window của Customer.

### 19.3 Booking window theo hạng

| Hạng thành viên | Số ngày đặt trước tối đa |
|---|---:|
| Member | 7 ngày |
| Silver | 10 ngày |
| Gold | 12 ngày |
| Platinum | 14 ngày |

### 19.4 Luồng minh họa

Ví dụ Customer là Member và được đặt trước tối đa 7 ngày:

```text
Các ngày có thể đặt:
1. 2026-06-25
2. 2026-06-26
3. 2026-06-27
4. 2026-06-28
5. 2026-06-29
6. 2026-06-30
7. 2026-07-01
8. 2026-07-02

Chọn ngày: 6

Chọn buổi:
1. Sáng
2. Chiều
3. Tối

Chọn buổi: 1
```

### 19.5 Lý do chọn

Hướng này tốt nhất cho nghiệp vụ của app vì:

- Customer không cần nhớ giới hạn booking window.
- Hệ thống thể hiện rõ quyền lợi theo hạng thành viên.
- Member, Silver, Gold, Platinum sẽ thấy số ngày đặt trước khác nhau.
- Dễ demo cho thầy thấy hạng cao được đặt xa hơn.
- Tránh lỗi nhập ngày vượt giới hạn.
- Làm Use Case tạo booking rõ hơn và chuyên nghiệp hơn.

---

## 20. Cập nhật luồng Customer tạo booking sau các quyết định mới

Sau các quyết định mới, luồng Customer tạo booking nên được hiểu như sau:

1. Người dùng vào Customer Menu.
2. Hệ thống yêu cầu nhập mã khách hàng.
3. Nếu mã khách hàng không hợp lệ, hệ thống cho nhập lại tối đa 3 lần.
4. Nếu mã hợp lệ, hệ thống xác định currentCustomer.
5. Customer chọn chức năng tạo booking.
6. Hệ thống kiểm tra currentCustomer có xe hay không.
7. Nếu chưa có xe, hệ thống từ chối tạo booking và yêu cầu liên hệ Admin/Nhân viên để thêm xe.
8. Nếu có xe, hệ thống hiển thị danh sách xe của currentCustomer.
9. Customer chọn một xe trong danh sách.
10. Hệ thống hiển thị danh sách dịch vụ hiện có.
11. Customer chọn một dịch vụ.
12. Hệ thống sinh danh sách ngày hợp lệ dựa trên booking window của currentCustomer.
13. Customer chọn ngày muốn rửa xe.
14. Customer chọn buổi muốn rửa xe: Sáng, Chiều hoặc Tối.
15. Hệ thống kiểm tra sức chứa của ngày/buổi được chọn.
16. Nếu hợp lệ, hệ thống tạo booking ở trạng thái `WAITING`.
17. Nếu không hợp lệ, hệ thống từ chối và thông báo lý do.

---

## 21. Quyết định 15 — Kiểm tra sức chứa sau khi Customer chọn ngày và buổi

### 21.1 Quyết định đã chọn

Chọn hướng **B — Buổi đã activate thì phân ngay; buổi chưa activate thì chỉ kiểm tra tổng sức chứa**.

### 21.2 Cách hiểu chính thức

Sau khi Customer chọn ngày và buổi, hệ thống kiểm tra trạng thái kích hoạt của buổi đó.

Nếu buổi đã được kích hoạt, booking được đưa trực tiếp vào:

- Slot chính nếu slot chính còn chỗ.
- Waitlist nếu slot chính đã đầy nhưng waitlist còn chỗ.
- Bị từ chối nếu cả slot chính và waitlist đều đầy.

Nếu buổi chưa được kích hoạt, hệ thống chỉ kiểm tra tổng sức chứa của buổi. Booking được lưu dưới dạng booking tương lai và chỉ được phân vào slot chính hoặc waitlist khi Admin/Nhân viên kích hoạt buổi.

### 21.3 Lý do chọn

Hướng này đúng nhất với nghiệp vụ đã chốt:

- Booking cho buổi đang chạy cần được xếp hàng ngay.
- Booking cho buổi tương lai chưa nên chốt slot chính hoặc waitlist ngay.
- Khi activate buổi, hệ thống mới sắp xếp theo ưu tiên hạng thành viên.
- Giữ được ý nghĩa của loyalty và booking window.

### 21.4 Quy tắc liên quan

- Slot chính dùng **Queue — hàng đợi FIFO, nghĩa là vào trước ra trước**.
- Waitlist dùng **Priority Queue — hàng đợi ưu tiên**.
- Booking tương lai chỉ được phân bổ khi activate buổi.

---

## 22. Quyết định 16 — Thông báo sau khi tạo booking thành công

### 22.1 Quyết định đã chọn

Chọn hướng **B — Thông báo rõ trạng thái xếp chỗ của booking**.

### 22.2 Cách hiểu chính thức

Khi booking được tạo thành công, hệ thống hiển thị:

- Mã booking.
- Trạng thái booking.
- Vị trí xử lý hiện tại.

Nếu booking thuộc buổi đã kích hoạt, hệ thống thông báo booking nằm trong slot chính hoặc hàng chờ phụ.

Nếu booking thuộc buổi chưa kích hoạt, hệ thống thông báo booking đang là booking tương lai và sẽ được phân bổ khi buổi được kích hoạt.

### 22.3 Ví dụ thông báo

Trường hợp booking vào slot chính:

```text
Tạo booking thành công.
Mã booking: B001
Trạng thái: WAITING
Vị trí: Slot chính của buổi hiện tại.
```

Trường hợp booking vào waitlist:

```text
Tạo booking thành công.
Mã booking: B002
Trạng thái: WAITING
Vị trí: Hàng chờ phụ.
Lưu ý: Booking sẽ được đẩy lên slot chính nếu có chỗ trống phù hợp.
```

Trường hợp booking tương lai:

```text
Tạo booking thành công.
Mã booking: B003
Trạng thái: WAITING
Vị trí: Booking tương lai, chờ kích hoạt buổi.
Lưu ý: Booking sẽ được xếp vào slot chính hoặc hàng chờ phụ khi buổi được kích hoạt.
```

### 22.4 Lý do chọn

Hướng này rõ ràng hơn việc chỉ báo tạo booking thành công. Customer hiểu booking của mình đang ở đâu nhưng hệ thống không cần thông báo số thứ tự cụ thể, tránh gây hiểu nhầm khi Priority Queue thay đổi thứ tự ưu tiên.

---

## 23. Quyết định 17 — Ưu tiên trong waitlist khi buổi đã activate

### 23.1 Quyết định đã chọn

Chọn hướng **B — Waitlist ưu tiên theo hạng, nếu cùng hạng thì theo thời gian tạo booking**.

### 23.2 Cách hiểu chính thức

Khi booking được đưa vào **Waitlist — hàng chờ phụ**, hệ thống sắp xếp theo độ ưu tiên của hạng thành viên.

Booking của khách có hạng cao hơn được ưu tiên trước.

Nếu nhiều booking có cùng hạng, booking được tạo sớm hơn được ưu tiên trước.

### 23.3 Thứ tự ưu tiên hạng

```text
Platinum > Gold > Silver > Member
```

### 23.4 Ví dụ

```text
Member đặt lúc 08:00
Platinum đặt lúc 08:05
Gold đặt lúc 08:10
Silver đặt lúc 08:15
```

Thứ tự trong waitlist:

```text
1. Platinum - 08:05
2. Gold - 08:10
3. Silver - 08:15
4. Member - 08:00
```

Nếu cùng hạng:

```text
Gold A đặt lúc 08:00
Gold B đặt lúc 08:10
```

Thứ tự:

```text
1. Gold A - 08:00
2. Gold B - 08:10
```

### 23.5 Lý do chọn

Hướng này nhất quán với thiết kế **Priority Queue — hàng đợi ưu tiên**. Hạng thành viên có giá trị thực tế trong việc được kéo từ waitlist lên slot chính. Nếu cùng hạng thì xét thời gian tạo booking để đảm bảo công bằng.

---

## 24. Quyết định 18 — Ưu tiên booking tương lai khi activate buổi

### 24.1 Quyết định đã chọn

Chọn hướng **B — Theo hạng thành viên trước, nếu cùng hạng thì theo thời gian tạo booking**.

### 24.2 Cách hiểu chính thức

Khi Admin/Nhân viên kích hoạt buổi rửa xe, hệ thống lấy toàn bộ booking tương lai của ngày/buổi đó và sắp xếp theo độ ưu tiên.

Tiêu chí sắp xếp:

1. Hạng thành viên cao hơn được ưu tiên trước.
2. Nếu cùng hạng, booking được tạo sớm hơn được ưu tiên trước.

Sau khi sắp xếp, hệ thống đưa lần lượt các booking ưu tiên cao vào slot chính cho đến khi đầy. Các booking tiếp theo được đưa vào waitlist nếu waitlist còn chỗ.

### 24.3 Ví dụ

Buổi sáng có:

```text
Slot chính: 10
Waitlist: 3
Tổng sức chứa: 13
```

Nếu có 13 booking tương lai hợp lệ, hệ thống xử lý:

```text
10 booking ưu tiên cao nhất → slot chính.
3 booking tiếp theo → waitlist.
```

### 24.4 Lý do chọn

Hướng này nhất quán với cơ chế loyalty:

- Hạng thành viên có giá trị thật.
- Khách hạng cao có cơ hội vào slot chính cao hơn khi buổi được kích hoạt.
- Khách cùng hạng vẫn được xét công bằng theo thời gian tạo booking.
- Không cần chia quota phức tạp theo từng hạng.

---

## 25. Quyết định 19 — Xử lý booking tương lai khi buổi đã đầy

### 25.1 Quyết định đã chọn

Chọn hướng **D — Vẫn hiển thị ngày/buổi đó, nhưng nếu sức chứa đã đầy thì báo rõ và không cho chọn để tạo booking**.

### 25.2 Cách hiểu chính thức

Khi Customer chọn ngày và buổi để tạo booking, hệ thống vẫn có thể hiển thị các ngày/buổi nằm trong booking window.

Tuy nhiên, nếu một buổi đã đạt tổng sức chứa tối đa, hệ thống hiển thị trạng thái **Đã đầy** bên cạnh buổi đó và không cho tạo booking vào buổi này.

Customer cần chọn ngày khác hoặc buổi khác còn sức chứa.

### 25.3 Luồng minh họa

```text
Các ngày có thể đặt:

1. 2026-06-25
   - Sáng: Còn chỗ
   - Chiều: Đã đầy — vui lòng chọn buổi khác
   - Tối: Còn chỗ

2. 2026-06-26
   - Sáng: Đã đầy — vui lòng chọn buổi khác
   - Chiều: Còn chỗ
   - Tối: Còn chỗ
```

Nếu Customer chọn buổi đã đầy:

```text
Buổi sáng ngày 2026-06-26 đã đầy.
Vui lòng chọn ngày khác hoặc buổi khác.
```

### 25.4 Lý do chọn

Hướng này tốt hơn việc ẩn buổi đã đầy vì Customer thấy rõ lý do không đặt được.

Cần phân biệt rõ:

- Booking window quyết định **ngày nào nằm trong phạm vi được phép đặt**.
- Sức chứa quyết định **buổi nào còn nhận booking được không**.

Một ngày vẫn có thể hợp lệ theo hạng thành viên, nhưng một hoặc nhiều buổi trong ngày đó có thể đã đầy.

---

## 26. Quyết định 20 — Mức hiển thị sức chứa khi chọn ngày/buổi

### 26.1 Quyết định đã chọn

Chọn hướng **B — Hiển thị số lượng đã dùng / tổng sức chứa**.

### 26.2 Cách hiểu chính thức

Khi hiển thị ngày và buổi hợp lệ để Customer tạo booking, hệ thống hiển thị tổng số booking hợp lệ hiện tại trên tổng sức chứa của buổi đó.

Nếu số booking đã đạt tổng sức chứa, buổi được đánh dấu là **Đã đầy** và không được chọn để tạo booking.

### 26.3 Ví dụ

```text
Các ngày có thể đặt:

1. 2026-06-26
   1. Sáng  - 12/13 chỗ đã dùng - Còn chỗ
   2. Chiều - 13/13 chỗ đã dùng - Đã đầy
   3. Tối   - 3/7 chỗ đã dùng  - Còn chỗ

2. 2026-06-27
   1. Sáng  - 13/13 chỗ đã dùng - Đã đầy
   2. Chiều - 8/13 chỗ đã dùng  - Còn chỗ
   3. Tối   - 7/7 chỗ đã dùng   - Đã đầy
```

Nếu Customer chọn buổi đã đầy:

```text
Buổi chiều ngày 2026-06-26 đã đầy.
Vui lòng chọn ngày khác hoặc buổi khác.
```

### 26.4 Lý do chọn

Hướng này rõ hơn việc chỉ hiển thị “Còn chỗ” hoặc “Đã đầy”, nhưng không gây mâu thuẫn như việc hiển thị tách slot chính và waitlist.

Ở bước chọn ngày/buổi, hệ thống chỉ nên hiển thị tổng sức chứa vì booking tương lai chưa được phân vào slot chính hoặc waitlist trước khi activate.

---

## 27. Quyết định 21 — Xử lý khi Customer chọn ngày/buổi đã đầy

### 27.1 Quyết định đã chọn

Chọn hướng **B — Báo lỗi rồi quay lại bước chọn ngày/buổi**.

### 27.2 Cách hiểu chính thức

Nếu Customer chọn một ngày/buổi đã đạt tổng sức chứa, hệ thống thông báo buổi đó đã đầy và yêu cầu Customer chọn lại ngày hoặc buổi khác.

Các thông tin đã chọn trước đó như xe và dịch vụ được giữ nguyên trong luồng tạo booking.

### 27.3 Luồng minh họa

```text
Bạn đã chọn:
Xe: V002 - 59B-99999
Dịch vụ: S002 - Rửa xe cao cấp - 200,000 VND - 45 phút

Chọn ngày/buổi:
2026-06-26 - Chiều - 13/13 chỗ đã dùng - Đã đầy

Thông báo:
Buổi chiều ngày 2026-06-26 đã đầy.
Vui lòng chọn ngày khác hoặc buổi khác.
```

Sau đó hệ thống quay lại bước chọn ngày/buổi:

```text
Các ngày có thể đặt:

1. 2026-06-26
   1. Sáng  - 12/13 chỗ đã dùng - Còn chỗ
   2. Chiều - 13/13 chỗ đã dùng - Đã đầy
   3. Tối   - 3/7 chỗ đã dùng  - Còn chỗ

2. 2026-06-27
   1. Sáng  - 13/13 chỗ đã dùng - Đã đầy
   2. Chiều - 8/13 chỗ đã dùng  - Còn chỗ
   3. Tối   - 7/7 chỗ đã dùng   - Đã đầy
```

### 27.4 Lý do chọn

Hướng này tốt nhất cho trải nghiệm người dùng:

- Không bắt Customer chọn lại xe.
- Không bắt Customer chọn lại dịch vụ.
- Chỉ yêu cầu chọn lại ngày/buổi.
- Phù hợp với CLI.
- Dễ viết thành luồng thay thế trong UC Tạo booking.

---

## 28. Quyết định 22 — Xử lý khi tất cả ngày/buổi trong booking window đều đầy

### 28.1 Quyết định đã chọn

Chọn hướng **A — Vẫn hiển thị toàn bộ nhưng không cho tạo booking**.

### 28.2 Cách hiểu chính thức

Nếu tất cả ngày/buổi trong booking window của Customer đều đã đạt tổng sức chứa, hệ thống vẫn hiển thị danh sách ngày/buổi kèm trạng thái **Đã đầy**, nhưng không cho phép tạo booking.

Hệ thống thông báo Customer cần quay lại sau hoặc liên hệ Admin/Nhân viên.

### 28.3 Luồng minh họa

```text
Các ngày có thể đặt:

1. 2026-06-26
   1. Sáng  - 13/13 chỗ đã dùng - Đã đầy
   2. Chiều - 13/13 chỗ đã dùng - Đã đầy
   3. Tối   - 7/7 chỗ đã dùng   - Đã đầy

2. 2026-06-27
   1. Sáng  - 13/13 chỗ đã dùng - Đã đầy
   2. Chiều - 13/13 chỗ đã dùng - Đã đầy
   3. Tối   - 7/7 chỗ đã dùng   - Đã đầy
```

Thông báo:

```text
Tất cả ngày/buổi trong phạm vi đặt lịch của bạn đã đầy.
Vui lòng quay lại sau hoặc liên hệ Admin/Nhân viên.
```

### 28.4 Lý do chọn

Hướng này minh bạch nhất:

- Không nhận booking vượt sức chứa.
- Không tạo trạng thái mới như pending capacity.
- Customer thấy rõ toàn bộ ngày/buổi trong booking window đều đã đầy.
- Dễ giải thích khi demo.
- Không làm hệ thống bị phình phạm vi.

---

## 29. Cập nhật luồng Customer tạo booking sau quyết định sức chứa

Sau các quyết định mới, luồng Customer tạo booking đầy đủ hơn như sau:

1. Người dùng vào Customer Menu.
2. Hệ thống yêu cầu nhập mã khách hàng.
3. Nếu mã khách hàng không hợp lệ, hệ thống cho nhập lại tối đa 3 lần.
4. Nếu mã hợp lệ, hệ thống xác định currentCustomer.
5. Customer chọn chức năng tạo booking.
6. Hệ thống kiểm tra currentCustomer có xe hay không.
7. Nếu chưa có xe, hệ thống từ chối tạo booking và yêu cầu liên hệ Admin/Nhân viên để thêm xe.
8. Nếu có xe, hệ thống hiển thị danh sách xe của currentCustomer.
9. Customer chọn một xe trong danh sách.
10. Hệ thống hiển thị danh sách dịch vụ hiện có.
11. Customer chọn một dịch vụ.
12. Hệ thống sinh danh sách ngày hợp lệ dựa trên booking window của currentCustomer.
13. Với mỗi ngày/buổi, hệ thống hiển thị số lượng đã dùng trên tổng sức chứa.
14. Nếu tất cả ngày/buổi đều đầy, hệ thống vẫn hiển thị danh sách và thông báo không thể tạo booking.
15. Customer chọn ngày và buổi còn chỗ.
16. Nếu Customer chọn nhầm buổi đã đầy, hệ thống báo lỗi và quay lại bước chọn ngày/buổi, giữ nguyên xe và dịch vụ đã chọn.
17. Hệ thống kiểm tra buổi đã activate hay chưa.
18. Nếu buổi đã activate:
    - Slot chính còn chỗ thì booking vào slot chính.
    - Slot chính đầy nhưng waitlist còn chỗ thì booking vào waitlist.
    - Cả hai đầy thì từ chối.
19. Nếu buổi chưa activate:
    - Hệ thống chỉ kiểm tra tổng sức chứa.
    - Nếu còn chỗ, booking được lưu là booking tương lai.
20. Khi tạo thành công, hệ thống thông báo mã booking, trạng thái và vị trí xử lý hiện tại.
21. Booking mới có trạng thái `WAITING`.

---

## 30. Chi tiết các Use Case đã phân rã

## UC-01 — Xem danh sách dịch vụ

### 1. Actor chính

* Customer

### 2. Mục tiêu

Cho phép Customer xem danh sách các dịch vụ rửa xe hiện có trong hệ thống để biết tên dịch vụ, giá tiền và thời gian thực hiện trước khi tạo booking.

### 3. Tiền điều kiện

* Hệ thống đã được khởi động.
* Danh sách dịch vụ đã được nạp từ file dữ liệu.
* Customer đang ở trong Customer Menu.

### 4. Hậu điều kiện

* Danh sách dịch vụ được hiển thị cho Customer.
* Dữ liệu dịch vụ không bị thay đổi.
* Customer có thể quay lại Customer Menu hoặc tiếp tục chọn chức năng khác.

### 5. Luồng chính

1. Customer chọn chức năng **Xem danh sách dịch vụ** trong Customer Menu.
2. Hệ thống lấy danh sách dịch vụ hiện có.
3. Hệ thống kiểm tra danh sách dịch vụ có dữ liệu hay không.
4. Nếu có dữ liệu, hệ thống hiển thị danh sách dịch vụ cho Customer.
5. Mỗi dịch vụ được hiển thị tối thiểu gồm:

   * Mã dịch vụ.
   * Tên dịch vụ.
   * Giá tiền.
   * Thời gian thực hiện.
6. Customer xem thông tin dịch vụ.
7. Hệ thống quay lại Customer Menu sau khi Customer hoàn tất xem danh sách.

### 6. Luồng thay thế / ngoại lệ

#### 6.1 Danh sách dịch vụ rỗng

1. Customer chọn chức năng **Xem danh sách dịch vụ**.
2. Hệ thống kiểm tra và phát hiện chưa có dịch vụ nào trong hệ thống.
3. Hệ thống hiển thị thông báo:

```text
Hiện chưa có dịch vụ rửa xe nào trong hệ thống.
Vui lòng liên hệ Admin/Nhân viên để được hỗ trợ.
```

4. Hệ thống quay lại Customer Menu.

#### 6.2 File dữ liệu dịch vụ không đọc được

1. Customer chọn chức năng **Xem danh sách dịch vụ**.
2. Hệ thống không thể đọc dữ liệu dịch vụ từ file.
3. Hệ thống hiển thị thông báo lỗi phù hợp.
4. Hệ thống không cho Customer tạo hoặc sửa dịch vụ.
5. Hệ thống quay lại Customer Menu.

### 7. Quy tắc nghiệp vụ liên quan

* Customer chỉ có quyền xem danh sách dịch vụ.
* Customer không được thêm, sửa hoặc xóa dịch vụ.
* Việc quản lý dịch vụ thuộc quyền Admin/Nhân viên.
* Dịch vụ là dữ liệu nền của hệ thống, dùng cho quá trình tạo booking.
* Dịch vụ hợp lệ cần có tối thiểu mã dịch vụ, tên dịch vụ, giá tiền và thời gian thực hiện.

### 8. Cấu trúc dữ liệu liên quan

* **List/ArrayList** dùng để lưu danh sách dịch vụ.
* Hệ thống duyệt danh sách dịch vụ để hiển thị từng dịch vụ cho Customer.
* Không cần Queue, Priority Queue hoặc Stack cho UC này.

### 9. Ví dụ hiển thị CLI

```text
Danh sách dịch vụ:

1. S001 - Rửa xe cơ bản - 100,000 VND - 30 phút
2. S002 - Rửa xe cao cấp - 200,000 VND - 45 phút
3. S003 - Rửa xe + phủ bóng - 350,000 VND - 60 phút

Nhấn Enter để quay lại Customer Menu.
```

### 10. Ghi chú thiết kế

UC này chỉ có mục đích xem dữ liệu, không làm thay đổi trạng thái hệ thống. Vì vậy, đây là Use Case đơn giản và không cần phân rã thêm thành các Use Case nhỏ hơn.

## UC-02 — Xem thông tin cá nhân

### 1. Actor chính

* Customer

### 2. Mục tiêu

Cho phép Customer xem thông tin cá nhân của chính mình trong hệ thống, bao gồm các thông tin định danh và thông tin thành viên liên quan đến quyền lợi đặt lịch.

### 3. Tiền điều kiện

* Hệ thống đã được khởi động.
* Customer đã vào Customer Menu bằng mã khách hàng hợp lệ.
* Hệ thống đã xác định được `currentCustomer`.
* Dữ liệu khách hàng đã được nạp từ file dữ liệu.

### 4. Hậu điều kiện

* Thông tin cá nhân của Customer được hiển thị.
* Dữ liệu khách hàng không bị thay đổi.
* Customer có thể quay lại Customer Menu hoặc tiếp tục chọn chức năng khác.

### 5. Luồng chính

1. Customer chọn chức năng **Xem thông tin cá nhân** trong Customer Menu.
2. Hệ thống lấy thông tin của `currentCustomer`.
3. Hệ thống kiểm tra `currentCustomer` có tồn tại trong danh sách khách hàng hay không.
4. Nếu hợp lệ, hệ thống hiển thị thông tin cá nhân của Customer.
5. Thông tin hiển thị tối thiểu gồm:

   * Mã khách hàng.
   * Họ tên.
   * Số điện thoại.
   * Hạng thành viên.
   * Booking window tương ứng với hạng thành viên.
6. Customer xem thông tin.
7. Hệ thống quay lại Customer Menu sau khi Customer hoàn tất xem thông tin.

### 6. Luồng thay thế / ngoại lệ

#### 6.1 Không tìm thấy currentCustomer

1. Customer chọn chức năng **Xem thông tin cá nhân**.
2. Hệ thống không tìm thấy `currentCustomer` trong danh sách khách hàng.
3. Hệ thống hiển thị thông báo:

```text
Không tìm thấy thông tin khách hàng hiện tại.
Vui lòng quay lại Main Menu và nhập lại mã khách hàng.
```

4. Hệ thống quay lại Main Menu hoặc Customer Menu tùy theo thiết kế xử lý lỗi.

#### 6.2 File dữ liệu khách hàng không đọc được

1. Customer chọn chức năng **Xem thông tin cá nhân**.
2. Hệ thống không thể đọc dữ liệu khách hàng từ file.
3. Hệ thống hiển thị thông báo lỗi phù hợp.
4. Hệ thống không cho Customer sửa thông tin trực tiếp.
5. Hệ thống quay lại Customer Menu.

### 7. Quy tắc nghiệp vụ liên quan

* Customer chỉ được xem thông tin của chính mình.
* Customer không được xem thông tin của khách hàng khác.
* Customer không được tự sửa thông tin cá nhân trong UC này.
* Việc thêm, sửa hoặc xóa thông tin khách hàng thuộc quyền Admin/Nhân viên trong UC-07.
* Booking window được xác định theo hạng thành viên của Customer.

### 8. Booking window theo hạng thành viên

| Hạng thành viên | Số ngày đặt trước tối đa |
| --------------- | -----------------------: |
| Member          |                   7 ngày |
| Silver          |                  10 ngày |
| Gold            |                  12 ngày |
| Platinum        |                  14 ngày |

### 9. Cấu trúc dữ liệu liên quan

* **List/ArrayList** dùng để lưu danh sách khách hàng.
* Có thể dùng **Map** nếu hệ thống muốn tra cứu khách hàng nhanh theo mã khách hàng.
* Không cần Queue, Priority Queue hoặc Stack cho UC này.

### 10. Ví dụ hiển thị CLI

```text
Thông tin cá nhân:

Mã khách hàng: C001
Họ tên: Ngô Gia Long
Số điện thoại: 0909123456
Hạng thành viên: Gold
Booking window: 12 ngày

Nhấn Enter để quay lại Customer Menu.
```

### 11. Ghi chú thiết kế

UC này chỉ có mục đích xem thông tin, không làm thay đổi dữ liệu hệ thống. Vì Customer Menu đã xác định `currentCustomer` từ trước, Customer không cần nhập lại mã khách hàng khi xem thông tin cá nhân.

## UC-04 — Xem booking cá nhân

### 1. Actor chính

* Customer

### 2. Mục tiêu

Cho phép Customer xem các booking hiện tại hoặc booking tương lai của chính mình, từ đó biết lịch rửa xe nào đang chờ xử lý hoặc đang được phục vụ.

### 3. Tiền điều kiện

* Hệ thống đã được khởi động.
* Customer đã vào Customer Menu bằng mã khách hàng hợp lệ.
* Hệ thống đã xác định được `currentCustomer`.
* Dữ liệu booking đã được nạp từ file dữ liệu.

### 4. Hậu điều kiện

* Danh sách booking chưa kết thúc của Customer được hiển thị.
* Dữ liệu booking không bị thay đổi.
* Customer có thể quay lại Customer Menu hoặc tiếp tục chọn chức năng khác.

### 5. Luồng chính

1. Customer chọn chức năng **Xem booking của tôi** trong Customer Menu.
2. Hệ thống lấy mã khách hàng của `currentCustomer`.
3. Hệ thống lọc danh sách booking theo mã khách hàng hiện tại.
4. Hệ thống chỉ lấy các booking chưa kết thúc, gồm:
   * `WAITING`
   * `SERVING`
5. Hệ thống kiểm tra Customer có booking chưa kết thúc nào hay không.
6. Nếu có, hệ thống hiển thị danh sách booking của Customer.
7. Mỗi booking nên hiển thị tối thiểu:
   * Mã booking.
   * Biển số xe.
   * Tên dịch vụ.
   * Ngày rửa xe.
   * Buổi rửa xe.
   * Trạng thái booking.
   * Vị trí xử lý hiện tại nếu có.
8. Customer xem danh sách booking.
9. Hệ thống quay lại Customer Menu sau khi Customer hoàn tất xem danh sách.

### 6. Luồng thay thế / ngoại lệ

#### 6.1 Customer không có booking đang chờ hoặc đang phục vụ

1. Customer chọn chức năng **Xem booking của tôi**.
2. Hệ thống lọc danh sách booking theo `currentCustomer`.
3. Hệ thống không tìm thấy booking nào thuộc trạng thái `WAITING` hoặc `SERVING`.
4. Hệ thống hiển thị thông báo:

```text
Bạn không có booking nào đang chờ hoặc đang được phục vụ.
```

5. Hệ thống quay lại Customer Menu.

#### 6.2 File dữ liệu booking không đọc được

1. Customer chọn chức năng **Xem booking của tôi**.
2. Hệ thống không thể đọc dữ liệu booking từ file.
3. Hệ thống hiển thị thông báo lỗi phù hợp.
4. Hệ thống không hiển thị dữ liệu booking không chắc chắn.
5. Hệ thống quay lại Customer Menu.

### 7. Quy tắc nghiệp vụ liên quan

* Customer chỉ được xem booking của chính mình.
* Customer không được xem booking của khách hàng khác.
* UC này chỉ xem dữ liệu, không thay đổi trạng thái booking.
* UC này chỉ hiển thị booking chưa kết thúc gồm `WAITING` và `SERVING`.
* Booking `COMPLETED` được xem trong UC-06 — Xem lịch sử rửa xe cá nhân.
* Booking `CANCELLED` không hiển thị trong UC này để giữ danh sách booking cá nhân gọn.
* Booking ở trạng thái `WAITING` có thể được Customer hủy thông qua UC-05 nếu booking đó thuộc về chính Customer.
* Booking ở trạng thái `SERVING` không được Customer hủy.

### 8. Cấu trúc dữ liệu liên quan

* **List/ArrayList** dùng để lưu danh sách booking.
* Hệ thống duyệt danh sách booking và lọc theo:
  * Mã khách hàng của `currentCustomer`.
  * Trạng thái `WAITING` hoặc `SERVING`.
* Có thể dùng **Map** nếu hệ thống muốn tra cứu booking nhanh theo mã khách hàng.
* Không cần Queue, Priority Queue hoặc Stack trực tiếp trong UC này, vì UC này chỉ xem danh sách booking cá nhân.

### 9. Ví dụ hiển thị CLI

```text
Booking của tôi:

1. B001
   Xe: 51A-12345
   Dịch vụ: Rửa xe cao cấp
   Ngày/Buổi: 2026-06-26 - Sáng
   Trạng thái: WAITING
   Vị trí: Booking tương lai, chờ kích hoạt buổi

2. B005
   Xe: 59B-99999
   Dịch vụ: Rửa xe cơ bản
   Ngày/Buổi: 2026-06-25 - Chiều
   Trạng thái: SERVING
   Vị trí: Đang được phục vụ

Nhấn Enter để quay lại Customer Menu.
```

### 10. Ghi chú thiết kế

UC này tập trung vào câu hỏi: **booking hiện tại hoặc booking sắp tới của Customer đang như thế nào?**

Vì vậy, UC này không hiển thị booking `COMPLETED`. Các booking đã hoàn tất được đưa sang UC-06 — Xem lịch sử rửa xe cá nhân để tránh trùng trách nhiệm giữa hai Use Case.

## UC-06 — Xem lịch sử rửa xe cá nhân

### 1. Actor chính

* Customer

### 2. Mục tiêu

Cho phép Customer xem lịch sử các booking đã hoàn tất của chính mình, từ đó biết những lần đã sử dụng dịch vụ rửa xe trong hệ thống.

### 3. Tiền điều kiện

* Hệ thống đã được khởi động.
* Customer đã vào Customer Menu bằng mã khách hàng hợp lệ.
* Hệ thống đã xác định được `currentCustomer`.
* Dữ liệu booking/lịch sử rửa xe đã được nạp từ file dữ liệu.

### 4. Hậu điều kiện

* Lịch sử rửa xe của Customer được hiển thị.
* Dữ liệu lịch sử không bị thay đổi.
* Customer có thể quay lại Customer Menu hoặc tiếp tục chọn chức năng khác.

### 5. Luồng chính

1. Customer chọn chức năng **Xem lịch sử rửa xe của tôi** trong Customer Menu.
2. Hệ thống lấy mã khách hàng của `currentCustomer`.
3. Hệ thống lọc danh sách booking hoặc lịch sử theo mã khách hàng hiện tại.
4. Hệ thống chỉ lấy các booking đã hoàn tất với trạng thái `COMPLETED`.
5. Nếu có lịch sử, hệ thống hiển thị danh sách các lần rửa xe đã hoàn tất.
6. Mỗi bản ghi lịch sử nên hiển thị tối thiểu:

   * Mã booking.
   * Biển số xe.
   * Tên dịch vụ.
   * Ngày rửa xe.
   * Buổi rửa xe.
   * Giá dịch vụ.
   * Trạng thái `COMPLETED`.
7. Customer xem lịch sử.
8. Hệ thống quay lại Customer Menu sau khi Customer hoàn tất xem lịch sử.

### 6. Luồng thay thế / ngoại lệ

#### 6.1 Customer chưa có lịch sử rửa xe

1. Customer chọn chức năng **Xem lịch sử rửa xe của tôi**.
2. Hệ thống lọc dữ liệu theo `currentCustomer`.
3. Hệ thống không tìm thấy booking nào ở trạng thái `COMPLETED`.
4. Hệ thống hiển thị thông báo:

```text
Bạn chưa có lịch sử rửa xe nào.
```

5. Hệ thống quay lại Customer Menu.

#### 6.2 File dữ liệu lịch sử không đọc được

1. Customer chọn chức năng **Xem lịch sử rửa xe của tôi**.
2. Hệ thống không thể đọc dữ liệu booking hoặc lịch sử từ file.
3. Hệ thống hiển thị thông báo lỗi phù hợp.
4. Hệ thống không hiển thị dữ liệu không chắc chắn.
5. Hệ thống quay lại Customer Menu.

### 7. Quy tắc nghiệp vụ liên quan

* Customer chỉ được xem lịch sử rửa xe của chính mình.
* Customer không được xem lịch sử của khách hàng khác.
* Chỉ booking có trạng thái `COMPLETED` mới được xem là lịch sử rửa xe hợp lệ.
* Booking `WAITING` và `SERVING` thuộc UC-04 — Xem booking cá nhân.
* Booking `CANCELLED` không được tính là lịch sử rửa xe hoàn tất.
* UC này chỉ xem dữ liệu, không thay đổi trạng thái booking.

### 8. Cấu trúc dữ liệu liên quan

* **List/ArrayList** dùng để lưu danh sách booking hoặc danh sách lịch sử rửa xe.
* Hệ thống duyệt danh sách và lọc theo:

  * Mã khách hàng của `currentCustomer`.
  * Trạng thái `COMPLETED`.
* Không cần Queue, Priority Queue hoặc Stack trực tiếp trong UC này.

### 9. Ví dụ hiển thị CLI

```text
Lịch sử rửa xe của tôi:

1. B002
   Xe: 59B-99999
   Dịch vụ: Rửa xe cơ bản
   Ngày/Buổi: 2026-06-25 - Chiều
   Giá: 100,000 VND
   Trạng thái: COMPLETED

2. B007
   Xe: 51A-12345
   Dịch vụ: Rửa xe cao cấp
   Ngày/Buổi: 2026-06-20 - Sáng
   Giá: 200,000 VND
   Trạng thái: COMPLETED

Nhấn Enter để quay lại Customer Menu.
```

### 10. Ghi chú thiết kế

UC này nên tách khỏi UC-04 để phân biệt rõ:

* **UC-04 — Xem booking cá nhân**: xem các booking đang chờ hoặc đang được phục vụ.
* **UC-06 — Xem lịch sử rửa xe cá nhân**: xem các booking đã hoàn tất.

Cách tách này giúp Customer Menu rõ nghĩa hơn và tránh danh sách booking cá nhân bị quá dài.

## UC-12 — Xem hàng chờ và booking theo buổi

### 1. Actor chính

* Admin/Nhân viên

### 2. Mục tiêu

Cho phép Admin/Nhân viên xem danh sách booking đang chờ xử lý trong một ngày/buổi cụ thể, bao gồm slot chính và hàng chờ phụ nếu buổi đó đã được kích hoạt.

### 3. Tiền điều kiện

* Hệ thống đã được khởi động.
* Admin/Nhân viên đang ở trong Admin Menu.
* Dữ liệu booking đã được nạp từ file dữ liệu.
* Ngày/buổi cần xem tồn tại trong hệ thống.

### 4. Hậu điều kiện

* Danh sách hàng chờ được hiển thị cho Admin/Nhân viên.
* Dữ liệu booking không bị thay đổi.
* Admin/Nhân viên có thể quay lại Admin Menu hoặc tiếp tục chọn chức năng khác.

### 5. Luồng chính

1. Admin/Nhân viên chọn chức năng **Xem hàng chờ / booking theo buổi** trong Admin Menu.
2. Hệ thống yêu cầu Admin/Nhân viên chọn ngày cần xem.
3. Hệ thống yêu cầu Admin/Nhân viên chọn buổi cần xem: Sáng, Chiều hoặc Tối.
4. Hệ thống kiểm tra ngày/buổi đó đã được kích hoạt hay chưa.
5. Nếu buổi đã được kích hoạt, hệ thống hiển thị:

   * Slot chính.
   * Hàng chờ phụ.
6. Với mỗi booking trong slot chính hoặc hàng chờ phụ, hệ thống hiển thị tối thiểu:

   * Mã booking.
   * Khách hàng.
   * Biển số xe.
   * Dịch vụ.
   * Hạng thành viên.
   * Trạng thái booking.
7. Hệ thống quay lại Admin Menu sau khi Admin/Nhân viên hoàn tất xem hàng chờ.

### 6. Luồng thay thế / ngoại lệ

#### 6.1 Buổi chưa được kích hoạt

1. Admin/Nhân viên chọn ngày/buổi cần xem.
2. Hệ thống phát hiện buổi đó chưa được kích hoạt.
3. Hệ thống hiển thị danh sách booking tương lai của ngày/buổi đó nếu có.
4. Hệ thống thông báo:

```text
Buổi này chưa được kích hoạt.
Các booking dưới đây là booking tương lai, chưa được phân vào slot chính hoặc hàng chờ phụ.
```

5. Hệ thống quay lại Admin Menu sau khi Admin/Nhân viên xem xong.

#### 6.2 Không có booking nào trong ngày/buổi được chọn

1. Admin/Nhân viên chọn ngày/buổi cần xem.
2. Hệ thống không tìm thấy booking nào thuộc ngày/buổi đó.
3. Hệ thống hiển thị thông báo:

```text
Không có booking nào trong ngày/buổi được chọn.
```

4. Hệ thống quay lại Admin Menu.

#### 6.3 File dữ liệu booking không đọc được

1. Admin/Nhân viên chọn chức năng **Xem hàng chờ**.
2. Hệ thống không thể đọc dữ liệu booking từ file.
3. Hệ thống hiển thị thông báo lỗi phù hợp.
4. Hệ thống không hiển thị dữ liệu không chắc chắn.
5. Hệ thống quay lại Admin Menu.

### 7. Quy tắc nghiệp vụ liên quan

* Chỉ Admin/Nhân viên được xem hàng chờ hệ thống.
* Customer không được xem hàng chờ tổng của tiệm.
* Nếu buổi đã activate, hệ thống hiển thị slot chính và waitlist.
* Nếu buổi chưa activate, hệ thống chỉ hiển thị booking tương lai, không gọi đó là slot chính hoặc waitlist.
* UC này chỉ xem dữ liệu, không thay đổi trạng thái booking.
* Slot chính chỉ chứa booking đang chờ phục vụ trong buổi đã kích hoạt.
* Waitlist chứa các booking chờ phụ, được ưu tiên theo hạng thành viên và thời gian tạo booking.

### 8. Cấu trúc dữ liệu liên quan

* **Queue** dùng cho slot chính.
* **Priority Queue** dùng cho waitlist.
* **List/ArrayList** dùng để lưu danh sách booking tổng hoặc booking tương lai.
* UC này chỉ đọc dữ liệu từ các cấu trúc trên, không thêm, xóa hoặc thay đổi thứ tự booking.

### 9. Ví dụ hiển thị CLI

#### 9.1 Buổi đã kích hoạt

```text
Hàng chờ ngày 2026-06-26 - Buổi sáng

Slot chính:
1. B001 - C001 Ngô Gia Long - 51A-12345 - Rửa xe cao cấp - Gold - WAITING
2. B002 - C003 Nguyễn Văn A - 59B-99999 - Rửa xe cơ bản - Member - WAITING

Hàng chờ phụ:
1. B011 - C005 Trần Văn B - 60C-88888 - Rửa xe cao cấp - Platinum - WAITING
2. B012 - C006 Lê Văn C - 61D-77777 - Rửa xe cơ bản - Gold - WAITING

Nhấn Enter để quay lại Admin Menu.
```

#### 9.2 Buổi chưa kích hoạt

```text
Booking tương lai ngày 2026-06-27 - Buổi chiều

Buổi này chưa được kích hoạt.
Các booking dưới đây chưa được phân vào slot chính hoặc hàng chờ phụ.

1. B020 - C001 Ngô Gia Long - 51A-12345 - Rửa xe cao cấp - Gold - WAITING
2. B021 - C004 Phạm Văn D - 62E-55555 - Rửa xe cơ bản - Silver - WAITING

Nhấn Enter để quay lại Admin Menu.
```

### 10. Ghi chú thiết kế

UC này giúp Admin/Nhân viên quan sát trạng thái vận hành của từng ngày/buổi. Tuy nhiên, UC này không xử lý booking tiếp theo, không kích hoạt buổi và không thay đổi trạng thái booking. Các thao tác đó thuộc UC-11, UC-13, UC-14 và UC-15.

## UC-07 — Quản lý khách hàng

### 1. Actor chính

* Admin/Nhân viên

### 2. Mục tiêu

Cho phép Admin/Nhân viên quản lý dữ liệu khách hàng trong hệ thống, bao gồm xem danh sách khách hàng, tìm kiếm khách hàng, thêm khách hàng mới, cập nhật thông tin khách hàng và xóa khách hàng khi khách hàng chưa có dữ liệu liên kết.

### 3. Tiền điều kiện

* Hệ thống đã được khởi động.
* Admin/Nhân viên đang ở trong Admin Menu.
* Dữ liệu khách hàng đã được nạp từ file dữ liệu.

### 4. Hậu điều kiện

* Nếu Admin/Nhân viên chỉ xem hoặc tìm kiếm khách hàng, dữ liệu không thay đổi.
* Nếu Admin/Nhân viên thêm, cập nhật hoặc xóa khách hàng hợp lệ, dữ liệu khách hàng được thay đổi và lưu lại vào file.
* Hệ thống quay lại menu quản lý khách hàng hoặc Admin Menu sau khi thao tác hoàn tất.

### 5. Luồng chính

1. Admin/Nhân viên chọn chức năng **Quản lý khách hàng** trong Admin Menu.
2. Hệ thống hiển thị menu quản lý khách hàng.
3. Admin/Nhân viên chọn một thao tác:
   * Xem danh sách khách hàng.
   * Tìm kiếm khách hàng.
   * Thêm khách hàng mới.
   * Cập nhật thông tin khách hàng.
   * Xóa khách hàng.
   * Quay lại Admin Menu.
4. Hệ thống thực hiện thao tác tương ứng.
5. Nếu thao tác làm thay đổi dữ liệu, hệ thống kiểm tra dữ liệu hợp lệ.
6. Nếu dữ liệu hợp lệ, hệ thống lưu thay đổi xuống file.
7. Hệ thống thông báo kết quả thao tác.
8. Hệ thống quay lại menu quản lý khách hàng.

### 6. Luồng con

#### 6.1 Xem danh sách khách hàng

1. Admin/Nhân viên chọn **Xem danh sách khách hàng**.
2. Hệ thống lấy danh sách khách hàng hiện có.
3. Nếu danh sách không rỗng, hệ thống hiển thị danh sách khách hàng.
4. Mỗi khách hàng nên hiển thị tối thiểu:
   * Mã khách hàng.
   * Họ tên.
   * Số điện thoại.
   * Hạng thành viên.
5. Hệ thống quay lại menu quản lý khách hàng.

#### 6.2 Tìm kiếm khách hàng

1. Admin/Nhân viên chọn **Tìm kiếm khách hàng**.
2. Hệ thống yêu cầu nhập từ khóa tìm kiếm.
3. Admin/Nhân viên nhập mã khách hàng, tên hoặc số điện thoại.
4. Hệ thống tìm khách hàng phù hợp.
5. Nếu có kết quả, hệ thống hiển thị danh sách khách hàng phù hợp.
6. Nếu không có kết quả, hệ thống thông báo không tìm thấy khách hàng.
7. Hệ thống quay lại menu quản lý khách hàng.

#### 6.3 Thêm khách hàng mới

1. Admin/Nhân viên chọn **Thêm khách hàng mới**.
2. Hệ thống yêu cầu nhập thông tin khách hàng.
3. Admin/Nhân viên nhập:
   * Họ tên.
   * Số điện thoại.
4. Hệ thống kiểm tra dữ liệu hợp lệ.
5. Nếu hợp lệ, hệ thống tạo mã khách hàng mới.
6. Hệ thống gán hạng thành viên mặc định là `Member`.
7. Hệ thống thêm khách hàng mới vào danh sách khách hàng.
8. Hệ thống lưu dữ liệu xuống file.
9. Hệ thống thông báo thêm khách hàng thành công.

#### 6.4 Cập nhật thông tin khách hàng

1. Admin/Nhân viên chọn **Cập nhật thông tin khách hàng**.
2. Hệ thống yêu cầu tìm hoặc nhập mã khách hàng cần cập nhật.
3. Hệ thống kiểm tra khách hàng có tồn tại không.
4. Nếu tồn tại, hệ thống hiển thị thông tin hiện tại.
5. Admin/Nhân viên nhập thông tin mới cần cập nhật.
6. Hệ thống kiểm tra dữ liệu hợp lệ.
7. Nếu hợp lệ, hệ thống cập nhật thông tin khách hàng.
8. Hệ thống lưu dữ liệu xuống file.
9. Hệ thống thông báo cập nhật thành công.

#### 6.5 Xóa khách hàng

1. Admin/Nhân viên chọn **Xóa khách hàng**.
2. Hệ thống yêu cầu nhập mã khách hàng cần xóa.
3. Hệ thống kiểm tra khách hàng có tồn tại không.
4. Nếu khách hàng tồn tại, hệ thống kiểm tra khách hàng có dữ liệu liên kết hay không:
   * Có xe trong hệ thống không.
   * Có booking trong hệ thống không.
   * Có lịch sử rửa xe không.
5. Nếu khách hàng không có dữ liệu liên kết, hệ thống yêu cầu Admin/Nhân viên xác nhận xóa.
6. Nếu Admin/Nhân viên xác nhận, hệ thống xóa khách hàng khỏi danh sách khách hàng.
7. Hệ thống lưu thay đổi xuống file.
8. Hệ thống thông báo xóa khách hàng thành công.

### 7. Luồng thay thế / ngoại lệ

#### 7.1 Danh sách khách hàng rỗng

1. Admin/Nhân viên chọn **Xem danh sách khách hàng**.
2. Hệ thống phát hiện chưa có khách hàng nào.
3. Hệ thống hiển thị thông báo:

```text
Hiện chưa có khách hàng nào trong hệ thống.
```

4. Hệ thống quay lại menu quản lý khách hàng.

#### 7.2 Không tìm thấy khách hàng

1. Admin/Nhân viên tìm kiếm hoặc nhập mã khách hàng.
2. Hệ thống không tìm thấy khách hàng phù hợp.
3. Hệ thống hiển thị thông báo:

```text
Không tìm thấy khách hàng phù hợp.
```

4. Hệ thống quay lại menu quản lý khách hàng.

#### 7.3 Số điện thoại bị trùng

1. Admin/Nhân viên thêm hoặc cập nhật khách hàng.
2. Hệ thống phát hiện số điện thoại đã tồn tại trong hệ thống.
3. Hệ thống từ chối thao tác.
4. Hệ thống hiển thị thông báo:

```text
Số điện thoại đã tồn tại trong hệ thống.
Vui lòng kiểm tra lại thông tin khách hàng.
```

5. Hệ thống yêu cầu nhập lại hoặc quay lại menu quản lý khách hàng.

#### 7.4 Dữ liệu nhập không hợp lệ

1. Admin/Nhân viên nhập thiếu tên, thiếu số điện thoại hoặc nhập sai định dạng.
2. Hệ thống từ chối thao tác.
3. Hệ thống hiển thị thông báo lỗi phù hợp.
4. Hệ thống yêu cầu nhập lại.

#### 7.5 Không thể xóa khách hàng vì có dữ liệu liên kết

1. Admin/Nhân viên chọn **Xóa khách hàng**.
2. Hệ thống tìm thấy khách hàng cần xóa.
3. Hệ thống phát hiện khách hàng đã có xe, booking hoặc lịch sử rửa xe.
4. Hệ thống từ chối xóa khách hàng.
5. Hệ thống hiển thị thông báo:

```text
Không thể xóa khách hàng này vì khách hàng đã có dữ liệu liên kết.
Vui lòng giữ hồ sơ khách hàng để đảm bảo dữ liệu booking và lịch sử không bị sai lệch.
```

6. Hệ thống quay lại menu quản lý khách hàng.

#### 7.6 Admin/Nhân viên hủy xác nhận xóa

1. Hệ thống yêu cầu xác nhận xóa khách hàng.
2. Admin/Nhân viên chọn không xác nhận.
3. Hệ thống không xóa khách hàng.
4. Hệ thống quay lại menu quản lý khách hàng.

#### 7.7 File dữ liệu khách hàng không đọc hoặc không ghi được

1. Admin/Nhân viên thực hiện thao tác quản lý khách hàng.
2. Hệ thống không thể đọc hoặc ghi file dữ liệu khách hàng.
3. Hệ thống hiển thị thông báo lỗi phù hợp.
4. Nếu chưa ghi thành công, hệ thống không xác nhận thao tác thành công.

### 8. Quy tắc nghiệp vụ liên quan

* Chỉ Admin/Nhân viên được quản lý khách hàng.
* Customer không được tự thêm, sửa hoặc xóa thông tin khách hàng.
* Mỗi khách hàng có một mã khách hàng duy nhất.
* Số điện thoại khách hàng không nên bị trùng để tránh tạo nhiều hồ sơ cho cùng một người.
* Khách hàng mới mặc định có hạng thành viên `Member`.
* Hạng thành viên không nên sửa thủ công trong UC này nếu hệ thống có cơ chế xét hạng tự động.
* Thông tin khách hàng được dùng cho tạo booking, xem lịch sử và tính booking window.
* Admin/Nhân viên chỉ được xóa khách hàng nếu khách hàng chưa có xe, chưa có booking và chưa có lịch sử rửa xe.
* Nếu khách hàng đã có dữ liệu liên kết, hệ thống phải từ chối xóa để tránh làm sai lệch dữ liệu.

### 9. Cấu trúc dữ liệu liên quan

* **List/ArrayList** dùng để lưu danh sách khách hàng.
* Có thể dùng **Map** để tra cứu nhanh khách hàng theo mã khách hàng hoặc số điện thoại.
* Khi xóa khách hàng, hệ thống cần kiểm tra dữ liệu liên kết trong:
  * Danh sách xe.
  * Danh sách booking.
  * Danh sách lịch sử rửa xe nếu tách riêng.
* Không cần Queue, Priority Queue hoặc Stack cho UC này.

### 10. Ví dụ menu CLI

```text
Quản lý khách hàng

1. Xem danh sách khách hàng
2. Tìm kiếm khách hàng
3. Thêm khách hàng mới
4. Cập nhật thông tin khách hàng
5. Xóa khách hàng
0. Quay lại Admin Menu
```

### 11. Ví dụ hiển thị khách hàng

```text
Danh sách khách hàng:

1. C001 - Ngô Gia Long - 0909123456 - Gold
2. C002 - Nguyễn Văn A - 0988888888 - Member
3. C003 - Trần Văn B - 0911222333 - Silver
```

### 12. Ghi chú thiết kế

UC này tập trung vào quản lý hồ sơ khách hàng cơ bản. Không nên cho Admin/Nhân viên sửa trực tiếp điểm tích lũy hoặc hạng thành viên trong UC này nếu hệ thống có cơ chế loyalty riêng.

Việc xóa khách hàng chỉ áp dụng cho trường hợp nhập nhầm hoặc tạo nhầm hồ sơ khách hàng chưa phát sinh dữ liệu liên kết. Nếu khách hàng đã có xe, booking hoặc lịch sử, hệ thống phải giữ hồ sơ để đảm bảo dữ liệu không bị đứt liên kết.

## Kết luận / trạng thái hiện tại

Các quyết định đã chốt:

1. Hệ thống có **2 actor chính**: Customer và Admin/Nhân viên.
2. Customer được **xem dịch vụ, xem thông tin cá nhân, tạo booking, xem booking, hủy booking của mình, xem lịch sử cá nhân**.
3. Admin/Nhân viên được **quản lý dữ liệu và vận hành toàn bộ booking**.
4. Use Case được viết theo **hành vi chính của actor**, không quá tổng quát và không quá vụn.
5. UC **Tạo booking** là một UC chính, có biến thể theo Customer/Admin.
6. UC **Hủy booking** là một UC chính, có biến thể theo Customer/Admin.
7. Customer vào Customer Menu bằng cách **nhập mã khách hàng một lần** để xác định currentCustomer.
8. Admin/Nhân viên vào Admin Menu **trực tiếp**, không cần tài khoản hoặc mật khẩu.
9. Main Menu chỉ gồm **Customer Menu**, **Admin Menu** và **Exit**.
10. Nếu Customer nhập sai mã khách hàng, hệ thống cho nhập lại tối đa **3 lần**.
11. Khi Customer tạo booking, hệ thống **hiển thị danh sách xe thuộc về Customer hiện tại** để chọn.
12. Nếu Customer chưa có xe, hệ thống **từ chối tạo booking** và yêu cầu liên hệ Admin/Nhân viên để thêm xe.
13. Khi Customer chọn dịch vụ, hệ thống **hiển thị danh sách dịch vụ** kèm mã, tên, giá và thời gian thực hiện.
14. Khi Customer chọn ngày đặt lịch, hệ thống **hiển thị danh sách ngày hợp lệ theo booking window** dựa trên hạng thành viên.
15. Sau khi chọn ngày/buổi, hệ thống kiểm tra **buổi đã activate hay chưa** để quyết định phân ngay hoặc lưu booking tương lai.
16. Khi tạo booking thành công, hệ thống thông báo rõ **mã booking, trạng thái và vị trí xử lý hiện tại**.
17. Waitlist ưu tiên theo **hạng thành viên trước, thời gian tạo booking sau**.
18. Booking tương lai khi activate cũng ưu tiên theo **hạng thành viên trước, thời gian tạo booking sau**.
19. Ngày/buổi đã đầy vẫn được hiển thị, nhưng được đánh dấu **Đã đầy** và không cho tạo booking.
20. Khi hiển thị sức chứa, hệ thống dùng dạng **số chỗ đã dùng / tổng sức chứa**.
21. Nếu Customer chọn nhầm buổi đã đầy, hệ thống báo lỗi và quay lại bước chọn ngày/buổi, giữ nguyên xe và dịch vụ đã chọn.
22. Nếu tất cả ngày/buổi trong booking window đều đầy, hệ thống vẫn hiển thị toàn bộ nhưng không cho tạo booking.
23. UC-04 **Xem booking cá nhân** chỉ hiển thị booking chưa kết thúc gồm `WAITING` và `SERVING`.
24. UC-06 **Xem lịch sử rửa xe cá nhân** chỉ hiển thị booking `COMPLETED`.
25. UC-12 được đổi thành **Xem hàng chờ và booking theo buổi** để bao quát cả buổi đã activate và buổi chưa activate.
26. UC-07 **Quản lý khách hàng** cho phép xóa khách hàng chỉ khi khách chưa có xe, booking hoặc lịch sử rửa xe.

Các Use Case đã phân rã chi tiết trong file hiện tại:

- UC-01 — Xem danh sách dịch vụ.
- UC-02 — Xem thông tin cá nhân.
- UC-04 — Xem booking cá nhân.
- UC-06 — Xem lịch sử rửa xe cá nhân.
- UC-12 — Xem hàng chờ và booking theo buổi.
- UC-07 — Quản lý khách hàng.

Bước tiếp theo nên tiếp tục phân rã **UC-08 — Quản lý xe**.

