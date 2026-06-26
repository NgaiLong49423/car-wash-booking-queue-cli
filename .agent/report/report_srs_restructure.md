# Báo cáo tái cấu trúc tài liệu yêu cầu SRS

## 1. File đã tạo
- Đã tạo thành công file [SRSV2.md](file:///d:/Semester%204/CSD201/autowash-priority-booking-engine/docs/requirements/SRSV2.md) trong thư mục `docs/requirements/`.

## 2. Các phần đã tái cấu trúc
Tài liệu đã được cơ cấu lại hoàn toàn theo chuẩn cấu trúc nghiệp vụ chuyên nghiệp gần với IEEE 830, bao gồm:
1. **Giới thiệu**: Mục đích, phạm vi, đối tượng sử dụng, thuật ngữ chi tiết, tài liệu tham khảo.
2. **Mô tả tổng quan**: Bối cảnh, mục tiêu hệ thống, đặc thù ứng dụng CLI, nhóm người dùng, giả định/ràng buộc và phạm vi chi tiết.
3. **Quy tắc nghiệp vụ**: Hệ thống hóa toàn bộ 15 nhóm quy tắc nghiệp vụ cốt lõi từ việc chia buổi, hạng thành viên, booking window, quy tắc xử lý booking hiện tại/tương lai, kích hoạt buổi, xử lý hàng chờ phụ, thanh toán mô phỏng, hủy, hoàn tất, loyalty, lịch sử, hoàn tác bằng Stack, trạng thái booking và lưu trữ file.
4. **Yêu cầu chức năng**: Trình bày mạch lạc toàn bộ 26 yêu cầu chức năng (FR-01 đến FR-26) với mô tả, lý do cần có, luồng xử lý chính, điều kiện/ràng buộc và kết quả mong đợi cho từng chức năng.
5. **Yêu cầu dữ liệu và lưu trữ**: Mô tả nhóm dữ liệu quản lý, file lưu trữ (customers, vehicles, services, bookings, history, periods), định dạng file text dùng dấu `|`, vai trò của file và RAM, cơ chế sinh mã tự động và khởi tạo dữ liệu mẫu.
6. **Định hướng cấu trúc dữ liệu và giải thuật**: Gom toàn bộ định hướng khái niệm (MyLinkedList, MyQueue, MyPriorityQueue dùng Max Heap, MyStack, MyMap, Linear Search, Selection Sort, BST mở rộng) vào một chương riêng.
7. **Yêu cầu phi chức năng**: Nêu rõ 6 yêu cầu phi chức năng (dễ demo, dữ liệu đơn giản, tập trung nghiệp vụ chính, dễ kiểm tra, dễ giải thích, phù hợp phạm vi CSD).
8. **Luồng nghiệp vụ chính**: Liệt kê chi tiết các luồng đặt lịch thành công/thất bại, xử lý buổi, kích hoạt buổi, hủy, hoàn tất, loyalty, hoàn tác.
9. **Demo flow đề xuất**: Trình bày rõ 5 kịch bản demo (độ ưu tiên hạng thành viên, buổi hiện tại, hủy & kéo waitlist, hoàn tất & loyalty, lịch sử & hoàn tác).
10. **Phụ lục**: Bảng tóm tắt các quyết định đã chốt và bảng tổng hợp cấu trúc dữ liệu đề xuất cùng các điểm cần làm rõ ở bước thiết kế kỹ thuật.

## 3. Tính toàn vẹn nghiệp vụ
- Giữ nguyên vẹn 100% mọi quy tắc nghiệp vụ, chức năng, ràng buộc, slot, booking window, loyalty rule, file storage rule, cấu trúc dữ liệu đề xuất, demo flow và ghi chú từ `SRS.md` cũ.
- Tích hợp toàn bộ nội dung từ các phần "Cập nhật bổ sung" vào đúng các mục chức năng, quy tắc nghiệp vụ hoặc cấu trúc dữ liệu tương ứng trong tài liệu, loại bỏ hoàn toàn các đề mục bổ sung thô.
- Đảm bảo tính nhất quán: quy tắc hoàn tất booking kiểm tra thời gian phục vụ của waitlist; quy tắc hủy kéo booking từ waitlist lên; hoàn tác dùng Stack; thanh toán mô phỏng; lưu trữ RAM kết hợp file text phẳng dùng dấu `|`.

## 4. Điểm mâu thuẫn / Cần quyết định thêm
- Hiện tại không phát hiện điểm mâu thuẫn nghiệp vụ nào giữa các phần cũ và mới. Tài liệu đã được thống nhất theo các quyết định cuối cùng và sẵn sàng cho việc thiết kế chi tiết.
