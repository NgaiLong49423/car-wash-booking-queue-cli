# SRS.md — Smart Car Wash Queue Management System

| Item | Value |
|---|---|
| Project name | Smart Car Wash Queue Management System |
| Subject | CSD — Cấu trúc dữ liệu và giải thuật |
| Version | 0.2.2 |
| Status | Draft |
| Main focus | Quản lý hàng chờ và đặt lịch rửa xe |
| Source idea | Rút gọn từ đề tài PRJ301 AutoWash Pro |

---

## 1. Giới thiệu

### 1.1 Mục đích tài liệu

Tài liệu này mô tả yêu cầu phần mềm cho project **Smart Car Wash Queue Management System**.

Project mô phỏng hoạt động đặt lịch và xếp hàng rửa xe trong một tiệm rửa xe. Hệ thống tập trung vào việc quản lý khách hàng, xe, dịch vụ, lịch đặt rửa xe, hàng chờ theo từng buổi, lịch sử rửa xe và cập nhật hạng thành viên sau khi khách hoàn tất dịch vụ.

Tài liệu này chỉ mô tả **chức năng, luồng xử lý, lý do cần có chức năng và phạm vi nghiệp vụ**. Tài liệu không đi vào chi tiết code, thuộc tính class, cách cài đặt cụ thể hoặc cấu trúc dữ liệu cụ thể.

### 1.2 Mục tiêu hệ thống

Hệ thống cần giải quyết bài toán chính:

> Khi nhiều khách hàng đặt lịch rửa xe trong cùng một ngày, hệ thống phải quản lý được ai được nhận vào slot chính, ai phải vào hàng chờ phụ, ai được xử lý trước, và lịch sử sau khi rửa xe được lưu lại như thế nào.

Mục tiêu cụ thể:

- Giúp tiệm rửa xe tiếp nhận booking có giới hạn theo từng buổi.
- Giúp phân biệt booking đặt ngay và booking đặt trước.
- Giúp khách hàng hạng cao có quyền lợi tốt hơn khi đặt lịch trước.
- Giúp nhân viên biết xe nào cần xử lý tiếp theo trong buổi hiện tại.
- Giúp hệ thống lưu lại lịch sử rửa xe sau khi hoàn tất.
- Giúp điểm và hạng thành viên thay đổi sau khi khách sử dụng dịch vụ.

---

## 2. Phạm vi hệ thống

### 2.1 Chức năng nằm trong phạm vi

Hệ thống sẽ bao gồm các nhóm chức năng chính:

1. Quản lý khách hàng.
2. Quản lý xe của khách hàng.
3. Quản lý dịch vụ rửa xe.
4. Tạo booking rửa xe.
5. Kiểm tra quyền đặt trước theo hạng thành viên.
6. Chia lịch rửa xe theo buổi sáng, chiều và tối.
7. Quản lý slot chính và hàng chờ phụ.
8. Kích hoạt buổi rửa xe khi đến thời gian phục vụ.
9. Xử lý booking tiếp theo trong buổi hiện tại.
10. Hoàn tất booking.
11. Hủy booking.
12. Cập nhật điểm và hạng thành viên sau khi hoàn tất booking.
13. Xem lịch sử rửa xe.
14. Hoàn tác booking hoàn tất gần nhất nếu nhân viên nhập nhầm.

### 2.2 Chức năng không nằm trong phạm vi

Các chức năng sau không thuộc phạm vi hiện tại:

- Đăng nhập và đăng xuất thật.
- Phân quyền tài khoản thật.
- Giao diện web.
- Ứng dụng mobile.
- Cơ sở dữ liệu SQL Server.
- Thanh toán online.
- Nhận diện biển số bằng hình ảnh.
- Gửi email hoặc thông báo tự động.
- Khuyến mãi nâng cao.
- Đổi điểm lấy quà hoặc giảm giá.
- Điểm hết hạn sau 12 tháng.
- Tự động hạ hạng theo tháng.
- Quản trị cấu hình điểm và hạng thành viên.

Lý do loại bỏ: project này cần tập trung vào phạm vi mô phỏng nghiệp vụ rửa xe và luồng xử lý booking. Các chức năng trên phù hợp với hệ thống web lớn hơn, nhưng sẽ làm project CSD bị quá rộng.

---

## 3. Thuật ngữ nghiệp vụ

| Thuật ngữ | Ý nghĩa |
|---|---|
| Customer | Khách hàng sử dụng dịch vụ rửa xe |
| Vehicle | Xe của khách hàng |
| Service | Dịch vụ rửa xe |
| Booking | Lịch đặt rửa xe |
| Service period | Buổi phục vụ trong ngày |
| Current period | Buổi đang diễn ra |
| Future period | Buổi chưa diễn ra |
| Main slot | Suất rửa xe chính, gần như chắc chắn được phục vụ |
| Waitlist | Hàng chờ phụ khi suất chính đã đầy |
| Tier | Hạng thành viên của khách hàng |
| Booking window | Số ngày tối đa khách được phép đặt trước |
| Loyalty | Cơ chế tích điểm và nâng hạng khách hàng |
| Completed booking | Booking đã hoàn tất rửa xe |
| Undo | Hoàn tác thao tác hoàn tất booking gần nhất |

---

## 4. Mô tả tổng quan hệ thống

### 4.1 Bối cảnh sử dụng

Một tiệm rửa xe có số lượng xe phục vụ giới hạn trong từng buổi. Khách hàng có thể đặt lịch trước hoặc đặt ngay trong buổi đang diễn ra. Vì số lượng slot có hạn, hệ thống cần kiểm tra khả năng nhận booking và phân loại booking vào slot chính hoặc hàng chờ phụ.

Khách hàng có hạng thành viên khác nhau. Hạng cao hơn sẽ có lợi thế khi đặt lịch trước hoặc khi nằm trong hàng chờ phụ. Sau khi khách hoàn tất rửa xe, hệ thống cập nhật số lần rửa, tổng tiền đã chi, điểm tích lũy và hạng thành viên.

### 4.2 Người dùng hệ thống

Hệ thống mô phỏng hai nhóm người dùng:

#### Khách hàng

Khách hàng là người có xe và muốn đặt lịch rửa xe. Khách hàng có thể có hạng thành viên khác nhau như Member, Silver, Gold hoặc Platinum.

#### Nhân viên / quản lý tiệm

Nhân viên hoặc quản lý là người thao tác với hệ thống để thêm dữ liệu, tạo booking, xử lý xe tiếp theo, hoàn tất booking, hủy booking và xem lịch sử.

Trong phạm vi project này, hệ thống không cần đăng nhập thật. Việc phân biệt vai trò chỉ dùng để mô tả luồng nghiệp vụ.

---

## 5. Quy tắc chia buổi rửa xe

### 5.1 Các buổi trong ngày

Một ngày được chia thành 3 buổi phục vụ:

| Buổi | Thời gian |
|---|---|
| Sáng | 07:00 - 12:00 |
| Chiều | 13:00 - 17:00 |
| Tối | 18:00 - 21:00 |

### 5.2 Số lượng slot mỗi buổi

Mỗi buổi có hai loại slot:

- Slot chính: suất rửa xe được đảm bảo hơn.
- Slot chờ phụ: suất dự phòng khi slot chính đã đầy.

| Buổi | Slot chính | Slot chờ phụ |
|---|---:|---:|
| Sáng | 10 | 3 |
| Chiều | 10 | 3 |
| Tối | 5 | 2 |

### 5.3 Lý do cần chia buổi

Hệ thống cần chia buổi vì tiệm rửa xe không thể nhận vô hạn booking trong một ngày. Nếu không chia buổi, booking sẽ khó kiểm soát, khó xác định khách đặt cho thời điểm nào, và khó biết buổi nào còn chỗ.

Việc chia buổi giúp:

- Giới hạn số lượng booking hợp lý.
- Dễ xác định buổi hiện tại và buổi tương lai.
- Dễ kiểm tra booking có còn slot không.
- Dễ mô phỏng hoạt động thực tế của tiệm rửa xe.


### 5.4 Đề xuất cấu trúc dữ liệu

Nên dùng **List — danh sách** hoặc **Array — mảng** để lưu các buổi rửa xe trong ngày.

Lý do: số buổi trong ngày đã được xác định rõ là sáng, chiều và tối. Đây là nhóm dữ liệu nhỏ, cố định và thường xuyên được duyệt qua để kiểm tra buổi nào đang diễn ra, buổi nào còn slot và buổi nào cần được kích hoạt.

Mỗi buổi nên được xem như một nhóm quản lý riêng, bên trong có danh sách slot chính, hàng chờ phụ và danh sách booking đặt trước. Cách tách theo buổi giúp hệ thống không bị lẫn booking sáng, chiều và tối.

---

## 6. Quy tắc hạng thành viên

### 6.1 Danh sách hạng thành viên

Hệ thống có 4 hạng thành viên:

1. Member.
2. Silver.
3. Gold.
4. Platinum.

Hạng càng cao thì khách hàng càng có nhiều quyền lợi khi đặt lịch.

### 6.2 Điều kiện lên hạng

| Hạng | Điều kiện |
|---|---|
| Member | Khách đã đăng ký và có ít nhất 1 lần rửa |
| Silver | Có ít nhất 5 lần rửa hoặc chi tiêu từ 2,000,000 VND |
| Gold | Có ít nhất 15 lần rửa hoặc chi tiêu từ 6,000,000 VND |
| Platinum | Có ít nhất 30 lần rửa hoặc chi tiêu từ 15,000,000 VND |

### 6.3 Lý do cần hạng thành viên

Hạng thành viên giúp hệ thống tạo ra chính sách ưu tiên hợp lý. Khách hàng sử dụng dịch vụ thường xuyên sẽ có lợi thế khi đặt lịch trước hoặc khi nằm trong hàng chờ phụ. Điều này mô phỏng chương trình khách hàng thân thiết của tiệm rửa xe.


### 6.4 Đề xuất cấu trúc dữ liệu

Nên dùng **List — danh sách** để lưu khách hàng và thông tin hạng hiện tại của từng khách.

Lý do: hệ thống cần thường xuyên tìm khách hàng khi tạo booking, cập nhật hạng sau khi hoàn tất booking và kiểm tra quyền lợi đặt trước. List phù hợp với phạm vi project vì dữ liệu mô phỏng không quá lớn và dễ trình bày khi demo.

Nếu muốn nâng cao hơn, có thể dùng **Binary Search Tree — cây tìm kiếm nhị phân** cho khách hàng theo mã khách hàng hoặc số lần rửa. Tuy nhiên phần này chỉ nên để mở rộng, không bắt buộc trong bản hiện tại.

---

## 7. Quy tắc booking window

### 7.1 Giới hạn đặt trước theo hạng

Mỗi hạng thành viên có số ngày được đặt trước tối đa khác nhau:

| Hạng | Được đặt trước tối đa |
|---|---:|
| Member | 7 ngày |
| Silver | 10 ngày |
| Gold | 12 ngày |
| Platinum | 14 ngày |

### 7.2 Luồng kiểm tra booking window

Khi khách tạo booking, hệ thống kiểm tra:

1. Khách hàng hiện thuộc hạng nào.
2. Ngày khách muốn đặt lịch cách ngày hiện tại bao nhiêu ngày.
3. Số ngày đó có vượt quá giới hạn của hạng không.
4. Nếu vượt quá giới hạn, booking bị từ chối.
5. Nếu không vượt quá giới hạn, hệ thống tiếp tục kiểm tra buổi và số slot còn lại.

### 7.3 Lý do cần booking window

Booking window giúp hạng thành viên có giá trị thực tế. Khách hàng hạng cao có thể đặt lịch xa hơn, phù hợp với tinh thần ưu đãi của loyalty program. Đồng thời, hệ thống tránh việc khách hạng thấp đặt quá xa làm chiếm slot của khách hạng cao.


### 7.4 Đề xuất cấu trúc dữ liệu

Nên dùng **Map — bảng ánh xạ khóa và giá trị** để lưu giới hạn đặt trước của từng hạng thành viên.

Ví dụ về mặt ý tưởng: Member gắn với 7 ngày, Silver gắn với 10 ngày, Gold gắn với 12 ngày, Platinum gắn với 14 ngày.

Lý do: khi tạo booking, hệ thống chỉ cần biết hạng của khách hàng rồi tra ra số ngày được đặt trước tối đa. Map phù hợp vì mỗi hạng thành viên tương ứng với một giá trị giới hạn rõ ràng.

---

## 8. Quy tắc tạo booking

### 8.1 Thông tin cần có khi tạo booking

Khi tạo booking, hệ thống cần biết:

- Khách hàng nào đặt lịch.
- Xe nào được rửa.
- Dịch vụ nào được chọn.
- Ngày muốn rửa.
- Buổi muốn rửa.
- Thời điểm tạo booking.

### 8.2 Luồng tạo booking tổng quát

Khi nhân viên tạo booking cho khách, hệ thống xử lý theo luồng:

1. Kiểm tra khách hàng có tồn tại trong hệ thống không.
2. Kiểm tra xe có thuộc về khách hàng đó không.
3. Kiểm tra dịch vụ được chọn có hợp lệ không.
4. Kiểm tra ngày đặt có nằm trong booking window của hạng thành viên không.
5. Xác định booking thuộc buổi đang diễn ra hay buổi tương lai.
6. Kiểm tra slot chính và hàng chờ phụ của buổi đó.
7. Nếu còn chỗ, hệ thống nhận booking.
8. Nếu không còn chỗ, hệ thống từ chối booking và thông báo lý do.

### 8.3 Lý do cần chức năng tạo booking

Đây là chức năng trung tâm của hệ thống. Nếu không có booking, hệ thống không có dữ liệu để quản lý hàng chờ, xử lý xe tiếp theo, cập nhật lịch sử hoặc cập nhật loyalty.


### 8.4 Đề xuất cấu trúc dữ liệu

Nên dùng **List — danh sách** để lưu khách hàng, xe và dịch vụ phục vụ bước tạo booking.

Lý do: trước khi nhận booking, hệ thống cần kiểm tra khách hàng có tồn tại không, xe có thuộc về khách không và dịch vụ có hợp lệ không. Các dữ liệu này có thể được duyệt và tìm kiếm trong danh sách.

Với booking sau khi được tạo, hệ thống không nên chỉ lưu tất cả vào một danh sách chung. Booking cần được đưa vào nhóm xử lý phù hợp: buổi hiện tại, buổi tương lai, slot chính hoặc hàng chờ phụ. Cách này giúp luồng booking rõ ràng hơn và tránh việc phải lọc lại toàn bộ booking mỗi lần xử lý.

---

## 9. Booking trong buổi đang diễn ra

### 9.1 Quy tắc xử lý

Nếu khách đặt lịch cho buổi đang diễn ra:

1. Nếu slot chính còn chỗ, booking được đưa vào cuối hàng chờ chính của buổi hiện tại.
2. Nếu slot chính đã đầy nhưng hàng chờ phụ còn chỗ, booking được đưa vào hàng chờ phụ.
3. Nếu cả slot chính và hàng chờ phụ đều đầy, booking bị từ chối.

### 9.2 Luồng xử lý

Ví dụ đang là buổi sáng:

1. Khách muốn rửa xe ngay trong buổi sáng.
2. Hệ thống kiểm tra buổi sáng còn slot chính không.
3. Nếu còn, khách được nhận vào danh sách rửa chính của buổi sáng.
4. Nếu slot chính đầy, hệ thống kiểm tra hàng chờ phụ.
5. Nếu hàng chờ phụ còn chỗ, khách được nhận vào danh sách chờ.
6. Nếu hàng chờ phụ cũng đầy, hệ thống báo buổi sáng đã đầy.

### 9.3 Lý do cần quy tắc này

Khách đặt ngay cần được xử lý khác với khách đặt trước. Khi buổi đang diễn ra, thứ tự phục vụ phải ổn định để nhân viên dễ làm việc. Vì vậy booking đã nằm trong slot chính sẽ được xử lý theo thứ tự đã nhận.


### 9.4 Đề xuất cấu trúc dữ liệu

Nên dùng **Queue — hàng đợi** cho slot chính của buổi hiện tại.

Lý do: booking đặt ngay trong buổi đang diễn ra cần được xử lý theo nguyên tắc ai vào trước thì được rửa trước. Queue phù hợp với nguyên tắc **FIFO — First In First Out, nghĩa là vào trước ra trước**.

Nếu slot chính đã đầy, booking được đưa vào hàng chờ phụ. Hàng chờ phụ nên dùng **Priority Queue — hàng đợi ưu tiên** vì khách hạng cao cần có cơ hội được đẩy lên slot chính trước khi có chỗ trống.

---

## 10. Booking cho buổi tương lai

### 10.1 Quy tắc xử lý

Nếu khách đặt lịch cho buổi chưa diễn ra, booking được lưu vào danh sách booking tương lai của buổi đó. Booking tương lai được ưu tiên theo hạng thành viên. Khách hạng cao hơn có khả năng được xếp vào slot chính trước khi buổi bắt đầu.

Nếu nhiều khách cùng hạng, khách tạo booking trước sẽ được ưu tiên hơn.

### 10.2 Luồng xử lý

1. Khách chọn một ngày và một buổi trong tương lai.
2. Hệ thống kiểm tra booking window.
3. Hệ thống kiểm tra tổng sức chứa của buổi đó.
4. Nếu còn khả năng nhận booking, hệ thống ghi nhận booking tương lai.
5. Khi đến thời điểm bắt đầu buổi, hệ thống sắp xếp các booking tương lai theo quyền ưu tiên.
6. Những booking ưu tiên cao nhất được đưa vào slot chính.
7. Những booking tiếp theo được đưa vào hàng chờ phụ nếu còn chỗ.

### 10.3 Lý do cần quy tắc này

Khách đặt trước cần được xử lý theo quyền lợi thành viên. Nếu tất cả khách đặt trước đều xử lý như nhau, hạng thành viên sẽ mất ý nghĩa. Quy tắc này giúp khách hạng cao có cơ hội tốt hơn khi buổi đó có nhiều người cùng đặt.


### 10.4 Đề xuất cấu trúc dữ liệu

Nên dùng **Priority Queue — hàng đợi ưu tiên** cho booking của buổi tương lai.

Lý do: booking tương lai chưa cần xử lý ngay, nhưng khi buổi bắt đầu, hệ thống cần chọn khách có quyền ưu tiên cao hơn để đưa vào slot chính trước. Priority Queue phù hợp vì nó cho phép hệ thống luôn lấy ra booking có độ ưu tiên cao nhất trước.

Tiêu chí ưu tiên nên là hạng thành viên. Nếu cùng hạng, hệ thống ưu tiên người tạo booking trước để đảm bảo công bằng.

---

## 11. Kích hoạt buổi rửa xe

### 11.1 Khái niệm

Kích hoạt buổi rửa xe là thao tác chuyển một buổi từ trạng thái “chưa diễn ra” sang trạng thái “đang diễn ra”.

Ví dụ:

- Đến 07:00, hệ thống kích hoạt buổi sáng.
- Đến 13:00, hệ thống kích hoạt buổi chiều.
- Đến 18:00, hệ thống kích hoạt buổi tối.

### 11.2 Luồng kích hoạt buổi

Khi buổi bắt đầu:

1. Hệ thống lấy danh sách booking tương lai của buổi đó.
2. Hệ thống chọn các booking có quyền ưu tiên cao nhất để đưa vào slot chính.
3. Nếu slot chính đã đủ, các booking tiếp theo được đưa vào hàng chờ phụ.
4. Sau khi kích hoạt, danh sách slot chính trở thành hàng chờ xử lý của buổi hiện tại.
5. Trong buổi đang diễn ra, nhân viên xử lý xe theo thứ tự trong danh sách slot chính.

### 11.3 Lý do cần chức năng kích hoạt buổi

Chức năng này giúp tách rõ hai giai đoạn:

- Trước khi buổi bắt đầu: hệ thống còn có thể sắp xếp booking theo quyền ưu tiên.
- Khi buổi đã bắt đầu: thứ tự xử lý cần ổn định để nhân viên làm việc.

Nếu không có bước kích hoạt, hệ thống sẽ khó phân biệt booking đang chờ xử lý ngay và booking chỉ là đặt trước.


### 11.4 Đề xuất cấu trúc dữ liệu

Nên dùng kết hợp **Priority Queue — hàng đợi ưu tiên** và **Queue — hàng đợi thường** trong chức năng kích hoạt buổi.

Lý do: trước khi buổi bắt đầu, booking tương lai cần được chọn theo quyền ưu tiên nên dùng Priority Queue. Sau khi đã kích hoạt buổi, các booking được chọn vào slot chính cần có thứ tự ổn định để nhân viên xử lý, nên chuyển sang Queue.

Cách kết hợp này giúp hệ thống vừa đảm bảo quyền lợi theo hạng thành viên trước khi buổi bắt đầu, vừa đảm bảo sự ổn định trong quá trình phục vụ thực tế.

---

## 12. Hàng chờ phụ

### 12.1 Quy tắc hàng chờ phụ

Hàng chờ phụ được dùng khi slot chính của một buổi đã đầy.

Nếu có slot chính trống do khách hủy hoặc do hệ thống còn khả năng nhận thêm xe, khách trong hàng chờ phụ sẽ được đẩy lên slot chính.

Khách hạng cao hơn trong hàng chờ phụ được ưu tiên trước. Nếu cùng hạng, khách vào hàng chờ phụ trước được ưu tiên trước.

### 12.2 Luồng đẩy khách từ hàng chờ phụ lên slot chính

1. Hệ thống phát hiện slot chính còn trống.
2. Hệ thống kiểm tra hàng chờ phụ có khách không.
3. Nếu có, hệ thống chọn khách có quyền ưu tiên cao nhất.
4. Booking của khách đó được chuyển vào cuối danh sách slot chính.
5. Khách được xem là đã có suất chính trong buổi đó.

### 12.3 Lý do cần hàng chờ phụ

Nếu chỉ có slot chính, hệ thống sẽ từ chối ngay khi buổi đầy. Hàng chờ phụ giúp tiệm rửa xe linh hoạt hơn, đặc biệt khi có khách hủy hoặc khi còn thời gian xử lý thêm.

Hàng chờ phụ cũng giúp thể hiện quyền lợi của khách hạng cao mà không phá vỡ thứ tự phục vụ của slot chính.


### 12.4 Đề xuất cấu trúc dữ liệu

Nên dùng **Priority Queue — hàng đợi ưu tiên** cho hàng chờ phụ.

Lý do: hàng chờ phụ không phải là danh sách xử lý trực tiếp ngay lập tức. Nó là nơi chờ để được đẩy lên slot chính khi có chỗ trống. Vì vậy, khách hạng cao nên được ưu tiên trước. Priority Queue phù hợp vì hệ thống có thể chọn ra booking có quyền ưu tiên cao nhất từ hàng chờ phụ.

Nếu hai khách có cùng hạng, hệ thống vẫn nên xét ai vào hàng chờ trước để tránh mất công bằng.

---

## 13. Xử lý booking tiếp theo

### 13.1 Quy tắc xử lý

Trong buổi đang diễn ra, nhân viên sẽ lấy booking tiếp theo từ danh sách slot chính.

Booking được xử lý theo thứ tự đã được xếp trong slot chính. Người đứng trước được xử lý trước.

### 13.2 Luồng xử lý

1. Nhân viên chọn chức năng xử lý booking tiếp theo.
2. Hệ thống lấy booking đầu tiên trong danh sách slot chính.
3. Hệ thống chuyển booking đó sang trạng thái đang rửa.
4. Nhân viên thực hiện dịch vụ rửa xe.
5. Sau khi rửa xong, nhân viên chuyển booking sang hoàn tất.

### 13.3 Lý do cần chức năng này

Chức năng này mô phỏng hoạt động thực tế của tiệm rửa xe. Nhân viên cần biết xe nào sẽ được rửa tiếp theo, tránh xử lý sai thứ tự hoặc bỏ sót booking.


### 13.4 Đề xuất cấu trúc dữ liệu

Nên dùng **Queue — hàng đợi** cho danh sách slot chính của buổi hiện tại.

Lý do: nhân viên chỉ cần lấy booking ở đầu danh sách để xử lý tiếp theo. Sau khi booking đầu tiên được lấy ra, booking phía sau tự động tiến lên theo thứ tự. Đây là đúng bản chất của Queue và phù hợp với hoạt động xếp hàng ngoài đời.

---

## 14. Hoàn tất booking

### 14.1 Quy tắc hoàn tất

Khi xe đã được rửa xong, booking được chuyển sang trạng thái hoàn tất.

Sau khi hoàn tất booking, hệ thống cần:

1. Ghi nhận booking vào lịch sử rửa xe.
2. Cập nhật tổng số lần rửa của khách hàng.
3. Cập nhật tổng tiền khách đã chi.
4. Cộng điểm tích lũy cho khách hàng.
5. Xét lại hạng thành viên của khách hàng.
6. Kiểm tra hàng chờ phụ để bổ sung vào slot chính nếu cần.

### 14.2 Luồng hoàn tất booking

1. Nhân viên chọn booking đang rửa.
2. Nhân viên xác nhận booking đã hoàn tất.
3. Hệ thống ghi nhận thông tin hoàn tất.
4. Hệ thống cập nhật loyalty của khách.
5. Hệ thống lưu booking vào lịch sử.
6. Nếu hàng chờ phụ còn khách và slot chính còn chỗ, hệ thống đẩy một booking từ hàng chờ phụ lên slot chính.

### 14.3 Lý do cần chức năng hoàn tất

Nếu không có hoàn tất booking, hệ thống không thể biết dịch vụ đã được thực hiện hay chưa. Đây cũng là thời điểm hợp lý để cộng điểm, tăng số lần rửa và xét lại hạng thành viên.


### 14.4 Đề xuất cấu trúc dữ liệu

Nên dùng **List — danh sách** để lưu lịch sử các booking đã hoàn tất và dùng **Stack — ngăn xếp** để lưu thao tác hoàn tất gần nhất.

Lý do: List phù hợp để xem lại toàn bộ lịch sử rửa xe. Stack phù hợp cho thao tác hoàn tác gần nhất vì Stack hoạt động theo nguyên tắc **LIFO — Last In First Out, nghĩa là vào sau ra trước**. Booking vừa hoàn tất sau cùng sẽ là booking đầu tiên được hoàn tác nếu nhân viên nhập nhầm.

---

## 15. Hủy booking

### 15.1 Quy tắc hủy booking

Booking có thể bị hủy khi khách không đến hoặc muốn đổi lịch.

Khi một booking bị hủy:

1. Hệ thống chuyển booking sang trạng thái đã hủy.
2. Booking không còn được xử lý trong buổi đó.
3. Nếu booking bị hủy nằm trong slot chính, hệ thống kiểm tra hàng chờ phụ.
4. Nếu hàng chờ phụ có khách, hệ thống chọn một booking phù hợp để đẩy lên slot chính.

### 15.2 Luồng hủy booking

1. Nhân viên nhập hoặc chọn booking cần hủy.
2. Hệ thống tìm booking trong danh sách hiện tại, hàng chờ phụ hoặc danh sách booking tương lai.
3. Nếu tìm thấy, hệ thống xác nhận hủy.
4. Booking được đánh dấu là đã hủy.
5. Nếu việc hủy tạo ra slot trống, hệ thống bổ sung khách từ hàng chờ phụ nếu có.

### 15.3 Lý do cần chức năng hủy booking

Trong thực tế, khách có thể đổi ý, không đến hoặc đặt nhầm. Nếu hệ thống không hỗ trợ hủy, slot sẽ bị giữ sai, làm giảm khả năng phục vụ khách khác.


### 15.4 Đề xuất cấu trúc dữ liệu

Nên tìm booking cần hủy trong các nhóm dữ liệu đang quản lý booking: slot chính, hàng chờ phụ hoặc danh sách booking tương lai.

Về mặt cấu trúc, slot chính nên dùng Queue, hàng chờ phụ nên dùng Priority Queue và booking tương lai nên dùng Priority Queue. Khi hủy booking, hệ thống cần có bước tìm kiếm trong nhóm phù hợp.

Lý do: booking có thể nằm ở nhiều trạng thái khác nhau. Nếu hủy một booking trong slot chính, hệ thống cần kiểm tra hàng chờ phụ để bổ sung khách khác lên slot chính. Vì vậy chức năng hủy phải liên kết trực tiếp với cấu trúc quản lý hàng chờ.

---

## 16. Loyalty đơn giản

### 16.1 Phạm vi loyalty

Loyalty trong project này chỉ gồm:

- Tăng số lần rửa sau khi booking hoàn tất.
- Tăng tổng tiền đã chi.
- Cộng điểm tích lũy.
- Xét lại hạng thành viên.

Không bao gồm đổi điểm, điểm hết hạn hoặc tự động hạ hạng.

### 16.2 Quy tắc cộng điểm

Khi booking hoàn tất, khách hàng nhận điểm dựa trên số tiền của dịch vụ đã sử dụng.

Công thức mô phỏng:

> 1 điểm tương ứng với 1,000 VND đã chi.

Ví dụ:

- Dịch vụ có giá 100,000 VND.
- Khách nhận 100 điểm.

### 16.3 Quy tắc xét lại hạng

Sau khi cộng số lần rửa và tổng tiền, hệ thống kiểm tra khách có đủ điều kiện lên hạng mới không.

Nếu khách đủ điều kiện của nhiều hạng, hệ thống chọn hạng cao nhất mà khách đạt được.

### 16.4 Lý do cần loyalty

Loyalty giúp hệ thống không chỉ xử lý booking tĩnh. Khách hàng sử dụng dịch vụ nhiều hơn sẽ có hạng cao hơn, từ đó được ưu tiên hơn trong những lần đặt lịch sau. Điều này giúp project có luồng nghiệp vụ liên kết giữa lịch sử rửa xe, điểm tích lũy và quyền ưu tiên booking.


### 16.5 Đề xuất cấu trúc dữ liệu

Nên dùng **List — danh sách** để lưu khách hàng và cập nhật thông tin loyalty của từng khách sau khi booking hoàn tất.

Lý do: khi hoàn tất booking, hệ thống cần tìm đúng khách hàng để cập nhật số lần rửa, tổng chi tiêu, điểm và hạng. Với phạm vi project CSD hiện tại, List đủ đơn giản để demo và dễ giải thích.

Nếu muốn mở rộng, có thể dùng **Map — bảng ánh xạ khóa và giá trị** để tìm khách hàng nhanh hơn theo mã khách hàng hoặc số điện thoại. Tuy nhiên hướng này chỉ nên xem là cải tiến sau, không bắt buộc trong bản hiện tại.

---

## 17. Lịch sử rửa xe

### 17.1 Quy tắc lưu lịch sử

Chỉ booking đã hoàn tất mới được đưa vào lịch sử rửa xe.

Lịch sử cần cho phép xem:

- Khách hàng nào đã rửa.
- Xe nào đã rửa.
- Dịch vụ đã dùng.
- Thời gian hoàn tất.
- Số tiền đã tính.
- Điểm đã cộng.

### 17.2 Luồng lưu lịch sử

1. Booking được xác nhận hoàn tất.
2. Hệ thống tạo bản ghi lịch sử.
3. Bản ghi lịch sử được lưu lại.
4. Người dùng có thể xem toàn bộ lịch sử hoặc lịch sử của một khách cụ thể.

### 17.3 Lý do cần lịch sử

Lịch sử giúp kiểm tra lại hoạt động của tiệm rửa xe. Đây cũng là cơ sở để tính số lần rửa, tổng tiền đã chi và điểm tích lũy của khách hàng.


### 17.4 Đề xuất cấu trúc dữ liệu

Nên dùng **List — danh sách** để lưu toàn bộ lịch sử rửa xe.

Lý do: lịch sử cần được xem lại nhiều bản ghi, có thể lọc theo khách hàng, xe hoặc dịch vụ. List phù hợp vì nó lưu được nhiều booking đã hoàn tất và dễ duyệt để hiển thị toàn bộ lịch sử.

Ngoài ra, nên dùng thêm **Stack — ngăn xếp** cho các booking hoàn tất gần nhất để hỗ trợ hoàn tác. List dùng để xem lịch sử dài hạn, còn Stack dùng để xử lý thao tác gần nhất.

---

## 18. Hoàn tác booking hoàn tất gần nhất

### 18.1 Quy tắc hoàn tác

Hệ thống cho phép hoàn tác booking hoàn tất gần nhất trong trường hợp nhân viên nhập nhầm.

Khi hoàn tác:

1. Booking hoàn tất gần nhất được lấy ra để xử lý lại.
2. Lịch sử rửa xe được cập nhật.
3. Điểm đã cộng bị trừ lại.
4. Tổng tiền đã chi bị trừ lại.
5. Tổng số lần rửa bị trừ lại.
6. Hạng thành viên được xét lại.

### 18.2 Luồng hoàn tác

1. Nhân viên chọn chức năng hoàn tác booking hoàn tất gần nhất.
2. Hệ thống hiển thị booking gần nhất đã hoàn tất.
3. Nhân viên xác nhận hoàn tác.
4. Hệ thống đảo ngược các thay đổi đã tạo ra từ booking đó.
5. Hệ thống cập nhật lại thông tin khách hàng.

### 18.3 Lý do cần chức năng hoàn tác

Trong quá trình vận hành, nhân viên có thể bấm nhầm hoàn tất booking. Nếu không có chức năng hoàn tác, điểm và lịch sử khách hàng có thể bị sai. Chức năng hoàn tác giúp sửa lỗi nhanh trong phạm vi đơn giản.


### 18.4 Đề xuất cấu trúc dữ liệu

Nên dùng **Stack — ngăn xếp** cho chức năng hoàn tác booking hoàn tất gần nhất.

Lý do: hệ thống chỉ cần hoàn tác thao tác hoàn tất mới nhất, không cần cho phép hoàn tác tùy ý ở giữa lịch sử. Stack phù hợp vì phần tử được thêm sau cùng sẽ được lấy ra trước. Điều này khớp với tình huống nhân viên vừa bấm nhầm và muốn sửa ngay thao tác gần nhất.

---

## 19. Trạng thái booking

Booking có thể đi qua các trạng thái sau:

| Trạng thái | Ý nghĩa |
|---|---|
| Waiting | Booking đã được nhận nhưng chưa rửa |
| Serving | Xe đang được rửa |
| Completed | Xe đã rửa xong |
| Cancelled | Booking đã bị hủy |

Luồng trạng thái chính:

```text
Waiting → Serving → Completed
```

Luồng hủy:

```text
Waiting → Cancelled
```

Luồng hoàn tác:

```text
Completed → Waiting hoặc trạng thái trước đó tùy cách mô phỏng
```

---

## 20. Yêu cầu chức năng chi tiết

### FR-01 — Quản lý khách hàng

Hệ thống cho phép thêm, xem và tìm khách hàng.

Chức năng này cần có vì mọi booking đều phải gắn với một khách hàng cụ thể. Thông tin khách hàng cũng quyết định hạng thành viên và quyền đặt trước.

---

### FR-02 — Quản lý xe

Hệ thống cho phép thêm và xem xe của khách hàng.

Chức năng này cần có vì một khách có thể sở hữu nhiều xe. Khi đặt lịch, hệ thống cần biết xe nào sẽ được rửa.

---

### FR-03 — Quản lý dịch vụ

Hệ thống cho phép xem danh sách dịch vụ rửa xe.

Chức năng này cần có vì booking phải chọn dịch vụ cụ thể. Giá dịch vụ cũng ảnh hưởng đến điểm tích lũy và tổng chi tiêu của khách hàng.

---

### FR-04 — Tạo booking

Hệ thống cho phép tạo booking cho khách hàng.

Chức năng này cần có vì đây là đầu vào chính của hệ thống hàng chờ. Tạo booking sẽ kích hoạt các bước kiểm tra khách hàng, xe, dịch vụ, booking window, buổi rửa và sức chứa.

---

### FR-05 — Kiểm tra booking window

Hệ thống phải kiểm tra khách có được phép đặt trước ngày đã chọn hay không.

Chức năng này cần có để đảm bảo quyền lợi theo hạng thành viên. Khách hạng cao được đặt trước xa hơn khách hạng thấp.

---

### FR-06 — Xếp booking vào buổi phù hợp

Hệ thống phải xác định booking thuộc buổi sáng, chiều hay tối.

Chức năng này cần có vì mỗi buổi có giới hạn slot riêng. Nếu không xác định buổi, hệ thống không thể kiểm tra sức chứa chính xác.

---

### FR-07 — Xử lý booking trong buổi hiện tại

Hệ thống phải nhận booking đặt ngay vào slot chính nếu còn chỗ, hoặc hàng chờ phụ nếu slot chính đã đầy.

Chức năng này cần có để hỗ trợ khách đến trong buổi đang hoạt động.

---

### FR-08 — Xử lý booking tương lai

Hệ thống phải lưu booking tương lai và ưu tiên khách hạng cao khi buổi đó được kích hoạt.

Chức năng này cần có để mô phỏng quyền lợi của khách hàng thân thiết khi đặt lịch trước.

---

### FR-09 — Kích hoạt buổi rửa xe

Hệ thống phải chuyển booking tương lai của buổi đó thành danh sách xử lý chính và hàng chờ phụ khi buổi bắt đầu.

Chức năng này cần có để tách rõ booking đặt trước và booking đang được xử lý trong thực tế.

---

### FR-10 — Xem hàng chờ hiện tại

Hệ thống cho phép xem danh sách booking đang chờ xử lý trong buổi hiện tại.

Chức năng này cần có để nhân viên biết thứ tự xe cần rửa.

---

### FR-11 — Xem hàng chờ phụ

Hệ thống cho phép xem danh sách booking trong hàng chờ phụ.

Chức năng này cần có để biết khách nào đang chờ được đẩy lên slot chính.

---

### FR-12 — Xử lý booking tiếp theo

Hệ thống cho phép lấy booking tiếp theo trong buổi hiện tại để bắt đầu rửa xe.

Chức năng này cần có để mô phỏng hoạt động vận hành của tiệm rửa xe.

---

### FR-13 — Hoàn tất booking

Hệ thống cho phép xác nhận xe đã rửa xong.

Chức năng này cần có để chuyển booking sang lịch sử và cập nhật loyalty cho khách hàng.

---

### FR-14 — Hủy booking

Hệ thống cho phép hủy booking khi khách không đến hoặc muốn đổi lịch.

Chức năng này cần có để giải phóng slot và cho khách khác trong hàng chờ phụ có cơ hội được phục vụ.

---

### FR-15 — Cập nhật loyalty

Hệ thống cập nhật điểm, tổng tiền, tổng số lần rửa và hạng thành viên sau khi booking hoàn tất.

Chức năng này cần có để hạng thành viên không cố định mà thay đổi theo lịch sử sử dụng dịch vụ.

---

### FR-16 — Xem lịch sử rửa xe

Hệ thống cho phép xem các booking đã hoàn tất.

Chức năng này cần có để kiểm tra hoạt động đã xảy ra và làm cơ sở cho loyalty.

---

### FR-17 — Hoàn tác booking hoàn tất gần nhất

Hệ thống cho phép hoàn tác booking hoàn tất gần nhất nếu nhân viên nhập nhầm.

Chức năng này cần có để sửa lỗi thao tác mà không phải chỉnh thủ công toàn bộ điểm, tổng tiền và lịch sử.

---

## 21. Luồng nghiệp vụ chính

### 21.1 Luồng đặt booking thành công

1. Nhân viên chọn khách hàng.
2. Nhân viên chọn xe.
3. Nhân viên chọn dịch vụ.
4. Nhân viên chọn ngày và buổi rửa xe.
5. Hệ thống kiểm tra booking window.
6. Hệ thống kiểm tra buổi và sức chứa.
7. Hệ thống nhận booking.
8. Booking được đưa vào đúng nhóm xử lý: buổi hiện tại hoặc buổi tương lai.

### 21.2 Luồng đặt booking thất bại

Booking bị từ chối nếu:

- Khách hàng không tồn tại.
- Xe không hợp lệ.
- Dịch vụ không hợp lệ.
- Ngày đặt vượt quá booking window.
- Buổi đã đầy cả slot chính và hàng chờ phụ.

### 21.3 Luồng xử lý buổi hiện tại

1. Buổi được kích hoạt.
2. Hệ thống tạo danh sách slot chính và hàng chờ phụ.
3. Nhân viên lấy booking tiếp theo để rửa.
4. Booking chuyển sang trạng thái đang rửa.
5. Nhân viên xác nhận hoàn tất.
6. Hệ thống cập nhật lịch sử và loyalty.

### 21.4 Luồng hủy booking

1. Nhân viên chọn booking cần hủy.
2. Hệ thống kiểm tra booking có tồn tại không.
3. Nếu tồn tại, booking được đánh dấu hủy.
4. Nếu booking đang chiếm slot chính, hệ thống kiểm tra hàng chờ phụ.
5. Nếu hàng chờ phụ còn khách, một booking phù hợp được đẩy lên slot chính.

### 21.5 Luồng cập nhật loyalty

1. Booking được hoàn tất.
2. Hệ thống cộng số lần rửa.
3. Hệ thống cộng tổng chi tiêu.
4. Hệ thống cộng điểm.
5. Hệ thống xét lại hạng.
6. Hạng mới được dùng cho các booking sau.

### 21.6 Luồng hoàn tác booking hoàn tất

1. Nhân viên chọn hoàn tác booking hoàn tất gần nhất.
2. Hệ thống hiển thị booking sẽ bị hoàn tác.
3. Nhân viên xác nhận.
4. Hệ thống trừ lại điểm, tổng chi tiêu và số lần rửa.
5. Hệ thống cập nhật lại lịch sử.
6. Hệ thống xét lại hạng thành viên.

---

## 22. Yêu cầu phi chức năng

### NFR-01 — Dễ demo

Hệ thống cần có luồng thao tác rõ ràng để dễ trình bày trong buổi vấn đáp.

### NFR-02 — Dữ liệu đơn giản

Project có thể dùng dữ liệu mẫu hoặc dữ liệu nhập trong lúc chạy. Không bắt buộc dùng database.

### NFR-03 — Tập trung vào nghiệp vụ chính

Hệ thống không nên mở rộng sang web, payment, AI hoặc notification để tránh lệch khỏi mục tiêu project.

### NFR-04 — Dễ kiểm tra

Mỗi chức năng nên có kết quả rõ ràng sau khi chạy, ví dụ booking được nhận, bị từ chối, được đưa vào hàng chờ, được hoàn tất hoặc bị hủy.

### NFR-05 — Dễ giải thích

Người làm project phải giải thích được vì sao có từng chức năng, chức năng đó phục vụ luồng nghiệp vụ nào và ảnh hưởng đến phần nào của hệ thống.

---

## 23. Demo flow đề xuất

### Demo 1 — Booking theo hạng thành viên

1. Tạo nhiều khách hàng với hạng khác nhau.
2. Cho các khách cùng đặt một buổi tương lai.
3. Kích hoạt buổi đó.
4. Kiểm tra khách hạng cao được đưa vào slot chính trước.

### Demo 2 — Booking trong buổi hiện tại

1. Kích hoạt buổi sáng.
2. Cho khách đặt ngay trong buổi sáng.
3. Kiểm tra khách được đưa vào cuối danh sách slot chính nếu còn chỗ.
4. Nếu slot chính đầy, khách được đưa vào hàng chờ phụ.

### Demo 3 — Hủy booking và bổ sung từ hàng chờ phụ

1. Làm đầy slot chính.
2. Thêm một số khách vào hàng chờ phụ.
3. Hủy một booking trong slot chính.
4. Kiểm tra hệ thống đẩy một khách phù hợp từ hàng chờ phụ lên slot chính.

### Demo 4 — Hoàn tất booking và cập nhật loyalty

1. Xử lý một booking.
2. Hoàn tất booking.
3. Kiểm tra điểm, tổng tiền, số lần rửa và hạng thành viên được cập nhật.

### Demo 5 — Lịch sử và hoàn tác

1. Hoàn tất nhiều booking.
2. Xem lịch sử rửa xe.
3. Hoàn tác booking hoàn tất gần nhất.
4. Kiểm tra lịch sử và loyalty được cập nhật lại.

---

## 24. Tóm tắt quyết định đã chốt

| Nội dung | Quyết định |
|---|---|
| Mục tiêu chính | Quản lý hàng chờ rửa xe |
| Cách xử lý buổi hiện tại | Theo thứ tự nhận vào slot chính |
| Cách xử lý buổi tương lai | Ưu tiên theo hạng thành viên khi buổi được kích hoạt |
| Chia buổi | Sáng, chiều, tối |
| Slot mỗi buổi | Sáng 10+3, chiều 10+3, tối 5+2 |
| Hàng chờ phụ | Ưu tiên khách hạng cao, cùng hạng thì ai vào trước ưu tiên trước |
| Booking đặt ngay | Vào slot chính nếu còn chỗ, nếu đầy thì vào hàng chờ phụ |
| Booking đặt trước | Kiểm tra booking window và ưu tiên theo hạng khi kích hoạt buổi |
| Booking window | Member 7 ngày, Silver 10 ngày, Gold 12 ngày, Platinum 14 ngày |
| Loyalty | Hoàn tất booking thì cộng điểm và xét lại hạng |
| Lịch sử | Lưu lịch sử booking hoàn tất và hỗ trợ hoàn tác booking gần nhất |

---

## 25. Ghi chú cho bước tiếp theo

Tài liệu hiện chỉ mô tả yêu cầu và luồng chức năng. Các phần sau sẽ được quyết định sau, không đưa vào SRS phiên bản này:

1. Thiết kế class.
2. Thuộc tính chi tiết của từng class.
3. Code cụ thể.
4. Cách cài đặt cấu trúc dữ liệu.
5. Menu console chi tiết.
6. Dữ liệu mẫu để test.
## 25. Ghi chú cho bước tiếp theo

Tài liệu hiện chỉ mô tả yêu cầu và luồng chức năng. Các phần sau sẽ được quyết định sau, không đưa vào SRS phiên bản này:

1. Thiết kế class.
2. Thuộc tính chi tiết của từng class.
3. Code cụ thể.
4. Cách cài đặt cấu trúc dữ liệu.
5. Menu console chi tiết.
6. Dữ liệu mẫu để test.

---

## 26. Tổng hợp đề xuất cấu trúc dữ liệu

| Khu vực chức năng | Đề xuất cấu trúc dữ liệu | Lý do |
|---|---|---|
| Khách hàng | List | Dễ lưu, duyệt, tìm và cập nhật thông tin loyalty trong phạm vi project |
| Xe của khách hàng | List | Một khách có thể có nhiều xe, cần duyệt để chọn xe khi tạo booking |
| Dịch vụ rửa xe | List | Danh sách dịch vụ ít thay đổi, dễ hiển thị và tìm kiếm |
| Buổi rửa xe | Array hoặc List | Số buổi cố định gồm sáng, chiều và tối |
| Booking trong buổi hiện tại | Queue | Đảm bảo ai vào slot chính trước thì được xử lý trước |
| Booking cho buổi tương lai | Priority Queue | Ưu tiên khách hạng cao trước khi buổi được kích hoạt |
| Hàng chờ phụ | Priority Queue | Khi có slot trống, khách hạng cao trong hàng chờ được đẩy lên trước |
| Lịch sử rửa xe | List | Lưu toàn bộ booking đã hoàn tất để xem lại |
| Hoàn tác booking gần nhất | Stack | Phù hợp với thao tác hoàn tác gần nhất theo nguyên tắc vào sau ra trước |
| Booking window theo hạng | Map | Mỗi hạng thành viên tương ứng với một số ngày được đặt trước |

Ghi chú: các cấu trúc dữ liệu trên mới là **đề xuất thiết kế**, không phải phần code cụ thể. Khi chuyển sang bước thiết kế kỹ thuật, có thể quyết định tự cài đặt hoặc dùng thư viện có sẵn tùy yêu cầu của môn học.

---

## 27. Cập nhật bổ sung sau khi chốt phạm vi

Phần này được thêm vào để ghi nhận các quyết định đã được phân tích và chốt sau bản SRS hiện tại. Nội dung bên trên được giữ nguyên, phần này chỉ bổ sung những điểm còn thiếu để tài liệu đầy đủ hơn trước khi chuyển sang thiết kế kỹ thuật và code.

### 27.1 Kiểu ứng dụng

Project sẽ được triển khai dưới dạng **CLI — Command Line Interface, nghĩa là chương trình chạy bằng menu trong terminal**.

Lý do chọn CLI:

- Phù hợp với project CSD vì trọng tâm là cấu trúc dữ liệu và giải thuật.
- Không làm project bị lệch sang web app như PRJ301.
- Giúp demo trực tiếp các thao tác như tạo booking, đưa vào hàng chờ, xử lý booking tiếp theo, hủy booking, hoàn tất booking và hoàn tác booking gần nhất.

### 27.2 Lưu dữ liệu bằng file

Project sẽ lưu dữ liệu bằng file text, không dùng database.

Lý do không dùng database:

- Database có thể tự xử lý tìm kiếm, sắp xếp và ưu tiên bằng SQL.
- Nếu để database xử lý quá nhiều, phần cấu trúc dữ liệu và giải thuật trong Java sẽ bị mờ đi.
- File text đủ dùng cho app CLI và vẫn giúp dữ liệu không mất sau khi tắt chương trình.

Các nhóm dữ liệu nên được lưu thành các file riêng:

| File | Mục đích |
|---|---|
| customers.txt | Lưu thông tin khách hàng, điểm, hạng, tổng chi tiêu và số lần rửa |
| vehicles.txt | Lưu xe của khách hàng |
| services.txt | Lưu dịch vụ rửa xe |
| bookings.txt | Lưu booking hiện tại, booking tương lai, booking đã hủy hoặc đang xử lý |
| history.txt | Lưu lịch sử booking đã hoàn tất |

### 27.3 Thời điểm load và save file

Hệ thống sẽ load dữ liệu khi mở app và save dữ liệu sau mỗi thao tác quan trọng.

Luồng tổng quát:

```text
Start app
→ load dữ liệu từ file
→ đưa dữ liệu vào các cấu trúc dữ liệu trong RAM
→ người dùng thao tác trên menu CLI
→ sau mỗi thao tác quan trọng, hệ thống save lại file
→ exit app thì save lần cuối
```

Các thao tác cần save file:

- Thêm khách hàng.
- Thêm xe.
- Thêm dịch vụ.
- Tạo booking.
- Kích hoạt buổi rửa xe.
- Xử lý booking tiếp theo.
- Hủy booking.
- Thanh toán booking.
- Hoàn tất booking.
- Hoàn tác booking hoàn tất gần nhất.
- Cập nhật loyalty.

Lý do cần save sau mỗi thao tác quan trọng: giảm rủi ro mất dữ liệu nếu chương trình CLI bị tắt đột ngột.

### 27.4 Định dạng file

File text sẽ dùng dấu `|` để phân tách các trường dữ liệu.

Ví dụ `customers.txt`:

```text
C001|Nguyen Van A|0901234567|GOLD|120|5000000|12
C002|Tran Van B|0912345678|MEMBER|20|500000|2
```

Ví dụ `services.txt`:

```text
S001|Basic Wash|50000|30|ACTIVE
S002|Premium Wash|100000|45|ACTIVE
S003|Interior Cleaning|150000|60|ACTIVE
```

Lý do chọn dấu `|`:

- Dễ đọc và dễ ghi bằng Java thuần.
- Không cần thư viện ngoài như JSON parser.
- Ít lỗi hơn CSV dùng dấu phẩy, vì tên người hoặc tên dịch vụ có thể chứa dấu phẩy.

### 27.5 Vai trò của file và cấu trúc dữ liệu trong RAM

File chỉ dùng để lưu trữ lâu dài. Khi chương trình chạy, dữ liệu phải được load vào các cấu trúc dữ liệu trong RAM để xử lý.

Luồng đúng:

```text
File
→ load dữ liệu
→ đưa vào MyLinkedList, MyQueue, MyPriorityQueue, MyStack
→ xử lý bằng cấu trúc dữ liệu tự cài
→ save kết quả lại file
```

Hệ thống không được xem file như nơi xử lý thuật toán chính. File chỉ là nơi lưu dữ liệu.

---

## 28. Cập nhật bổ sung về cấu trúc dữ liệu tự cài

### 28.1 Quyết định chung

Project sẽ ưu tiên tự cài các cấu trúc dữ liệu thay vì chỉ dùng thư viện có sẵn của Java.

Các cấu trúc dữ liệu dự kiến:

| Cấu trúc dữ liệu tự cài | Mục đích |
|---|---|
| MyLinkedList | Lưu khách hàng, xe, dịch vụ hoặc các danh sách dữ liệu cần duyệt |
| MyQueue | Quản lý slot chính của buổi hiện tại |
| MyPriorityQueue | Quản lý booking tương lai và hàng chờ phụ |
| MyStack | Quản lý booking hoàn tất gần nhất để hỗ trợ hoàn tác |
| Node | Đại diện cho phần tử trong danh sách liên kết, queue hoặc stack nếu cần |

Lý do tự cài: project thuộc môn CSD, nên cần thể hiện rõ cách dữ liệu được tổ chức và thao tác bằng cấu trúc dữ liệu.

### 28.2 MyPriorityQueue dùng Max Heap

`MyPriorityQueue` sẽ được cài bằng **Max Heap — cây nhị phân ưu tiên lớn nhất**.

Ý nghĩa:

- Booking có độ ưu tiên cao nhất luôn nằm ở đầu heap.
- Khi cần lấy booking ưu tiên nhất, hệ thống lấy phần tử ở root.
- Sau khi thêm hoặc xóa phần tử, heap phải được điều chỉnh lại.

Tiêu chí ưu tiên:

1. Hạng thành viên cao hơn được ưu tiên hơn.
2. Nếu cùng hạng, booking được tạo trước được ưu tiên hơn.

Quy đổi độ ưu tiên theo hạng:

| Tier | Priority |
|---|---:|
| Member | 1 |
| Silver | 2 |
| Gold | 3 |
| Platinum | 4 |

Các thao tác chính về mặt ý tưởng:

| Thao tác | Ý nghĩa |
|---|---|
| insert | Thêm booking vào heap |
| heapify up | Đẩy booking lên nếu nó có độ ưu tiên cao hơn node cha |
| remove root | Lấy booking có độ ưu tiên cao nhất |
| heapify down | Đẩy booking xuống để heap trở lại đúng thứ tự ưu tiên |
| peek | Xem booking ưu tiên cao nhất nhưng chưa lấy ra |

Lý do chọn Heap: Heap thể hiện rõ giải thuật ưu tiên, phù hợp với bài toán booking tương lai và hàng chờ phụ theo tier.

### 28.3 MyQueue cho slot chính

Slot chính của buổi hiện tại dùng **MyQueue — hàng đợi tự cài**.

Nguyên tắc:

```text
FIFO — First In First Out
```

Nghĩa là booking vào slot chính trước sẽ được xử lý trước.

Lý do: khi booking đã được đưa vào slot chính của buổi hiện tại, thứ tự xử lý cần ổn định để nhân viên không bị rối.

### 28.4 MyStack cho hoàn tác

Booking hoàn tất gần nhất được lưu vào **MyStack — ngăn xếp tự cài**.

Nguyên tắc:

```text
LIFO — Last In First Out
```

Nghĩa là booking hoàn tất sau cùng sẽ là booking đầu tiên được hoàn tác.

Lý do: thao tác nhập nhầm thường cần sửa ngay thao tác mới nhất, nên Stack phù hợp hơn List thường.

---

## 29. Cập nhật bổ sung về tìm kiếm và sắp xếp

### 29.1 Tìm kiếm

Project sẽ có các chức năng tìm kiếm cơ bản:

- Tìm khách hàng theo mã khách hàng hoặc số điện thoại.
- Tìm xe theo biển số.
- Tìm booking theo mã booking.

Giải thuật dùng: **Linear Search — tìm kiếm tuyến tính**.

Ý nghĩa: hệ thống duyệt từng phần tử trong danh sách cho đến khi tìm thấy dữ liệu phù hợp.

Lý do chọn Linear Search:

- Dễ cài đặt và dễ giải thích trong phạm vi project CLI.
- Phù hợp với dữ liệu mô phỏng không quá lớn.
- Không làm project bị phức tạp hơn mức cần thiết.

### 29.2 Sắp xếp dịch vụ

Project sẽ hỗ trợ sắp xếp dịch vụ rửa xe theo:

- Giá dịch vụ.
- Thời gian rửa.

Giải thuật dùng: **Selection Sort — sắp xếp chọn**.

Cách hiểu:

```text
Mỗi vòng lặp, hệ thống tìm phần tử nhỏ nhất trong phần chưa sắp xếp
→ đưa phần tử đó về đúng vị trí
```

Lý do chọn Selection Sort:

- Dễ cài đặt bằng Java thuần.
- Dễ trình bày khi vấn đáp.
- Danh sách dịch vụ thường nhỏ, nên không cần thuật toán quá phức tạp.

---

## 30. Cập nhật bổ sung về thanh toán đơn giản

Project vẫn giữ thanh toán ở mức mô phỏng đơn giản, nhưng không đưa thanh toán thành một module lớn.

Booking chỉ có trạng thái nghiệp vụ chính:

```text
WAITING
SERVING
COMPLETED
CANCELLED
```

Thanh toán được lưu như thông tin phụ:

```text
paymentStatus: UNPAID hoặc PAID
paymentMethod: CASH hoặc BANKING
```

Luồng thanh toán đơn giản:

```text
Booking đang SERVING
→ nhân viên xác nhận đã thanh toán
→ cập nhật paymentStatus = PAID
→ chọn paymentMethod = CASH hoặc BANKING
```

Booking chỉ nên được hoàn tất khi đã thanh toán.

Lý do: cách này giữ được nghiệp vụ thanh toán trong CSD.md nhưng không làm luồng trạng thái booking quá phức tạp.

---

## 31. Cập nhật bổ sung về hoàn tất booking

Booking chỉ được hoàn tất khi đang ở trạng thái `SERVING`.

Luồng hoàn tất chốt:

```text
SERVING
→ kiểm tra paymentStatus đã PAID
→ COMPLETED
→ cập nhật loyalty
→ push vào CompletedHistoryStack
→ save file
```

Quy tắc chống cộng điểm hai lần:

- Booking đã `COMPLETED` thì không được complete lại.
- Chỉ booking đang `SERVING` và đã `PAID` mới được chuyển sang `COMPLETED`.
- Khi hoàn tất thành công, hệ thống mới cộng điểm, tăng tổng tiền, tăng số lần rửa và xét lại hạng.

Ghi chú làm rõ: trong phạm vi đã chốt, việc hủy booking trong slot chính sẽ kéo booking từ hàng chờ phụ lên. Còn thao tác hoàn tất booking tập trung vào cập nhật loyalty và lịch sử; không bắt buộc tự kéo waitlist lên để tránh làm luồng xử lý bị quá phức tạp.

---

## 32. Cập nhật bổ sung về hủy booking

Khi hủy booking trong slot chính của buổi hiện tại:

```text
Booking bị hủy
→ status chuyển sang CANCELLED
→ lấy booking ưu tiên cao nhất từ Current Waitlist PriorityQueue
→ đưa booking đó vào cuối Current FIFO Queue
→ save file
```

Lý do booking từ waitlist được đưa vào cuối slot chính: khi đã vào slot chính, booking phải tuân theo FIFO để giữ thứ tự xử lý ổn định.

Nếu hủy booking trong hàng chờ phụ hoặc booking tương lai, hệ thống chỉ cần đánh dấu hủy hoặc loại khỏi nhóm chờ phù hợp, sau đó save file.

---

## 33. Cập nhật bổ sung về phạm vi chưa chốt

Các phần sau vẫn chưa được chốt chi tiết và có thể quyết định sau:

1. Cấu trúc menu CLI cụ thể.
2. Có tạo seed data tự động hay không.
3. Danh sách service mẫu.
4. Tên class cụ thể.
5. Thuộc tính chi tiết của từng class.
6. Cách tổ chức package trong source code.
7. Format chính xác của từng dòng trong từng file dữ liệu.

Những phần này chưa cần đưa vào code ngay nếu mục tiêu hiện tại là hoàn thiện phạm vi nghiệp vụ và yêu cầu hệ thống.

---

## 34. Cập nhật bổ sung sau khi chốt các vấn đề còn thiếu

Phần này bổ sung các quyết định mới đã được chốt sau mục 33. Nội dung bên trên được giữ nguyên. Nếu có điểm nào trước đó ghi là “chưa chốt”, thì phần này là quyết định cập nhật mới nhất để dùng khi chuyển sang thiết kế kỹ thuật và code.

### 34.1 Tên project sau khi thu gọn phạm vi

Tên project được chốt là:

> **Car Wash Booking Queue CLI**

Ý nghĩa:

- **Car Wash**: chủ đề quản lý tiệm rửa xe.
- **Booking Queue**: trọng tâm là đặt lịch và hàng chờ.
- **CLI**: chương trình chạy bằng menu trong terminal.

Lý do chọn tên này: tên cũ **Smart Car Wash Queue Management System** vẫn mô tả đúng ý tưởng ban đầu, nhưng tên mới ngắn hơn và nhấn mạnh rõ đây là app CLI nhỏ cho môn CSD, không phải hệ thống web đầy đủ như PRJ301.

### 34.2 Các chức năng lớn bị loại khỏi phạm vi

Vì project cần là app nhỏ, các chức năng lớn sau sẽ bị loại hẳn khỏi phạm vi, không đưa vào phần triển khai:

- Login / logout thật.
- Phân quyền bảo mật thật.
- Promotion.
- Report.
- LPR nhận diện biển số.
- AI personalization.
- Web app.
- Mobile app.
- Database.
- Redeem điểm phức tạp.
- Điểm hết hạn sau 12 tháng.
- Auto downgrade monthly review.

Lý do: các chức năng này phù hợp với hệ thống PRJ301 lớn, nhưng không phù hợp với mục tiêu chính của project CSD là mô phỏng hàng chờ, booking, heap, queue, stack, linked list, file và thuật toán xử lý.

---

## 35. Cập nhật bổ sung về ngày, buổi demo và activate period

### 35.1 Cách xác định ngày và buổi hiện tại

Vì app là CLI, hệ thống không bắt buộc dùng ngày giờ thật của máy.

Admin sẽ nhập hoặc thiết lập thủ công:

```text
currentDate
currentPeriod
```

Trong đó `currentPeriod` gồm:

```text
MORNING
AFTERNOON
EVENING
```

Lý do: khi demo, người dùng cần chủ động giả lập ngày và buổi để kiểm tra nhiều tình huống như booking vượt booking window, booking trong buổi hiện tại, booking tương lai và activate từng buổi.

### 35.2 Activate service period chỉ áp dụng cho buổi hiện tại

Chức năng `Activate service period` chỉ kích hoạt đúng buổi đang được chọn bởi `currentDate` và `currentPeriod`.

Ví dụ:

```text
currentDate = 2026-06-25
currentPeriod = MORNING
```

Khi admin chọn activate, hệ thống chỉ kích hoạt booking của buổi sáng ngày 2026-06-25. Booking buổi chiều và buổi tối của cùng ngày vẫn chưa được kích hoạt.

### 35.3 Không cho activate lại cùng một buổi

Nếu một buổi đã được activate, hệ thống không cho activate lại.

Luật:

```text
Nếu currentDate + currentPeriod đã ACTIVATED
→ không cho activate lại
→ thông báo lỗi
```

Lý do: tránh lỗi duplicate booking, nghĩa là cùng một booking bị đưa vào hàng chờ hiện tại nhiều lần.

### 35.4 Booking mới cho buổi đã activate

Nếu khách tạo booking mới cho đúng buổi đang diễn ra và buổi đó đã activate, booking được xử lý như booking đặt ngay trong buổi hiện tại.

Luật:

```text
Nếu bookingDate = currentDate
và bookingPeriod = currentPeriod
và buổi đã ACTIVATED
→ xử lý như booking đặt ngay
```

Cách đưa vào hàng chờ:

```text
Nếu slot chính còn chỗ
→ đưa vào cuối Current FIFO Queue

Nếu slot chính đầy nhưng waitlist còn chỗ
→ đưa vào Current Waitlist PriorityQueue

Nếu cả hai đều đầy
→ từ chối booking
```

Lý do: sau khi buổi đã mở, khách đặt thêm cho đúng buổi đó giống như khách đến trong buổi hiện tại. Nếu tiệm vẫn còn sức chứa thì hệ thống nên nhận booking.

---

## 36. Cập nhật bổ sung về trạng thái buổi đã activate

### 36.1 Lưu activated period vào file

Hệ thống sẽ lưu trạng thái buổi đã activate vào file:

```text
periods.txt
```

Ví dụ nội dung:

```text
2026-06-25|MORNING|ACTIVATED
2026-06-25|AFTERNOON|ACTIVATED
2026-06-25|EVENING|NOT_ACTIVATED
```

### 36.2 Lý do cần lưu periods.txt

Vì hệ thống đã chốt không cho activate lại cùng một buổi, nên cần lưu thông tin buổi nào đã activate.

Nếu chỉ lưu trong RAM, khi tắt app rồi mở lại, hệ thống có thể quên buổi đã activate và cho activate lại, gây lỗi đưa booking vào queue nhiều lần.

`periods.txt` giúp hệ thống kiểm tra được trạng thái activate ngay cả sau khi đóng và mở lại chương trình.

---

## 37. Cập nhật bổ sung về menu CLI

### 37.1 Cấu trúc menu tổng quát

App CLI sẽ chia thành hai nhóm menu:

```text
1. Customer Menu
2. Admin Menu
0. Exit
```

Hệ thống không làm login thật. Việc chia menu chỉ nhằm mô phỏng vai trò nghiệp vụ và giúp demo rõ ràng hơn.

### 37.2 Customer Menu

Customer Menu gồm:

```text
1. View my profile
2. View services
3. Create booking
4. View my bookings
5. Cancel my booking
0. Back
```

Ý nghĩa:

- `View my profile`: xem tier, điểm, tổng tiền và tổng số lần rửa.
- `View services`: xem danh sách dịch vụ rửa xe.
- `Create booking`: tạo booking và đưa vào nhóm xử lý phù hợp.
- `View my bookings`: xem booking của khách hàng.
- `Cancel my booking`: hủy booking nếu còn hợp lệ.

### 37.3 Admin Menu

Admin Menu gồm:

```text
1. Manage customers
2. Manage vehicles
3. Manage services
4. Activate service period
5. View current queue
6. View current waitlist
7. Process next booking
8. Mark payment as PAID
9. Complete booking
10. Cancel booking
11. View completed history
12. Undo last completed booking
0. Back
```

Ý nghĩa:

- `Manage customers`: thêm, xem, tìm khách hàng.
- `Manage vehicles`: thêm, xem, tìm xe.
- `Manage services`: thêm, xem, tìm, sắp xếp dịch vụ.
- `Activate service period`: chuyển booking tương lai sang hàng chờ hiện tại.
- `View current queue`: xem slot chính của buổi hiện tại.
- `View current waitlist`: xem hàng chờ phụ của buổi hiện tại.
- `Process next booking`: lấy booking đầu tiên trong current queue để rửa.
- `Mark payment as PAID`: xác nhận thanh toán cho booking đang phục vụ.
- `Complete booking`: hoàn tất booking, cập nhật loyalty và lưu lịch sử.
- `Cancel booking`: hủy booking.
- `View completed history`: xem lịch sử booking đã hoàn tất.
- `Undo last completed booking`: hoàn tác booking hoàn tất gần nhất.

Lý do chia menu theo cách này: menu đủ nhỏ để code trong CLI nhưng vẫn bao phủ được các cấu trúc dữ liệu chính của project.

---

## 38. Cập nhật bổ sung về Process Next Booking và Serving

### 38.1 Cách chuyển sang SERVING

Admin chọn chức năng:

```text
Process next booking
```

Hệ thống xử lý:

```text
Lấy booking đầu tiên trong Current FIFO Queue
→ chuyển bookingStatus từ WAITING sang SERVING
→ save file
```

### 38.2 Chỉ có một booking SERVING tại một thời điểm

Luật:

```text
Nếu chưa có booking nào SERVING
→ được process next booking

Nếu đang có booking SERVING
→ không được process booking tiếp theo
→ phải pay, complete hoặc cancel booking đang SERVING trước
```

Lý do: app CLI nhỏ không cần quản lý nhiều wash bay. Chỉ một booking đang rửa tại một thời điểm giúp luồng trạng thái dễ kiểm soát hơn.

### 38.3 Luồng vận hành chính

Luồng xử lý một booking trong buổi hiện tại:

```text
WAITING
→ Process Next Booking
→ SERVING
→ Mark payment as PAID
→ Complete Booking
→ COMPLETED
→ cập nhật loyalty
→ push vào CompletedHistoryStack
→ save file
```

---

## 39. Cập nhật bổ sung về ID tự tăng

### 39.1 Quy tắc sinh ID

Hệ thống sẽ tự sinh ID tăng dần cho các dữ liệu chính.

Quy ước:

```text
Customer: C001, C002, C003...
Vehicle:  V001, V002, V003...
Service:  S001, S002, S003...
Booking:  B001, B002, B003...
```

### 39.2 Cách xử lý khi load file

Khi mở app và load dữ liệu từ file, hệ thống quét ID lớn nhất hiện có rồi sinh ID tiếp theo.

Ví dụ `customers.txt` đang có:

```text
C001|Nguyen Van A|...
C002|Tran Van B|...
C005|Le Van C|...
```

Customer mới sẽ được tạo với ID:

```text
C006
```

Lý do: giảm lỗi nhập trùng ID và giúp demo CLI dễ hơn.

---

## 40. Cập nhật bổ sung về validate dữ liệu

### 40.1 Mức validate được chọn

Hệ thống sẽ validate ở mức vừa đủ, không làm quá phức tạp như app thật.

### 40.2 Quy tắc validate customer

- Tên khách hàng không được rỗng.
- Số điện thoại không được rỗng.
- Số điện thoại không được trùng với khách hàng khác.

### 40.3 Quy tắc validate vehicle

- Biển số không được rỗng.
- Biển số không được trùng với xe khác.
- Customer owner phải tồn tại.

### 40.4 Quy tắc validate service

- Tên dịch vụ không được rỗng.
- Giá dịch vụ phải lớn hơn 0.
- Thời gian rửa phải lớn hơn 0.

### 40.5 Quy tắc validate booking

- Customer phải tồn tại.
- Vehicle phải tồn tại.
- Vehicle phải thuộc về customer đang tạo booking.
- Service phải tồn tại.
- Booking date không được vượt booking window theo tier.
- Booking period phải hợp lệ: MORNING, AFTERNOON hoặc EVENING.
- Buổi được chọn phải còn slot chính hoặc waitlist thì mới nhận booking.

### 40.6 Quy tắc validate payment và complete

- Chỉ booking đang SERVING mới được thanh toán.
- Chỉ booking đang SERVING và có paymentStatus = PAID mới được complete.
- Booking đã COMPLETED không được complete lại.
- Booking đã CANCELLED không được thanh toán hoặc complete.
- Booking chưa PAID không được chuyển sang COMPLETED.

Lý do: mức validate này đủ để tránh lỗi làm sai luồng queue, payment, complete, loyalty và history, nhưng không làm project bị nặng vì các kiểm tra phức tạp như regex số điện thoại hoặc format biển số thật.

---

## 41. Cập nhật bổ sung về seed data

### 41.1 Quy tắc tạo seed data

Hệ thống sẽ tự tạo seed data khi file dữ liệu rỗng hoặc chưa tồn tại.

Luồng:

```text
Start app
→ load dữ liệu từ file
→ nếu file rỗng hoặc chưa tồn tại
→ tạo dữ liệu mẫu ban đầu
→ save dữ liệu mẫu xuống file
```

### 41.2 Dữ liệu mẫu tối thiểu

Seed data nên có:

```text
4 customers:
- 1 Member
- 1 Silver
- 1 Gold
- 1 Platinum

4 vehicles:
- mỗi customer có ít nhất 1 xe

3 services:
- Basic Wash
- Premium Wash
- Interior Cleaning

Một vài booking mẫu:
- booking cho MORNING
- booking cho AFTERNOON
- booking cho EVENING
- booking thuộc nhiều tier khác nhau
```

Lý do: cần dữ liệu mẫu để demo rõ PriorityQueue bằng Heap. Nếu không có nhiều tier khác nhau, phần ưu tiên theo hạng sẽ không thể hiện rõ trong buổi vấn đáp.

---

## 42. Cập nhật bổ sung về cấu trúc thư mục dữ liệu

### 42.1 Mỗi loại dữ liệu một file riêng

Hệ thống lưu mỗi nhóm dữ liệu vào một file riêng trong thư mục `data`.

Cấu trúc đề xuất:

```text
data/
├── customers.txt
├── vehicles.txt
├── services.txt
├── bookings.txt
├── history.txt
└── periods.txt
```

### 42.2 Ý nghĩa từng file

| File | Ý nghĩa |
|---|---|
| customers.txt | Lưu khách hàng, tier, points, totalSpent, visitCount |
| vehicles.txt | Lưu xe và ownerCustomerId |
| services.txt | Lưu dịch vụ, giá, thời gian rửa, trạng thái |
| bookings.txt | Lưu booking, bookingStatus, paymentStatus, ngày, buổi |
| history.txt | Lưu booking đã completed |
| periods.txt | Lưu buổi nào đã activate |

Lý do: tách file riêng giúp dễ debug. Nếu lỗi liên quan customer thì kiểm tra `customers.txt`, nếu lỗi booking thì kiểm tra `bookings.txt`, nếu lỗi activate thì kiểm tra `periods.txt`.

---

## 43. Cập nhật bổ sung về tóm tắt quyết định mới nhất

| Nội dung | Quyết định mới nhất |
|---|---|
| Tên project | Car Wash Booking Queue CLI |
| Kiểu app | CLI chạy bằng menu trong terminal |
| Chức năng lớn bị loại | Login, role thật, promotion, report, LPR, AI, web/mobile, database, redeem phức tạp, expiry, auto downgrade |
| Ngày và buổi demo | Admin tự set currentDate và currentPeriod |
| Activate period | Chỉ activate đúng currentDate + currentPeriod |
| Activate lại cùng buổi | Không cho activate lại |
| Booking mới cho buổi đã activate | Xử lý như booking đặt ngay trong buổi hiện tại |
| Serving | Chỉ 1 booking SERVING tại một thời điểm |
| Process next booking | Lấy đầu Current FIFO Queue chuyển sang SERVING |
| Customer Menu | View profile, view services, create booking, view bookings, cancel booking |
| Admin Menu | Manage data, activate period, view queue/waitlist, process, pay, complete, cancel, history, undo |
| ID | Tự tăng C001, V001, S001, B001 |
| Validate | Validate vừa đủ cho customer, vehicle, service, booking, payment, complete |
| Seed data | Tự tạo khi file rỗng hoặc chưa tồn tại |
| File storage | Mỗi loại dữ liệu một file riêng, dùng dấu `|` |
| Activated period storage | Lưu vào `periods.txt` |

