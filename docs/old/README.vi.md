# Tài Liệu Dự Án - AutoWash Priority Booking Engine

## Mục Đích Thư Mục `docs/`

Thư mục `docs/` (viết tắt của documents - tài liệu) là nơi lưu trữ toàn bộ tài liệu chính thức liên quan đến dự án.
* Tài liệu giúp các thành viên trong nhóm, giảng viên hoặc người đánh giá dễ dàng hiểu được yêu cầu nghiệp vụ, kiến trúc thiết kế, sơ đồ hoạt động và báo cáo tiến độ của dự án.
* Việc lưu trữ tập trung tại đây giúp đảm bảo tài liệu luôn đi kèm với mã nguồn, tránh tình trạng thất lạc hoặc mâu thuẫn giữa tài liệu và mã nguồn thực tế.

## Cấu Trúc Thư Mục

Dưới đây là cấu trúc tổ chức các thư mục tài liệu trong dự án:

```text
docs/
├── requirements/
│   ├── PRD.md
│   └── SRS.md
│
├── architecture/
│   └── TECH_STACK.md
├── old/              # Tài liệu lịch sử, không phải nguồn hiện hành
└── reports/
    └── README.md
```

## Ý Nghĩa Các Thư Mục

* **`requirements/`** (Requirement - yêu cầu): Lưu trữ các yêu cầu của dự án, bao gồm cả yêu cầu nghiệp vụ chi tiết và đặc tả kỹ thuật.
* **`architecture/`**: Chứa quyết định kỹ thuật và cấu trúc dự án.
* **`old/`**: Chứa phiên bản tài liệu cũ để tra cứu lịch sử; không dùng làm nguồn yêu cầu hiện hành.
  * **`UseCase/`** (Sơ đồ ca sử dụng): Chứa sơ đồ mô tả sự tương tác giữa người dùng (Actor) và các chức năng hệ thống.
* **`reports/`** (Report - báo cáo): Chứa các báo cáo tiến độ, nhật ký làm việc nhóm, biên bản họp hoặc tài liệu nộp môn học.

## Quy Tắc Viết Tài Liệu

Khi cập nhật tài liệu trong thư mục này, vui lòng tuân thủ các checklist (danh sách kiểm tra) sau:
- [ ] Viết rõ ràng, ngắn gọn và dễ hiểu.
- [ ] Cập nhật tài liệu kịp thời mỗi khi thay đổi chức năng.
- [ ] Không để tài liệu mâu thuẫn với mã nguồn thực tế đang chạy.
- [ ] Nếu thay đổi định dạng file dữ liệu `.txt`, phải cập nhật SRS/PRD và hướng dẫn lưu trữ liên quan.
- [ ] Nếu thay đổi luồng xử lý hoặc nghiệp vụ, phải cập nhật sơ đồ hoạt động (Activity Diagram) tương ứng.
