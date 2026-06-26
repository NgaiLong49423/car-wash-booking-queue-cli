# [Foundation][DS] Implement Custom Data Structures

## Tóm tắt
Tự cài đặt các lớp cấu trúc dữ liệu thuần bằng Java (`MyLinkedList`, `MyQueue`, `MyPriorityQueue`, `MyStack`, `MyMap`) để lưu trữ dữ liệu và vận hành hệ thống trong bộ nhớ RAM, phục vụ mục đích học tập môn CSD201.

## Source Trace
- PRD: docs/requirements/PRD.md (Mục 10)
- SRS/Spec: docs/requirements/SRS.md (Mục 7)
- FR/NFR/UC/Business Rule: NFR-01 (Tính đúng đắn), Constraints 10.4

## Mục tiêu
Cung cấp các lớp cấu trúc dữ liệu tự định nghĩa, hoàn toàn độc lập, hoạt động ổn định và chính xác theo lý thuyết giải thuật, làm nền tảng cho việc lưu trữ thực thể (Khách hàng, Xe, Dịch vụ, Booking) và điều phối hàng chờ phục vụ.

## Phạm vi
- Triển khai `MyLinkedList` (danh sách liên kết đơn) hỗ trợ thêm, duyệt tuyến tính để tìm kiếm, xóa node.
- Triển khai `MyQueue` (hàng đợi FIFO) bằng LinkedList hoặc mảng vòng để quản lý slot chính.
- Triển khai `MyPriorityQueue` sử dụng cấu trúc Max Heap để quản lý Waitlist, hỗ trợ thao tác `insert`, `poll`, `peek` và các giải thuật `heapify up`, `heapify down`.
- Triển khai `MyStack` (ngăn xếp LIFO) bằng LinkedList hoặc mảng để quản lý hoàn tác (Undo).
- Triển khai `MyMap` đơn giản hoặc mảng cấu trúc tra cứu để lấy booking window theo hạng thành viên.

## Không nằm trong phạm vi
- Không sử dụng các lớp cấu trúc dữ liệu có sẵn của Java như `java.util.LinkedList`, `java.util.Queue`, `java.util.Stack`, `java.util.PriorityQueue`, `java.util.Map`.
- Không xây dựng cơ chế tự động mở rộng mảng nâng cao nếu sử dụng linked nodes.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Priority Queue phải so sánh theo thứ tự ưu tiên: Hạng thành viên cao hơn (Platinum > Gold > Silver > Member) được ưu tiên trước. Nếu cùng hạng, booking nào được tạo trước (thời gian tạo sớm hơn) được ưu tiên trước.

## Implementation Notes
Theo SRS Mục 7, các cấu trúc dữ liệu phải tự viết hoàn toàn, sử dụng liên kết Node hoặc mảng phù hợp. Lớp `MyPriorityQueue` bắt buộc phải cài đặt Max Heap để đảm bảo hiệu năng và logic ưu tiên.

## Acceptance Criteria
- [ ] Lớp `MyLinkedList` hoạt động đúng cho việc lưu trữ, hỗ trợ duyệt qua các phần tử.
- [ ] Lớp `MyQueue` thực hiện đúng nguyên lý FIFO (vào trước ra trước).
- [ ] Lớp `MyPriorityQueue` thực hiện đúng giải thuật Max Heap, trích xuất chính xác phần tử có độ ưu tiên cao nhất theo quy tắc Hạng thành viên + Thời gian tạo.
- [ ] Lớp `MyStack` thực hiện đúng nguyên lý LIFO (vào sau ra trước).
- [ ] Lớp `MyMap` hoặc mảng tra cứu trả về đúng số ngày đặt trước tương ứng của 4 hạng thành viên.
- [ ] Tất cả cấu trúc dữ liệu không được import bất kỳ lớp collection nào từ `java.util.*` (ngoại trừ các interface so sánh nếu cần).

## Project Metadata
- Type: ⚙️ Backend (ID: 7b41a69f)
- Size: M
- Story Points: 3
- Estimation Reason: Lập trình thủ công 5 lớp cấu trúc dữ liệu chính. Phần phức tạp nhất là logic Heapify Up/Down của Max Heap trong Priority Queue để xử lý độ ưu tiên kép.
- Priority: ⬆️ High (ID: 06f6c1b8)
- Priority Reason: Đây là nền tảng cốt lõi, tất cả các chức năng nghiệp vụ và thực thể khác đều phụ thuộc trực tiếp vào các cấu trúc dữ liệu này.
- Start Date: TBD
- Target Date: TBD

## Labels
- 🛠️ Backend
- 📋 Task
- 🔴 priority-high

## Relationships
- Parent: None
- Blocked by: None
- Blocking: .agent/report/github-issue-drafts/002-persistence-file-storage.md, .agent/report/github-issue-drafts/003-entities-customer-management.md, .agent/report/github-issue-drafts/004-entities-vehicle-management.md, .agent/report/github-issue-drafts/005-entities-service-management.md, .agent/report/github-issue-drafts/006-simulation-time-settings.md, .agent/report/github-issue-drafts/007-booking-creation-validation.md, .agent/report/github-issue-drafts/008-queue-period-activation.md, .agent/report/github-issue-drafts/009-queue-view-monitoring.md, .agent/report/github-issue-drafts/010-process-service-processing.md, .agent/report/github-issue-drafts/011-completion-booking-completion.md, .agent/report/github-issue-drafts/012-cancellation-booking-cancellation.md, .agent/report/github-issue-drafts/013-history-view.md, .agent/report/github-issue-drafts/014-undo-booking-rollback.md, .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`backend/custom-data-structures`

## Ghi chú cho người thực hiện
- Chú ý viết code rõ ràng, tách biệt logic các cấu trúc dữ liệu vào package riêng (ví dụ `autowash.util` hoặc `autowash.ds`).
- Viết sẵn các phương thức unit test/main test nhỏ để kiểm tra tính đúng đắn của Max Heap trước khi tích hợp vào nghiệp vụ booking.
