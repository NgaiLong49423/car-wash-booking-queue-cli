# [Persistence][FR-22/23/25] Implement File Storage and Seed Data Initialization

## Tóm tắt
Triển khai logic lưu trữ dữ liệu bền vững của ứng dụng bằng các file văn bản phẳng (`.txt`) trong thư mục `data/`. Nạp dữ liệu vào RAM khi khởi động (FR-22) và tự động lưu lại sau mỗi thay đổi quan trọng (FR-23). Khởi tạo dữ liệu mẫu nếu các file trống hoặc chưa tồn tại (FR-25).

## Source Trace
- PRD: docs/requirements/PRD.md (FR-22, FR-23, FR-25)
- SRS/Spec: docs/requirements/SRS.md (FR-22, FR-23, FR-25, Mục 3.15, Mục 6.5, Mục 6.6)

## Mục tiêu
Đảm bảo thông tin của tiệm rửa xe được duy trì sau mỗi lần đóng/mở ứng dụng, tự động ghi nhận thay đổi và cung cấp sẵn dữ liệu demo khi chạy ứng dụng lần đầu.

## Phạm vi
- Đọc/ghi 6 file văn bản phẳng trong thư mục `data/`: `customers.txt`, `vehicles.txt`, `services.txt`, `bookings.txt`, `periods.txt`, `history.txt`.
- Định dạng mỗi trường dữ liệu trên một dòng phân cách bằng ký tự `|`.
- Triển khai hàm `loadAllData()` chạy lúc khởi động để nạp dữ liệu từ các file văn bản vào các cấu trúc dữ liệu tự định nghĩa (`MyLinkedList`...) trong RAM (FR-22).
- Triển khai các hàm ghi đè `save...()` để cập nhật dữ liệu xuống file ngay khi hoàn thành thao tác nghiệp vụ làm thay đổi dữ liệu trong RAM (FR-23).
- Viết logic `initializeSeedData()` tự động tạo thư mục `data/` và ghi các bản ghi mẫu (ít nhất 4 khách hàng thuộc 4 hạng khác nhau, 4 xe liên kết tương ứng, 3 dịch vụ mẫu, trạng thái buổi phục vụ, và một số booking tương lai làm mẫu) nếu file chưa tồn tại hoặc rỗng (FR-25).

## Không nằm trong phạm vi
- Không sử dụng bất kỳ DBMS nào (MySQL, SQL Server, SQLite...).
- Không mã hóa file dữ liệu, chỉ sử dụng định dạng văn bản phẳng đọc được (UTF-8).

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Sử dụng dấu phân tách đứng `|` để tránh lỗi khi tên khách hàng hoặc tên dịch vụ chứa dấu phẩy.
- Phải ghi file ngay lập tức sau các thao tác thay đổi trạng thái dữ liệu (thêm khách hàng, xe, dịch vụ, tạo booking, kích hoạt buổi, đổi trạng thái booking, thanh toán, hoàn tất, hủy, undo).

## Grouping Justification
- **Grouped FRs**: FR-22 (Đọc dữ liệu file), FR-23 (Ghi dữ liệu file) và FR-25 (Khởi tạo dữ liệu mẫu).
- **Lý do gộp**: Đọc, ghi và tạo dữ liệu mẫu là các khối chức năng tương tác trực tiếp với ổ đĩa (I/O persistence layer). Dữ liệu mẫu (FR-25) chỉ được khởi tạo khi hàm đọc file (FR-22) phát hiện file rỗng hoặc không tồn tại, và việc ghi dữ liệu mẫu xuống đĩa sử dụng chính các hàm ghi file (FR-23). Việc gom nhóm giúp xây dựng tầng lưu trữ hoàn chỉnh, nhất quán ngay từ đầu.

## Acceptance Criteria
- [ ] Khi khởi động lần đầu (thư mục `data/` chưa có hoặc trống), hệ thống tự động sinh ra 6 file với đầy đủ seed data hợp lệ.
- [ ] Dữ liệu mẫu chứa đủ 4 hạng thành viên (`MEMBER`, `SILVER`, `GOLD`, `PLATINUM`), xe liên kết, dịch vụ (`Basic Wash`, `Premium Wash`, `Interior Cleaning`).
- [ ] Dữ liệu trong các file text sử dụng định dạng phân tách bằng dấu đứng `|` (Ví dụ: `C001|Nguyen Van A|0987654321|MEMBER|0|0|0`).
- [ ] Khi người dùng thực hiện thay đổi trong app và đóng app, dữ liệu mới được lưu xuống file thành công và nạp lại đúng khi khởi động lại app.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: M
- Story Points: 3
- Estimation Reason: Cần viết parser thủ công cho 6 thực thể khác nhau với các quy tắc đọc/ghi file văn bản và xử lý lỗi vào/ra (IO).
- Priority: ⬆️ High (ID: 06f6c1b8)
- Priority Reason: Cần thiết để các issue quản lý thực thể phía sau có thể ghi và kiểm tra dữ liệu thực tế trên ổ đĩa.
- Start Date: TBD
- Target Date: TBD

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🔴 priority-high

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/001-foundation-custom-data-structures.md
- Blocking: .agent/report/github-issue-drafts/003-entities-customer-management.md, .agent/report/github-issue-drafts/004-entities-vehicle-management.md, .agent/report/github-issue-drafts/005-entities-service-management.md, .agent/report/github-issue-drafts/006-simulation-time-settings.md, .agent/report/github-issue-drafts/007-booking-creation-validation.md, .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`backend/file-persistence`

## Ghi chú cho người thực hiện
- Nên thiết kế một lớp tiện ích (Helper/Utility) để đóng gói các thao tác Parse dòng chữ thành đối tượng và ngược lại.
- Xử lý kỹ ngoại lệ `FileNotFoundException` và tự tạo file/thư mục tự động.
