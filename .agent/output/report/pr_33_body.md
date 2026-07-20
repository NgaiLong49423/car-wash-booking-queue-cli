# Cập nhật logic tính toán thời gian còn lại (Remaining Time)

## Tóm tắt thay đổi
Pull Request này khắc phục một lỗi logic nghiêm trọng trong tài liệu đặc tả (SRS và PRD) liên quan đến việc tính toán tổng thời gian đã sử dụng (`usedMinutes`) và thời gian còn lại (`remainingMinutes`) của các buổi rửa xe (MORNING, AFTERNOON, EVENING).

Trước đây, tài liệu quy định chỉ cộng dồn thời gian của các xe có trạng thái `COMPLETED`. Điều này dẫn đến sai số vì hệ thống đánh giá sai thời gian trống thực tế, gây ra hiện tượng vượt quá tải (overbooking) do vẫn kéo thêm xe từ Waitlist lên Main Queue trong khi các xe `SERVING` (đang rửa) và `WAITING` (đang xếp hàng) vẫn đang chiếm dụng quỹ thời gian của buổi.

## Chi tiết các thay đổi
- **`docs/requirements/SRS.md`**:
  - Đồng bộ công thức ở **Mục 3.9.2** để tính toán dựa trên 3 trạng thái: `COMPLETED`, `SERVING`, và `WAITING` (chỉ xét xe trong Main Queue).
  - Sửa lại phần mô tả Use Case ở **Step 6 (Line 552)** và **Step 8 (Line 1318)** đang bị thiếu sót (trước đó chỉ ghi `COMPLETED`) để đồng nhất hoàn toàn với công thức chuẩn.
  - Cập nhật Changelog phiên bản tài liệu lên `v3.0.4`.
- **`docs/requirements/PRD.md`**:
  - Cập nhật lại công thức tính thời gian còn lại ở phần **7.9 Remaining Time**.
  - Chuẩn hóa thẻ Header Metadata ở đầu file (Version `v1.0.1`).

## Tác động nghiệp vụ (Business Impact) & Hành động tiếp theo
Việc cập nhật này yêu cầu đội ngũ Developer phải review và refactor lại các phương thức tính toán `usedMinutes` trong `CompletionService`, `CancellationService` và bất cứ module nào có luồng kéo xe từ Waitlist lên.

- [x] Tài liệu đã được Audit mâu thuẫn (Doc Sync Audit).
- [x] Cấu trúc chuẩn hóa theo quy định của repository.
