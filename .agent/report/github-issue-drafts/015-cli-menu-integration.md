# [CLI][FR-26] Implement CLI Menus and System Integration

## Tóm tắt
Triển khai hệ thống giao diện dòng lệnh (CLI - Command Line Interface) bao gồm Main Menu, Customer Menu (login giả lập bằng ID, cho phép nhập sai tối đa 3 lần), Admin Menu phân cấp, và các wrapper validation đầu vào (FR-26) để khởi chạy toàn bộ hệ thống.

## Source Trace
- PRD: docs/requirements/PRD.md (FR-26)
- SRS/Spec: docs/requirements/SRS.md (FR-26, UC-01, UC-02, UC-03, UC-04, UC-05, UC-06, UC-07, UC-08, UC-09, UC-10, UC-11, UC-12, UC-13, UC-14, UC-15, UC-16, UC-17, Mục 9)

## Mục tiêu
Cung cấp cổng tương tác trực tiếp cho người vận hành và khách hàng, kết nối tất cả các thành phần logic của hệ thống lại với nhau.

## Phạm vi
- Viết class Main khởi chạy ứng dụng, nạp dữ liệu từ file lên RAM khi khởi động.
- Triển khai Main Menu với các lựa chọn:
  1. Customer Menu.
  2. Admin Menu.
  0. Exit.
- Triển khai **Customer Menu**:
  - Yêu cầu nhập mã khách hàng hợp lệ (Ví dụ: `C001`). Hệ thống kiểm tra sự tồn tại trong danh sách. Nếu sai, cho phép nhập lại tối đa 3 lần. Vượt quá 3 lần quay về Main Menu.
  - Sau khi login thành công, hiển thị Customer Menu chứa các chức năng: xem danh sách dịch vụ, xem thông tin cá nhân, tạo booking cho mình, xem booking của mình, hủy booking của mình, xem lịch sử của mình, quay lại (đăng xuất).
- Triển khai **Admin Menu**:
  - Truy cập trực tiếp không cần mật khẩu.
  - Hiển thị menu chứa các chức năng: quản lý khách hàng (menu con gồm xem, tìm kiếm, thêm, sửa, xóa), quản lý xe, quản lý dịch vụ, thiết lập ngày/buổi hiện tại, tạo booking cho khách, kích hoạt buổi, xem hàng chờ/booking theo buổi, xử lý booking tiếp theo, xác nhận thanh toán, hoàn tất booking, hủy booking, xem lịch sử, hoàn tác, quay lại.
- Triển khai các wrapper nhập liệu và kiểm tra đầu vào (validate) (Tích hợp FR-26):
  - Kiểm tra rỗng, kiểm tra số nguyên lớn hơn 0 cho dịch vụ, kiểm tra định dạng số điện thoại, biển số, định dạng ngày `YYYY-MM-DD`.
  - Hỗ trợ nhập lại tại chỗ khi có lỗi nghiệp vụ (ví dụ chọn buổi đầy cho phép chọn ngày/buổi khác ngay tại màn hình đặt lịch mà không cần nhập lại xe và dịch vụ).

## Không nằm trong phạm vi
- Không xây dựng giao diện đồ họa (GUI) hay giao diện Web/Mobile.
- Không triển khai mật khẩu thật hay phân quyền bảo mật mã hóa nâng cao.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Các chức năng trong Customer Menu chỉ hiển thị và thao tác trên dữ liệu thuộc về chính khách hàng đã đăng nhập (`currentCustomer`).
- Khi chọn Exit, giải phóng bộ nhớ và đảm bảo dữ liệu cuối đã lưu xuống ổ đĩa an toàn.

## Grouping Justification
- **Grouped FRs**: FR-26 (Kiểm tra dữ liệu đầu vào - CLI Integration phần còn lại).
- **Lý do gộp**: CLI Menu Integration kết nối toàn bộ các Use Cases (UC-01 đến UC-17) thành một vòng lặp ứng dụng hoạt động được. Các kiểm tra dữ liệu CLI đầu vào (FR-26) như Scanner input format, chống crash khi nhập sai kiểu dữ liệu là phần cốt lõi của giao diện người dùng này.

## Acceptance Criteria
- [ ] Ứng dụng khởi chạy không lỗi, in đúng thông báo nạp dữ liệu.
- [ ] Nhập sai mã khách hàng 3 lần liên tiếp tại Customer Menu sẽ tự động thoát ra Main Menu.
- [ ] Đăng nhập mã khách hàng hợp lệ hiển thị lời chào kèm tên khách hàng thân thiết.
- [ ] Tất cả các chức năng từ 1 đến 13 ở Admin Menu và 1 đến 6 ở Customer Menu được kết nối đúng tới các hàm xử lý nghiệp vụ tương ứng đã xây dựng.
- [ ] Khi nhập liệu lỗi (Ví dụ: nhập chữ vào trường yêu cầu số như giá tiền), chương trình không bị crash (văng lỗi Exception) mà hiển thị thông báo lỗi thân thiện và yêu cầu nhập lại.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: M
- Story Points: 3
- Estimation Reason: Cần xây dựng toàn bộ giao diện điều hướng menu phân cấp và xử lý nhập liệu, trôi dòng scanner, bọc các khối try-catch để ngăn crash ứng dụng.
- Priority: ⬆️ High (ID: 06f6c1b8)
- Priority Reason: Tích hợp cuối cùng kết nối toàn bộ hệ thống để bàn giao sản phẩm chạy thử.
- Start Date: TBD
- Target Date: TBD

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🔴 priority-high

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/001-foundation-custom-data-structures.md, .agent/report/github-issue-drafts/002-persistence-file-storage.md, .agent/report/github-issue-drafts/003-entities-customer-management.md, .agent/report/github-issue-drafts/004-entities-vehicle-management.md, .agent/report/github-issue-drafts/005-entities-service-management.md, .agent/report/github-issue-drafts/006-simulation-time-settings.md, .agent/report/github-issue-drafts/007-booking-creation-validation.md, .agent/report/github-issue-drafts/008-queue-period-activation.md, .agent/report/github-issue-drafts/009-queue-view-monitoring.md, .agent/report/github-issue-drafts/010-process-service-processing.md, .agent/report/github-issue-drafts/011-completion-booking-completion.md, .agent/report/github-issue-drafts/012-cancellation-booking-cancellation.md, .agent/report/github-issue-drafts/013-history-view.md, .agent/report/github-issue-drafts/014-undo-booking-rollback.md
- Blocking: None
- Security alert: None

## Suggested Branch
`feature/cli-menu-integration`

## Ghi chú cho người thực hiện
- Viết các hàm nhập liệu dùng chung như `inputString()`, `inputInt()`, `inputDouble()` bọc xử lý ngoại lệ để tái sử dụng nhiều nơi.
