# Software Requirements Specification
# Car Wash Booking Queue CLI

## 1. Giới thiệu

### 1.1 Mục đích tài liệu
Tài liệu này mô tả yêu cầu phần mềm cho dự án **Car Wash Booking Queue CLI**.
Dự án mô phỏng hoạt động đặt lịch và xếp hàng rửa xe trong một tiệm rửa xe. Hệ thống tập trung vào việc quản lý khách hàng, xe, dịch vụ, lịch đặt rửa xe, hàng chờ theo từng buổi, lịch sử rửa xe và cập nhật hạng thành viên sau khi khách hoàn tất dịch vụ.
Tài liệu này tập trung mô tả chức năng, luồng xử lý, lý do cần có chức năng và phạm vi nghiệp vụ. Ngoài ra, vì dự án thuộc môn Cấu trúc dữ liệu và giải thuật (CSD), tài liệu bổ sung phần định hướng cấu trúc dữ liệu ở mức khái niệm để giải thích sự phù hợp của các cấu trúc dữ liệu như **Queue, Priority Queue, Stack, List, Map** và các thuật toán tìm kiếm, sắp xếp cơ bản. Tài liệu không đi sâu vào thiết kế code chi tiết, thuộc tính class hay hướng dẫn lập trình chi tiết.

### 1.2 Phạm vi sản phẩm
Sản phẩm được xây dựng dưới dạng ứng dụng giao diện dòng lệnh (**CLI - Command Line Interface**) chạy bằng menu trong terminal, phục vụ mục đích học tập và mô phỏng nghiệp vụ thực tế của một tiệm rửa xe. Hệ thống sử dụng file text phẳng (`.txt`) dùng dấu phân tách `|` để lưu trữ dữ liệu lâu dài và nạp toàn bộ vào bộ nhớ RAM khi khởi động để xử lý bằng các cấu trúc dữ liệu tự định nghĩa.

### 1.3 Đối tượng sử dụng tài liệu
- Giảng viên môn CSD201 dùng để đánh giá yêu cầu và định hướng thiết kế cấu trúc dữ liệu của dự án.
- Sinh viên thực hiện dự án dùng làm căn cứ lập trình, kiểm thử và chuẩn bị nội dung vấn đáp (Q&A).

### 1.4 Thuật ngữ và định nghĩa

| Thuật ngữ | Ý nghĩa |
|---|---|
| Customer | Khách hàng sử dụng dịch vụ rửa xe |
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

### 1.5 Tài liệu tham khảo hoặc nguồn ý tưởng
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
Hệ thống mô phỏng hai nhóm vai trò (không cần cơ chế đăng nhập/đăng xuất thực tế, việc phân chia chỉ phục vụ cho giao diện menu CLI phân cấp):
- **Khách hàng (Customer)**: Xem thông tin cá nhân (hạng, điểm, chi tiêu), xem danh sách dịch vụ rửa xe, tạo booking, xem danh sách booking cá nhân và hủy booking của mình.
- **Nhân viên / Quản lý tiệm (Admin)**: Quản lý khách hàng, quản lý xe, quản lý dịch vụ (thêm, xem, tìm kiếm, sắp xếp), kích hoạt buổi phục vụ, xem hàng chờ chính/phụ, bắt đầu xử lý xe tiếp theo, xác nhận thanh toán, hoàn tất booking, hủy booking, xem lịch sử và hoàn tác booking hoàn tất gần nhất.

### 2.5 Giả định và ràng buộc
- **Giả định về thời gian**: Hệ thống không bắt buộc lấy thời gian thực từ máy tính mà cho phép Admin thiết lập thủ công ngày hiện tại (`currentDate`) và buổi hiện tại (`currentPeriod`) để phục vụ kiểm thử và demo các tình huống nghiệp vụ khác nhau.
- **Ràng buộc lưu trữ**: Không sử dụng hệ quản trị cơ sở dữ liệu (SQL Server, MySQL, v.v.). Toàn bộ dữ liệu được lưu trữ lâu dài trong các file text (`.txt`) và tải vào bộ nhớ RAM khi khởi động.
- **Ràng buộc về xử lý**: Tại một thời điểm, tiệm chỉ có đúng 1 vị trí rửa xe hoạt động (chỉ cho phép tối đa 1 booking ở trạng thái `SERVING`).

### 2.6 Chức năng nằm trong phạm vi
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

### 2.7 Chức năng ngoài phạm vi
Các chức năng sau được loại bỏ để tập trung vào mục tiêu của môn học CSD:
- Đăng nhập/đăng xuất thực tế và phân quyền tài khoản bảo mật.
- Giao diện người dùng Web hoặc Mobile.
- Tương tác với cơ sở dữ liệu (DBMS).
- Thanh toán trực tuyến thực tế (online payment gateway).
- Nhận diện biển số xe tự động (LPR - License Plate Recognition).
- Gửi email, SMS hoặc thông báo tự động.
- Quản lý chương trình khuyến mãi nâng cao, đổi điểm lấy quà, hạ hạng định kỳ hoặc hết hạn điểm.

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

Nếu ngày đặt lịch vượt quá giới hạn này, hệ thống sẽ từ chối nhận booking.

### 3.5 Quy tắc booking hiện tại
Booking hiện tại là booking được tạo cho đúng ngày hiện tại (`currentDate`) và buổi hiện tại (`currentPeriod`).
- Nếu buổi đó **đã được kích hoạt** (Activated):
  - Nếu slot chính còn trống: booking được xếp vào cuối hàng chờ chính (FIFO Queue).
  - Nếu slot chính đầy nhưng hàng chờ phụ còn trống: booking được đưa vào hàng chờ phụ (Priority Queue).
  - Nếu cả hai đều đầy: hệ thống từ chối booking.
- Nếu buổi đó **chưa được kích hoạt**: booking được lưu vào danh sách booking tương lai của buổi đó (để xếp thứ tự ưu tiên khi kích hoạt).

### 3.6 Quy tắc booking tương lai
Booking tương lai là booking được đặt cho một ngày sau ngày hiện tại, hoặc đặt cho buổi sau của ngày hiện tại (khi buổi đó chưa diễn ra và chưa được kích hoạt).
- Khi tạo booking tương lai, hệ thống kiểm tra tổng sức chứa (Slot chính + Slot chờ phụ) của buổi đó. Nếu còn chỗ, hệ thống ghi nhận booking.
- Các booking tương lai chưa được xếp vào hàng chờ phục vụ ngay mà được lưu tạm. Khi buổi đó được kích hoạt, hệ thống mới tiến hành sắp xếp chúng theo mức độ ưu tiên để phân bổ vào slot chính và hàng chờ phụ.

### 3.7 Quy tắc kích hoạt buổi (Activate period)
- Thao tác kích hoạt chuyển đổi trạng thái của buổi được chỉ định từ "chưa diễn ra" sang "đang diễn ra".
- Chỉ cho phép kích hoạt buổi trùng khớp với ngày hiện tại (`currentDate`) và buổi hiện tại (`currentPeriod`) được cấu hình trong hệ thống.
- **Ngăn chặn kích hoạt trùng**: Mỗi buổi (ngày + period) chỉ được kích hoạt duy nhất một lần. Nếu buổi đó đã kích hoạt trước đó, hệ thống sẽ báo lỗi. Trạng thái kích hoạt được lưu xuống file `periods.txt` để đảm bảo không bị reset khi khởi động lại app.
- Khi kích hoạt, hệ thống lấy toàn bộ booking tương lai đã đặt trước của buổi đó, sắp xếp theo thứ tự ưu tiên:
  1. Hạng thành viên cao hơn được ưu tiên trước (Platinum > Gold > Silver > Member).
  2. Nếu cùng hạng thành viên, booking nào được tạo trước (thời gian đặt sớm hơn) được ưu tiên trước.
- Sau khi sắp xếp, hệ thống đổ lần lượt các booking ưu tiên cao vào slot chính (tối đa bằng giới hạn slot chính của buổi). Số còn lại (nếu có) được đổ vào hàng chờ phụ (tối đa bằng giới hạn slot chờ phụ). Số dư thừa còn lại sẽ chuyển sang trạng thái thất bại hoặc hủy (do vượt quá sức chứa khi sắp xếp).

### 3.8 Quy tắc xử lý hàng chờ phụ
Khi slot chính của buổi hiện tại xuất hiện chỗ trống (do hủy booking hoặc do hệ thống thực hiện nghiệp vụ kéo thêm xe):
- Hệ thống sẽ lấy booking có độ ưu tiên cao nhất trong hàng chờ phụ (Waitlist Priority Queue).
- Đưa booking này vào cuối hàng chờ chính (FIFO Queue).
- Sau khi được đưa vào hàng chờ chính, booking này tuân thủ nguyên tắc FIFO để giữ thứ tự phục vụ ổn định.

### 3.9 Quy tắc hoàn tất booking (Complete booking)
- Một booking chỉ được hoàn tất khi đang ở trạng thái đang phục vụ (`SERVING`) và đã được thanh toán (`paymentStatus = PAID`).
- Khi bấm hoàn tất:
  1. Trạng thái booking chuyển sang `COMPLETED`.
  2. Ghi nhận thông tin vào lịch sử rửa xe (`history.txt`).
  3. Cập nhật các chỉ số tích lũy của khách hàng: tăng số lần rửa xe, cộng tổng tiền chi tiêu, cộng điểm loyalty theo tỷ lệ.
  4. Xét lại hạng thành viên của khách hàng.
  5. Đẩy thông tin booking này vào Stack hoàn tác để hỗ trợ sửa lỗi.
  6. **Kiểm tra và bổ sung từ hàng chờ phụ**: Hệ thống kiểm tra thời gian còn lại của buổi hiện tại. Nếu còn đủ thời gian phục vụ thêm 1 xe (dựa vào thời gian thực hiện của dịch vụ của xe tiếp theo trong waitlist), hệ thống sẽ kéo booking ưu tiên cao nhất từ waitlist lên cuối hàng chờ chính. Nếu không đủ thời gian hoặc waitlist trống, hệ thống không kéo thêm.
  7. Lưu toàn bộ thay đổi xuống các file lưu trữ tương ứng.

### 3.10 Quy tắc hủy booking (Cancel booking)
- Booking ở trạng thái chờ (`WAITING`) hoặc đang phục vụ (`SERVING`) có thể được hủy bởi Admin hoặc chính khách hàng (chỉ WAITING). Booking đã hoàn tất (`COMPLETED`) không được hủy (chỉ được hoàn tác nếu là booking mới nhất).
- Khi hủy booking trong slot chính của buổi hiện tại:
  - Trạng thái booking chuyển sang `CANCELLED`.
  - Hệ thống ngay lập tức kiểm tra hàng chờ phụ (Waitlist), nếu có khách đang chờ, hệ thống lấy booking ưu tiên cao nhất từ Waitlist đưa vào cuối hàng chờ chính (FIFO Queue).
  - Lưu trạng thái xuống file.
- Khi hủy booking trong hàng chờ phụ hoặc booking tương lai: hệ thống chỉ cần cập nhật trạng thái booking thành `CANCELLED` và loại khỏi danh sách chờ tương ứng, sau đó lưu file.

### 3.11 Quy tắc loyalty (Điểm và hạng)
- **Tỷ lệ tích điểm**: Mỗi **1,000 VND** chi tiêu tích lũy được **1 điểm** loyalty (Ví dụ: dịch vụ giá 100,000 VND tích 100 điểm).
- **Quy đổi độ ưu tiên hạng thành viên**:
  - Member: Ưu tiên mức 1.
  - Silver: Ưu tiên mức 2.
  - Gold: Ưu tiên mức 3.
  - Platinum: Ưu tiên mức 4.
- Điểm loyalty và hạng thành viên được cập nhật ngay lập tức sau khi booking được chuyển sang trạng thái `COMPLETED` thành công.

### 3.12 Quy tắc lịch sử
- Lịch sử rửa xe chỉ ghi nhận các booking đã chuyển sang trạng thái `COMPLETED` thành công.
- Thông tin ghi nhận gồm: mã booking, khách hàng, xe, dịch vụ, thời gian hoàn tất, số tiền thanh toán và số điểm tích lũy được cộng.
- Dữ liệu lịch sử được ghi nhận lâu dài vào file `history.txt`.

### 3.13 Quy tắc hoàn tác booking hoàn tất gần nhất (Undo)
- Hệ thống chỉ cho phép hoàn tác thao tác hoàn tất của **booking vừa được complete gần nhất** (sử dụng cấu trúc Stack). Không cho phép hoàn tác tùy ý các booking cũ hơn trong lịch sử.
- Khi thực hiện hoàn tác:
  1. Lấy booking hoàn tất gần nhất ra khỏi Stack.
  2. Chuyển trạng thái booking đó ngược lại từ `COMPLETED` về trạng thái trước đó (mặc định là `SERVING`, giữ nguyên trạng thái thanh toán `PAID`).
  3. Trừ bớt số lần rửa xe, tổng chi tiêu và số điểm loyalty đã cộng của khách hàng tương ứng.
  4. Xét lại hạng thành viên của khách hàng (có thể bị hạ hạng về đúng hạng cũ trước khi complete booking này nếu không còn đủ điều kiện).
  5. Xóa bản ghi tương ứng khỏi lịch sử rửa xe.
  6. Lưu dữ liệu đã hoàn tác xuống file.

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

### FR-01 — Quản lý khách hàng
- **Mô tả**: Cho phép admin thêm khách hàng mới, xem toàn bộ danh sách khách hàng và tìm kiếm thông tin khách hàng.
- **Lý do cần có**: Mọi giao dịch đặt lịch và cơ chế tích điểm loyalty đều phải gắn với một thực thể khách hàng cụ thể.
- **Luồng xử lý chính**:
  1. Người dùng chọn chức năng quản lý khách hàng từ menu Admin.
  2. Để thêm mới: nhập tên và số điện thoại. Hệ thống tự động sinh ID khách hàng tăng dần (dạng `C001, C002...`), gán hạng mặc định là `MEMBER`, các chỉ số tích lũy bằng 0.
  3. Để tìm kiếm: nhập mã khách hàng hoặc số điện thoại. Hệ thống duyệt tìm tuyến tính và hiển thị thông tin.
- **Điều kiện/ràng buộc**: Tên khách hàng và số điện thoại không được để trống. Số điện thoại của khách hàng phải là duy nhất (không được trùng lặp).
- **Kết quả mong đợi**: Khách hàng mới được thêm vào bộ nhớ và file `customers.txt`. Thông tin tìm kiếm hiển thị chính xác.

### FR-02 — Quản lý xe của khách hàng
- **Mô tả**: Cho phép admin liên kết thông tin xe (biển số) với khách hàng, xem danh sách xe và tìm kiếm xe.
- **Lý do cần có**: Một khách hàng có thể sở hữu nhiều xe khác nhau. Khi tạo booking, hệ thống cần biết chính xác xe nào sẽ được rửa để phục vụ việc tiếp nhận xe tại tiệm.
- **Luồng xử lý chính**:
  1. Admin nhập thông tin xe bao gồm biển số và mã khách hàng sở hữu.
  2. Hệ thống kiểm tra mã khách hàng xem có tồn tại không.
  3. Nếu hợp lệ, sinh mã xe tự động (`V001, V002...`), lưu liên kết chủ sở hữu.
  4. Hỗ trợ xem danh sách xe của một khách hàng cụ thể.
- **Điều kiện/ràng buộc**: Biển số xe không được để trống và không được trùng lặp trong hệ thống. Chủ sở hữu xe phải là một khách hàng đã tồn tại.
- **Kết quả mong đợi**: Xe được thêm thành công và lưu vào file `vehicles.txt`.

### FR-03 — Quản lý dịch vụ rửa xe
- **Mô tả**: Cho phép admin thêm, xem, tìm kiếm và sắp xếp danh sách dịch vụ rửa xe theo giá hoặc thời gian thực hiện.
- **Lý do cần có**: Mỗi lượt rửa xe cần chọn dịch vụ cụ thể để xác định giá tiền thanh toán và thời gian thi công dự kiến.
- **Luồng xử lý chính**:
  1. Admin nhập tên dịch vụ, giá tiền và thời gian rửa (phút).
  2. Hệ thống kiểm tra tính hợp lệ và tự động sinh mã dịch vụ (`S001, S002...`).
  3. Cho phép sắp xếp dịch vụ hiển thị tăng/giảm dần theo giá hoặc thời gian thi công.
- **Điều kiện/ràng buộc**: Tên dịch vụ không được trống, giá dịch vụ và thời gian rửa phải lớn hơn 0.
- **Kết quả mong đợi**: Dịch vụ được ghi nhận vào file `services.txt`. Sắp xếp hiển thị chính xác.

### FR-04 — Thiết lập ngày và buổi hiện tại
- **Mô tả**: Cho phép admin thay đổi ngày (`currentDate`) và buổi (`currentPeriod`) mô phỏng của hệ thống.
- **Lý do cần có**: Giúp dễ dàng demo và chạy thử các kịch bản kiểm thử (như kích hoạt buổi, đặt lịch trước) mà không phụ thuộc vào thời gian thực tế của máy tính.
- **Luồng xử lý chính**:
  1. Admin chọn chức năng thiết lập thời gian mô phỏng.
  2. Nhập ngày (định dạng `YYYY-MM-DD`) và chọn buổi (`MORNING`, `AFTERNOON`, `EVENING`).
  3. Hệ thống kiểm tra tính hợp lệ của định dạng và cập nhật biến toàn cục trong RAM.
- **Điều kiện/ràng buộc**: Ngày nhập phải đúng định dạng ngày tháng hợp lệ. Buổi nhập phải thuộc danh sách buổi quy định.
- **Kết quả mong đợi**: Hệ thống chuyển sang mốc thời gian mô phỏng mới, ảnh hưởng trực tiếp đến nghiệp vụ kiểm tra thời gian đặt trước.

### FR-05 — Tạo booking rửa xe
- **Mô tả**: Cho phép khách hàng hoặc admin tạo lịch đặt rửa xe cho một khách hàng, một xe, một dịch vụ, vào một ngày và buổi cụ thể.
- **Lý do cần có**: Là dữ liệu đầu vào cốt lõi để xếp hàng chờ và vận hành tiệm.
- **Luồng xử lý chính**:
  1. Nhập mã khách hàng, mã xe, mã dịch vụ, ngày muốn đặt và buổi muốn đặt.
  2. Hệ thống kiểm tra sự tồn tại của khách, xe, dịch vụ và kiểm tra xe có thuộc khách hàng đó không.
  3. Kiểm tra ràng buộc Booking window theo hạng của khách hàng.
  4. Xác định booking thuộc buổi hiện tại (đang diễn ra) hay buổi tương lai để kiểm tra sức chứa tương ứng.
  5. Nếu hợp lệ, sinh mã booking tự động (`B001, B002...`), gán trạng thái mặc định là `WAITING` và lưu trữ.
- **Điều kiện/ràng buộc**: Khách hàng, xe, dịch vụ phải tồn tại và liên kết đúng. Ngày đặt phải nằm trong booking window. Sức chứa của buổi phải còn chỗ (slot chính hoặc waitlist).
- **Kết quả mong đợi**: Booking được tạo thành công, ghi vào file `bookings.txt`.

### FR-06 — Kiểm tra booking window theo hạng thành viên
- **Mô tả**: Hệ thống tự động kiểm tra số ngày đặt trước của khách hàng có vượt quá giới hạn cho phép của hạng thành viên của họ hay không.
- **Lý do cần có**: Đảm bảo đặc quyền đặt lịch sớm của khách hàng VIP (Platinum, Gold, Silver) và công bằng cho các khách hàng khác.
- **Luồng xử lý chính**:
  1. Khi nhận yêu cầu tạo booking, hệ thống tính khoảng cách số ngày: $D = \text{ngày đặt lịch} - \text{ngày hiện tại của hệ thống}$.
  2. Tra cứu hạng thành viên của khách hàng để lấy số ngày đặt trước tối đa ($W$).
  3. Nếu $D > W$, từ chối booking và thông báo giới hạn của hạng.
  4. Nếu $D \le W$, cho phép tiếp tục luồng tạo booking.
- **Điều kiện/ràng buộc**: Khoảng cách ngày phải lớn hơn hoặc bằng 0.
- **Kết quả mong đợi**: Ngăn chặn chính xác các lượt đặt trước quá xa so với đặc quyền hạng.

### FR-07 — Xác định buổi rửa xe của booking
- **Mô tả**: Phân loại booking vào đúng buổi phục vụ (Sáng, Chiều hoặc Tối) dựa trên thông tin đăng ký của khách hàng.
- **Lý do cần có**: Mỗi buổi có giới hạn chỗ ngồi độc lập, việc phân loại giúp kiểm tra và kích hoạt chính xác theo từng buổi.
- **Luồng xử lý chính**:
  - Gắn thuộc tính buổi (`MORNING` / `AFTERNOON` / `EVENING`) vào đối tượng booking khi lưu trữ.
- **Điều kiện/ràng buộc**: Thuộc tính buổi phải nằm trong danh mục buổi quy định.
- **Kết quả mong đợi**: Hệ thống phân loại chính xác booking theo từng buổi phục vụ.

### FR-08 — Xử lý booking trong buổi hiện tại
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

### FR-09 — Xử lý booking cho buổi tương lai
- **Mô tả**: Lưu trữ và quản lý tạm thời các booking đặt trước cho các buổi trong tương lai.
- **Lý do cần có**: Khách hàng đặt trước chưa được đưa vào hàng chờ phục vụ thực tế ngay mà phải chờ đến thời điểm kích hoạt buổi để phân loại độ ưu tiên.
- **Luồng xử lý chính**:
  1. Xác định booking thuộc ngày/buổi tương lai.
  2. Kiểm tra tổng sức chứa của buổi tương lai đó (tổng slot chính + phụ).
  3. Nếu còn chỗ, ghi nhận booking vào danh sách booking tương lai và lưu file.
- **Điều kiện/ràng buộc**: Chỉ kiểm tra tổng sức chứa của buổi đó tại thời điểm tạo, chưa phân bổ vào hàng chờ chính/phụ cụ thể.
- **Kết quả mong đợi**: Booking tương lai được tiếp nhận và lưu trữ ổn định.

### FR-10 — Kích hoạt buổi rửa xe
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

### FR-11 — Ngăn kích hoạt trùng buổi
- **Mô tả**: Kiểm tra và ngăn chặn admin kích hoạt lại một buổi đã được kích hoạt trước đó.
- **Lý do cần có**: Tránh việc lặp lại giải thuật sắp xếp làm xáo trộn hàng chờ đang chạy và làm trùng lặp các booking trong queue.
- **Luồng xử lý chính**:
  1. Khi nhận lệnh kích hoạt, tra cứu trong danh sách trạng thái buổi (tải từ `periods.txt`).
  2. Nếu buổi hiện tại đã có trạng thái `ACTIVATED`, thông báo từ chối kích hoạt.
  3. Nếu chưa, cho phép tiếp tục kích hoạt và cập nhật trạng thái thành `ACTIVATED`.
- **Điều kiện/ràng buộc**: Trạng thái phải được lưu xuống file để duy trì sau khi tắt app.
- **Kết quả mong đợi**: Không xảy ra tình trạng kích hoạt trùng lặp cho cùng một ngày và buổi.

### FR-12 — Xem hàng chờ chính của buổi hiện tại
- **Mô tả**: Hiển thị danh sách các booking đang nằm trong slot chính (hàng chờ chính) của buổi đang diễn ra theo đúng thứ tự xếp hàng.
- **Lý do cần có**: Giúp nhân viên và admin nắm bắt được thứ tự phục vụ và các xe đang chờ tại tiệm.
- **Luồng xử lý chính**:
  - Truy xuất cấu trúc dữ liệu hàng chờ chính (MyQueue) của buổi hiện tại, duyệt qua các phần tử và hiển thị thông tin chi tiết (mã booking, biển số xe, tên khách, dịch vụ, trạng thái).
- **Kết quả mong đợi**: Danh sách hiển thị rõ ràng, đúng thứ tự vào trước ra trước.

### FR-13 — Xem hàng chờ phụ của buổi hiện tại
- **Mô tả**: Hiển thị danh sách các booking đang nằm trong hàng chờ phụ (Waitlist) của buổi đang diễn ra theo thứ tự ưu tiên.
- **Lý do cần có**: Giúp admin biết khách hàng nào có cơ hội được đẩy lên slot chính tiếp theo.
- **Luồng xử lý chính**:
  - Duyệt và hiển thị các booking trong cấu trúc hàng chờ phụ (MyPriorityQueue) của buổi hiện tại theo thứ tự ưu tiên từ cao xuống thấp.
- **Kết quả mong đợi**: Hiển thị đúng danh sách theo độ ưu tiên hạng thành viên và thời gian đặt.

### FR-14 — Xem booking của khách hàng
- **Mô tả**: Cho phép khách hàng tra cứu toàn bộ danh sách booking của cá nhân họ cùng trạng thái tương ứng.
- **Lý do cần có**: Giúp khách hàng tự quản lý lịch đặt xe của mình.
- **Luồng xử lý chính**:
  1. Khách hàng nhập mã số của mình.
  2. Hệ thống lọc trong danh sách booking và hiển thị các booking liên quan (chờ, đang rửa, hoàn tất, đã hủy).
- **Kết quả mong đợi**: Hiển thị chính xác các booking của riêng khách hàng đó.

### FR-15 — Xử lý booking tiếp theo
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

### FR-16 — Xác nhận thanh toán booking
- **Mô tả**: Cho phép admin xác nhận khách hàng đã thanh toán hóa đơn cho booking đang ở trạng thái `SERVING`.
- **Lý do cần có**: Đảm bảo nguyên tắc thu tiền trước khi xác nhận hoàn tất dịch vụ và ghi nhận doanh thu.
- **Luồng xử lý chính**:
  1. Kiểm tra có booking nào đang ở trạng thái `SERVING` không.
  2. Admin chọn xác nhận thanh toán, nhập phương thức thanh toán (`CASH` hoặc `BANKING`).
  3. Hệ thống cập nhật `paymentStatus = PAID`.
  4. Lưu thay đổi xuống file `bookings.txt`.
- **Điều kiện/ràng buộc**: Chỉ thanh toán cho booking đang ở trạng thái `SERVING`.
- **Kết quả mong đợi**: Trạng thái thanh toán của booking chuyển thành `PAID`.

### FR-17 — Hoàn tất booking
- **Mô tả**: Xác nhận dịch vụ rửa xe đã hoàn tất cho xe đang rửa, thực hiện tích điểm, cập nhật hạng thành viên và lưu lịch sử.
- **Lý do cần có**: Đóng luồng phục vụ của một booking, kích hoạt tích lũy loyalty cho khách hàng và giải phóng vị trí phục vụ của tiệm.
- **Luồng xử lý chính**:
  1. Kiểm tra có booking đang ở trạng thái `SERVING` và đã thanh toán (`paymentStatus = PAID`) hay chưa.
  2. Chuyển trạng thái booking sang `COMPLETED`.
  3. Gọi nghiệp vụ cập nhật loyalty của khách hàng (FR-19).
  4. Ghi nhận booking vào lịch sử rửa xe (`history.txt`).
  5. Đẩy booking vào Stack hoàn tác.
  6. Kiểm tra thời gian còn lại của buổi. Nếu còn đủ thời gian thực hiện dịch vụ của xe đứng đầu Waitlist, lấy booking ưu tiên cao nhất trong Waitlist (MyPriorityQueue.poll) chuyển vào cuối hàng chờ chính (MyQueue).
  7. Lưu toàn bộ dữ liệu thay đổi xuống file.
- **Điều kiện/ràng buộc**: Booking phải ở trạng thái `SERVING` và có `paymentStatus = PAID`.
- **Kết quả mong đợi**: Booking được hoàn tất thành công, các dữ liệu tích lũy được cập nhật chính xác.

### FR-18 — Hủy booking
- **Mô tả**: Cho phép admin hoặc khách hàng hủy một booking chưa thực hiện.
- **Lý do cần có**: Giải phóng suất đặt lịch của khách hàng khi họ thay đổi kế hoạch, nhường chỗ cho khách hàng trong hàng chờ phụ.
- **Luồng xử lý chính**:
  1. Tìm booking cần hủy theo mã booking.
  2. Kiểm tra trạng thái booking có hợp lệ để hủy không (đang `WAITING` hoặc `SERVING`).
  3. Chuyển trạng thái booking sang `CANCELLED`.
  4. Nếu booking bị hủy đang nằm trong slot chính của buổi hiện tại:
     - Lấy booking ưu tiên cao nhất từ hàng chờ phụ (Waitlist Priority Queue).
     - Đưa booking đó vào cuối hàng chờ chính (FIFO Queue).
  5. Lưu thay đổi xuống file.
- **Điều kiện/ràng buộc**: Không cho phép hủy booking đã ở trạng thái `COMPLETED`.
- **Kết quả mong đợi**: Booking được hủy, giải phóng chỗ và bổ sung kịp thời khách hàng từ hàng chờ phụ lên slot chính.

### FR-19 — Cập nhật loyalty sau khi hoàn tất booking
- **Mô tả**: Tự động tính toán cộng điểm, tăng chi tiêu, tăng số lần rửa xe và xét lại hạng thành viên của khách hàng sau khi booking hoàn tất.
- **Lý do cần có**: Đảm bảo ghi nhận đóng góp chi tiêu của khách hàng để áp dụng các đặc quyền ưu tiên trong lần đặt lịch sau.
- **Luồng xử lý chính**:
  1. Lấy thông tin khách hàng từ booking vừa hoàn tất.
  2. Tăng số lần rửa xe (`visitCount`) lên 1.
  3. Cộng giá trị dịch vụ vào tổng chi tiêu (`totalSpent`).
  4. Cộng điểm tích lũy (`loyaltyPoints`): 1 điểm cho mỗi 1,000 VND chi tiêu.
  5. So sánh các chỉ số mới với điều kiện hạng thành viên (Silver, Gold, Platinum) để xét nâng hạng cao nhất có thể đạt được.
  6. Cập nhật thông tin khách hàng trong bộ nhớ và ghi file `customers.txt`.
- **Kết quả mong đợi**: Dữ liệu hạng, điểm của khách hàng thay đổi chính xác.

### FR-20 — Xem lịch sử rửa xe
- **Mô tả**: Hiển thị danh sách các booking đã hoàn tất phục vụ của tiệm.
- **Lý do cần có**: Phục vụ đối soát doanh thu và kiểm tra hoạt động của tiệm.
- **Luồng xử lý chính**:
  - Đọc và hiển thị dữ liệu từ danh sách lịch sử rửa xe (tải từ `history.txt`), hỗ trợ lọc theo mã khách hàng cụ thể.
- **Kết quả mong đợi**: Hiển thị đầy đủ thông tin các lượt rửa xe đã hoàn tất.

### FR-21 — Hoàn tác booking hoàn tất gần nhất
- **Mô tả**: Đảo ngược toàn bộ tác động của booking vừa hoàn tất gần nhất về trạng thái trước đó.
- **Lý do cần có**: Giúp nhân viên sửa sai nhanh chóng nếu lỡ tay bấm nhầm hoàn tất cho một xe chưa xong hoặc nhầm xe.
- **Luồng xử lý chính**:
  1. Lấy booking hoàn tất gần nhất ra khỏi Stack hoàn tác.
  2. Chuyển trạng thái booking đó từ `COMPLETED` về `SERVING` (và giữ trạng thái thanh toán `PAID`).
  3. Lấy thông tin khách hàng liên quan, thực hiện trừ đi các chỉ số đã cộng (giảm `visitCount` đi 1, trừ `totalSpent` tương ứng giá dịch vụ, trừ điểm loyalty tương ứng).
  4. Xét lại hạng thành viên của khách hàng dựa trên các chỉ số sau khi giảm (có thể bị hạ hạng về hạng cũ).
  5. Xóa dòng lịch sử của booking này trong danh sách lịch sử.
  6. Lưu toàn bộ thay đổi xuống các file lưu trữ.
- **Điều kiện/ràng buộc**: Stack hoàn tác phải không được trống. Chỉ hoàn tác được booking hoàn tất gần nhất.
- **Kết quả mong đợi**: Hệ thống quay về trạng thái trước khi complete booking đó một cách nhất quán.

### FR-22 — Tải dữ liệu từ file khi khởi động
- **Mô tả**: Nạp toàn bộ dữ liệu từ các file text phẳng trong thư mục `data` vào các cấu trúc dữ liệu tương ứng trong RAM khi ứng dụng khởi chạy.
- **Lý do cần có**: Đảm bảo tính toàn vẹn và duy trì dữ liệu qua các lần tắt/mở ứng dụng CLI.
- **Luồng xử lý chính**:
  1. Khi khởi chạy app, kiểm tra sự tồn tại của thư mục `data` và các file text.
  2. Đọc từng dòng của các file, cắt chuỗi bằng ký tự phân tách `|`.
  3. Khởi tạo đối tượng và đưa vào các cấu trúc dữ liệu trong RAM (`MyLinkedList`, `MyQueue`, `MyPriorityQueue`, `MyStack`).
  4. Nếu các file chưa tồn tại hoặc rỗng, kích hoạt luồng khởi tạo dữ liệu mẫu (FR-25).
- **Kết quả mong đợi**: Ứng dụng khởi động thành công với đầy đủ dữ liệu cũ được nạp vào bộ nhớ.

### FR-23 — Lưu dữ liệu sau thao tác quan trọng
- **Mô tả**: Tự động ghi đè dữ liệu mới nhất trong RAM xuống các file text tương ứng ngay sau khi thực hiện xong một thao tác làm thay đổi dữ liệu hệ thống.
- **Lý do cần có**: Ngăn ngừa mất dữ liệu khi ứng dụng CLI bị tắt đột ngột (ví dụ tắt terminal, mất điện, v.v.).
- **Luồng xử lý chính**:
  - Ghi đè toàn bộ danh sách đối tượng hiện tại trong RAM xuống file tương ứng dưới dạng các dòng text phân tách bằng ký tự `|`.
- **Các thao tác bắt buộc lưu file**: Thêm khách hàng, thêm xe, thêm dịch vụ, tạo booking, kích hoạt buổi, xử lý xe tiếp theo, xác nhận thanh toán, hoàn tất booking, hủy booking, cập nhật loyalty và hoàn tác booking.
- **Kết quả mong đợi**: File text luôn phản ánh trạng thái dữ liệu mới nhất của RAM.

### FR-24 — Sinh mã tự động
- **Mô tả**: Tự động tạo mã định danh duy nhất tăng dần cho Khách hàng (`C001, C002...`), Xe (`V001, V002...`), Dịch vụ (`S001, S002...`) và Booking (`B001, B002...`).
- **Lý do cần có**: Tránh trùng lặp mã định danh và giảm thiểu việc nhập liệu thủ công rườm rà trên giao diện CLI.
- **Luồng xử lý chính**:
  1. Khi load file, hệ thống tìm ra ID lớn nhất hiện tại (ví dụ `C005`).
  2. Khi tạo mới thực thể, hệ thống tăng số thứ tự lên 1 và định dạng chuỗi tương ứng (ví dụ sinh ra `C006`).
- **Kết quả mong đợi**: Mã định danh sinh ra luôn duy nhất và có thứ tự tăng dần chuẩn xác.

### FR-25 — Khởi tạo dữ liệu mẫu
- **Mô tả**: Tự động điền dữ liệu mẫu ban đầu vào các file lưu trữ khi phát hiện các file này trống hoặc chưa tồn tại.
- **Lý do cần có**: Giúp ứng dụng sẵn sàng để demo các chức năng phức tạp (như xếp hàng chờ theo hạng, đặt lịch trước) ngay khi chạy lần đầu mà không bắt người dùng nhập liệu từ đầu.
- **Luồng xử lý chính**:
  1. Tạo thư mục `data` và các file nếu chưa có.
  2. Tạo 4 khách hàng mẫu đại diện cho 4 hạng thành viên (`MEMBER`, `SILVER`, `GOLD`, `PLATINUM`).
  3. Tạo ít nhất 4 xe tương ứng liên kết với 4 khách hàng trên.
  4. Tạo 3 dịch vụ mẫu (`Basic Wash`, `Premium Wash`, `Interior Cleaning`).
  5. Tạo một số booking mẫu đặt trước ở các buổi và các hạng thành viên khác nhau để chuẩn bị demo.
  6. Ghi toàn bộ dữ liệu mẫu này xuống file text tương ứng.
- **Kết quả mong đợi**: Các file text được điền đầy đủ thông tin demo chuẩn nghiệp vụ.

### FR-26 — Kiểm tra dữ liệu đầu vào (Validate)
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

## 5. Yêu cầu dữ liệu và lưu trữ

### 5.1 Nhóm dữ liệu cần quản lý
Hệ thống cần quản lý các nhóm thông tin chính sau (mức khái niệm):
- **Khách hàng (Customer)**: Mã khách hàng, Tên khách hàng, Số điện thoại, Hạng thành viên, Điểm tích lũy, Tổng chi tiêu, Số lần rửa xe.
- **Xe (Vehicle)**: Mã xe, Biển số xe, Mã khách hàng sở hữu.
- **Dịch vụ (Service)**: Mã dịch vụ, Tên dịch vụ, Giá tiền, Thời gian thực hiện (phút), Trạng thái hoạt động.
- **Booking (Booking)**: Mã booking, Mã khách hàng, Mã xe, Mã dịch vụ, Ngày đặt lịch, Buổi đặt lịch, Trạng thái booking (`WAITING`, `SERVING`, `COMPLETED`, `CANCELLED`), Trạng thái thanh toán (`UNPAID`, `PAID`), Phương thức thanh toán (`NONE`, `CASH`, `BANKING`), Thời điểm tạo booking.
- **Trạng thái buổi (Period State)**: Ngày, Buổi, Trạng thái kích hoạt (`ACTIVATED`, `NOT_ACTIVATED`).

### 5.2 File lưu trữ
Dữ liệu được tổ chức lưu trữ trong thư mục `data/` với 6 file text độc lập:
1. `customers.txt`: Lưu trữ thông tin khách hàng.
2. `vehicles.txt`: Lưu trữ thông tin xe của khách hàng.
3. `services.txt`: Lưu trữ thông tin dịch vụ rửa xe.
4. `bookings.txt`: Lưu trữ thông tin tất cả các booking (hiện tại, tương lai, hủy...).
5. `history.txt`: Lưu trữ lịch sử các booking đã hoàn tất (`COMPLETED`).
6. `periods.txt`: Lưu trữ trạng thái kích hoạt của các buổi phục vụ.

### 5.3 Thời điểm load dữ liệu
Dữ liệu được load từ các file text trong thư mục `data/` duy nhất một lần khi **chương trình khởi động** (Start app). Hệ thống sẽ phân tích cú pháp các dòng text và dựng lại toàn bộ các đối tượng trong RAM.

### 5.4 Thời điểm save dữ liệu
Hệ thống tự động thực hiện save dữ liệu (ghi đè toàn bộ danh sách đối tượng trong RAM xuống file tương ứng) **ngay sau khi một thao tác nghiệp vụ quan trọng hoàn tất thành công**. Các thao tác này bao gồm: thêm khách hàng/xe/dịch vụ, tạo booking, kích hoạt buổi, xử lý xe tiếp theo, xác nhận thanh toán, hoàn tất booking, hủy booking và hoàn tác.

### 5.5 Định dạng file text dùng dấu |
Để đơn giản hóa việc đọc/ghi dữ liệu bằng code Java thuần mà không cần sử dụng các thư viện phân tích cú pháp phức tạp (như JSON, XML), dữ liệu được định dạng theo dòng text phẳng, các trường thông tin ngăn cách bởi ký tự `|`.
- *Ví dụ dòng trong `customers.txt`*:
  `C001|Nguyen Van A|0901234567|GOLD|120|5,000,000|12`
- *Ví dụ dòng trong `services.txt`*:
  `S001|Basic Wash|50000|30|ACTIVE`

### 5.6 Vai trò của file và RAM
- **File text**: Đóng vai trò là nơi lưu trữ dữ liệu lâu dài (Persistence Layer) khi tắt ứng dụng. Hệ thống không thực hiện các giải thuật tìm kiếm, sắp xếp hay điều phối trực tiếp trên file text.
- **RAM**: Đóng vai trò là nơi xử lý thuật toán chính. Khi ứng dụng chạy, toàn bộ dữ liệu nằm trên RAM và được quản lý bằng các cấu trúc dữ liệu tự cài đặt để đảm bảo hiệu năng và mục tiêu môn học CSD.

### 5.7 Sinh mã tự động
Mã định danh (ID) của Khách hàng, Xe, Dịch vụ và Booking được hệ thống tự động sinh theo định dạng: Ký tự phân loại (`C` / `V` / `S` / `B`) kết hợp với số thứ tự tăng dần có độ dài 3 chữ số (ví dụ: `C001`, `C002`, `C003`...). Khi load dữ liệu từ file, hệ thống sẽ xác định số thứ tự lớn nhất hiện tại để làm điểm bắt đầu sinh mã cho các thực thể tiếp theo.

### 5.8 Khởi tạo dữ liệu mẫu
Khi chương trình khởi chạy, nếu phát hiện thư mục `data/` chưa tồn tại hoặc các file text bị rỗng, hệ thống sẽ tự động tạo thư mục và ghi dữ liệu mẫu chuẩn (gồm 4 khách hàng thuộc 4 hạng thành viên khác nhau, xe tương ứng, 3 dịch vụ rửa xe và một vài booking mẫu) vào các file để người dùng có thể thực hiện demo ngay lập tức.

---

## 6. Định hướng cấu trúc dữ liệu và giải thuật

Phần này mô tả định hướng thiết kế cấu trúc dữ liệu và giải thuật ở mức khái niệm, phục vụ cho mục tiêu học tập và minh họa của môn học CSD201.

### 6.1 Danh sách liên kết tự cài (MyLinkedList)
- **Mục đích**: Sử dụng để lưu trữ danh sách Khách hàng, danh sách Xe, danh sách Dịch vụ và danh sách Lịch sử rửa xe trong RAM.
- **Lý do**: Đây là các danh sách dữ liệu tuyến tính cần các thao tác thêm mới vào cuối danh sách, duyệt qua toàn bộ danh sách để tìm kiếm tuyến tính (Linear Search) hoặc hiển thị thông tin. Sử dụng danh sách liên kết đơn tự cài đặt với các đối tượng Node giúp minh họa trực quan cấu trúc dữ liệu tuyến tính cơ bản.

### 6.2 Hàng đợi tự cài (MyQueue)
- **Mục đích**: Quản lý danh sách các booking trong slot chính (Main slot) của buổi phục vụ đang diễn ra.
- **Lý do**: Slot chính yêu cầu phục vụ theo đúng thứ tự xếp hàng thực tế - xe nào đặt lịch trước hoặc được đưa vào trước thì rửa trước, phù hợp hoàn hảo với nguyên lý **FIFO (First In First Out)** của Hàng đợi. Khi bắt đầu rửa xe tiếp theo, hệ thống chỉ cần thực hiện thao tác lấy ra ở đầu hàng đợi (`dequeue`).

### 6.3 Hàng đợi ưu tiên tự cài (MyPriorityQueue)
- **Mục đích**: Quản lý danh sách các booking cho buổi tương lai (trước khi kích hoạt) và danh sách hàng chờ phụ (Waitlist) của buổi hiện tại.
- **Lý do**:
  - Đối với booking tương lai: Cần sắp xếp và lấy ra các booking có mức độ ưu tiên hạng thành viên cao nhất khi kích hoạt buổi để đưa vào slot chính.
  - Đối với hàng chờ phụ (Waitlist): Khi slot chính trống, khách hàng hạng cao hơn trong Waitlist cần được ưu tiên đẩy lên trước.
- **Định hướng giải thuật**: Cài đặt `MyPriorityQueue` sử dụng cấu trúc **Max Heap (cây nhị phân ưu tiên lớn nhất)**. Phần tử ở gốc (Root) luôn là booking có mức độ ưu tiên cao nhất.
  - *Tiêu chí ưu tiên*: So sánh Hạng thành viên trước (Platinum: 4 > Gold: 3 > Silver: 2 > Member: 1). Nếu hai booking có cùng hạng thành viên, booking nào được tạo trước (dựa trên thời điểm tạo) sẽ được ưu tiên cao hơn.
  - *Các thao tác trên Heap*: `insert` (thêm booking mới và gọi `heapify up` để điều chỉnh vị trí), `remove root` hay `poll` (lấy ra booking ưu tiên nhất ở root và gọi `heapify down` để tái cấu trúc heap), `peek` (xem booking ưu tiên nhất ở root mà không lấy ra).

### 6.4 Ngăn xếp tự cài (MyStack)
- **Mục đích**: Lưu trữ các booking đã được hoàn tất phục vụ để hỗ trợ tính năng hoàn tác (Undo) booking gần nhất.
- **Lý do**: Chức năng hoàn tác chỉ áp dụng cho booking vừa hoàn tất sau cùng. Nguyên lý **LIFO (Last In First Out)** của Ngăn xếp đáp ứng chính xác yêu cầu này. Khi hoàn tất booking thành công, hệ thống `push` booking đó vào Stack. Khi admin chọn hoàn tác, hệ thống thực hiện `pop` để lấy ra và xử lý đảo ngược trạng thái.

### 6.5 Bảng ánh xạ tự cài hoặc mảng tra cứu (MyMap)
- **Mục đích**: Lưu trữ cấu hình giới hạn ngày đặt trước (Booking window) tương ứng với từng hạng thành viên.
- **Lý do**: Giúp tra cứu nhanh số ngày được phép đặt trước tối đa từ hạng thành viên của khách hàng khi tạo booking ($O(1)$).

### 6.6 Thuật toán tìm kiếm (Linear Search)
- **Mục đích**: Tìm kiếm khách hàng theo mã/số điện thoại, tìm xe theo biển số, tìm booking theo mã booking.
- **Lý do**: Dữ liệu mô phỏng trong dự án CLI có quy mô vừa và nhỏ. Thuật toán tìm kiếm tuyến tính (Linear Search) dễ cài đặt, dễ giải thích cấu trúc duyệt node trong LinkedList và phù hợp với phạm vi yêu cầu môn học.

### 6.7 Thuật toán sắp xếp (Selection Sort)
- **Mục đích**: Sắp xếp danh sách dịch vụ theo giá dịch vụ hoặc thời gian thực hiện tăng/giảm dần.
- **Lý do**: Danh sách dịch vụ của tiệm rửa xe thường có số lượng ít và ít biến động. Thuật toán sắp xếp chọn (Selection Sort) đơn giản, trực quan, dễ cài đặt bằng Java thuần và dễ giải thích trong quá trình vấn đáp môn học.

### 6.8 Hướng mở rộng: Cây tìm kiếm nhị phân (Binary Search Tree)
- **Trạng thái**: *Không bắt buộc* (chỉ là định hướng mở rộng).
- **Mục đích đề xuất**: Có thể sử dụng BST để quản lý và tìm kiếm khách hàng nhanh hơn theo mã khách hàng thay thế cho LinkedList, nhằm tăng tính đa dạng cấu trúc dữ liệu cho dự án nếu sinh viên muốn đạt điểm tối đa.

---

## 7. Yêu cầu phi chức năng

### NFR-01 — Dễ trình diễn (Easy to Demo)
Hệ thống phải có luồng hoạt động trực quan thông qua các menu CLI được chia nhóm rõ ràng, đi kèm thông báo phản hồi chi tiết sau mỗi thao tác (ví dụ: hiển thị rõ xe nào vừa được đẩy lên slot chính, lý do từ chối booking là gì). Hệ thống cần hỗ trợ dữ liệu mẫu khởi tạo sẵn để có thể demo ngay lập tức.

### NFR-02 — Dữ liệu đơn giản (Simple Data Representation)
Dữ liệu của hệ thống phải được mô phỏng ở mức đơn giản, dễ đọc hiểu trong các file text phẳng, giúp giảng viên và sinh viên dễ dàng đối chiếu trực tiếp dữ liệu trong file với trạng thái hiển thị trên màn hình terminal.

### NFR-03 — Tập trung vào nghiệp vụ cốt lõi (Focus on Core Business Logic)
Ứng dụng tập trung hoàn toàn vào việc hiện thực hóa các cấu trúc dữ liệu tự cài đặt và các quy tắc nghiệp vụ hàng chờ, booking, loyalty. Không cố gắng tích hợp các tính năng công nghệ phức tạp bên ngoài như giao diện đồ họa, kết nối mạng hay thanh toán online thực tế.

### NFR-04 — Dễ kiểm thử (Testability)
Mọi chức năng nghiệp vụ phải cho ra kết quả hiển thị tức thì trên CLI để người kiểm thử dễ dàng xác minh (ví dụ: sau khi hủy một booking trong slot chính, danh sách hàng chờ chính hiển thị lại phải ngay lập tức có sự xuất hiện của xe đứng đầu hàng chờ phụ).

### NFR-05 — Dễ giải thích (Explainability)
Cấu trúc code và các giải thuật tự cài đặt (LinkedList, Heap, Queue, Stack) phải được tổ chức mạch lạc, có chú thích đầy đủ để sinh viên có thể dễ dàng giải thích bản chất thuật toán và lý do lựa chọn cấu trúc dữ liệu đó trước câu hỏi của giảng viên khi bảo vệ dự án.

### NFR-06 — Phù hợp phạm vi CSD (Appropriate Scope)
Độ phức tạp của dự án được khống chế ở mức vừa phải, đảm bảo sinh viên có thể hoàn thành trong khung thời gian môn học CSD201 mà không bị quá tải bởi các yêu cầu phát triển phần mềm công nghiệp.

---

## 8. Luồng nghiệp vụ chính

### 8.1 Luồng đặt booking thành công
1. Khách hàng hoặc nhân viên nhập mã khách hàng, mã xe, mã dịch vụ, ngày đặt và buổi đặt.
2. Hệ thống kiểm tra tính tồn tại của các thực thể và tính sở hữu của xe.
3. Hệ thống tính khoảng cách ngày đặt và so sánh với giới hạn Booking window của hạng thành viên của khách.
4. Hệ thống xác định booking thuộc buổi hiện tại (đang hoạt động) hay tương lai:
   - Nếu tương lai: kiểm tra tổng sức chứa buổi tương lai, nếu còn chỗ thì nhận booking và đưa vào danh sách booking tương lai.
   - Nếu hiện tại (đã kích hoạt): kiểm tra slot chính (còn chỗ $\rightarrow$ xếp vào FIFO Queue) hoặc hàng chờ phụ (còn chỗ $\rightarrow$ xếp vào Priority Queue).
5. Hệ thống sinh mã booking tự động, gán trạng thái `WAITING`, lưu dữ liệu trong RAM và ghi file `bookings.txt`.

### 8.2 Luồng đặt booking thất bại
Một lượt đặt booking sẽ bị hệ thống từ chối và thông báo lỗi rõ ràng nếu vi phạm một trong các điều kiện sau:
- Khách hàng, xe hoặc dịch vụ chọn không tồn tại trong hệ thống.
- Xe được chọn không thuộc sở hữu của khách hàng đang đặt lịch.
- Khoảng cách ngày đặt vượt quá giới hạn Booking window quy định cho hạng thành viên hiện tại của khách.
- Ngày và buổi đặt lịch đã đầy hoàn toàn sức chứa (không còn chỗ trong cả slot chính lẫn hàng chờ phụ).

### 8.3 Luồng xử lý buổi hiện tại
1. Admin thiết lập ngày hiện tại và buổi hiện tại.
2. Admin kích hoạt buổi hiện tại (chỉ được thực hiện 1 lần duy nhất cho buổi đó).
3. Hệ thống sắp xếp các booking tương lai của buổi đó theo độ ưu tiên, phân bổ vào hàng chờ chính (FIFO Queue) và hàng chờ phụ (Priority Queue).
4. Nhân viên xem hàng chờ chính để biết thứ tự xe cần rửa.
5. Nhân viên chọn xử lý xe tiếp theo: Hệ thống dequeue xe đầu tiên, chuyển trạng thái sang `SERVING`.
6. Khách hàng thanh toán: chuyển trạng thái thanh toán sang `PAID`.
7. Nhân viên bấm hoàn tất: chuyển trạng thái booking sang `COMPLETED`, cập nhật lịch sử, cập nhật loyalty của khách hàng, lưu file.
8. Hệ thống kiểm tra thời gian còn lại của buổi và tự động kéo thêm booking từ hàng chờ phụ lên cuối hàng chờ chính nếu đủ thời gian phục vụ.

### 8.4 Luồng kích hoạt buổi tương lai
1. Admin điều chỉnh thời gian mô phỏng (`currentDate` và `currentPeriod`) sang mốc thời gian mới.
2. Admin chọn kích hoạt buổi phục vụ (Activate service period).
3. Hệ thống kiểm tra trong `periods.txt` xem buổi này đã từng kích hoạt chưa. Nếu rồi thì báo lỗi và dừng.
4. Hệ thống quét tất cả các booking trong bộ nhớ đã được đặt trước cho ngày và buổi này.
5. Thực hiện sắp xếp các booking này qua Max Heap (Priority Queue) dựa trên hạng thành viên và thời gian đặt lịch.
6. Lấy lần lượt các booking từ Max Heap đổ vào hàng chờ chính (MyQueue) và hàng chờ phụ (MyPriorityQueue) của buổi hiện tại theo giới hạn slot.
7. Đổi trạng thái buổi thành đã kích hoạt và ghi đè trạng thái vào file `periods.txt`.

### 8.5 Luồng hủy booking
1. Người dùng chọn chức năng hủy và nhập mã booking.
2. Hệ thống kiểm tra trạng thái booking có phải là `WAITING` hoặc `SERVING` hay không. Nếu đã `COMPLETED` hoặc đã `CANCELLED` thì báo lỗi.
3. Chuyển trạng thái booking sang `CANCELLED`.
4. Nếu booking bị hủy nằm trong slot chính của buổi hiện tại:
   - Hệ thống tự động truy xuất hàng chờ phụ (Waitlist Priority Queue).
   - Nếu hàng chờ phụ không trống, lấy ra booking có độ ưu tiên cao nhất và đưa vào cuối hàng chờ chính (FIFO Queue).
5. Lưu dữ liệu mới xuống file `bookings.txt`.

### 8.6 Luồng hoàn tất booking
1. Admin chọn chức năng hoàn tất cho booking đang ở trạng thái `SERVING`.
2. Hệ thống kiểm tra xem booking đó đã được thanh toán (`paymentStatus == PAID`) chưa. Nếu chưa, yêu cầu thanh toán trước.
3. Chuyển trạng thái booking sang `COMPLETED`.
4. Cộng 1 vào số lần rửa xe, cộng giá trị dịch vụ vào tổng chi tiêu và tích lũy điểm loyalty cho khách hàng.
5. Xét điều kiện để nâng hạng thành viên cho khách hàng lên mức cao nhất có thể đạt được.
6. Thêm bản ghi hoàn tất vào danh sách lịch sử và ghi file `history.txt`.
7. Đẩy thông tin booking này vào Stack hoàn tất gần nhất.
8. Kiểm tra thời gian còn lại của buổi hiện tại để quyết định có bổ sung xe từ Waitlist lên hàng chờ chính hay không.
9. Ghi nhận toàn bộ thay đổi xuống file.

### 8.7 Luồng cập nhật loyalty
1. Khi booking của một khách hàng chuyển sang trạng thái `COMPLETED` thành công:
   - Tính điểm loyalty mới: $\Delta P = \text{giá dịch vụ} / 1000$.
   - Cập nhật thông tin khách hàng:
     - $\text{visitCount} = \text{visitCount} + 1$
     - $\text{totalSpent} = \text{totalSpent} + \text{giá dịch vụ}$
     - $\text{loyaltyPoints} = \text{loyaltyPoints} + \Delta P$
2. Đối chiếu các chỉ số mới (`visitCount`, `totalSpent`) với điều kiện nâng hạng:
   - Platinum: $\text{visitCount} \ge 30$ hoặc $\text{totalSpent} \ge 15,000,000$ VND.
   - Gold: $\text{visitCount} \ge 15$ hoặc $\text{totalSpent} \ge 6,000,000$ VND.
   - Silver: $\text{visitCount} \ge 5$ hoặc $\text{totalSpent} \ge 2,000,000$ VND.
   - Member: Các trường hợp còn lại.
3. Cập nhật hạng thành viên (`tier`) mới nhất của khách hàng và lưu file `customers.txt`.

### 8.8 Luồng hoàn tác booking hoàn tất
1. Admin chọn chức năng hoàn tác booking hoàn tất gần nhất.
2. Hệ thống kiểm tra Stack hoàn tác có trống hay không. Nếu trống, báo lỗi.
3. Thực hiện `pop` lấy booking hoàn tất gần nhất ra khỏi Stack.
4. Thay đổi trạng thái booking này từ `COMPLETED` quay lại `SERVING`.
5. Truy xuất khách hàng sở hữu booking:
     - Giảm `visitCount` đi 1.
     - Trừ `totalSpent` đi một lượng bằng giá dịch vụ của booking.
     - Trừ `loyaltyPoints` đi một lượng bằng số điểm đã tích lũy từ booking đó.
6. Xét lại hạng thành viên của khách hàng dựa trên các chỉ số sau khi đã giảm trừ.
7. Xóa dòng lịch sử tương ứng của booking này khỏi danh sách lịch sử rửa xe.
8. Lưu lại toàn bộ dữ liệu khách hàng, booking, lịch sử mới xuống file.

---

## 9. Demo flow đề xuất

### Demo 1 — Booking theo hạng thành viên
- **Mục tiêu**: Chứng minh thuật toán sắp xếp độ ưu tiên theo hạng thành viên hoạt động chính xác khi kích hoạt buổi tương lai.
- **Kịch bản thực hiện**:
  1. Tạo 4 khách hàng mẫu với các hạng thành viên khác nhau: C001 (Member), C002 (Silver), C003 (Gold), C004 (Platinum).
  2. Tạo 4 booking đặt trước cho cùng một buổi tương lai (ví dụ: ngày 2026-06-26, buổi MORNING).
  3. Cài đặt thời gian hệ thống sang ngày 2026-06-26, buổi MORNING.
  4. Thực hiện kích hoạt buổi (Activate service period).
  5. Xem hàng chờ chính của buổi hiện tại.
  6. *Kết quả kiểm chứng*: Khách hàng C004 (Platinum) phải đứng đầu hàng chờ chính, tiếp theo là C003 (Gold), C002 (Silver), và cuối cùng là C001 (Member), dù thứ tự nhập booking ban đầu có thể khác.

### Demo 2 — Booking trong buổi hiện tại
- **Mục tiêu**: Chứng minh quy tắc xếp nốt khách đặt ngay vào buổi đang hoạt động.
- **Kịch bản thực hiện**:
  1. Thiết lập hệ thống đang ở ngày hiện tại, buổi hiện tại đã được kích hoạt.
  2. Xem số slot chính đang trống. Giả sử slot chính còn chỗ.
  3. Tạo một booking mới cho chính buổi hiện tại.
  4. *Kết quả kiểm chứng*: Booking mới được chấp nhận ngay và xếp vào cuối hàng chờ chính (FIFO Queue).
  5. Tiếp tục tạo booking cho đến khi đầy slot chính và bắt đầu tràn sang hàng chờ phụ. Xem danh sách hàng chờ phụ để thấy các booking mới này được đưa vào waitlist.

### Demo 3 — Hủy booking và bổ sung từ hàng chờ phụ
- **Mục tiêu**: Chứng minh tính năng tự động điều phối, kéo xe từ hàng chờ phụ lên slot chính khi xuất hiện chỗ trống do hủy lịch.
- **Kịch bản thực hiện**:
  1. Tạo kịch bản buổi hiện tại có hàng chờ chính đã đầy và có ít nhất 2 khách đang nằm trong hàng chờ phụ (Waitlist) với hạng thành viên khác nhau (ví dụ: 1 Silver đặt trước và 1 Gold đặt sau).
  2. Chọn hủy một booking bất kỳ đang nằm trong hàng chờ chính.
  3. *Kết quả kiểm chứng*: Booking trong hàng chờ chính chuyển sang `CANCELLED`. Hệ thống tự động quét Waitlist, chọn khách hàng hạng Gold (ưu tiên cao hơn) đẩy vào vị trí cuối cùng của hàng chờ chính. Danh sách hàng chờ phụ giảm đi 1 phần tử.

### Demo 4 — Hoàn tất booking và cập nhật loyalty
- **Mục tiêu**: Chứng minh luồng phục vụ khép kín từ lúc xe đang rửa cho đến khi hoàn tất và nâng hạng thành viên.
- **Kịch bản thực hiện**:
  1. Chọn xử lý booking tiếp theo để đưa 1 xe lên trạng thái `SERVING`.
  2. Xác nhận thanh toán cho xe đó (`paymentStatus = PAID`).
  3. Chọn hoàn tất booking.
  4. Kiểm tra thông tin hồ sơ khách hàng.
  5. *Kết quả kiểm chứng*: Trạng thái booking chuyển sang `COMPLETED`. Số lần rửa xe, chi tiêu tích lũy và điểm của khách hàng được tăng lên. Nếu đạt đủ điều kiện, hạng thành viên của khách hiển thị sẽ tự động được nâng lên hạng mới (ví dụ từ Member lên Silver).

### Demo 5 — Lịch sử và hoàn tác
- **Mục tiêu**: Chứng minh tính năng lưu trữ lịch sử và tính năng Undo sửa sai của nhân viên hoạt động nhất quán.
- **Kịch bản thực hiện**:
  1. Thực hiện hoàn tất thành công booking của khách hàng A (được nâng từ Member lên Silver).
  2. Xem danh sách lịch sử rửa xe để xác nhận có bản ghi của khách hàng A.
  3. Chọn chức năng hoàn tác booking hoàn tất gần nhất (Undo).
  4. *Kết quả kiểm chứng*: Bản ghi của khách hàng A biến mất khỏi lịch sử rửa xe. Trạng thái booking của khách hàng A quay trở lại `SERVING`. Xem hồ sơ của khách hàng A thấy các chỉ số chi tiêu, điểm bị giảm về mức cũ và hạng thành viên bị hạ ngược lại từ Silver về Member.

---

## 10. Phụ lục

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
