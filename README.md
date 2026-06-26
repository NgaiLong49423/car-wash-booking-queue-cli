# Car Wash Booking Queue CLI

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)

> Ghi chú: Danh sách badge trên thể hiện các công nghệ và công cụ chính đang được sử dụng trực tiếp trong dự án này.

## 1. Giới Thiệu

**Car Wash Booking Queue CLI** là một ứng dụng giao diện dòng lệnh (**CLI - Command Line Interface**) chạy trên terminal mô phỏng hoạt động đặt lịch và điều phối xếp hàng rửa xe trong một tiệm rửa xe chuyên nghiệp.

Dự án này phục vụ mục tiêu học tập môn **CSD201 - Cấu trúc dữ liệu và giải thuật** và được phát triển trong bối cảnh đồ án/bài tập lớn thực hành môn học. Trọng tâm của hệ thống là việc tự định nghĩa và cài đặt các cấu trúc dữ liệu cốt lõi (**Queue, Priority Queue, Stack, LinkedList, Map**) cùng các thuật toán tìm kiếm và sắp xếp cơ bản để giải quyết bài toán nghiệp vụ mà không cần sử dụng đến các thư viện ngoài hay hệ quản trị cơ sở dữ liệu (DBMS).

## 2. Mục Tiêu Dự Án

Hệ thống cần đạt được các mục tiêu cụ thể sau:
- **Quản lý hàng chờ thông minh**: Điều phối lịch đặt rửa xe có giới hạn sức chứa theo từng buổi phục vụ (Sáng, Chiều, Tối).
- **Phân loại booking**: Tách biệt rõ ràng giữa các booking đặt ngay cho buổi hiện tại và các booking đặt trước cho các buổi trong tương lai.
- **Cơ chế ưu tiên nâng cao**: Ưu tiên xếp chỗ và phục vụ cho các khách hàng có hạng thành viên cao hơn (`Platinum` > `Gold` > `Silver` > `Member`).
- **Tự động hóa Loyalty**: Tự động tính toán tích lũy chi tiêu, số lần rửa, điểm tích lũy và xét duyệt nâng/hạ hạng thành viên ngay sau khi hoàn tất dịch vụ.
- **Hỗ trợ hoàn tác (Undo)**: Cho phép nhân viên hoàn tác nhanh booking hoàn tất gần nhất trong trường hợp thao tác nhầm lẫn.
- **Tối ưu lưu trữ dữ liệu**: Tải và lưu dữ liệu thông qua các tệp văn bản phẳng (flat text files) dùng dấu phân tách `|` để đảm bảo tính gọn nhẹ và toàn vẹn dữ liệu.
- **Đạt yêu cầu môn học CSD201**: Rèn luyện kỹ năng cài đặt cấu trúc dữ liệu thuần bằng Java trong bộ nhớ RAM và tối ưu hóa giải thuật.

## 3. Chức Năng Chính

Hệ thống cung cấp các tính năng được phân cấp theo hai vai trò (Actor) chính:

### 3.1 Khách hàng (Customer)
*Khi đăng nhập tượng trưng bằng mã khách hàng hợp lệ:*
- [x] **Xem danh sách dịch vụ**: Hiển thị các gói dịch vụ rửa xe hiện có kèm đơn giá và thời gian dự kiến.
- [x] **Xem thông tin cá nhân**: Kiểm tra hạng thành viên hiện tại, điểm tích lũy, tổng chi tiêu và số lần đã rửa xe.
- [x] **Tạo booking**: Đặt lịch rửa xe cho chính mình (chọn xe, dịch vụ, ngày và buổi cụ thể) tuân theo giới hạn booking window của hạng thành viên.
- [x] **Xem booking cá nhân**: Xem danh sách các lịch đặt của mình đang ở trạng thái chờ (`WAITING`) hoặc đang thực hiện (`SERVING`).
- [x] **Hủy booking**: Hủy lịch đặt của chính mình khi vẫn còn ở trạng thái `WAITING`.
- [x] **Xem lịch sử rửa xe**: Xem danh sách các booking đã hoàn tất (`COMPLETED`) trong quá khứ.

### 3.2 Admin / Nhân viên vận hành
*Truy cập trực tiếp để quản lý và vận hành tiệm:*
- [x] **Quản lý khách hàng**: Xem danh sách, tìm kiếm, thêm mới, cập nhật thông tin và xóa khách hàng (chỉ cho phép xóa khi không có dữ liệu liên kết).
- [x] **Quản lý xe**: Liên kết các phương tiện với tài khoản khách hàng, tìm kiếm và xem danh sách xe.
- [x] **Quản lý dịch vụ**: Thêm mới, chỉnh sửa thông tin dịch vụ, sắp xếp danh sách dịch vụ theo đơn giá hoặc thời gian thực hiện.
- [x] **Thiết lập ngày và buổi hiện tại**: Thay đổi ngày/buổi mô phỏng hệ thống để kiểm thử nghiệp vụ.
- [x] **Tạo booking cho khách**: Tạo lịch đặt rửa xe cho bất kỳ khách hàng hợp lệ nào trong hệ thống.
- [x] **Kích hoạt buổi rửa xe**: Duyệt toàn bộ booking tương lai của buổi đó, tự động xếp vào hàng chờ chính (Main slot) hoặc hàng chờ phụ (Waitlist) dựa trên độ ưu tiên.
- [x] **Xem hàng chờ / booking**: Hiển thị trực quan danh sách xe trong hàng chờ chính, hàng chờ phụ và các booking tương lai.
- [x] **Xử lý booking tiếp theo**: Chuyển đổi trạng thái xe ở đầu hàng chờ chính sang `SERVING` (hệ thống chỉ phục vụ tối đa 1 xe tại một thời điểm).
- [x] **Xác nhận thanh toán**: Mô phỏng quá trình thanh toán trước khi hoàn tất dịch vụ.
- [x] **Hoàn tất booking**: Đổi trạng thái sang `COMPLETED`, lưu lịch sử và tự động cập nhật loyalty của khách hàng.
- [x] **Hủy booking bất kỳ**: Hủy booking đang ở trạng thái `WAITING` hoặc `SERVING` (hủy `SERVING` hoặc `WAITING` trong slot chính sẽ tự động đẩy booking ưu tiên nhất từ Waitlist lên thay thế).
- [x] **Xem lịch sử hệ thống**: Đối soát toàn bộ các giao dịch đã hoàn tất.
- [x] **Hoàn tác (Undo)**: Hoàn tác booking vừa hoàn tất gần nhất, khôi phục lại trạng thái cũ của booking và điểm loyalty của khách hàng tương ứng.

## 4. Công Nghệ Sử Dụng

Dự án được phát triển hoàn toàn bằng Java thuần hướng đối tượng, tập trung vào cấu trúc dữ liệu tự định nghĩa:

- **Ngôn ngữ lập trình**: Java (JDK 8)
- **Kiến trúc**: Monolithic CLI Application (Ứng dụng Console chạy trực tiếp trên terminal)
- **Lưu trữ dữ liệu (Database)**: Không sử dụng DBMS. Dữ liệu được lưu trong các file văn bản phẳng (`.txt`) ở thư mục `data/` với định dạng ngăn cách các trường bằng dấu `|`.
- **Cấu trúc dữ liệu tự định nghĩa (RAM)**:
  - `MyLinkedList`: Cài đặt danh sách liên kết đơn tự viết để quản lý Khách hàng, Xe, Dịch vụ và Lịch sử.
  - `MyQueue`: Hàng chờ chính phục vụ theo nguyên lý FIFO (First In First Out).
  - `MyPriorityQueue`: Hàng chờ phụ (Waitlist) và xếp lịch tương lai sử dụng cấu trúc **Max Heap** tự cài đặt (ưu tiên theo hạng thành viên và thời gian tạo).
  - `MyStack`: Quản lý các booking hoàn tất phục vụ cho tính năng hoàn tác (Undo) theo nguyên lý LIFO (Last In First Out).
  - `MyMap` hoặc mảng cấu trúc tra cứu: Lưu cấu hình Booking window để tra cứu nhanh giới hạn ngày đặt trước theo hạng thành viên.
- **Thuật toán tự viết**:
  - **Tìm kiếm**: Tìm kiếm tuyến tính (Linear Search) trên LinkedList.
  - **Sắp xếp**: Thuật toán sắp xếp chọn (Selection Sort) hoặc sắp xếp chèn (Insertion Sort) hiển thị danh sách dịch vụ.
- **IDE chính**: NetBeans IDE

## 5. Cấu Trúc Thư Mục

Dưới đây là cấu trúc tổ chức mã nguồn và tài liệu của repository:

```text
.
├── .agent/            # Cấu hình tác vụ tự động
├── .github/           # Mẫu Issue và Pull Request của GitHub
├── App/               # Thư mục mã nguồn chính Java của dự án
│   ├── model/         # Chứa các lớp thực thể (Customer, Vehicle, Service, Booking...)
│   ├── service/       # Xử lý quy tắc nghiệp vụ (booking, queue, loyalty, undo...)
│   ├── repository/    # Xử lý đọc/ghi file phẳng trong thư mục data/
│   ├── util/          # Chứa các cấu trúc dữ liệu tự định nghĩa (MyLinkedList, MyQueue, MyStack, MyPriorityQueue...)
│   ├── main/          # Chứa class khởi chạy ứng dụng và điều hướng Menu CLI
│   └── README.md      # Hướng dẫn chi tiết thư mục App
├── data/              # Thư mục chứa các file văn bản phẳng lưu dữ liệu (được tự động sinh khi chạy)
│   ├── customers.txt  # Dữ liệu khách hàng và loyalty
│   ├── vehicles.txt   # Dữ liệu phương tiện
│   ├── services.txt   # Dữ liệu dịch vụ
│   ├── bookings.txt   # Dữ liệu booking hiện tại
│   ├── periods.txt    # Trạng thái kích hoạt của các buổi
│   └── history.txt    # Lịch sử dịch vụ hoàn tất
├── docs/              # Tài liệu phân tích và đặc tả dự án
│   ├── architecture/
│   │   └── TECH_STACK.md # Quyết định kỹ thuật và kiến trúc của dự án
│   ├── requirements/
│   │   ├── PRD.md     # Tài liệu yêu cầu sản phẩm (Product Requirements Document)
│   │   └── SRS.md     # Đặc tả yêu cầu phần mềm (Software Requirements Specification)
│   └── README.md      # Hướng dẫn chi tiết thư mục docs
├── CHANGELOG.md       # Nhật ký thay đổi và lịch sử cập nhật phiên bản
├── CONTRIBUTING.md    # Hướng dẫn đóng góp mã nguồn và chuẩn Git
├── LICENSE            # Giấy phép mã nguồn mở MIT
└── README.md          # File giới thiệu dự án này
```

## 6. Tài Liệu Dự Án

Các tài liệu đặc tả quan trọng liên quan đến dự án nằm trong thư mục `docs/`:
- **Yêu cầu hệ thống**: Đặc tả chi tiết hành vi và quy tắc nghiệp vụ tại [SRS.md](docs/requirements/SRS.md) (đây là tài liệu nguồn gốc đáng tin cậy nhất) và tóm tắt sản phẩm tại [PRD.md](docs/requirements/PRD.md).
- **Quyết định kỹ thuật**: Tham khảo chi tiết tại [TECH_STACK.md](docs/architecture/TECH_STACK.md) để hiểu lý do lựa chọn kiến trúc CLI đơn giản và các ràng buộc công nghệ.
- **Sơ đồ ERD & Use Case**: Đang cập nhật.

## 7. Database / Lưu Trữ Dữ Liệu

Dự án không kết nối với bất kỳ hệ quản trị cơ sở dữ liệu nào. Dữ liệu được lưu trữ lâu dài trong thư mục `data/` dưới dạng các file phẳng:
- Tải dữ liệu: Khi ứng dụng khởi động, toàn bộ dữ liệu từ 6 file văn bản phẳng trong thư mục `data/` được nạp vào bộ nhớ RAM và dựng lại thành các cấu trúc dữ liệu tự định nghĩa (`MyLinkedList`, `MyQueue`, `MyPriorityQueue`, `MyStack`).
- Lưu trữ dữ liệu: Để tránh mất mát thông tin, dữ liệu trong RAM được tự động tuần tự hóa và ghi đè trực tiếp xuống các file `.txt` tương ứng ngay sau khi kết thúc một thao tác làm thay đổi trạng thái dữ liệu (thêm khách hàng, tạo booking, hoàn tất dịch vụ, hủy, undo...).
- Khởi tạo dữ liệu mẫu (Seed Data): Nếu thư mục `data/` trống hoặc chưa tồn tại các file dữ liệu, hệ thống sẽ tự động tạo các file và chèn dữ liệu mẫu chuẩn hóa để hệ thống sẵn sàng demo ngay lập tức.

## 8. Cách Chạy Dự Án

Để chạy dự án trên máy tính cá nhân, hãy thực hiện theo các bước hướng dẫn sau:

1. **Chuẩn bị môi trường**:
   - Cài đặt Java SE Development Kit (JDK 8 trở lên).
   - Cài đặt NetBeans IDE.

2. **Tải mã nguồn về máy**:
   - Sử dụng lệnh `git clone` để tải repository:
     ```bash
     git clone https://github.com/NgaiLong49423/autowash-priority-booking-engine.git
     ```

3. **Mở dự án trong NetBeans**:
   - Khởi động NetBeans IDE.
   - Chọn **File** -> **Open Project...** từ menu chính.
   - Duyệt đến thư mục chứa mã nguồn (thư mục `App` trong thư mục repo vừa clone) và nhấn **Open Project**.

4. **Chạy ứng dụng**:
   - Nhấp chuột phải vào tên dự án trong tab *Projects* và chọn **Run** (hoặc nhấn `F6`).
   - Ứng dụng CLI sẽ hiển thị giao diện menu chính trên Console/Terminal của NetBeans.
   - Làm theo các hướng dẫn nhập liệu trên Console để trải nghiệm các tính năng của Khách hàng hoặc Admin.

## 9. Quy Trình Làm Việc Với GitHub

Để đảm bảo lịch sử git và chất lượng mã nguồn luôn sạch sẽ, các thành viên tham gia phát triển cần tuân thủ quy trình sau:
1. **Tạo Issue**: Tạo issue trên GitHub mô tả công việc (bug fix, new feature, docs...) để theo dõi tiến độ.
2. **Tạo Branch**: Tạo branch mới từ branch chính (`main`) với tên chuẩn hóa theo quy ước tại [CONTRIBUTING.md](CONTRIBUTING.md) (ví dụ: `feature/booking-queue`).
3. **Phát triển và Commit**: Thực hiện sửa đổi trên branch riêng và commit mã nguồn tuân thủ Conventional Commits.
4. **Tạo Pull Request (PR)**: Đẩy branch lên GitHub và tạo Pull Request gộp vào `main`.
5. **Kiểm tra và Merge**: Đánh giá code, kiểm thử cục bộ và tiến hành merge branch sau khi được phê duyệt.

## 10. Quy Tắc Commit

Dự án áp dụng quy chuẩn **Conventional Commits** để ghi nhận lịch sử thay đổi một cách rõ ràng và chuyên nghiệp:

Cú pháp chuẩn:
```text
<type>(<scope>): <description>
```

Các tiền tố `<type>` được chấp nhận bao gồm:
- `feat`: Tính năng mới (ví dụ: `feat(booking): thêm kiểm tra booking window`)
- `fix`: Sửa lỗi (ví dụ: `fix(queue): sửa lỗi tràn chỉ số trong heap`)
- `docs`: Tài liệu (ví dụ: `docs: cập nhật README và hướng dẫn chạy`)
- `refactor`: Tái cấu trúc mã nguồn mà không làm thay đổi hành vi hệ thống
- `test`: Thêm hoặc chỉnh sửa các bộ kiểm thử
- `chore`: Các tác vụ bảo trì, cấu hình hoặc nâng cấp công cụ

Tham khảo chi tiết quy ước tại file [CONTRIBUTING.md](CONTRIBUTING.md).

## 11. Issue Và Label

Repository được cấu hình sẵn các mẫu Issue (Issue Templates) trong thư mục `.github/ISSUE_TEMPLATE/` giúp chuẩn hóa việc báo cáo lỗi và yêu cầu tính năng:
- **Bug Report**: Dành cho việc báo cáo lỗi phát sinh trong hệ thống.
- **Feature Request**: Dành cho đề xuất phát triển thêm chức năng mới.
- **Non-Functional Requirement**: Các yêu cầu về mặt phi chức năng (hiệu năng, bảo mật, khả năng bảo trì).
- **Task**: Các đầu việc kỹ thuật hoặc bảo trì hệ thống.

Danh sách nhãn phân loại (Labels) chuẩn hóa được định nghĩa trong file `.github/labels.yml`.

## 12. Changelog

Mọi thay đổi quan trọng, cập nhật phiên bản hoặc sửa lỗi lớn của hệ thống đều được ghi chép một cách chi tiết tại file [CHANGELOG.md](CHANGELOG.md). Vui lòng cập nhật nhật ký này mỗi khi thực hiện thay đổi ảnh hưởng lớn đến ứng dụng.

## 13. License

Dự án này được phân phối dưới giấy phép mã nguồn mở MIT. Xem chi tiết quyền và nghĩa vụ sử dụng mã nguồn tại file [LICENSE](LICENSE).

## 14. Thành Viên / Tác Giả

| Họ tên | Vai trò | GitHub |
|---|---|---|
| Đang cập nhật | Đang cập nhật | [TODO] |

## 15. Ghi Chú

Tài liệu `README.md` này mô tả hiện trạng kỹ thuật và yêu cầu thực tế của dự án **Car Wash Booking Queue CLI**. Khi có bất kỳ cập nhật nào về cấu trúc thư mục của source code hoặc thay đổi về cách chạy/cấu hình dự án, vui lòng cập nhật lại tài liệu này để các thành viên khác trong nhóm và người chấm điểm có thể nắm bắt thông tin chính xác nhất.
