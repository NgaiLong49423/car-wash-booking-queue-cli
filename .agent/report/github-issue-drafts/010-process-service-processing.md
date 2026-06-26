# [Process][FR-15/16] Implement Service Processing and Payment Confirmation

## Tóm tắt
Triển khai nghiệp vụ chuyển đổi trạng thái phục vụ bao gồm gọi xe tiếp theo vào vị trí rửa (chuyển trạng thái booking đầu Main Queue từ `WAITING` sang `SERVING` - FR-15) và xác nhận thanh toán mô phỏng cho booking đang phục vụ (`UNPAID -> PAID` - FR-16).

## Source Trace
- PRD: docs/requirements/PRD.md (FR-15, FR-16)
- SRS/Spec: docs/requirements/SRS.md (FR-15, FR-16, UC-13, UC-14, Mục 3.10, Mục 3.14)

## Mục tiêu
Quản lý luồng chuyển đổi trạng thái rửa xe tuần tự tại tiệm và mô phỏng xác nhận giao dịch tài chính trước khi hoàn tất dịch vụ.

## Phạm vi
- Triển khai chức năng "Xử lý booking tiếp theo":
  - Kiểm tra xem hiện tại trong tiệm có booking nào đang ở trạng thái `SERVING` hay chưa. Nếu có, báo lỗi và từ chối.
  - Lấy phần tử đứng đầu Main Queue (MyQueue.dequeue) (FR-15).
  - Chuyển trạng thái booking đó từ `WAITING` sang `SERVING`.
  - Cập nhật file `bookings.txt`.
- Triển khai chức năng "Xác nhận thanh toán":
  - Tìm kiếm booking đang ở trạng thái `SERVING` trong bộ nhớ RAM.
  - Hiển thị thông tin dịch vụ, giá tiền cần thu.
  - Cho phép Admin chọn phương thức thanh toán (`CASH` hoặc `BANKING`) (FR-16).
  - Ghi nhận trạng thái thanh toán của booking thành `PAID` và phương thức thanh toán tương ứng.
  - Cập nhật file `bookings.txt`.

## Không nằm trong phạm vi
- Không xử lý thanh toán thực tế qua cổng ngân hàng.
- Không tự động kích hoạt chuyển trạng thái booking sang `COMPLETED` tại bước thanh toán này.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Tại một thời điểm chỉ có tối đa 1 booking ở trạng thái `SERVING` hoạt động.
- Chỉ cho phép xác nhận thanh toán cho booking đang ở trạng thái `SERVING`.
- Phương thức thanh toán hợp lệ là `CASH` hoặc `BANKING`.

## Grouping Justification
- **Grouped FRs**: FR-15 (Xử lý booking tiếp theo) và FR-16 (Xác nhận thanh toán booking).
- **Lý do gộp**: Đây là hai bước liên tiếp trong quy trình phục vụ xe tại trạm rửa (gọi xe vào trạm -> thanh toán dịch vụ). Việc gom nhóm giúp quản lý chặt chẽ trạng thái của "booking đang SERVING duy nhất", đảm bảo tiền điều kiện (phải SERVING và đã thanh toán PAID) được thiết lập đầy đủ trước khi thực hiện hoàn tất dịch vụ.

## Acceptance Criteria
- [ ] Chức năng gọi xe tiếp theo lấy đúng xe đứng đầu Main Queue (theo nguyên tắc vào trước ra trước), đổi trạng thái thành `SERVING`.
- [ ] Báo lỗi và không cho phép gọi xe mới nếu trong hệ thống đã có 1 xe đang ở trạng thái `SERVING`.
- [ ] Xác nhận thanh toán thành công chuyển trạng thái thanh toán của booking `SERVING` thành `PAID`.
- [ ] Từ chối xác nhận thanh toán nếu không tìm thấy xe nào đang rửa (`SERVING`).
- [ ] Sau mỗi thao tác đổi trạng thái hoặc thanh toán, ghi nhận thành công xuống file `bookings.txt` lập tức.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: S
- Story Points: 2
- Estimation Reason: Logic xử lý các trạng thái đơn giản tuần tự trên Queue và cập nhật thuộc tính đối tượng trong RAM. Rủi ro logic thấp.
- Priority: ⬆️ High (ID: 06f6c1b8)
- Priority Reason: Cần thiết để chuẩn bị cho giao dịch hoàn tất dịch vụ ở issue tiếp theo.
- Start Date: TBD
- Target Date: TBD

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🔴 priority-high

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/007-booking-creation-validation.md, .agent/report/github-issue-drafts/008-queue-period-activation.md
- Blocking: .agent/report/github-issue-drafts/011-completion-booking-completion.md, .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/service-processing`

## Ghi chú cho người thực hiện
- Khi gọi xe tiếp theo, in rõ thông tin biển số xe và tên khách hàng lên màn hình để nhân viên biết xe nào cần đánh vào tiệm rửa.
