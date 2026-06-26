# Software Requirements Specification (SRS)
# Car Wash Booking Queue CLI

## Thông tin tài liệu

| Thuộc tính | Giá trị |
|---|---|
| Tên tài liệu | Software Requirements Specification (SRS) |
| Tên hệ thống | Car Wash Booking Queue CLI |
| Phiên bản | v3.0.1 |
| Ngày tạo | 2026-06-26 |
| Sửa đổi gần nhất | 2026-06-26 |
| Nguồn xây dựng | SRSV2.md; actor_usecase_summary_car_wash_cli.md |
| Ngôn ngữ | Tiếng Việt |
| Trạng thái | Hoàn thiện |

## Lịch sử phiên bản

| Phiên bản | Ngày | Người/nguồn cập nhật | Mô tả thay đổi |
|---|---|---|---|
| v3.0 | 2026-06-26 | SRSV2.md; actor_usecase_summary_car_wash_cli.md | Chuẩn hóa tài liệu theo cấu trúc IEEE 830 và tích hợp phân tích Actor/Use Case |

## 1. Giới thiệu

### 1.1 Mục đích tài liệu
Tài liệu này mô tả chi tiết các yêu cầu phần mềm cho dự án **Car Wash Booking Queue CLI**.
Dự án mô phỏng toàn bộ hoạt động đặt lịch và điều phối xếp hàng rửa xe trong một tiệm rửa xe. Hệ thống tập trung vào việc quản lý khách hàng, xe, dịch vụ, lịch đặt rửa xe, hàng chờ theo từng buổi phục vụ, lịch sử rửa xe và cập nhật tích lũy loyalty (điểm, hạng thành viên) sau khi khách hoàn tất dịch vụ.
Tài liệu này mô tả chức năng, luồng xử lý, lý do cần có chức năng và phạm vi nghiệp vụ. Ngoài ra, vì dự án thuộc môn Cấu trúc dữ liệu và giải thuật (CSD), tài liệu bổ sung phần định hướng cấu trúc dữ liệu ở mức khái niệm để giải thích sự phù hợp của các cấu trúc dữ liệu tự định nghĩa như **Queue, Priority Queue, Stack, List, Map** và các thuật toán tìm kiếm, sắp xếp cơ bản. Tài liệu không đi sâu vào thiết kế code chi tiết, thuộc tính class hay hướng dẫn lập trình chi tiết.

### 1.2 Phạm vi hệ thống
Sản phẩm được xây dựng dưới dạng ứng dụng giao diện dòng lệnh (**CLI - Command Line Interface**) chạy bằng menu trong terminal, phục vụ mục đích học tập và mô phỏng nghiệp vụ thực tế của một tiệm rửa xe. Hệ thống sử dụng file text phẳng (`.txt`) dùng dấu phân tách `|` để lưu trữ dữ liệu lâu dài và nạp toàn bộ vào bộ nhớ RAM khi khởi động để xử lý bằng các cấu trúc dữ liệu tự định nghĩa.

### 1.3 Đối tượng sử dụng tài liệu
- Giảng viên môn CSD201 dùng để đánh giá yêu cầu và định hướng thiết kế cấu trúc dữ liệu của dự án.
- Sinh viên thực hiện dự án dùng làm căn cứ lập trình, kiểm thử và chuẩn bị nội dung vấn đáp (Q&A).

### 1.4 Thuật ngữ và định nghĩa

| Thuật ngữ | Ý nghĩa |
|---|---|
| Customer | Khách hàng sử dụng dịch vụ rửa xe |
| currentCustomer | Khách hàng hiện tại đang thao tác trong Customer Menu (được xác định sau khi nhập mã khách hàng hợp lệ) |
| Vehicle | Xe của khách hàng |
| Service | Dịch vụ rửa xe |
| Booking | Lịch đặt rửa xe |
| Service period | Buổi phục vụ trong ngày (Sáng, Chiều, Tối) |
| Current period | Buổi đang diễn ra (buổi được thiết lập hiện tại) |
| Future period | Buổi chưa diễn ra trong tương lai |
| Main slot | Suất rửa xe chính trong buổi, được đảm bảo phục vụ |
| Waitlist | Hàng chờ phụ, chứa các booking dự phòng khi suất chính đã đầy |
| Tier | Hạng thành viên của khách hàng (Member, Silver, Gold, Platinum) |
| Booking window | Số ngày tối đa khách hàng được phép đặt trước dựa vào hạng thành viên |
| Loyalty | Cơ chế tích điểm và xét hạng khách hàng thân thiết |
| Completed booking | Booking đã rửa xe xong và hoàn tất dịch vụ |
| Undo | Hoàn tác thao tác hoàn tất booking gần nhất |
| CLI | Giao diện dòng lệnh (chạy trên terminal) |

### 1.5 Tài liệu tham khảo
- Ý tưởng gốc được rút gọn từ đề tài môn học PRJ301 (AutoWash Pro).
- Chuẩn đặc tả yêu cầu phần mềm IEEE 830 (được tinh chỉnh phù hợp với phạm vi môn học CSD201).

---

## 2. Mô tả tổng quan

### 2.1 Bối cảnh hệ thống
Một tiệm rửa xe có số lượng xe phục vụ giới hạn trong từng buổi do giới hạn về thời gian, nhân lực và mặt bằng. Khách hàng có thể đặt lịch trước (tương lai) hoặc đặt ngay trong buổi đang diễn ra. Do sức chứa có hạn, hệ thống cần kiểm tra khả năng tiếp nhận và phân bổ booking vào slot chính (Main slot) hoặc hàng chờ phụ (Waitlist). 
Khách hàng có hạng thành viên khác nhau. Hạng càng cao sẽ có nhiều đặc quyền hơn như thời gian đặt trước xa hơn (Booking window lớn hơn) hoặc mức độ ưu tiên xếp chỗ cao hơn khi nằm trong hàng chờ. Sau khi hoàn tất dịch vụ, hệ thống sẽ tích lũy chi tiêu, tính điểm tích lũy và xét nâng hạng cho khách hàng.

### 2.2 Mục tiêu hệ thống
Hệ thống cần giải quyết bài toán cốt lõi:
> Khi nhiều khách hàng đặt lịch rửa xe trong cùng một ngày, hệ thống phải quản lý được ai được nhận vào slot chính, ai phải vào hàng chờ phụ, ai được xử lý trước dựa trên độ ưu tiên, và lịch sử sau khi rửa xe được lưu lại như thế nào.

Mục tiêu cụ thể:
- Giúp tiệm rửa xe tiếp nhận booking có giới hạn theo từng buổi cụ thể.
- Phân biệt booking đặt ngay trong buổi hiện tại và booking đặt trước cho buổi tương lai.
- Ưu tiên cho khách hàng hạng thành viên cao khi sắp xếp lịch và hàng chờ.
- Giúp nhân viên quản lý thứ tự xe cần rửa tiếp theo một cách khoa học.
- Lưu trữ lịch sử rửa xe và tự động hóa cập nhật loyalty (điểm, hạng) chính xác.
- Hỗ trợ hoàn tác nhanh nếu nhân viên thao tác nhầm lẫn.

### 2.3 Kiểu ứng dụng CLI
Hệ thống được triển khai dưới dạng **CLI (Command Line Interface)**, chạy bằng menu trong terminal. Kiểu ứng dụng này giúp tập trung tối đa vào cấu trúc dữ liệu và thuật toán trong bộ nhớ RAM mà không bị phân tán bởi việc thiết kế giao diện đồ họa.

### 2.4 Nhóm người dùng
Hệ thống mô phỏng hai nhóm vai trò sử dụng (không cần cơ chế đăng nhập/đăng xuất thực tế, việc phân chia chỉ phục vụ cho giao diện menu CLI phân cấp):
- **Khách hàng (Customer)**: Xem thông tin cá nhân (hạng, điểm, chi tiêu), xem danh sách dịch vụ rửa xe, tạo booking, xem danh sách booking cá nhân, xem lịch sử rửa xe cá nhân và hủy booking của mình.
- **Admin/Nhân viên (Admin)**: Quản lý khách hàng, quản lý xe của khách hàng, quản lý dịch vụ (thêm, xem, tìm kiếm, sắp xếp), thiết lập ngày/buổi hiện tại, kích hoạt buổi phục vụ, tạo booking cho khách hàng, xem hàng chờ chính/phụ, bắt đầu xử lý xe tiếp theo, xác nhận thanh toán mô phỏng, hoàn tất booking, hủy booking bất kỳ, xem lịch sử và hoàn tác booking hoàn tất gần nhất.

### 2.5 Actor và quyền theo vai trò
Hệ thống không triển khai đăng nhập và phân quyền bảo mật thực tế. Tuy nhiên, để mô phỏng nghiệp vụ và tổ chức menu CLI rõ ràng, hệ thống chia hành vi thành hai vai trò sử dụng (Actor):
1. **Customer (Khách hàng)**: Đại diện cho người sử dụng dịch vụ rửa xe. Khi vào Customer Menu, hệ thống yêu cầu nhập mã khách hàng để xác định khách hàng hiện tại (`currentCustomer`). Phiên làm việc sẽ kết thúc khi quay lại Main Menu.
2. **Admin/Nhân viên (Người vận hành)**: Đại diện cho người quản lý dữ liệu và vận hành tiệm. Admin Menu có thể truy cập trực tiếp từ Main Menu mà không cần tài khoản hay mật khẩu.

Bảng tổng hợp quyền theo vai trò (Actor Matrix):

| Chức năng / Hành vi | Customer | Admin/Nhân viên |
|---|:---:|:---:|
| Xem danh sách dịch vụ | Có | Có |
| Xem thông tin cá nhân | Có (chỉ của mình) | Có thể xem qua quản lý khách hàng |
| Tạo booking | Có (chỉ cho chính mình) | Có (cho bất kỳ khách hợp lệ) |
| Xem booking cá nhân | Có (chỉ booking WAITING/SERVING của mình) | Có thể xem booking hệ thống |
| Hủy booking | Có (chỉ booking của mình đang WAITING) | Có (booking WAITING hoặc SERVING) |
| Xem lịch sử cá nhân | Có (chỉ booking COMPLETED của mình) | Có thể xem toàn bộ lịch sử |
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

### 2.6 Danh sách Use Case tổng quan

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

### 2.7 Giả định và ràng buộc
- **Giả định về thời gian**: Hệ thống không bắt buộc lấy thời gian thực từ máy tính mà cho phép Admin thiết lập thủ công ngày hiện tại (`currentDate`) và buổi hiện tại (`currentPeriod`) để phục vụ kiểm thử và demo các tình huống nghiệp vụ khác nhau.
- **Ràng buộc lưu trữ**: Không sử dụng hệ quản trị cơ sở dữ liệu (SQL Server, MySQL, v.v.). Toàn bộ dữ liệu được lưu trữ lâu dài trong các file text (`.txt`) và tải vào bộ nhớ RAM khi khởi động.
- **Ràng buộc về xử lý**: Tại một thời điểm, tiệm chỉ có đúng 1 vị trí rửa xe hoạt động (chỉ cho phép tối đa 1 booking ở trạng thái `SERVING`).

### 2.8 Chức năng nằm trong phạm vi
- Quản lý thông tin khách hàng, xe của khách và dịch vụ rửa xe.
- Thiết lập ngày/buổi hiện tại và kích hoạt buổi rửa xe.
- Tạo booking rửa xe, kiểm tra booking window theo hạng thành viên.
- Quản lý slot chính (Queue) và hàng chờ phụ (Priority Queue) theo buổi.
- Xử lý xe tiếp theo, xác nhận thanh toán mô phỏng và hoàn tất dịch vụ.
- Hủy booking và tự động điều phối hàng chờ phụ lên slot chính.
- Cập nhật điểm tích lũy và xét hạng thành viên sau khi hoàn tất dịch vụ.
- Lưu lịch sử rửa xe và hoàn tác (Undo) booking hoàn tất gần nhất bằng Stack.
- Tải/Lưu dữ liệu qua file text phẳng sử dụng dấu phân tách `|`.
- Tự động sinh ID tăng dần và khởi tạo dữ liệu mẫu (seed data) khi file rỗng.

### 2.9 Chức năng ngoài phạm vi
- Đăng nhập/đăng xuất thực tế và phân quyền tài khoản bảo mật.
- Giao diện người dùng Web hoặc Mobile.
- Tương tác với cơ sở dữ liệu (DBMS).
- Thanh toán trực tuyến thực tế (online payment gateway).
- Nhận diện biển số xe tự động (LPR - License Plate Recognition).
- Gửi email, SMS hoặc thông báo tự động.
- Quản lý chương trình khuyến mãi nâng cao, đổi điểm lấy quà, hạ hạng định kỳ theo thời gian hoặc hết hạn điểm.
- Hệ thống không có chức năng tự động hạ hạng thành viên theo chu kỳ ngày/tháng/năm. Tuy nhiên, hạng thành viên có thể thay đổi khi hệ thống thực hiện tính toán lại loyalty từ dữ liệu booking đã hoàn tất. Việc thay đổi này không được xem là hạ hạng định kỳ, mà là kết quả của quá trình đồng bộ lại dữ liệu tích lũy.

---

## 3. Quy tắc nghiệp vụ

### 3.1 Quy tắc chia buổi rửa xe
Một ngày phục vụ được chia thành 3 buổi cố định với giới hạn sức chứa (slot chính và slot chờ phụ) riêng biệt:

| Buổi | Khung thời gian | Số slot chính | Số slot chờ phụ |
|---|---|---|---|
| **Sáng (MORNING)** | 07:00 - 12:00 | 10 | 3 |
| **Chiều (AFTERNOON)** | 13:00 - 17:00 | 10 | 3 |
| **Tối (EVENING)** | 18:00 - 21:00 | 5 | 2 |

Số slot này là giới hạn cứng của tiệm nhằm đảm bảo chất lượng dịch vụ.

### 3.2 Quy tắc slot chính và hàng chờ phụ
- **Slot chính (Main slot)**: Dành cho các khách hàng chắc chắn được phục vụ trong buổi. Thứ tự phục vụ trong slot chính tuân theo nguyên tắc "ai vào trước rửa trước" (FIFO).
- **Hàng chờ phụ (Waitlist)**: Dành cho khách đặt lịch khi slot chính của buổi đã đầy. Khi slot chính bị trống (do hủy lịch hoặc do hệ thống đủ điều kiện kéo thêm xe), khách hàng trong hàng chờ phụ sẽ được chọn để đẩy lên slot chính dựa theo độ ưu tiên (hạng thành viên và thời gian tạo).

### 3.3 Quy tắc hạng thành viên
Hệ thống quản lý 4 hạng thành viên của khách hàng với điều kiện nâng hạng dựa trên số lần sử dụng dịch vụ (Visit Count) hoặc tổng chi tiêu (Total Spent):
- **Member (Mặc định)**: Khách hàng mới đăng ký. Các chỉ số ban đầu: Visit Count = 0, Loyalty Points = 0, Total Spent = 0.
- **Silver**: Đạt tối thiểu **5 lần rửa** HOẶC tổng chi tiêu từ **2,000,000 VND**.
- **Gold**: Đạt tối thiểu **15 lần rửa** HOẶC tổng chi tiêu từ **6,000,000 VND**.
- **Platinum**: Đạt tối thiểu **30 lần rửa** HOẶC tổng chi tiêu từ **15,000,000 VND**.

*Quy tắc xét hạng*: Khi booking hoàn tất, hệ thống tính toán các chỉ số và nâng thẳng lên hạng cao nhất mà khách hàng đạt điều kiện (không nâng từng bậc, không tự động hạ hạng).

### 3.4 Quy tắc booking window
Giới hạn khoảng cách ngày đặt trước tối đa tính từ ngày hiện tại mô phỏng (`currentDate`) dựa trên hạng thành viên của khách hàng:

| Hạng thành viên | Số ngày đặt trước tối đa |
|---|---|
| **Member** | 7 ngày |
| **Silver** | 10 ngày |
| **Gold** | 12 ngày |
| **Platinum** | 14 ngày |

Nếu ngày đặt lịch vượt quá giới hạn này, hệ thống sẽ từ chối nhận booking. Ràng buộc này bắt buộc áp dụng đối với cả Customer tự đặt và Admin/Nhân viên đặt hộ khách. Admin/Nhân viên không được phép override booking window này.

### 3.5 Quy tắc booking hiện tại
Booking hiện tại là booking được tạo cho đúng ngày hiện tại (`currentDate`) và buổi hiện tại (`currentPeriod`).
- Nếu buổi đó **đã được kích hoạt** (Activated):
  - Nếu slot chính còn trống: booking được xếp vào cuối hàng chờ chính (FIFO Queue).
  - Nếu slot chính đầy nhưng hàng chờ phụ còn trống: booking được đưa vào hàng chờ phụ (Priority Queue).
  - Nếu cả hai đều đầy: hệ thống từ chối booking.
- Nếu buổi đó **chưa được kích hoạt**: booking được lưu vào danh sách booking tương lai của buổi đó (để xếp thứ tự ưu tiên khi kích hoạt).

### 3.6 Quy tắc booking tương lai
Booking tương lai là booking được đặt cho một ngày sau ngày hiện tại, hoặc đặt cho buổi sau của ngày hiện tại (khi buổi đó chưa diễn ra và chưa được kích hoạt).
- Khi tạo booking tương lai, hệ thống kiểm tra tổng sức chứa (Slot chính + Slot chờ phụ) của buổi đó. Nếu còn chỗ, hệ thống ghi nhận booking. Nếu sức chứa đã đầy, hệ thống hiển thị rõ trạng thái đã đầy của ngày/buổi đó và không cho tạo booking.
- Các booking tương lai chưa được xếp vào hàng chờ phục vụ ngay mà được lưu tạm. Khi buổi đó được kích hoạt, hệ thống mới tiến hành sắp xếp chúng theo mức độ ưu tiên để phân bổ vào slot chính và hàng chờ phụ.

### 3.7 Quy tắc kích hoạt buổi (Activate period)
- Thao tác kích hoạt chuyển đổi trạng thái của buổi được chỉ định từ "chưa diễn ra" sang "đang diễn ra".
- Chỉ cho phép kích hoạt buổi trùng khớp với ngày hiện tại (`currentDate`) và buổi hiện tại (`currentPeriod`) được cấu hình trong hệ thống.
- **Ngăn chặn kích hoạt trùng**: Mỗi buổi (ngày + period) chỉ được kích hoạt duy nhất một lần. Nếu buổi đó đã kích hoạt trước đó, hệ thống sẽ báo lỗi. Trạng thái kích hoạt được lưu xuống file `periods.txt` để đảm bảo không bị reset khi khởi động lại app.
- Khi kích hoạt, hệ thống lấy toàn bộ booking tương lai đã đặt trước của buổi đó, sắp xếp theo thứ tự ưu tiên:
  1. Hạng thành viên cao hơn được ưu tiên trước (Platinum > Gold > Silver > Member).
  2. Nếu cùng hạng thành viên, booking nào được tạo trước (thời gian đặt sớm hơn) được ưu tiên trước.
- Sắp xếp xong, hệ thống đổ lần lượt các booking ưu tiên cao vào slot chính (tối đa bằng giới hạn slot chính của buổi). Số còn lại (nếu có) được đổ vào hàng chờ phụ (tối đa bằng giới hạn slot chờ phụ). Trong điều kiện dữ liệu hợp lệ, hệ thống không phát sinh booking dư thừa vì tổng sức chứa đã được kiểm tra khi tạo booking. Trường hợp phát hiện dữ liệu vượt sức chứa do lỗi file hoặc dữ liệu thủ công, hệ thống sẽ từ chối phân bổ phần dư và báo lỗi dữ liệu. (do vượt quá sức chứa khi sắp xếp).

### 3.8 Quy tắc xử lý hàng chờ phụ
Khi slot chính của buổi hiện tại xuất hiện chỗ trống (do hủy booking hoặc do hệ thống thực hiện nghiệp vụ kéo thêm xe):
- Hệ thống sẽ lấy booking có độ ưu tiên cao nhất trong hàng chờ phụ (Waitlist Priority Queue).
- Tiêu chí ưu tiên trong Waitlist: Hạng thành viên cao hơn được ưu tiên trước (Platinum > Gold > Silver > Member). Nếu cùng hạng thành viên, booking nào được tạo trước (thời gian tạo sớm hơn) được ưu tiên trước.
- Đưa booking này vào cuối hàng chờ chính (FIFO Queue).
- Sau khi được đưa vào hàng chờ chính, booking này tuân thủ nguyên tắc FIFO để giữ thứ tự phục vụ ổn định.

### 3.9.1 Quy tắc hoàn tất booking (Complete booking)
- Một booking chỉ được hoàn tất khi đang ở trạng thái đang phục vụ (`SERVING`) và đã được thanh toán (`paymentStatus = PAID`).
- Khi bấm hoàn tất:
  1. Trạng thái booking chuyển sang `COMPLETED`.
  2. Ghi nhận thông tin vào lịch sử rửa xe (`history.txt`).
  3. Cập nhật các chỉ số tích lũy của khách hàng: tăng số lần rửa xe, cộng tổng tiền chi tiêu, cộng điểm loyalty theo tỷ lệ.
  4. Xét lại hạng thành viên của khách hàng.
  5. Đẩy thông tin booking này vào Stack hoàn tác để hỗ trợ sửa lỗi.
  6. **Kiểm tra và bổ sung từ hàng chờ phụ**: Hệ thống kiểm tra thời gian còn lại của buổi hiện tại. Nếu còn đủ thời gian phục vụ thêm 1 xe (dựa vào thời gian thực hiện của dịch vụ của xe tiếp theo trong waitlist), hệ thống sẽ kéo booking ưu tiên cao nhất từ waitlist lên cuối hàng chờ chính. Nếu không đủ thời gian hoặc waitlist trống, hệ thống không kéo thêm.
  7. Lưu toàn bộ thay đổi xuống các file lưu trữ tương ứng.

### 3.9.2 Quy tắc kiểm tra thời gian còn lại của buổi

Hệ thống sử dụng thời gian mô phỏng theo từng buổi phục vụ, không phụ thuộc vào thời gian thực của máy tính. Mỗi buổi có tổng thời lượng phục vụ cố định dựa trên khung giờ đã quy định:

| Buổi | Khung thời gian | Tổng thời lượng |
|---|---|---:|
| Sáng (MORNING) | 07:00 - 12:00 | 300 phút |
| Chiều (AFTERNOON) | 13:00 - 17:00 | 240 phút |
| Tối (EVENING) | 18:00 - 21:00 | 180 phút |

Hệ thống không lưu trực tiếp `usedMinutes` vào file `periods.txt`. Mỗi khi cần kiểm tra thời gian còn lại của một buổi, hệ thống tính lại `usedMinutes` từ dữ liệu booking đã hoàn tất.

Công thức:

```text
usedMinutes = tổng serviceDuration của các booking thỏa mãn:
- cùng ngày với buổi cần kiểm tra
- cùng period với buổi cần kiểm tra
- trạng thái booking là COMPLETED

remainingMinutes = periodTotalMinutes - usedMinutes
```

### 3.10 Quy tắc hủy booking (Cancel booking)
- Booking ở trạng thái chờ (`WAITING`) hoặc đang phục vụ (`SERVING`) có thể được hủy bởi Admin hoặc chính khách hàng (chỉ WAITING). Booking đã hoàn tất (`COMPLETED`) không được hủy.
- Khi hủy booking:
  - Nếu actor là **Customer**: chỉ được hủy booking của chính mình khi booking đó đang ở trạng thái `WAITING`.
  - Nếu actor là **Admin/Nhân viên**: được phép hủy booking của bất kỳ khách hàng nào ở trạng thái `WAITING` hoặc `SERVING`.
  - Khi hủy booking trong slot chính của buổi hiện tại: Trạng thái booking chuyển sang `CANCELLED`. Hệ thống ngay lập tức kiểm tra hàng chờ phụ (Waitlist), nếu có khách đang chờ, hệ thống lấy booking ưu tiên cao nhất từ Waitlist đưa vào cuối hàng chờ chính (FIFO Queue).
  - Khi hủy booking trong hàng chờ phụ hoặc booking tương lai: hệ thống cập nhật trạng thái booking thành `CANCELLED` và loại khỏi danh sách chờ tương ứng.
  - Lưu trạng thái mới xuống file `bookings.txt`.
  - Khi Admin/Nhân viên hủy booking đang ở trạng thái `SERVING`, booking được chuyển sang trạng thái `CANCELLED` và vị trí phục vụ hiện tại được giải phóng. Booking bị hủy khi đang `SERVING` không được ghi nhận vào lịch sử rửa xe hoàn tất (`history.txt`), không cộng loyalty, không tăng số lần rửa xe và không cộng tổng chi tiêu.
  - Nếu booking `SERVING` đã có `paymentStatus = PAID`, hệ thống giữ nguyên trạng thái thanh toán hiện tại để phản ánh rằng giao dịch thanh toán đã từng được xác nhận. Hệ thống không xử lý hoàn tiền hoặc đối soát thanh toán thực tế vì các nghiệp vụ này nằm ngoài phạm vi của ứng dụng CLI.
  - Sau khi hủy booking `SERVING`, hệ thống kiểm tra Waitlist theo quy tắc thời gian còn lại của buổi. Nếu Waitlist còn booking và thời gian còn lại đủ để phục vụ booking có độ ưu tiên cao nhất, hệ thống kéo booking đó từ Waitlist lên cuối Main Queue. Nếu không đủ thời gian hoặc Waitlist trống, hệ thống giữ nguyên hàng chờ.
  - Thao tác hủy booking `SERVING` không tự động chuyển booking khác sang trạng thái `SERVING`. Để bắt đầu rửa xe tiếp theo, Admin/Nhân viên phải sử dụng chức năng xử lý booking tiếp theo.

### 3.11 Quy tắc loyalty (Điểm và hạng)
- **Tỷ lệ tích điểm**: Mỗi **1,000 VND** chi tiêu tích lũy được **1 điểm** loyalty (Ví dụ: dịch vụ giá 100,000 VND tích 100 điểm).
- **Quy đổi độ ưu tiên hạng thành viên**:
  - Member: Ưu tiên mức 1.
  - Silver: Ưu tiên mức 2.
  - Gold: Ưu tiên mức 3.
  - Platinum: Ưu tiên mức 4.
- Điểm loyalty và hạng thành viên được cập nhật ngay lập tức sau khi booking được chuyển sang trạng thái `COMPLETED` thành công.

Hệ thống không xem điểm loyalty, số lần rửa xe, tổng chi tiêu và hạng thành viên là dữ liệu được chỉnh sửa thủ công độc lập. Các giá trị này phải được tính toán dựa trên dữ liệu booking đã hoàn tất (`COMPLETED`).

Mỗi khi có thao tác làm thay đổi dữ liệu ảnh hưởng đến loyalty, hệ thống sẽ thực hiện tính toán lại loyalty cho khách hàng liên quan. Các thao tác có thể kích hoạt việc tính toán lại bao gồm:
- Hoàn tất booking.
- Hoàn tác booking đã hoàn tất.
- Hủy hoặc thay đổi trạng thái booking trong trường hợp booking đó từng ảnh hưởng đến lịch sử hoàn tất.
- Chỉnh sửa dữ liệu liên quan đến giá dịch vụ của booking đã hoàn tất nếu hệ thống cho phép chỉnh sửa dữ liệu này.

Công thức tính lại:
- `visitCount` = tổng số booking `COMPLETED` của khách hàng.
- `totalSpent` = tổng giá trị dịch vụ của các booking `COMPLETED` của khách hàng.
- `loyaltyPoints` = `totalSpent / 1000`.
- `tier` = hạng thành viên cao nhất mà khách hàng đạt được dựa trên `visitCount` hoặc `totalSpent`.

Việc tính toán lại loyalty nhằm đảm bảo dữ liệu khách hàng luôn nhất quán với lịch sử booking thực tế. Hệ thống không thực hiện hạ hạng định kỳ theo thời gian; mọi thay đổi hạng chỉ là kết quả của việc tính toán lại dữ liệu sau một thao tác nghiệp vụ hợp lệ.

### 3.12 Quy tắc lịch sử
- Lịch sử rửa xe chỉ ghi nhận các booking đã chuyển sang trạng thái `COMPLETED` thành công.
- Thông tin ghi nhận gồm: mã booking, khách hàng, xe, dịch vụ, thời gian hoàn tất, số tiền thanh toán và số điểm tích lũy được cộng.
- Dữ liệu lịch sử được ghi nhận lâu dài vào file `history.txt`.

### 3.13 Quy tắc hoàn tác booking hoàn tất gần nhất (Undo)
- Hệ thống chỉ cho phép hoàn tác thao tác hoàn tất của **booking vừa được complete gần nhất** (sử dụng cấu trúc Stack). Không cho phép hoàn tác tùy ý các booking cũ hơn trong lịch sử.
- Khi thực hiện hoàn tác:
Khi thực hiện hoàn tác:
  1. Lấy bản ghi hoàn tác gần nhất ra khỏi Stack.
  2. Chuyển booking đã hoàn tất trong bản ghi đó từ `COMPLETED` về trạng thái trước đó, mặc định là `SERVING`, giữ nguyên trạng thái thanh toán `PAID`.
  3. Xóa hoặc vô hiệu hóa bản ghi lịch sử tương ứng của booking vừa được hoàn tác.
  4. Nếu bản ghi hoàn tác có lưu booking đã được kéo từ Waitlist lên Main Queue, hệ thống loại booking đó khỏi Main Queue và đưa trở lại Waitlist.
  5. Khi đưa booking trở lại Waitlist, hệ thống sắp xếp lại Waitlist theo Priority Queue.
  6. Gọi chức năng tính toán lại loyalty cho khách hàng liên quan dựa trên các booking còn ở trạng thái `COMPLETED`.
  7. Lưu dữ liệu đã hoàn tác xuống file.

- Nếu thao tác hoàn tất booking trước đó đã làm hệ thống kéo một booking từ Waitlist lên Main Queue, thao tác Undo phải hoàn tác cả tác động này. Booking đã được kéo lên sẽ bị loại khỏi Main Queue và được đưa trở lại Waitlist.
- Khi đưa booking trở lại Waitlist, hệ thống không cần khôi phục vị trí cũ tuyệt đối. Booking sẽ được đưa lại vào Priority Queue và được sắp xếp theo quy tắc ưu tiên hiện hành: hạng thành viên cao hơn được ưu tiên trước; nếu cùng hạng thì booking tạo sớm hơn được ưu tiên trước.
- Để thực hiện được nghiệp vụ này, Stack hoàn tác phải lưu thông tin về booking đã hoàn tất và booking đã được kéo từ Waitlist lên Main Queue, nếu có.

### 3.14 Quy tắc trạng thái booking
Mỗi booking trong hệ thống phải thuộc một trong các trạng thái sau:
- `WAITING`: Booking đã được tiếp nhận và đang xếp hàng chờ phục vụ.
- `SERVING`: Xe đang được rửa (tại một thời điểm chỉ có tối đa 1 booking ở trạng thái này).
- `COMPLETED`: Xe đã rửa xong, thanh toán xong và hoàn tất dịch vụ.
- `CANCELLED`: Booking đã bị hủy và không phục vụ nữa.

*Luồng chuyển đổi trạng thái hợp lệ*:
- Luồng phục vụ chuẩn: `WAITING` $\rightarrow$ `SERVING` $\rightarrow$ `COMPLETED`.
- Luồng hủy: `WAITING` $\rightarrow$ `CANCELLED` hoặc `SERVING` $\rightarrow$ `CANCELLED`.
- Luồng hoàn tác: `COMPLETED` $\rightarrow$ `SERVING` (để nhân viên xử lý lại).

### 3.15 Quy tắc lưu trữ bằng file
- Hệ thống không kết nối cơ sở dữ liệu. Toàn bộ dữ liệu được lưu dưới dạng file văn bản phẳng (`.txt`) trong thư mục `data`.
- Định dạng mỗi dòng trong file text sử dụng dấu đứng `|` làm ký tự phân tách giữa các trường dữ liệu để tránh lỗi phân tách khi dữ liệu (tên khách hàng, tên dịch vụ) chứa dấu phẩy.
- Thao tác ghi dữ liệu (Save) được thực hiện ngay lập tức sau mỗi thay đổi quan trọng của hệ thống (như tạo booking, đổi trạng thái, thanh toán, hủy, hoàn tất, hoàn tác, cập nhật thông tin khách hàng) để giảm thiểu rủi ro mất mát dữ liệu khi ứng dụng CLI bị đóng đột ngột.

---

## 4. Yêu cầu chức năng

### 4.1 Danh sách Functional Requirement
- **FR-01 — Quản lý khách hàng**: Cho phép admin thêm, xem, tìm kiếm, cập nhật và xóa khách hàng (kèm ràng buộc dữ liệu liên kết).
- **FR-02 — Quản lý xe của khách hàng**: Cho phép admin liên kết thông tin xe (biển số) với khách hàng, xem danh sách xe và tìm kiếm xe.
- **FR-03 — Quản lý dịch vụ rửa xe**: Cho phép admin thêm, xem, tìm kiếm và sắp xếp danh sách dịch vụ rửa xe theo giá hoặc thời gian thực hiện.
- **FR-04 — Thiết lập ngày và buổi hiện tại**: Cho phép admin thay đổi ngày (`currentDate`) và buổi (`currentPeriod`) mô phỏng của hệ thống.
- **FR-05 — Tạo booking rửa xe**: Cho phép khách hàng hoặc admin tạo lịch đặt rửa xe cho một khách hàng, một xe, một dịch vụ, vào một ngày và buổi cụ thể.
- **FR-06 — Kiểm tra booking window theo hạng thành viên**: Hệ thống tự động kiểm tra số ngày đặt trước của khách hàng có vượt quá giới hạn cho phép của hạng thành viên của họ hay không.
- **FR-07 — Xác định buổi rửa xe của booking**: Phân loại booking vào đúng buổi phục vụ (Sáng, Chiều hoặc Tối) dựa trên thông tin đăng ký của khách hàng.
- **FR-08 — Xử lý booking trong buổi hiện tại**: Tiếp nhận và phân bổ các booking được đặt cho chính ngày và buổi hiện tại đang hoạt động.
- **FR-09 — Xử lý booking cho buổi tương lai**: Lưu trữ và quản lý tạm thời các booking đặt trước cho các buổi trong tương lai.
- **FR-10 — Kích hoạt buổi rửa xe**: Cho phép admin kích hoạt buổi hiện tại để sắp xếp các booking tương lai của buổi đó vào hàng chờ chính và hàng chờ phụ thực tế.
- **FR-11 — Ngăn kích hoạt trùng buổi**: Kiểm tra và ngăn chặn admin kích hoạt lại một buổi đã được kích hoạt trước đó.
- **FR-12 — Xem hàng chờ chính của buổi hiện tại**: Hiển thị danh sách các booking đang nằm trong slot chính (hàng chờ chính) của buổi đang diễn ra theo đúng thứ tự xếp hàng.
- **FR-13 — Xem hàng chờ phụ của buổi hiện tại**: Hiển thị danh sách các booking đang nằm trong hàng chờ phụ (Waitlist) của buổi đang diễn ra theo thứ tự ưu tiên.
- **FR-14 — Xem booking của khách hàng**: Cho phép khách hàng tra cứu toàn bộ danh sách booking của cá nhân họ cùng trạng thái tương ứng.
- **FR-15 — Xử lý booking tiếp theo**: Lấy booking đứng đầu hàng chờ chính chuyển sang trạng thái đang rửa (`SERVING`).
- **FR-16 — Xác nhận thanh toán booking**: Cho phép admin xác nhận khách hàng đã thanh toán hóa đơn cho booking đang ở trạng thái `SERVING`.
- **FR-17 — Hoàn tất booking**: Xác nhận dịch vụ rửa xe đã hoàn tất cho xe đang rửa, thực hiện tích điểm, cập nhật hạng thành viên và lưu lịch sử.
- **FR-18 — Hủy booking**: Cho phép admin hoặc khách hàng hủy một booking chưa thực hiện.
- **FR-19 — Cập nhật loyalty sau khi hoàn tất booking**: Tự động tính toán cộng điểm, tăng chi tiêu, tăng số lần rửa xe và xét lại hạng thành viên của khách hàng sau khi booking hoàn tất.
- **FR-20 — Xem lịch sử rửa xe**: Hiển thị danh sách các booking đã hoàn tất phục vụ của tiệm.
- **FR-21 — Hoàn tác booking hoàn tất gần nhất**: Đảo ngược toàn bộ tác động của booking vừa hoàn tất gần nhất về trạng thái trước đó.
- **FR-22 — Tải dữ liệu từ file khi khởi động**: Nạp toàn bộ dữ liệu từ các file text phẳng trong thư mục `data` vào các cấu trúc dữ liệu tương ứng trong RAM khi ứng dụng khởi chạy.
- **FR-23 — Lưu dữ liệu sau thao tác quan trọng**: Tự động ghi đè dữ liệu mới nhất trong RAM xuống các file text tương ứng ngay sau khi thực hiện xong một thao tác làm thay đổi dữ liệu hệ thống.
- **FR-24 — Sinh mã tự động**: Tự động tạo mã định danh duy nhất tăng dần cho Khách hàng (`C001, C002...`), Xe (`V001, V002...`), Dịch vụ (`S001, S002...`) và Booking (`B001, B002...`).
- **FR-25 — Khởi tạo dữ liệu mẫu**: Tự động điền dữ liệu mẫu ban đầu vào các file lưu trữ khi phát hiện các file này trống hoặc chưa tồn tại.
- **FR-26 — Kiểm tra dữ liệu đầu vào (Validate)**: Thực hiện các kiểm tra tính hợp lệ cơ bản đối với thông tin nhập từ bàn phím trước khi xử lý nghiệp vụ.

### 4.2 Chi tiết Functional Requirement

#### FR-01 — Quản lý khách hàng
- **Mô tả**: Cho phép admin thêm khách hàng mới, xem toàn bộ danh sách khách hàng, tìm kiếm thông tin khách hàng, cập nhật thông tin khách hàng và xóa khách hàng chỉ khi không có dữ liệu liên kết.
- **Lý do cần có**: Mọi giao dịch đặt lịch và cơ chế tích điểm loyalty đều phải gắn với một thực thể khách hàng cụ thể.
- **Luồng xử lý chính**:
  1. Người dùng chọn chức năng quản lý khách hàng từ menu Admin.
  2. Để thêm mới: nhập tên và số điện thoại. Hệ thống tự động sinh ID khách hàng tăng dần (dạng `C001, C002...`), gán hạng mặc định là `MEMBER`, các chỉ số tích lũy bằng 0.
  3. Để tìm kiếm: nhập mã khách hàng, tên hoặc số điện thoại. Hệ thống duyệt tìm tuyến tính và hiển thị thông tin.
  4. Để cập nhật: nhập mã khách hàng cần sửa, hiển thị thông tin cũ, nhập tên mới và số điện thoại mới.
  5. Để xóa: nhập mã khách hàng cần xóa, hệ thống kiểm tra sự tồn tại và các liên kết dữ liệu. Nếu khách chưa có xe, chưa có booking và chưa có lịch sử rửa xe, hệ thống cho phép xóa và thực hiện cập nhật file.
- **Điều kiện/ràng buộc**: Tên khách hàng và số điện thoại không được để trống. Số điện thoại của khách hàng phải là duy nhất (không được trùng lặp). Không được xóa khách hàng nếu khách hàng đó đã có xe, booking hoặc lịch sử trong hệ thống.
- **Kết quả mong đợi**: Thao tác thay đổi dữ liệu khách hàng được ghi nhận thành công vào bộ nhớ và file `customers.txt`. Thông tin tìm kiếm hiển thị chính xác.

#### FR-02 — Quản lý xe của khách hàng
- **Mô tả**: Cho phép admin liên kết thông tin xe (biển số) với khách hàng, xem danh sách xe và tìm kiếm xe.
- **Lý do cần có**: Một khách hàng có thể sở hữu nhiều xe khác nhau. Khi tạo booking, hệ thống cần biết chính xác xe nào sẽ được rửa để phục vụ việc tiếp nhận xe tại tiệm.
- **Luồng xử lý chính**:
  1. Admin nhập thông tin xe bao gồm biển số và mã khách hàng sở hữu.
  2. Hệ thống kiểm tra mã khách hàng xem có tồn tại không.
  3. Nếu hợp lệ, sinh mã xe tự động (`V001, V002...`), lưu liên kết chủ sở hữu.
  4. Hỗ trợ xem danh sách xe của một khách hàng cụ thể.
- **Điều kiện/ràng buộc**: Biển số xe không được để trống và không được trùng lặp trong hệ thống. Chủ sở hữu xe phải là một khách hàng đã tồn tại.
- **Kết quả mong đợi**: Xe được thêm thành công và lưu vào file `vehicles.txt`.

#### FR-03 — Quản lý dịch vụ rửa xe
- **Mô tả**: Cho phép admin thêm, xem, tìm kiếm và sắp xếp danh sách dịch vụ rửa xe theo giá hoặc thời gian thực hiện.
- **Lý do cần có**: Mỗi lượt rửa xe cần chọn dịch vụ cụ thể để xác định giá tiền thanh toán và thời gian thi công dự kiến.
- **Luồng xử lý chính**:
  1. Admin nhập tên dịch vụ, giá tiền và thời gian rửa (phút).
  2. Hệ thống kiểm tra tính hợp lệ và tự động sinh mã dịch vụ (`S001, S002...`).
  3. Cho phép sắp xếp dịch vụ hiển thị tăng/giảm dần theo giá hoặc thời gian thi công.
- **Điều kiện/ràng buộc**: Tên dịch vụ không được trống, giá dịch vụ và thời gian rửa phải lớn hơn 0.
- **Kết quả mong đợi**: Dịch vụ được ghi nhận vào file `services.txt`. Sắp xếp hiển thị chính xác.

#### FR-04 — Thiết lập ngày và buổi hiện tại
- **Mô tả**: Cho phép admin thay đổi ngày (`currentDate`) và buổi (`currentPeriod`) mô phỏng của hệ thống.
- **Lý do cần có**: Giúp dễ dàng demo và chạy thử các kịch bản kiểm thử (như kích hoạt buổi, đặt lịch trước) mà không phụ thuộc vào thời gian thực tế của máy tính.
- **Luồng xử lý chính**:
  1. Admin chọn chức năng thiết lập thời gian mô phỏng.
  2. Nhập ngày (định dạng `YYYY-MM-DD`) và chọn buổi (`MORNING`, `AFTERNOON`, `EVENING`).
  3. Hệ thống kiểm tra tính hợp lệ của định dạng và cập nhật biến toàn cục trong RAM.
- **Điều kiện/ràng buộc**: Ngày nhập phải đúng định dạng ngày tháng hợp lệ. Buổi nhập phải thuộc danh sách buổi quy định.
- **Kết quả mong đợi**: Hệ thống chuyển sang mốc thời gian mô phỏng mới, ảnh hưởng trực tiếp đến nghiệp vụ kiểm tra thời gian đặt trước.

#### FR-05 — Tạo booking rửa xe
- **Mô tả**: Cho phép khách hàng hoặc admin tạo lịch đặt rửa xe cho một khách hàng, một xe, một dịch vụ, vào một ngày và buổi cụ thể.
- **Lý do cần có**: Là dữ liệu đầu vào cốt lõi để xếp hàng chờ và vận hành tiệm.
- **Luồng xử lý chính**:
  1. Nhập mã khách hàng, mã xe, mã dịch vụ, ngày muốn đặt và buổi muốn đặt.
  2. Hệ thống kiểm tra sự tồn tại của khách, xe, dịch vụ và kiểm tra xe có thuộc khách hàng đó không.
  3. Kiểm tra ràng buộc Booking window theo hạng của khách hàng. Admin không được phép override điều này.
  4. Xác định booking thuộc buổi hiện tại (đang diễn ra) hay buổi tương lai để kiểm tra sức chứa tương ứng. Admin không được phép override sức chứa.
  5. Nếu hợp lệ, sinh mã booking tự động (`B001, B002...`), gán trạng thái mặc định là `WAITING` và lưu trữ.
- **Điều kiện/ràng buộc**: Khách hàng, xe, dịch vụ phải tồn tại và liên kết đúng. Ngày đặt phải nằm trong booking window. Sức chứa của buổi phải còn chỗ (slot chính hoặc waitlist).
- **Kết quả mong đợi**: Booking được tạo thành công, ghi vào file `bookings.txt`.

#### FR-06 — Kiểm tra booking window theo hạng thành viên
- **Mô tả**: Hệ thống tự động kiểm tra số ngày đặt trước của khách hàng có vượt quá giới hạn cho phép của hạng thành viên của họ hay không.
- **Lý do cần có**: Đảm bảo đặc quyền đặt lịch sớm của khách hàng VIP (Platinum, Gold, Silver) và công bằng cho các khách hàng khác.
- **Luồng xử lý chính**:
  1. Khi nhận yêu cầu tạo booking, hệ thống tính khoảng cách số ngày: $D = \text{ngày đặt lịch} - \text{ngày hiện tại của hệ thống}$.
  2. Tra cứu hạng thành viên của khách hàng để lấy số ngày đặt trước tối đa ($W$).
  3. Nếu $D > W$, từ chối booking và thông báo giới hạn của hạng.
  4. Nếu $D \le W$, cho phép tiếp tục luồng tạo booking.
- **Điều kiện/ràng buộc**: Khoảng cách ngày phải lớn hơn hoặc bằng 0.
- **Kết quả mong đợi**: Ngăn chặn chính xác các lượt đặt trước quá xa so với đặc quyền hạng.

#### FR-07 — Xác định buổi rửa xe của booking
- **Mô tả**: Phân loại booking vào đúng buổi phục vụ (Sáng, Chiều hoặc Tối) dựa trên thông tin đăng ký của khách hàng.
- **Lý do cần có**: Mỗi buổi có giới hạn chỗ ngồi độc lập, việc phân loại giúp kiểm tra và kích hoạt chính xác theo từng buổi.
- **Luồng xử lý chính**:
  - Gắn thuộc tính buổi (`MORNING` / `AFTERNOON` / `EVENING`) vào đối tượng booking khi lưu trữ.
- **Điều kiện/ràng buộc**: Thuộc tính buổi phải nằm trong danh mục buổi quy định.
- **Kết quả mong đợi**: Hệ thống phân loại chính xác booking theo từng buổi phục vụ.

#### FR-08 — Xử lý booking trong buổi hiện tại
- **Mô tả**: Tiếp nhận và phân bổ các booking được đặt cho chính ngày và buổi hiện tại đang hoạt động.
- **Lý do cần có**: Xử lý cho các khách hàng đặt ngay trong buổi để đưa thẳng vào hàng chờ phục vụ thực tế của buổi đó.
- **Luồng xử lý chính**:
  1. Kiểm tra xem buổi hiện tại của ngày hiện tại đã được kích hoạt hay chưa.
  2. Nếu đã kích hoạt:
     - Nếu slot chính của buổi hiện tại còn chỗ trống: đưa booking vào cuối hàng chờ chính (FIFO Queue).
     - Nếu slot chính đã đầy nhưng hàng chờ phụ còn chỗ trống: đưa booking vào hàng chờ phụ (Priority Queue).
     - Nếu cả hai đều đầy: từ chối booking.
  3. Nếu buổi chưa kích hoạt: lưu booking vào danh sách chờ kích hoạt (như booking tương lai).
- **Điều kiện/ràng buộc**: Phải tuân thủ giới hạn sức chứa của buổi hiện tại.
- **Kết quả mong đợi**: Khách đặt ngay được xếp trực tiếp vào Queue chính hoặc Priority Queue của buổi đang hoạt động.

#### FR-09 — Xử lý booking cho buổi tương lai
- **Mô tả**: Lưu trữ và quản lý tạm thời các booking đặt trước cho các buổi trong tương lai.
- **Lý do cần có**: Khách hàng đặt trước chưa được đưa vào hàng chờ phục vụ thực tế ngay mà phải chờ đến thời điểm kích hoạt buổi để phân loại độ ưu tiên.
- **Luồng xử lý chính**:
  1. Xác định booking thuộc ngày/buổi tương lai.
  2. Kiểm tra tổng sức chứa của buổi tương lai đó (tổng slot chính + phụ).
  3. Nếu còn chỗ, ghi nhận booking vào danh sách booking tương lai và lưu file.
- **Điều kiện/ràng buộc**: Chỉ kiểm tra tổng sức chứa của buổi đó tại thời điểm tạo, chưa phân bổ vào hàng chờ chính/phụ cụ thể.
- **Kết quả mong đợi**: Booking tương lai được tiếp nhận và lưu trữ ổn định.

#### FR-10 — Kích hoạt buổi rửa xe
- **Mô tả**: Cho phép admin kích hoạt buổi hiện tại để sắp xếp các booking tương lai của buổi đó vào hàng chờ chính và hàng chờ phụ thực tế.
- **Lý do cần có**: Chuyển trạng thái vận hành của tiệm sang buổi mới và phân loại thứ tự ưu tiên của khách đặt lịch trước một cách công bằng trước khi nhân viên làm việc.
- **Luồng xử lý chính**:
  1. Admin chọn kích hoạt buổi hiện tại.
  2. Hệ thống lấy toàn bộ booking tương lai của buổi hiện tại.
  3. Sắp xếp các booking này theo thứ tự ưu tiên (Hạng thành viên $\rightarrow$ Thời gian đặt).
  4. Đổ các booking có thứ tự ưu tiên cao vào slot chính (MyQueue) cho đến khi đầy slot.
  5. Đổ các booking tiếp theo vào hàng chờ phụ (MyPriorityQueue) cho đến khi đầy hàng chờ phụ.
  6. Các booking thừa còn lại (nếu vượt quá sức chứa) được chuyển sang trạng thái hủy/từ chối.
  7. Ghi nhận trạng thái buổi đã kích hoạt vào file `periods.txt`.
- **Điều kiện/ràng buộc**: Chỉ kích hoạt buổi trùng khớp với ngày và buổi mô phỏng hiện tại. Buổi đó phải chưa từng được kích hoạt trước đây.
- **Kết quả mong đợi**: Hàng chờ chính và hàng chờ phụ được khởi tạo đầy đủ dữ liệu từ danh sách đặt trước.

#### FR-11 — Ngăn kích hoạt trùng buổi
- **Mô tả**: Kiểm tra và ngăn chặn admin kích hoạt lại một buổi đã được kích hoạt trước đó.
- **Lý do cần có**: Tránh việc lặp lại giải thuật sắp xếp làm xáo trộn hàng chờ đang chạy và làm trùng lặp các booking trong queue.
- **Luồng xử lý chính**:
  1. Khi nhận lệnh kích hoạt, tra cứu trong danh sách trạng thái buổi (tải từ `periods.txt`).
  2. Nếu buổi hiện tại đã có trạng thái `ACTIVATED`, thông báo từ chối kích hoạt.
  3. Nếu chưa, cho phép tiếp tục kích hoạt và cập nhật trạng thái thành `ACTIVATED`.
- **Điều kiện/ràng buộc**: Trạng thái phải được lưu xuống file để duy trì sau khi tắt app.
- **Kết quả mong đợi**: Không xảy ra tình trạng kích hoạt trùng lặp cho cùng một ngày và buổi.

#### FR-12 — Xem hàng chờ chính của buổi hiện tại
- **Mô tả**: Hiển thị danh sách các booking đang nằm trong slot chính (hàng chờ chính) của buổi đang diễn ra theo đúng thứ tự xếp hàng.
- **Lý do cần có**: Giúp nhân viên và admin nắm bắt được thứ tự phục vụ và các xe đang chờ tại tiệm.
- **Luồng xử lý chính**:
  - Truy xuất cấu trúc dữ liệu hàng chờ chính (MyQueue) của buổi hiện tại, duyệt qua các phần tử và hiển thị thông tin chi tiết.
- **Kết quả mong đợi**: Danh sách hiển thị rõ ràng, đúng thứ tự vào trước ra trước.

#### FR-13 — Xem hàng chờ phụ của buổi hiện tại
- **Mô tả**: Hiển thị danh sách các booking đang nằm trong hàng chờ phụ (Waitlist) của buổi đang diễn ra theo thứ tự ưu tiên.
- **Lý do cần có**: Giúp admin biết khách hàng nào có cơ hội được đẩy lên slot chính tiếp theo.
- **Luồng xử lý chính**:
  - Duyệt và hiển thị các booking trong cấu trúc hàng chờ phụ (MyPriorityQueue) của buổi hiện tại theo thứ tự ưu tiên từ cao xuống thấp (hạng thành viên và thời gian đặt).
- **Kết quả mong đợi**: Hiển thị đúng danh sách theo độ ưu tiên hạng thành viên và thời gian đặt.

#### FR-14 — Xem booking của khách hàng
- **Mô tả**: Cho phép khách hàng tra cứu toàn bộ danh sách booking của cá nhân họ (chỉ hiển thị booking chưa kết thúc bao gồm `WAITING` và `SERVING`).
- **Lý do cần có**: Giúp khách hàng tự quản lý lịch đặt xe của mình.
- **Luồng xử lý chính**:
  1. Khách hàng nhập mã số của mình.
  2. Hệ thống lọc trong danh sách booking và hiển thị các booking chưa kết thúc.
- **Kết quả mong đợi**: Hiển thị chính xác các booking WAITING và SERVING của riêng khách hàng đó.

#### FR-15 — Xử lý booking tiếp theo
- **Mô tả**: Lấy booking đứng đầu hàng chờ chính chuyển sang trạng thái đang rửa (`SERVING`).
- **Lý do cần có**: Bắt đầu thực hiện dịch vụ cho xe tiếp theo theo đúng thứ tự xếp hàng.
- **Luồng xử lý chính**:
  1. Kiểm tra xem có xe nào đang ở trạng thái `SERVING` tại tiệm hay chưa.
  2. Nếu có, từ chối xử lý xe mới (yêu cầu hoàn tất xe hiện tại trước).
  3. Nếu không, lấy phần tử đầu ra khỏi hàng chờ chính (MyQueue.dequeue).
  4. Chuyển trạng thái booking đó từ `WAITING` sang `SERVING`.
  5. Lưu thay đổi xuống file `bookings.txt`.
- **Điều kiện/ràng buộc**: Chỉ được phép có tối đa 1 booking ở trạng thái `SERVING` tại một thời điểm. Hàng chờ chính phải không trống.
- **Kết quả mong đợi**: Booking được chuyển trạng thái thành công, nhân viên bắt đầu rửa xe.

#### FR-16 — Xác nhận thanh toán booking
- **Mô tả**: Cho phép admin xác nhận khách hàng đã thanh toán hóa đơn cho booking đang ở trạng thái `SERVING`.
- **Lý do cần có**: Đảm bảo nguyên tắc thu tiền trước khi xác nhận hoàn tất dịch vụ và ghi nhận doanh thu.
- **Luồng xử lý chính**:
  1. Kiểm tra có booking nào đang ở trạng thái `SERVING` không.
  2. Admin chọn xác nhận thanh toán, nhập phương thức thanh toán (`CASH` hoặc `BANKING`).
  3. Hệ thống cập nhật `paymentStatus = PAID`.
  4. Lưu thay đổi xuống file `bookings.txt`.
- **Điều kiện/ràng buộc**: Chỉ thanh toán cho booking đang ở trạng thái `SERVING`.
- **Kết quả mong đợi**: Trạng thái thanh toán của booking chuyển thành `PAID`.

#### FR-17 — Hoàn tất booking
- **Mô tả**: Xác nhận dịch vụ rửa xe đã hoàn tất cho xe đang rửa, thực hiện tích điểm, cập nhật hạng thành viên và lưu lịch sử.
- **Lý do cần có**: Đóng luồng phục vụ của một booking, kích hoạt tích lũy loyalty cho khách hàng và giải phóng vị trí phục vụ của tiệm.
- **Luồng xử lý chính**:
  1. Kiểm tra có booking đang ở trạng thái `SERVING` và đã thanh toán (`paymentStatus = PAID`) hay chưa.
  2. Chuyển trạng thái booking sang `COMPLETED`.
  3. Gọi nghiệp vụ cập nhật loyalty của khách hàng (FR-19).
  4. Ghi nhận booking vào lịch sử rửa xe (`history.txt`).
  5. Đẩy booking vào Stack hoàn tác.
  6. Tính lại `usedMinutes` của buổi hiện tại bằng tổng thời gian dịch vụ của các booking `COMPLETED` trong cùng ngày và cùng buổi.
  7. Tính `remainingMinutes = periodTotalMinutes - usedMinutes`.
  8. Nếu Waitlist còn booking, hệ thống lấy booking có độ ưu tiên cao nhất trong Waitlist để kiểm tra.
  9. Nếu `remainingMinutes >= serviceDuration` của booking đó, hệ thống chuyển booking từ Waitlist vào cuối hàng chờ chính (Main Queue).
  10. Nếu không đủ thời gian, hệ thống giữ nguyên Waitlist và không xét tiếp các booking phía sau.
  11. Lưu toàn bộ dữ liệu thay đổi xuống file.
- **Điều kiện/ràng buộc**: Booking phải ở trạng thái `SERVING` và có `paymentStatus = PAID`.
- **Kết quả mong đợi**: Booking được hoàn tất thành công, các dữ liệu tích lũy được cập nhật chính xác.

#### FR-18 — Hủy booking
- **Mô tả**: Cho phép admin hoặc khách hàng hủy một booking chưa thực hiện (hoặc đang phục vụ đối với Admin).
- **Lý do cần có**: Giải phóng suất đặt lịch của khách hàng khi họ thay đổi kế hoạch, nhường chỗ cho khách hàng trong hàng chờ phụ.
- **Luồng xử lý chính**:
  1. Tìm booking cần hủy theo mã booking.
  2. Kiểm tra quyền và trạng thái booking có hợp lệ để hủy không.
  3. Nếu actor là Customer, chỉ cho phép hủy booking của chính mình khi booking đang ở trạng thái `WAITING`.
  4. Nếu actor là Admin/Nhân viên, cho phép hủy booking bất kỳ ở trạng thái `WAITING` hoặc `SERVING`.
  5. Chuyển trạng thái booking sang `CANCELLED`.
  6. Nếu booking bị hủy đang nằm trong Main Queue của buổi hiện tại, hệ thống loại booking khỏi Main Queue và kiểm tra Waitlist để bổ sung booking ưu tiên cao nhất nếu còn chỗ.
  7. Nếu booking bị hủy đang nằm trong Waitlist hoặc booking tương lai, hệ thống loại booking khỏi danh sách chờ tương ứng.
  8. Nếu booking bị hủy đang ở trạng thái `SERVING`, hệ thống giải phóng vị trí phục vụ hiện tại, không ghi history, không cộng loyalty và không tự động chuyển booking khác sang `SERVING`.
  9. Sau khi hủy `SERVING`, hệ thống có thể kiểm tra Waitlist theo quy tắc thời gian còn lại để kéo booking ưu tiên cao nhất lên Main Queue nếu đủ điều kiện.
  10. Lưu thay đổi xuống file `bookings.txt`.
- **Điều kiện/ràng buộc**: Không cho phép hủy booking đã ở trạng thái `COMPLETED`. Customer chỉ được hủy booking của chính mình khi đang `WAITING`. Admin được hủy bất kỳ booking `WAITING` hoặc `SERVING`.
- **Kết quả mong đợi**: Booking được hủy, giải phóng chỗ và bổ sung kịp thời khách hàng từ hàng chờ phụ lên slot chính.

#### FR-19 — Cập nhật loyalty sau khi hoàn tất booking
- **Mô tả**: Hệ thống tính toán lại `visitCount`, `totalSpent`, `loyaltyPoints` và `tier` của khách hàng dựa trên toàn bộ booking `COMPLETED` hiện có.
- **Lý do cần có**: Đảm bảo ghi nhận đóng góp chi tiêu của khách hàng để áp dụng các đặc quyền ưu tiên trong lần đặt lịch sau.
- **Luồng xử lý chính**:
  1. Xác định khách hàng cần tính lại loyalty.
  2. Lọc toàn bộ booking của khách hàng có trạng thái `COMPLETED`.
  3. Tính `visitCount` bằng số lượng booking `COMPLETED`.
  4. Tính `totalSpent` bằng tổng giá dịch vụ của các booking `COMPLETED`.
  5. Tính `loyaltyPoints = totalSpent / 1000`.
  6. Xét `tier` là hạng cao nhất khách hàng đạt được dựa trên `visitCount` hoặc `totalSpent`.
  7. Cập nhật thông tin khách hàng và ghi file `customers.txt`.
- **Kết quả mong đợi**: Dữ liệu hạng, điểm của khách hàng thay đổi chính xác.

#### FR-20 — Xem lịch sử rửa xe
- **Mô tả**: Hiển thị danh sách các booking đã hoàn tất phục vụ của tiệm.
- **Lý do cần có**: Phục vụ đối soát doanh thu và kiểm tra hoạt động của tiệm.
- **Luồng xử lý chính**:
  - Đọc và hiển thị dữ liệu từ danh sách lịch sử rửa xe (tải từ `history.txt`), hỗ trợ lọc theo mã khách hàng cụ thể.
- **Kết quả mong đợi**: Hiển thị đầy đủ thông tin các lượt rửa xe đã hoàn tất.

#### FR-21 — Hoàn tác booking hoàn tất gần nhất
- **Mô tả**: Đảo ngược toàn bộ tác động của booking vừa hoàn tất gần nhất về trạng thái trước đó.
- **Lý do cần có**: Giúp nhân viên sửa sai nhanh chóng nếu lỡ tay bấm nhầm hoàn tất cho một xe chưa xong hoặc nhầm xe.
- **Luồng xử lý chính**:
  1. Lấy booking hoàn tất gần nhất ra khỏi Stack hoàn tác.
  2. Chuyển trạng thái booking đó từ `COMPLETED` về `SERVING` (và giữ trạng thái thanh toán `PAID`).
  3. Lấy thông tin khách hàng liên quan, thực hiện trừ đi các chỉ số đã cộng (giảm `visitCount` đi 1, trừ `totalSpent` tương ứng giá dịch vụ, trừ điểm loyalty tương ứng).
  4. Xét lại hạng thành viên của khách hàng dựa trên các chỉ số sau khi giảm (có thể bị hạ hạng về hạng cũ).
  5. Xóa dòng lịch sử của booking này trong danh sách lịch sử.
  6. Lưu toàn bộ thay đổi xuống các file lưu trữ.
  7. Nếu thao tác Complete trước đó đã kéo một booking từ Waitlist lên Main Queue, hệ thống hoàn tác thao tác kéo này bằng cách đưa booking đó trở lại Waitlist.
  8. Waitlist được sắp xếp lại theo Priority Queue dựa trên hạng thành viên và thời gian tạo booking.
- **Điều kiện/ràng buộc**: Stack hoàn tác phải không được trống. Chỉ hoàn tác được booking hoàn tất gần nhất.
- **Kết quả mong đợi**: Hệ thống quay về trạng thái trước khi complete booking đó một cách nhất quán.

#### FR-22 — Tải dữ liệu từ file khi khởi động
- **Mô tả**: Nạp toàn bộ dữ liệu từ các file text phẳng trong thư mục `data` vào các cấu trúc dữ liệu tương ứng trong RAM khi ứng dụng khởi chạy.
- **Lý do cần có**: Đảm bảo tính toàn vẹn và duy trì dữ liệu qua các lần tắt/mở ứng dụng CLI.
- **Luồng xử lý chính**:
  1. Khi khởi chạy app, kiểm tra sự tồn tại của thư mục `data` và các file text.
  2. Đọc từng dòng của các file, cắt chuỗi bằng ký tự phân tách `|`.
  3. Khởi tạo đối tượng và đưa vào các cấu trúc dữ liệu trong RAM.
  4. Nếu các file chưa tồn tại hoặc rỗng, kích hoạt luồng khởi tạo dữ liệu mẫu (FR-25).
- **Kết quả mong đợi**: Ứng dụng khởi động thành công với đầy đủ dữ liệu cũ được nạp vào bộ nhớ.

#### FR-23 — Lưu dữ liệu sau thao tác quan trọng
- **Mô tả**: Tự động ghi đè dữ liệu mới nhất trong RAM xuống các file text tương ứng ngay sau khi thực hiện xong một thao tác làm thay đổi dữ liệu hệ thống.
- **Lý do cần có**: Ngăn ngừa mất dữ liệu khi ứng dụng CLI bị tắt đột ngột.
- **Luồng xử lý chính**:
  - Ghi đè toàn bộ danh sách đối tượng hiện tại trong RAM xuống file tương ứng dưới dạng các dòng text phân tách bằng ký tự `|`.
- **Các thao tác bắt buộc lưu file**: Thêm khách hàng, thêm xe, thêm dịch vụ, tạo booking, kích hoạt buổi, xử lý xe tiếp theo, xác nhận thanh toán, hoàn tất booking, hủy booking, cập nhật loyalty và hoàn tác booking.
- **Kết quả mong đợi**: File text luôn phản ánh trạng thái dữ liệu mới nhất của RAM.

#### FR-24 — Sinh mã tự động
- **Mô tả**: Tự động tạo mã định danh duy nhất tăng dần cho Khách hàng (`C001, C002...`), Xe (`V001, V002...`), Dịch vụ (`S001, S002...`) và Booking (`B001, B002...`).
- **Lý do cần có**: Tránh trùng lặp mã định danh và giảm thiểu việc nhập liệu thủ công rườm rà trên giao diện CLI.
- **Luồng xử lý chính**:
  1. Khi load file, hệ thống tìm ra ID lớn nhất hiện tại.
  2. Khi tạo mới thực thể, hệ thống tăng số thứ tự lên 1 và định dạng chuỗi tương ứng.
- **Kết quả mong đợi**: Mã định danh sinh ra luôn duy nhất và có thứ tự tăng dần chuẩn xác.

#### FR-25 — Khởi tạo dữ liệu mẫu
- **Mô tả**: Tự động điền dữ liệu mẫu ban đầu vào các file lưu trữ khi phát hiện các file này trống hoặc chưa tồn tại.
- **Lý do cần có**: Giúp ứng dụng sẵn sàng để demo các chức năng phức tạp ngay khi chạy lần đầu mà không bắt người dùng nhập liệu từ đầu.
- **Luồng xử lý chính**:
  1. Tạo thư mục `data` và các file nếu chưa có.
  2. Tạo 4 khách hàng mẫu đại diện cho 4 hạng thành viên (`MEMBER`, `SILVER`, `GOLD`, `PLATINUM`).
  3. Tạo ít nhất 4 xe tương ứng liên kết với 4 khách hàng trên.
  4. Tạo 3 dịch vụ mẫu (`Basic Wash`, `Premium Wash`, `Interior Cleaning`).
  5. Tạo một số booking mẫu đặt trước ở các buổi và các hạng thành viên khác nhau để chuẩn bị demo.
  6. Ghi toàn bộ dữ liệu mẫu này xuống file text tương ứng.
- **Kết quả mong đợi**: Các file text được điền đầy đủ thông tin demo chuẩn nghiệp vụ.

#### FR-26 — Kiểm tra dữ liệu đầu vào (Validate)
- **Mô tả**: Thực hiện các kiểm tra tính hợp lệ cơ bản đối với thông tin nhập từ bàn phím trước khi xử lý nghiệp vụ.
- **Lý do cần có**: Ngăn chặn dữ liệu lỗi, rỗng hoặc mâu thuẫn làm sai hỏng luồng trạng thái của hệ thống.
- **Luồng xử lý chính**:
  - Đối với Khách hàng: tên và số điện thoại không được rỗng, số điện thoại không trùng.
  - Đối với Xe: biển số không rỗng và không trùng, chủ xe phải tồn tại.
  - Đối với Dịch vụ: tên không rỗng, giá và thời gian phải $> 0$.
  - Đối với Booking: khách hàng, xe, dịch vụ phải tồn tại và xe phải thuộc khách đó; ngày đặt trong booking window; buổi hợp lệ; còn slot.
  - Đối với Thanh toán và Hoàn tất: chỉ thực hiện khi booking ở trạng thái `SERVING` và đã `PAID` (đối với hoàn tất).
- **Kết quả mong đợi**: Dữ liệu không hợp lệ sẽ bị hệ thống phát hiện, từ chối xử lý và hiển thị thông báo lỗi rõ ràng.

---

## 5. Chi tiết Use Case

### 5.1 Mẫu mô tả Use Case
Mỗi Use Case trong hệ thống được cấu trúc theo mẫu chuẩn sau:

```markdown
### UC-xx — Tên Use Case

**Actor chính:**  
**Mục tiêu:**  
**Tiền điều kiện:**  
**Hậu điều kiện:**  

**Luồng chính:**
1. ...
2. ...

**Luồng thay thế / ngoại lệ:**
- ...

**Quy tắc nghiệp vụ liên quan:**
- ...

**Cấu trúc dữ liệu liên quan:**
- ...
```

---

### 5.2 UC-01 — Xem danh sách dịch vụ

**Actor chính:** Customer  
**Mục tiêu:** Cho phép Customer xem danh sách các dịch vụ rửa xe hiện có trong hệ thống để biết tên dịch vụ, giá tiền và thời gian thực hiện trước khi tạo booking.  
**Tiền điều kiện:**  
- Hệ thống đã được khởi động.
- Danh sách dịch vụ đã được nạp từ file dữ liệu.
- Customer đang ở trong Customer Menu.  
**Hậu điều kiện:**  
- Danh sách dịch vụ được hiển thị cho Customer.
- Dữ liệu dịch vụ không bị thay đổi.

**Luồng chính:**
1. Customer chọn chức năng **Xem danh sách dịch vụ** trong Customer Menu.
2. Hệ thống lấy danh sách dịch vụ hiện có.
3. Hệ thống kiểm tra danh sách dịch vụ có dữ liệu hay không.
4. Nếu có dữ liệu, hệ thống hiển thị danh sách dịch vụ cho Customer. Mỗi dịch vụ hiển thị gồm: Mã dịch vụ, Tên dịch vụ, Giá tiền, Thời gian thực hiện.
5. Customer xem thông tin dịch vụ.
6. Hệ thống quay lại Customer Menu sau khi Customer nhấn Enter.

**Luồng thay thế / ngoại lệ:**
- **6.1 Danh sách dịch vụ rỗng**:
  1. Hệ thống phát hiện chưa có dịch vụ nào.
  2. Hệ thống hiển thị thông báo: "Hiện chưa có dịch vụ rửa xe nào trong hệ thống. Vui lòng liên hệ Admin/Nhân viên để được hỗ trợ."
  3. Hệ thống quay lại Customer Menu.
- **6.2 File dữ liệu dịch vụ không đọc được**:
  1. Hệ thống hiển thị thông báo lỗi file.
  2. Hệ thống quay lại Customer Menu.

**Quy tắc nghiệp vụ liên quan:**  
- Customer chỉ có quyền xem danh sách dịch vụ, không được thêm, sửa hoặc xóa.

**Cấu trúc dữ liệu liên quan:**  
- `List/ArrayList` dùng để lưu danh sách dịch vụ.

---

### 5.3 UC-02 — Xem thông tin cá nhân

**Actor chính:** Customer  
**Mục tiêu:** Cho phép Customer xem thông tin cá nhân của chính mình, bao gồm các thông tin định danh, điểm tích lũy, chi tiêu và hạng thành viên liên quan đến quyền lợi đặt lịch.  
**Tiền điều kiện:**  
- Hệ thống đã được khởi động.
- Customer đã vào Customer Menu bằng mã khách hàng hợp lệ.
- Hệ thống đã xác định được `currentCustomer`.
- Dữ liệu khách hàng đã được nạp từ file.  
**Hậu điều kiện:**  
- Thông tin cá nhân của Customer được hiển thị.
- Dữ liệu khách hàng không bị thay đổi.

**Luồng chính:**
1. Customer chọn chức năng **Xem thông tin cá nhân** trong Customer Menu.
2. Hệ thống lấy thông tin của `currentCustomer`.
3. Hệ thống hiển thị thông tin cá nhân của Customer gồm: Mã khách hàng, Họ tên, Số điện thoại, Hạng thành viên, Điểm tích lũy, Tổng chi tiêu, Số lần rửa xe và Booking window tương ứng.
4. Customer xem thông tin.
5. Hệ thống quay lại Customer Menu sau khi Customer nhấn Enter.

**Luồng thay thế / ngoại lệ:**
- **6.1 Không tìm thấy currentCustomer**:
  1. Hệ thống không tìm thấy `currentCustomer` trong danh sách.
  2. Hệ thống hiển thị thông báo: "Không tìm thấy thông tin khách hàng hiện tại. Vui lòng quay lại Main Menu và nhập lại mã khách hàng."
  3. Hệ thống quay lại Main Menu.

**Quy tắc nghiệp vụ liên quan:**  
- Customer chỉ được xem thông tin của chính mình.
- Booking window được xác định theo hạng thành viên (Quy tắc 3.4).

**Cấu trúc dữ liệu liên quan:**  
- `List/ArrayList` (hoặc `Map` tra cứu khách hàng nhanh theo mã khách hàng).

---

### 5.4 UC-03 — Tạo booking

**Actor chính:** Customer hoặc Admin/Nhân viên  
**Mục tiêu:** Tạo lịch rửa xe cho một khách hàng, một xe, một dịch vụ, vào một ngày và buổi cụ thể.  
**Tiền điều kiện:**  
- Hệ thống đã được khởi động, dữ liệu được nạp đầy đủ.
- Nếu actor là Customer, đã vào Customer Menu bằng mã hợp lệ (đã xác định `currentCustomer`).
- Nếu actor là Admin/Nhân viên, đang ở Admin Menu.  
**Hậu điều kiện:**  
- Booking được tạo thành công ở trạng thái `WAITING`, vị trí xếp chỗ được hiển thị, lưu vào bộ nhớ RAM và ghi xuống file `bookings.txt`.  

**Luồng chính:**
- **Biến thể 1: Customer tự tạo booking**:
  1. Customer chọn chức năng **Tạo booking** trong Customer Menu.
  2. Hệ thống kiểm tra `currentCustomer` có xe trong danh sách xe hay không. (Nếu chưa có xe, chuyển sang luồng ngoại lệ 6.2).
  3. Hệ thống hiển thị danh sách xe thuộc sở hữu của `currentCustomer` (gồm mã xe, biển số).
  4. Customer chọn một xe trong danh sách.
  5. Hệ thống hiển thị danh sách dịch vụ hiện có (kèm mã, tên, giá, thời gian thực hiện).
  6. Customer chọn một dịch vụ.
  7. Hệ thống sinh danh sách ngày hợp lệ dựa trên booking window tương ứng với hạng thành viên của `currentCustomer`.
  8. Hệ thống hiển thị danh sách ngày và các buổi (Sáng, Chiều, Tối) kèm trạng thái sức chứa tương ứng dưới dạng "số chỗ đã dùng / tổng sức chứa". (Nếu tất cả ngày/buổi đều đầy, chuyển sang luồng ngoại lệ 6.3).
  9. Customer chọn một ngày và buổi rửa xe còn chỗ. (Nếu chọn buổi đã đầy, chuyển sang luồng ngoại lệ 6.4).
  10. Hệ thống kiểm tra buổi được chọn đã được kích hoạt hay chưa.
  11. **Nếu buổi đã kích hoạt**:
      - Nếu slot chính của buổi còn chỗ trống: Hệ thống xếp booking vào cuối hàng chờ chính (FIFO Queue), thông báo: "Tạo booking thành công. Mã booking: [Mã]. Trạng thái: WAITING. Vị trí: Slot chính của buổi hiện tại."
      - Nếu slot chính đã đầy nhưng waitlist còn chỗ trống: Hệ thống đưa booking vào hàng chờ phụ (Priority Queue), thông báo: "Tạo booking thành công. Mã booking: [Mã]. Trạng thái: WAITING. Vị trí: Hàng chờ phụ. Lưu ý: Booking sẽ được đẩy lên slot chính nếu có chỗ trống phù hợp."
  12. **Nếu buổi chưa kích hoạt**:
      - Hệ thống kiểm tra tổng sức chứa của buổi. Nếu còn chỗ, lưu booking dưới dạng booking tương lai, thông báo: "Tạo booking thành công. Mã booking: [Mã]. Trạng thái: WAITING. Vị trí: Booking tương lai, chờ kích hoạt buổi."
  13. Hệ thống ghi đè dữ liệu mới xuống file `bookings.txt`.

- **Biến thể 2: Admin/Nhân viên tạo booking cho khách**:
  1. Admin chọn chức năng **Tạo booking cho khách** từ Admin Menu.
  2. Hệ thống yêu cầu Admin tìm kiếm khách hàng. Admin nhập mã khách hàng, tên hoặc số điện thoại.
  3. Hệ thống hiển thị danh sách khách hàng khớp. Admin chọn một khách hàng.
  4. Hệ thống kiểm tra khách hàng đã chọn có xe hay chưa. (Nếu chưa, hệ thống từ chối tạo booking và yêu cầu thêm xe cho khách hàng trước).
  5. Hệ thống hiển thị danh sách xe của khách hàng đã chọn. Admin chọn một xe.
  6. Hệ thống hiển thị danh sách dịch vụ. Admin chọn một dịch vụ.
  7. Hệ thống sinh danh sách ngày hợp lệ dựa trên booking window theo hạng thành viên của khách hàng được chọn. Admin không được phép override booking window và sức chứa của buổi.
  8. Hệ thống hiển thị danh sách ngày/buổi kèm sức chứa ("số chỗ đã dùng / tổng sức chứa").
  9. Admin chọn ngày và buổi còn chỗ.
  10. Hệ thống kiểm tra trạng thái kích hoạt của buổi để đưa vào slot chính, hàng chờ phụ hoặc booking tương lai (tương tự như luồng của Customer).
  11. Hệ thống lưu booking mới ở trạng thái `WAITING` và ghi file `bookings.txt`.

**Luồng thay thế / ngoại lệ:**
- **6.1 Customer nhập sai định danh**:
  1. Khi vào Customer Menu, nhập sai mã khách hàng $\rightarrow$ Hệ thống cho nhập lại tối đa 3 lần. Nếu sau 3 lần vẫn sai, quay lại Main Menu.
- **6.2 Khách hàng chưa có xe**:
  1. Hệ thống phát hiện khách hàng chưa có xe.
  2. Hệ thống thông báo: "Bạn chưa có xe nào trong hệ thống. Vui lòng liên hệ Admin/Nhân viên để thêm xe trước khi tạo booking."
  3. Hệ thống quay lại menu trước đó.
- **6.3 Tất cả ngày/buổi trong booking window đều đầy**:
  1. Hệ thống hiển thị danh sách ngày/buổi kèm trạng thái "Đã đầy".
  2. Hệ thống thông báo: "Tất cả ngày/buổi trong phạm vi đặt lịch của bạn đã đầy. Vui lòng quay lại sau hoặc liên hệ Admin/Nhân viên." và không cho tạo booking.
- **6.4 Chọn ngày/buổi đã đầy**:
  1. Customer chọn một buổi đã đầy.
  2. Hệ thống báo lỗi: "Buổi [Buổi] ngày [Ngày] đã đầy. Vui lòng chọn ngày khác hoặc buổi khác."
  3. Hệ thống quay lại bước chọn ngày/buổi, giữ nguyên xe và dịch vụ đã chọn.
- **6.5 Admin không tìm thấy khách hàng**:
  1. Admin nhập thông tin tìm kiếm khách hàng nhưng không có kết quả.
  2. Hệ thống báo lỗi và cho phép nhập lại hoặc hủy bỏ luồng.

**Quy tắc nghiệp vụ liên quan:**  
- Customer chỉ tạo booking cho chính mình và chọn xe của mình.
- Admin không được override booking window và sức chứa của buổi.
- Booking tạo thành công mặc định có trạng thái `WAITING`.
- Booking window áp dụng theo hạng thành viên (Quy tắc 3.4).

**Cấu trúc dữ liệu liên quan:**  
- `MyLinkedList` lưu danh sách khách hàng, xe, dịch vụ, booking.
- `MyQueue` (hàng chờ chính), `MyPriorityQueue` (waitlist / booking tương lai).
- `MyMap` (hoặc mảng tra cứu) lấy booking window.

---

### 5.5 UC-04 — Xem booking cá nhân

**Actor chính:** Customer  
**Mục tiêu:** Cho phép Customer xem các booking đang chờ hoặc đang được phục vụ của chính mình.  
**Tiền điều kiện:**  
- Customer đã vào Customer Menu thành công (đã xác định `currentCustomer`).
- Dữ liệu booking đã được nạp.  
**Hậu điều kiện:**  
- Danh sách booking chưa kết thúc của Customer được hiển thị. Dữ liệu không thay đổi.

**Luồng chính:**
1. Customer chọn chức năng **Xem booking của tôi** trong Customer Menu.
2. Hệ thống lọc danh sách booking theo mã khách hàng của `currentCustomer` và trạng thái `WAITING` hoặc `SERVING`.
3. Hệ thống hiển thị danh sách booking lọc được gồm: Mã booking, Biển số xe, Tên dịch vụ, Ngày rửa xe, Buổi rửa xe, Trạng thái booking và Vị trí xử lý hiện tại (Slot chính, hàng chờ phụ hoặc booking tương lai).
4. Customer xem danh sách.
5. Hệ thống quay lại Customer Menu sau khi Customer nhấn Enter.

**Luồng thay thế / ngoại lệ:**
- **6.1 Customer không có booking đang hoạt động**:
  1. Hệ thống không tìm thấy booking nào ở trạng thái `WAITING` hoặc `SERVING` của khách.
  2. Hệ thống hiển thị: "Bạn không có booking nào đang chờ hoặc đang được phục vụ."
  3. Hệ thống quay lại Customer Menu.

**Quy tắc nghiệp vụ liên quan:**  
- Chỉ hiển thị các booking chưa kết thúc (`WAITING` và `SERVING`).
- Không hiển thị booking `COMPLETED` (xem tại UC-06) hoặc `CANCELLED`.

**Cấu trúc dữ liệu liên quan:**  
- `MyLinkedList` để duyệt và lọc dữ liệu booking.

---

### 5.6 UC-05 — Hủy booking

**Actor chính:** Customer hoặc Admin/Nhân viên  
**Mục tiêu:** Hủy một booking hợp lệ và cập nhật lại trạng thái hàng chờ nếu việc hủy tạo ra slot trống trong slot chính của buổi hiện tại.  
**Tiền điều kiện:**  
- Hệ thống đã được khởi động.
- Dữ liệu booking đã được nạp.
- Nếu là Customer, đã xác định được `currentCustomer`.  
**Hậu điều kiện:**  
- Booking được chuyển sang trạng thái `CANCELLED` và lưu file.
- Nếu slot chính của buổi hiện tại bị trống, booking ưu tiên cao nhất trong Waitlist được đẩy lên slot chính.

**Luồng chính:**
1. Actor chọn chức năng hủy booking.
2. Hệ thống yêu cầu nhập mã booking cần hủy.
3. Actor nhập mã booking.
4. Hệ thống kiểm tra sự tồn tại của booking.
5. Hệ thống kiểm tra quyền của actor đối với booking:
   - Nếu actor là **Customer**: Hệ thống kiểm tra booking có thuộc về `currentCustomer` hay không. Nếu không, chuyển sang luồng ngoại lệ.
   - Nếu actor là **Admin/Nhân viên**: Cho phép hủy booking của bất kỳ khách hàng nào.
6. Hệ thống kiểm tra trạng thái booking:
   - Nếu actor là **Customer**: Chỉ cho phép hủy booking khi đang ở trạng thái `WAITING`.
   - Nếu actor là **Admin/Nhân viên**: Cho phép hủy booking ở trạng thái `WAITING` hoặc `SERVING`.
7. Hệ thống chuyển trạng thái booking sang `CANCELLED`.
8. Hệ thống kiểm tra xem booking bị hủy có nằm trong slot chính của buổi hiện tại hay không:
   - Nếu đúng và hàng chờ phụ (Waitlist Priority Queue) không rỗng, hệ thống lấy ra booking có độ ưu tiên cao nhất trong Waitlist (Max Heap poll) đưa vào cuối hàng chờ chính (FIFO Queue).
9. Hệ thống cập nhật trạng thái lưu trữ xuống file `bookings.txt`.
10. Hệ thống hiển thị thông báo hủy thành công (và thông báo xe từ waitlist được đẩy lên slot chính nếu có).

**Luồng thay thế / ngoại lệ:**
- **6.1 Không tìm thấy booking**:
  1. Hệ thống báo lỗi: "Mã booking không tồn tại." và quay lại menu.
- **6.2 Customer hủy booking của người khác**:
  1. Hệ thống báo lỗi: "Bạn không có quyền hủy booking này." và quay lại menu.
- **6.3 Hủy sai trạng thái quy định**:
  - Customer cố gắng hủy booking `SERVING` hoặc `COMPLETED` $\rightarrow$ Hệ thống báo lỗi: "Chỉ được phép hủy booking ở trạng thái WAITING."
  - Admin cố gắng hủy booking `COMPLETED` $\rightarrow$ Hệ thống báo lỗi: "Không thể hủy booking đã hoàn tất. Vui lòng sử dụng tính năng hoàn tác booking hoàn tất gần nhất nếu cần."

**Quy tắc nghiệp vụ liên quan:**  
- Booking `COMPLETED` không được hủy (Quy tắc 3.10).
- Quy tắc điều phối hàng chờ phụ lên slot chính (Quy tắc 3.8).

**Cấu trúc dữ liệu liên quan:**  
- `MyLinkedList` (danh sách booking), `MyQueue` (hàng chờ chính), `MyPriorityQueue` (waitlist).

---

### 5.7 UC-06 — Xem lịch sử rửa xe cá nhân

**Actor chính:** Customer  
**Mục tiêu:** Cho phép Customer xem lịch sử các booking đã hoàn tất của chính mình.  
**Tiền điều kiện:**  
- Customer đã vào Customer Menu (đã xác định `currentCustomer`).
- Dữ liệu booking/lịch sử đã được nạp.  
**Hậu điều kiện:**  
- Lịch sử rửa xe của Customer được hiển thị. Dữ liệu không thay đổi.

**Luồng chính:**
1. Customer chọn chức năng **Xem lịch sử rửa xe của tôi** trong Customer Menu.
2. Hệ thống lấy mã khách hàng của `currentCustomer`.
3. Hệ thống lọc danh sách booking hoặc lịch sử theo mã khách hàng hiện tại và trạng thái `COMPLETED`.
4. Hệ thống hiển thị danh sách các lần rửa xe đã hoàn tất gồm: Mã booking, Biển số xe, Tên dịch vụ, Ngày rửa xe, Buổi rửa xe, Giá dịch vụ và Trạng thái `COMPLETED`.
5. Customer xem lịch sử.
6. Hệ thống quay lại Customer Menu sau khi Customer nhấn Enter.

**Luồng thay thế / ngoại lệ:**
- **6.1 Customer chưa có lịch sử rửa xe**:
  1. Hệ thống không tìm thấy booking nào ở trạng thái `COMPLETED` của khách.
  2. Hệ thống hiển thị: "Bạn chưa có lịch sử rửa xe nào."
  3. Hệ thống quay lại Customer Menu.

**Quy tắc nghiệp vụ liên quan:**  
- Chỉ hiển thị các booking đã hoàn tất ở trạng thái `COMPLETED` (Quy tắc 3.12).

**Cấu trúc dữ liệu liên quan:**  
- `MyLinkedList` để lọc và duyệt lịch sử.

---

### 5.8 UC-07 — Quản lý khách hàng

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Cho phép Admin/Nhân viên quản lý dữ liệu khách hàng (xem danh sách, tìm kiếm, thêm mới, cập nhật thông tin và xóa khách hàng khi đủ điều kiện).  
**Tiền điều kiện:**  
- Admin đang ở trong Admin Menu.
- Dữ liệu khách hàng đã được nạp.  
**Hậu điều kiện:**  
- Dữ liệu khách hàng được thay đổi và cập nhật xuống file `customers.txt` (nếu có thêm, cập nhật hoặc xóa).

**Luồng chính:**
1. Admin chọn chức năng **Quản lý khách hàng** trong Admin Menu.
2. Hệ thống hiển thị menu quản lý khách hàng.
3. Admin chọn một trong các thao tác:
   - **Xem danh sách khách hàng**: Hệ thống hiển thị tất cả khách hàng (Mã, Tên, Số điện thoại, Hạng thành viên).
   - **Tìm kiếm khách hàng**: Admin nhập mã khách hàng, tên hoặc số điện thoại. Hệ thống hiển thị danh sách kết quả khớp.
   - **Thêm khách hàng mới**: Admin nhập Họ tên và Số điện thoại. Hệ thống sinh mã khách hàng mới dạng `CXXX`, gán hạng mặc định `Member`, các chỉ số tích lũy bằng 0, lưu và ghi file.
   - **Cập nhật thông tin khách hàng**: Admin nhập mã khách hàng cần sửa, hệ thống hiển thị thông tin cũ, Admin nhập họ tên và số điện thoại mới. Hệ thống cập nhật và ghi file.
   - **Xóa khách hàng**: Admin nhập mã khách hàng cần xóa. Hệ thống kiểm tra điều kiện xóa. Nếu hợp lệ, hệ thống yêu cầu Admin xác nhận. Khi xác nhận, hệ thống xóa khách hàng khỏi danh sách và ghi file.
4. Hệ thống thông báo kết quả và quay lại menu quản lý khách hàng.

**Luồng thay thế / ngoại lệ:**
- **6.1 Danh sách khách hàng rỗng**: Hệ thống thông báo: "Hiện chưa có khách hàng nào trong hệ thống."
- **6.2 Không tìm thấy khách hàng**: Hệ thống thông báo: "Không tìm thấy khách hàng phù hợp."
- **6.3 Số điện thoại bị trùng**: Khi thêm hoặc sửa, nếu số điện thoại đã tồn tại, hệ thống báo lỗi: "Số điện thoại đã tồn tại trong hệ thống. Vui lòng kiểm tra lại." và từ chối lưu.
- **6.4 Từ chối xóa khách hàng do có liên kết dữ liệu**:
  1. Admin yêu cầu xóa khách hàng.
  2. Hệ thống phát hiện khách hàng đã có xe, booking hoặc lịch sử rửa xe.
  3. Hệ thống từ chối xóa và hiển thị thông báo: "Không thể xóa khách hàng này vì khách hàng đã có dữ liệu liên kết. Vui lòng giữ hồ sơ khách hàng để đảm bảo dữ liệu booking và lịch sử không bị sai lệch."
  4. Hệ thống quay lại menu quản lý khách hàng.

**Quy tắc nghiệp vụ liên quan:**  
- Chỉ Admin có quyền quản lý khách hàng.
- Xóa khách hàng chỉ được phép khi khách chưa có xe, chưa có booking và chưa có lịch sử (Quy tắc 3.10 / UC-07 đặc biệt).

**Cấu trúc dữ liệu liên quan:**  
- `MyLinkedList` để lưu danh sách khách hàng, kiểm tra liên kết chéo trong danh sách xe, booking, lịch sử.

---

### 5.9 UC-08 — Quản lý xe

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Quản lý liên kết thông tin xe của khách hàng.  
**Tiền điều kiện:** Admin đang ở trong Admin Menu.  
**Hậu điều kiện:** Dữ liệu xe được thay đổi và cập nhật xuống file `vehicles.txt` (nếu thêm mới).  

*Chưa được phân rã chi tiết trong tài liệu nguồn.*

**Quy tắc nghiệp vụ liên quan:**  
- Biển số xe không được để trống và không được trùng lặp.
- Chủ sở hữu xe phải là một khách hàng đã tồn tại trong hệ thống.

**Cấu trúc dữ liệu liên quan:**  
- `MyLinkedList` để lưu trữ danh sách xe.

---

### 5.10 UC-09 — Quản lý dịch vụ

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Quản lý danh sách dịch vụ rửa xe của tiệm (thêm, xem, tìm kiếm và sắp xếp).  
**Tiền điều kiện:** Admin đang ở trong Admin Menu.  
**Hậu điều kiện:** Dữ liệu dịch vụ được cập nhật xuống file `services.txt` (nếu thêm mới).  

*Chưa được phân rã chi tiết trong tài liệu nguồn.*

**Quy tắc nghiệp vụ liên quan:**  
- Tên dịch vụ không được rỗng, giá dịch vụ và thời gian rửa phải lớn hơn 0.

**Cấu trúc dữ liệu liên quan:**  
- `MyLinkedList` để lưu trữ danh sách dịch vụ.
- Thuật toán sắp xếp chọn (`Selection Sort`) để sắp xếp dịch vụ hiển thị tăng/giảm dần theo giá hoặc thời gian.

---

### 5.11 UC-10 — Thiết lập ngày và buổi hiện tại

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Thiết lập ngày (`currentDate`) và buổi (`currentPeriod`) mô phỏng hiện tại cho toàn hệ thống.  
**Tiền điều kiện:** Admin đang ở trong Admin Menu.  
**Hậu điều kiện:** Ngày/buổi mô phỏng được cập nhật trong bộ nhớ và ghi vào `periods.txt`.  

*Chưa được phân rã chi tiết trong tài liệu nguồn.*

**Quy tắc nghiệp vụ liên quan:**  
- Ngày nhập phải đúng định dạng ngày tháng hợp lệ. Buổi nhập phải thuộc danh sách buổi quy định (`MORNING`, `AFTERNOON`, `EVENING`).

**Cấu trúc dữ liệu liên quan:**  
- Biến toàn cục trong bộ nhớ và ghi file text.

---

### 5.12 UC-11 — Kích hoạt buổi rửa xe

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Kích hoạt buổi rửa xe hiện tại để hệ thống sắp xếp các booking tương lai của buổi đó vào hàng chờ chính và hàng chờ phụ thực tế.  
**Tiền điều kiện:** Admin đang ở trong Admin Menu.  
**Hậu điều kiện:** Buổi chuyển sang trạng thái kích hoạt, các hàng chờ chính/phụ của buổi được phân bổ đầy đủ, cập nhật trạng thái vào file `periods.txt`.  

*Chưa được phân rã chi tiết trong tài liệu nguồn.*

**Quy tắc nghiệp vụ liên quan:**  
- Chỉ kích hoạt buổi trùng khớp với ngày và buổi hiện tại mô phỏng của hệ thống.
- Mỗi buổi chỉ được kích hoạt duy nhất một lần (ngăn kích hoạt trùng buổi).
- Sắp xếp booking tương lai theo hạng thành viên (Platinum > Gold > Silver > Member) và thời gian đặt sớm để đổ lần lượt vào slot chính, tiếp đến hàng chờ phụ. Số dư thừa còn lại bị hủy (vượt quá sức chứa).

**Cấu trúc dữ liệu liên quan:**  
- `MyQueue` (slot chính), `MyPriorityQueue` (hàng chờ phụ / booking tương lai), file `periods.txt`.

---

### 5.13 UC-12 — Xem hàng chờ và booking theo buổi

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Cho phép Admin/Nhân viên xem danh sách các booking trong một ngày/buổi cụ thể, bao gồm slot chính và hàng chờ phụ nếu buổi đó đã được kích hoạt, hoặc danh sách booking tương lai nếu buổi đó chưa được kích hoạt.  
**Tiền điều kiện:**  
- Admin/Nhân viên đang ở trong Admin Menu.
- Dữ liệu booking đã được nạp.  
**Hậu điều kiện:**  
- Danh sách hàng chờ và booking theo buổi được hiển thị. Dữ liệu không thay đổi.

**Luồng chính:**
1. Admin/Nhân viên chọn chức năng **Xem hàng chờ / booking theo buổi** trong Admin Menu.
2. Hệ thống yêu cầu Admin/Nhân viên chọn ngày và buổi cần xem (Sáng, Chiều, Tối).
3. Hệ thống kiểm tra buổi đó đã được kích hoạt hay chưa.
4. **Nếu buổi đã được kích hoạt**:
   - Hệ thống hiển thị danh sách các booking trong **Slot chính** (theo thứ tự Queue FIFO).
   - Hệ thống hiển thị danh sách các booking trong **Hàng chờ phụ** (theo thứ tự Priority Queue Heap).
5. **Nếu buổi chưa được kích hoạt**:
   - Hệ thống hiển thị danh sách các booking tương lai đã đặt trước của buổi đó (chuyển sang luồng phụ 6.1).
6. Mỗi booking hiển thị gồm: Mã booking, Họ tên khách, Biển số xe, Dịch vụ, Hạng thành viên, Trạng thái booking.
7. Hệ thống quay lại Admin Menu sau khi Admin nhấn Enter.

**Luồng thay thế / ngoại lệ:**
- **6.1 Buổi chưa được kích hoạt**:
  1. Hệ thống phát hiện buổi chưa kích hoạt.
  2. Hệ thống hiển thị thông báo: "Buổi này chưa được kích hoạt. Các booking dưới đây là booking tương lai, chưa được phân vào slot chính hoặc hàng chờ phụ."
  3. Hệ thống hiển thị danh sách booking tương lai của buổi đó (nếu có).
  4. Hệ thống quay lại Admin Menu.
- **6.2 Không có booking nào**:
  1. Hệ thống kiểm tra và phát hiện không có booking nào trong ngày/buổi được chọn.
  2. Hệ thống hiển thị: "Không có booking nào trong ngày/buổi được chọn."
  3. Hệ thống quay lại Admin Menu.

**Quy tắc nghiệp vụ liên quan:**  
- Phân biệt hiển thị giữa buổi đã kích hoạt (hiển thị slot chính và waitlist) và buổi chưa kích hoạt (chỉ gọi là booking tương lai, không dùng thuật ngữ slot chính hay waitlist).

**Cấu trúc dữ liệu liên quan:**  
- `MyQueue` (slot chính), `MyPriorityQueue` (waitlist / booking tương lai), `MyLinkedList` (dữ liệu booking).

---

### 5.14 UC-13 — Xử lý booking tiếp theo

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Lấy booking đứng đầu hàng chờ chính chuyển sang trạng thái đang rửa (`SERVING`).  
**Tiền điều kiện:** Admin đang ở trong Admin Menu.  
**Hậu điều kiện:** Booking đầu queue chuyển sang `SERVING`, cập nhật file `bookings.txt`.  

*Chưa được phân rã chi tiết trong tài liệu nguồn.*

**Quy tắc nghiệp vụ liên quan:**  
- Chỉ được phép có tối đa 1 booking ở trạng thái `SERVING` tại một thời điểm.
- Hàng chờ chính phải không trống.

**Cấu trúc dữ liệu liên quan:**  
- `MyQueue` (lấy ra ở đầu hàng chờ chính bằng thao tác `dequeue`), file `bookings.txt`.

---

### 5.15 UC-14 — Xác nhận thanh toán

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Xác nhận thanh toán hóa đơn cho booking đang ở trạng thái `SERVING`.  
**Tiền điều kiện:** Admin đang ở trong Admin Menu.  
**Hậu điều kiện:** Booking `SERVING` cập nhật trạng thái thanh toán `paymentStatus = PAID`, phương thức thanh toán cập nhật, ghi đè file `bookings.txt`.  

*Chưa được phân rã chi tiết trong tài liệu nguồn.*

**Quy tắc nghiệp vụ liên quan:**  
- Chỉ thanh toán cho booking đang ở trạng thái `SERVING`.
- Nhập phương thức thanh toán là `CASH` hoặc `BANKING`.

**Cấu trúc dữ liệu liên quan:**  
- `MyLinkedList` (duyệt tìm booking đang `SERVING`), file `bookings.txt`.

---

### 5.16 UC-15 — Hoàn tất booking

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Xác nhận dịch vụ rửa xe hoàn tất cho xe đang rửa, thực hiện tích lũy loyalty và kiểm tra kéo thêm xe từ waitlist lên slot chính.  
**Tiền điều kiện:** Admin đang ở trong Admin Menu.  
**Hậu điều kiện:** Booking chuyển sang `COMPLETED`, ghi lịch sử vào `history.txt`, cập nhật thông tin khách hàng, đẩy booking vào Stack hoàn tác, điều phối xe từ waitlist lên slot chính nếu còn đủ thời gian của buổi.  

*Chưa được phân rã chi tiết trong tài liệu nguồn.*

**Quy tắc nghiệp vụ liên quan:**  
- Chỉ thực hiện khi booking ở trạng thái `SERVING` và đã thanh toán (`PAID`).
- Cập nhật loyalty cho khách hàng (Quy tắc 3.11).
- Kiểm tra thời gian còn lại của buổi hiện tại so với thời gian dịch vụ của xe đầu waitlist để quyết định kéo lên slot chính (Quy tắc 3.9).

**Cấu trúc dữ liệu liên quan:**  
- `MyQueue` (slot chính), `MyPriorityQueue` (waitlist), `MyStack` (hoàn tác), file `bookings.txt`, `customers.txt`, `history.txt`.

---

### 5.17 UC-16 — Xem lịch sử rửa xe

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Xem toàn bộ danh sách các booking đã hoàn tất phục vụ của tiệm.  
**Tiền điều kiện:** Admin đang ở trong Admin Menu.  
**Hậu điều kiện:** Danh sách hiển thị cho Admin. Dữ liệu không thay đổi.  

*Chưa được phân rã chi tiết trong tài liệu nguồn.*

**Quy tắc nghiệp vụ liên quan:**  
- Chỉ hiển thị các booking đã hoàn tất (`COMPLETED`).

**Cấu trúc dữ liệu liên quan:**  
- `MyLinkedList` lưu danh sách lịch sử, nạp từ `history.txt`.

---

### 5.18 UC-17 — Hoàn tác booking hoàn tất gần nhất

**Actor chính:** Admin/Nhân viên  
**Mục tiêu:** Đảo ngược thao tác hoàn tất của booking hoàn tất gần nhất, đưa về trạng thái `SERVING` và hoàn trả các chỉ số loyalty của khách hàng.  
**Tiền điều kiện:** Admin đang ở trong Admin Menu. Stack hoàn tác phải không trống.  
**Hậu điều kiện:** Booking quay lại `SERVING`, điểm và hạng của khách hàng bị trừ tương ứng, dòng lịch sử bị xóa, cập nhật các file lưu trữ.  

*Chưa được phân rã chi tiết trong tài liệu nguồn.*

**Quy tắc nghiệp vụ liên quan:**  
- Chỉ cho phép hoàn tác booking hoàn tất gần nhất (sử dụng Stack LIFO).
- Khấu trừ các chỉ số chi tiêu, điểm loyalty, visit count và xét hạ hạng khách hàng nếu không còn đủ điều kiện hạng mới (Quy tắc 3.13).

**Cấu trúc dữ liệu liên quan:**  
- `MyStack` (lấy phần tử trên cùng bằng thao tác `pop`), file `bookings.txt`, `customers.txt`, `history.txt`.

---

## 6. Yêu cầu dữ liệu

Hệ thống quản lý dữ liệu hoàn toàn trong bộ nhớ RAM khi chạy và lưu trữ lâu dài dưới dạng các file văn bản phẳng (`.txt`) phân tách các trường bằng dấu đứng `|`.

### 6.1 Dữ liệu khách hàng
- Các trường thông tin cần lưu:
  - Mã khách hàng (Mã định danh duy nhất tự sinh, ví dụ: `C001`, `C002`...)
  - Họ và tên khách hàng
  - Số điện thoại (Duy nhất, không trùng)
  - Hạng thành viên hiện tại (`MEMBER`, `SILVER`, `GOLD`, `PLATINUM`)
  - Điểm tích lũy loyalty (Loyalty Points)
  - Tổng chi tiêu tích lũy (Total Spent)
  - Số lần rửa xe (Visit Count)
- Nơi lưu trữ: file `data/customers.txt`.

### 6.2 Dữ liệu xe
- Các trường thông tin cần lưu:
  - Mã xe (Mã định danh duy nhất tự sinh, ví dụ: `V001`, `V002`...)
  - Biển số xe (Duy nhất, không trùng)
  - Mã khách hàng sở hữu (Khóa ngoại liên kết tới Khách hàng)
- Nơi lưu trữ: file `data/vehicles.txt`.

### 6.3 Dữ liệu dịch vụ
- Các trường thông tin cần lưu:
  - Mã dịch vụ (Mã định danh duy nhất tự sinh, ví dụ: `S001`, `S002`...)
  - Tên dịch vụ rửa xe
  - Giá tiền dịch vụ (VND)
  - Thời gian thực hiện (Phút)
  - Trạng thái hoạt động (`ACTIVE`, `INACTIVE`)
- Nơi lưu trữ: file `data/services.txt`.

### 6.4 Dữ liệu booking
- Các trường thông tin cần lưu:
  - Mã booking (Mã định danh duy nhất tự sinh, ví dụ: `B001`, `B002`...)
  - Mã khách hàng
  - Mã xe
  - Mã dịch vụ
  - Ngày đặt lịch (`YYYY-MM-DD`)
  - Buổi đặt lịch (`MORNING`, `AFTERNOON`, `EVENING`)
  - Trạng thái booking (`WAITING`, `SERVING`, `COMPLETED`, `CANCELLED`)
  - Trạng thái thanh toán (`UNPAID`, `PAID`)
  - Phương thức thanh toán (`NONE`, `CASH`, `BANKING`)
  - Thời điểm tạo booking (Dùng để so sánh thứ tự ưu tiên khi cùng hạng thành viên)
- Nơi lưu trữ: file `data/bookings.txt`.

### 6.5 Dữ liệu lịch sử
- Các trường thông tin cần lưu:
  - Mã booking (Liên kết booking đã hoàn tất)
  - Mã khách hàng, Tên khách hàng, Biển số xe, Tên dịch vụ
  - Thời điểm hoàn tất dịch vụ
  - Số tiền đã thanh toán
  - Số điểm loyalty được cộng thêm
- Nơi lưu trữ: file `data/history.txt`.

### 6.6 Dữ liệu ngày/buổi hiện tại
- Các trường thông tin cần lưu:
  - Ngày phục vụ mô phỏng
  - Buổi phục vụ mô phỏng
  - Trạng thái kích hoạt của buổi phục vụ (`ACTIVATED`, `NOT_ACTIVATED`)
- Nơi lưu trữ: file `data/periods.txt`.

### 6.7 Dữ liệu hàng chờ
- Dữ liệu này chỉ tồn tại tạm thời trong bộ nhớ RAM khi hệ thống đang vận hành:
  - Danh sách booking trong slot chính của buổi hiện tại (FIFO Queue).
  - Danh sách booking trong Waitlist của buổi hiện tại (Priority Queue Max Heap).
  - Danh sách booking tương lai chờ kích hoạt.

---

## 7. Cấu trúc dữ liệu và thuật toán liên quan

Để phục vụ mục tiêu học tập và minh họa của môn học Cấu trúc dữ liệu và giải thuật (CSD201), toàn bộ các cấu trúc dữ liệu chính trong bộ nhớ RAM được tự cài đặt bằng code thuần.

### 7.1 List/ArrayList
- **Cài đặt**: `MyLinkedList` tự định nghĩa sử dụng các liên kết Node đơn.
- **Vai trò**: Lưu trữ danh sách Khách hàng, danh sách Xe, danh sách Dịch vụ và danh sách Lịch sử rửa xe. Hỗ trợ thao tác duyệt tuyến tính từ Node đầu đến Node cuối để hiển thị thông tin hoặc tìm kiếm.

### 7.2 Queue
- **Cài đặt**: `MyQueue` tự định nghĩa sử dụng LinkedList hoặc mảng vòng.
- **Vai trò**: Quản lý hàng chờ chính (Main slot) của buổi phục vụ đang diễn ra. Đảm bảo đúng nguyên lý **FIFO (First In First Out)**.
- **Thao tác chính**: `enqueue` (thêm vào cuối hàng chờ chính), `dequeue` (lấy ra ở đầu hàng chờ chính).

### 7.3 Priority Queue
- **Cài đặt**: `MyPriorityQueue` tự định nghĩa sử dụng cấu trúc **Max Heap (cây nhị phân ưu tiên lớn nhất)**.
- **Vai trò**: Quản lý hàng chờ phụ (Waitlist) của buổi phục vụ hiện tại và sắp xếp booking tương lai khi kích hoạt buổi.
- **Tiêu chí so sánh ưu tiên (Comparator)**:
  1. Hạng thành viên cao hơn được ưu tiên cao hơn (`Platinum` > `Gold` > `Silver` > `Member`).
  2. Nếu cùng hạng thành viên, booking nào được tạo trước (thời gian tạo sớm hơn) được ưu tiên cao hơn.
- **Thao tác chính**: `insert` (thêm vào heap và gọi `heapify up`), `poll` / `remove root` (lấy ra booking có độ ưu tiên cao nhất ở gốc heap và gọi `heapify down`), `peek` (xem booking ưu tiên nhất ở gốc heap).

### 7.4 Stack
- **Cài đặt**: `MyStack` tự định nghĩa sử dụng LinkedList hoặc Array.
- **Vai trò**: Lưu trữ các booking đã được hoàn tất phục vụ (`COMPLETED`) để phục vụ cho tính năng hoàn tác (Undo) theo nguyên lý **LIFO (Last In First Out)**.
- **Thao tác chính**: `push` (đẩy booking vào Stack), `pop` (lấy ra booking hoàn tất gần nhất khỏi Stack).

### 7.5 Map
- **Cài đặt**: `MyMap` tự cài đặt đơn giản hoặc mảng cấu trúc tra cứu.
- **Vai trò**: Lưu trữ cấu hình Booking window theo từng hạng thành viên giúp tra cứu nhanh với độ phức tạp $O(1)$.

### 7.6 File storage
- **Cài đặt**: Sử dụng lớp đọc/ghi file thuần của Java (`BufferedReader`, `BufferedWriter`, `FileWriter`).
- **Vai trò**: Load dữ liệu từ file văn bản phẳng khi khởi động ứng dụng và ghi đè dữ liệu RAM xuống file ngay sau các thao tác thay đổi trạng thái nghiệp vụ quan trọng. Các trường dữ liệu ngăn cách bằng ký tự `|`.

### 7.7 Tìm kiếm và sắp xếp cơ bản
- **Tìm kiếm**: Thuật toán tìm kiếm tuyến tính (**Linear Search**) duyệt qua các node của LinkedList để tìm kiếm khách hàng theo mã/số điện thoại, xe theo biển số, booking theo mã booking.
- **Sắp xếp**: Thuật toán sắp xếp chọn (**Selection Sort**) hoặc sắp xếp chèn (**Insertion Sort**) tự viết để sắp xếp danh sách dịch vụ rửa xe hiển thị theo giá tiền hoặc thời gian thực hiện tăng/giảm dần.

---

## 8. Yêu cầu phi chức năng

### 8.1 Tính đúng đắn
Hệ thống phải đảm bảo vận hành chính xác các quy tắc nghiệp vụ điều phối hàng chờ, quy tắc booking window, tính toán loyalty (visit count, total spent, points) và nâng hạng thành viên. Các cấu trúc dữ liệu tự cài đặt phải đảm bảo tính logic và không xảy ra lỗi tràn bộ nhớ khi chạy thử.

### 8.2 Tính dễ sử dụng trong CLI
- Giao diện CLI chạy bằng menu phân cấp trực quan, dễ thao tác.
- Hệ thống hỗ trợ tính năng khởi tạo dữ liệu mẫu (seed data) tự động giúp sẵn sàng demo ngay lập tức mà không cần nhập liệu thủ công từ đầu.
- Hiển thị rõ ràng trạng thái xếp chỗ của booking sau khi tạo thành công (vào slot chính, waitlist hay booking tương lai) và hiển thị trực quan sức chứa của các buổi dưới dạng phân số (đã dùng / tổng).

### 8.3 Tính toàn vẹn dữ liệu
- Dữ liệu phải được tự động ghi lại xuống các file text ngay sau khi kết thúc mỗi thao tác quan trọng để tránh mất mát khi tắt ứng dụng đột ngột.
- Ràng buộc khóa ngoại và tính duy nhất của số điện thoại, biển số xe, mã thực thể phải được kiểm tra chặt chẽ.
- Hệ thống phải từ chối xóa khách hàng nếu khách hàng đó đã phát sinh bất kỳ dữ liệu liên kết nào (xe, booking, lịch sử) để bảo vệ toàn vẹn lịch sử giao dịch.

### 8.4 Khả năng bảo trì
Cấu trúc code và các lớp tự cài đặt cấu trúc dữ liệu phải được tổ chức tách biệt, mạch lạc, có chú thích đầy đủ để sinh viên dễ dàng đọc hiểu, giải thích bản chất giải thuật và bảo trì.

### 8.5 Giới hạn phạm vi
Hệ thống khống chế độ phức tạp vừa phải, không tích hợp các thư viện UI phức tạp hoặc hệ quản trị cơ sở dữ liệu (DBMS), đảm bảo dự án tập trung vào mục tiêu cấu trúc dữ liệu và giải thuật của môn học CSD201.

---

## 9. Giao diện hệ thống

Hệ thống được thiết kế dưới dạng cây menu CLI phân cấp trực quan.

### 9.1 Main Menu
Khi ứng dụng khởi chạy, Main Menu hiển thị các nhánh chính để phân chia vai trò thao tác:

```text
Main Menu
1. Customer Menu
2. Admin Menu
0. Exit
```

### 9.2 Customer Menu
Khi chọn **Customer Menu**, hệ thống yêu cầu nhập mã khách hàng:

```text
Nhập mã khách hàng: C001
```

- Nếu mã khách hàng không tồn tại trong hệ thống: Hệ thống hiển thị thông báo lỗi và cho phép nhập lại tối đa 3 lần. Nếu sau 3 lần vẫn nhập sai, hệ thống tự động quay lại Main Menu.
- Nếu mã khách hàng hợp lệ: Hệ thống xác định `currentCustomer = C001`, in lời chào: `Xin chào [Tên khách hàng]` và hiển thị Customer Menu:

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

*Lưu ý*: Các chức năng từ 1 đến 6 chỉ được phép thao tác và truy xuất dữ liệu thuộc về chính `currentCustomer`. Khi chọn `0. Quay lại`, hệ thống xóa trạng thái `currentCustomer` và quay lại Main Menu.

### 9.3 Admin Menu
Khi chọn **Admin Menu**, hệ thống cho phép truy cập trực tiếp vào các chức năng quản lý và vận hành tiệm mà không cần mật khẩu:

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

Nhánh **1. Quản lý khách hàng** sẽ dẫn vào Menu con:

```text
Quản lý khách hàng
1. Xem danh sách khách hàng
2. Tìm kiếm khách hàng
3. Thêm khách hàng mới
4. Cập nhật thông tin khách hàng
5. Xóa khách hàng
0. Quay lại Admin Menu
```

### 9.4 Nguyên tắc hiển thị thông báo
- **Thông báo thành công**: Hiển thị rõ ràng mã định danh và trạng thái mới của thực thể (ví dụ: booking mới tạo hiển thị mã booking, trạng thái WAITING và vị trí cụ thể trong hàng chờ chính, phụ hoặc tương lai).
- **Thông báo lỗi nghiệp vụ**: Hiển thị rõ nguyên nhân lỗi (ví dụ: "Số điện thoại đã tồn tại", "Buổi chiều ngày 2026-06-26 đã đầy", "Không thể xóa khách hàng do có dữ liệu liên kết").
- **Hỗ trợ nhập liệu CLI**: Khi xảy ra lỗi nhập liệu (như chọn buổi đã đầy ở bước đặt lịch), hệ thống báo lỗi và cho phép chọn lại ngày/buổi tại chỗ, giữ nguyên các thông tin xe và dịch vụ đã nhập trước đó để tối ưu trải nghiệm người dùng CLI.

---

## 10. Ràng buộc và giới hạn

### 10.1 Không có đăng nhập thật
Hệ thống không có chức năng đăng ký tài khoản bảo mật hay nhập mật khẩu. Việc phân chia Customer/Admin chỉ nhằm mục đích tổ chức menu CLI và mô phỏng nghiệp vụ. Customer được xác thực tượng trưng bằng việc nhập mã khách hàng.

### 10.2 Không có phân quyền bảo mật thật
Không có cơ chế mã hóa hay ngăn chặn truy cập trái phép ở mức tệp tin hoặc mã nguồn. Ràng buộc bảo mật chỉ được thực thi ở mức logic hiển thị menu CLI.

### 10.3 Không có audit log
Hệ thống không ghi nhận nhật ký hoạt động chi tiết (như ghi nhận tài khoản nào đã thực hiện thay đổi dữ liệu nào vào lúc nào) nhằm giữ mã nguồn tập trung vào nghiệp vụ cốt lõi và các giải thuật.

### 10.4 Không có cơ sở dữ liệu
Hệ thống hoàn toàn không kết nối với bất kỳ hệ quản trị cơ sở dữ liệu (DBMS) nào. Mọi hoạt động lưu trữ lâu dài phụ thuộc hoàn toàn vào 6 tệp văn bản phẳng trong thư mục `data/`.

### 10.5 Không có giao diện Web/Mobile
Ứng dụng được thiết kế hoàn toàn chạy trong môi trường Terminal dòng lệnh. Không hỗ trợ giao diện đồ họa Web, Desktop hay Mobile.

### 10.6 Không tự thêm chức năng ngoài phạm vi
Hệ thống không tự ý cài đặt thêm các tính năng thanh toán thực tế, nhận diện biển số xe tự động (LPR), gửi tin nhắn SMS/Email tự động, hay các cơ chế khuyến mãi phức tạp ngoài đặc tả.

---

## 11. Vấn đề cần xác nhận

Không phát hiện mâu thuẫn trực tiếp nào giữa `SRSV2.md` và `actor_usecase_summary_car_wash_cli.md`. Các quyết định và hành vi được mô tả trong tài liệu actor đóng vai trò làm rõ và cụ thể hóa quyền hạn theo vai trò, cấu trúc Main Menu, luồng menu CLI, và các ràng buộc nghiệp vụ xóa khách hàng. 

Một số điểm cần lưu ý để thống nhất khi triển khai code:
1. **Admin thêm khách hàng hoặc xe mới trực tiếp trong luồng tạo booking**: Tài liệu nguồn chưa mô tả chi tiết luồng con này cho Admin. Vì vậy, hệ thống mặc định yêu cầu Admin phải thêm Khách hàng (FR-01) hoặc thêm Xe (FR-02) thành công trước khi tiến hành tạo booking cho khách hàng đó.
2. **Hạng thành viên của khách hàng trong Admin Menu**: Để đảm bảo tính nhất quán của cơ chế loyalty, Admin Menu chỉ cho phép quản lý thông tin hồ sơ cơ bản (Họ tên, Số điện thoại) của khách hàng và không cho phép Admin thay đổi thủ công điểm tích lũy hoặc hạng thành viên của khách hàng. Điểm tích lũy và hạng thành viên phải được cập nhật hoàn toàn tự động bởi hệ thống sau khi hoàn tất booking hoặc hoàn tác booking.

---

## 12. Phụ lục

### Bảng tóm tắt quyết định đã chốt

| Danh mục | Quyết định đã chốt |
|---|---|
| **Kiểu ứng dụng** | CLI (Command Line Interface) chạy bằng menu trong terminal. |
| **Lưu trữ lâu dài** | Sử dụng file text phẳng (`.txt`) trong thư mục `data/`, ngăn cách bằng ký tự `|`. |
| **Xử lý RAM** | Nạp dữ liệu vào RAM khi khởi động, xử lý bằng các cấu trúc dữ liệu tự cài đặt. |
| **Thời gian hệ thống** | Admin tự thiết lập ngày (`currentDate`) và buổi (`currentPeriod`) mô phỏng. |
| **Quy tắc kích hoạt** | Kích hoạt buổi hiện tại duy nhất 1 lần. Trạng thái lưu vào `periods.txt` để chống kích hoạt lại. |
| **Ràng buộc phục vụ** | Tại một thời điểm, chỉ cho phép tối đa 1 booking ở trạng thái `SERVING`. |
| **Thanh toán** | Mô phỏng xác nhận thanh toán (`paymentStatus = PAID`) trước khi cho phép bấm hoàn tất. |
| **Hoàn tác (Undo)** | Chỉ hỗ trợ hoàn tác duy nhất 1 booking hoàn tất gần nhất bằng cấu trúc Stack. |
| **Loyalty** | Tích lũy 1 điểm cho mỗi 1,000 VND. Tự động xét nâng hạng (Silver, Gold, Platinum). |
| **Sinh mã tự động** | Tự sinh ID tăng dần (`C001`, `V001`, `S001`, `B001`...) dựa trên dữ liệu hiện tại khi load. |
| **Khởi tạo dữ liệu mẫu**| Tự động tạo seed data mẫu khi file lưu trữ chưa tồn tại hoặc rỗng. |

### Bảng tổng hợp cấu trúc dữ liệu đề xuất

| Thực thể / Khu vực chức năng | Cấu trúc dữ liệu đề xuất | Lý do và vai trò |
|---|---|---|
| **Khách hàng (Customer)** | `MyLinkedList` | Lưu trữ danh sách động, duyệt tuyến tính để tìm kiếm và cập nhật loyalty. |
| **Xe (Vehicle)** | `MyLinkedList` | Lưu trữ danh sách xe, duyệt tuyến tính để kiểm tra tính sở hữu khi đặt lịch. |
| **Dịch vụ (Service)** | `MyLinkedList` | Lưu trữ danh sách dịch vụ, hỗ trợ sắp xếp hiển thị bằng Selection Sort. |
| **Buổi phục vụ** | Mảng (`Array`) cố định | Số lượng buổi cố định trong ngày gồm Sáng, Chiều, Tối. |
| **Hàng chờ chính (Main slot)** | `MyQueue` | Quản lý thứ tự phục vụ trong buổi hiện tại theo nguyên lý FIFO. |
| **Hàng chờ phụ (Waitlist)** | `MyPriorityQueue` | Quản lý booking chờ phụ, sắp xếp độ ưu tiên bằng Max Heap (Hạng + Thời gian tạo). |
| **Booking tương lai** | `MyPriorityQueue` | Lưu trữ các booking đặt trước, sẵn sàng sắp xếp ưu tiên khi kích hoạt buổi. |
| **Lịch sử rửa xe (History)** | `MyLinkedList` | Lưu trữ toàn bộ các booking đã hoàn tất để hiển thị báo cáo đối soát. |
| **Hoàn tác (Undo)** | `MyStack` | Quản lý booking hoàn tất gần nhất phục vụ thao tác Undo theo nguyên lý LIFO. |
| **Booking window theo hạng** | `MyMap` hoặc Mảng tra cứu | Tra cứu nhanh giới hạn ngày đặt trước tương ứng với từng hạng thành viên. |

### Các điểm sẽ làm rõ ở bước thiết kế kỹ thuật
Các nội dung dưới đây nằm ngoài phạm vi tài liệu đặc tả yêu cầu này và sẽ được quyết định ở bước thiết kế chi tiết:
1. Thiết kế chi tiết các Class Java và sơ đồ lớp (Class Diagram).
2. Các thuộc tính chi tiết và kiểu dữ liệu cụ thể của từng trường trong code.
3. Cách phân chia Package và cấu trúc thư mục source code Java.
4. Cách thức cài đặt chi tiết các cấu trúc dữ liệu tự định nghĩa (`MyLinkedList`, `MyQueue`, `MyPriorityQueue`, `MyStack`).
5. Các đoạn code Java cụ thể thực thi giải thuật Max Heap, Selection Sort và Linear Search.
