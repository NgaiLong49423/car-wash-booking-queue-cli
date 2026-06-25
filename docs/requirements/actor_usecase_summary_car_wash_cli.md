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
7. Xem hàng chờ
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
| UC-12 | Xem hàng chờ | Admin/Nhân viên |
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
7. Xem hàng chờ
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

## 21. Kết luận

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

Đây là nền phù hợp để tiếp tục viết Use Case chi tiết, đặc biệt là UC-03 **Tạo booking**, rồi bổ sung vào SRSV2.md.
