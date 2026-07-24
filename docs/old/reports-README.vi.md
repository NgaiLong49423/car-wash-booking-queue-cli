# Reports - Báo Cáo Dự Án

## Mục Đích

Thư mục `reports/` (Báo cáo) là nơi lưu trữ toàn bộ các biên bản họp nhóm, nhật ký làm việc cá nhân, báo cáo tiến độ định kỳ và tài liệu báo cáo tổng kết môn học. Việc này giúp:
* Lưu lại lịch sử phát triển và đóng góp của từng thành viên trong nhóm.
* Giúp giảng viên hoặc người hướng dẫn theo dõi sát sao tiến trình thực hiện dự án.
* Chuẩn bị sẵn tài liệu hoàn chỉnh để phục vụ cho việc chấm điểm và nghiệm thu dự án.

## Các Loại Báo Cáo Có Thể Lưu

Bạn có thể lưu các loại file sau trong thư mục này:
* **Báo cáo tiến độ:** Ghi nhận kết quả làm việc theo tuần hoặc theo sprint.
* **Báo cáo phân tích yêu cầu:** Tài liệu tóm tắt sau khi khảo sát nghiệp vụ.
* **Báo cáo thiết kế hệ thống:** Tóm tắt kiến trúc và quyết định công nghệ.
* **Báo cáo kiểm thử:** Nhật ký phát hiện lỗi và kết quả xác minh lỗi.
* **Báo cáo tổng kết:** Báo cáo hoàn thiện cuối cùng của môn học.
* **Biên bản họp nhóm:** Ghi chú nội dung phân công công việc sau mỗi buổi họp.

## Quy Tắc Đặt Tên File

Đặt tên các file báo cáo chính thức thống nhất theo cú pháp sau để dễ tìm kiếm và quản lý:
```text
Report_[so]_AutoWash_Priority_Booking_Engine.docx
```

Ví dụ cụ thể:
* `Report_3_AutoWash_Priority_Booking_Engine.docx`
* `Report_4_AutoWash_Priority_Booking_Engine.docx`
* `Report_5_AutoWash_Priority_Booking_Engine.docx`

---

## Mẫu Báo Cáo Ngắn

Dưới đây là cấu trúc mẫu cho một file báo cáo tiến độ ngắn hoặc biên bản làm việc:

### # [Tên Báo Cáo, ví dụ: Báo Cáo Tiến Độ Tuần 1]

#### 1. Thông Tin Chung
* **Dự án:** [Tên Dự Án]
* **Ngày thực hiện:** [yyyy-mm-dd]
* **Người thực hiện:** [Họ và tên thành viên thực hiện báo cáo]

#### 2. Nội Dung Đã Làm
* [x] Đã hoàn thành cấu trúc thư mục dự án.
* [x] Đã hoàn thành kiểm thử cấu trúc dữ liệu và luồng booking CLI.
* [ ] Đang rà soát tính nhất quán giữa SRS, PRD và báo cáo.

#### 3. Vấn Đề Gặp Phải
* Cần kiểm tra sự nhất quán giữa dữ liệu file `.txt`, quy tắc Waitlist và tài liệu yêu cầu.
* Khó khăn khi thống nhất luồng đặt lịch hẹn giữa các thành viên. (Đã họp và chốt phương án).

#### 4. Việc Cần Làm Tiếp Theo
* Bổ sung hoặc cập nhật test cho các edge case Waitlist và Undo.
* Cập nhật ảnh/sơ đồ khi thay đổi kiến trúc hoặc luồng booking.
