# [Booking][FR-05/06/07/08/09] Implement Booking Creation and Validation

## Tóm tắt
Triển khai nghiệp vụ tạo booking mới cho khách hàng (Customer tự đặt hoặc Admin đặt hộ) kèm theo kiểm tra chặt chẽ các ràng buộc về ngày đặt tối đa (booking window - FR-06) theo hạng thành viên, xác định buổi rửa xe (FR-07), xử lý đặt cho buổi hiện tại (FR-08) hay buổi tương lai (FR-09), tự sinh mã booking (FR-24) và kiểm tra validate dữ liệu đầu vào (FR-26).

## Source Trace
- PRD: docs/requirements/PRD.md (FR-05, FR-06, FR-07, FR-08, FR-09, FR-24, FR-26)
- SRS/Spec: docs/requirements/SRS.md (FR-05, FR-06, FR-07, FR-08, FR-09, FR-24, FR-26, UC-03, Mục 3.4, Mục 3.5, Mục 3.6)

## Mục tiêu
Đảm bảo booking được tiếp nhận đúng quy trình, thực thi công bằng các đặc quyền ưu tiên đặt trước theo tier và kiểm tra dung lượng buổi để ngăn chặn đặt lịch vượt sức chứa của tiệm.

## Phạm vi
- Tạo lớp thực thể `Booking` chứa các trường thông tin quy định tại SRS Mục 6.4: mã booking (`id`), `customerId`, `vehicleId`, `serviceId`, ngày đặt (`bookingDate`), buổi đặt (`period`), trạng thái booking (`status`), trạng thái thanh toán (`paymentStatus`), phương thức thanh toán (`paymentMethod`), thời điểm tạo booking (`createdAt`).
- Tự động sinh mã booking tăng dần theo định dạng `B001, B002...` dựa trên ID lớn nhất hiện tại (Tích hợp FR-24).
- Triển khai kiểm tra logic liên kết và tính hợp lệ: Khách hàng phải tồn tại, Xe phải tồn tại và Xe bắt buộc phải thuộc sở hữu của khách hàng đó (Tích hợp FR-26).
- Triển khai kiểm tra **Booking window**: Tính khoảng cách số ngày giữa ngày muốn đặt lịch và ngày hiện tại mô phỏng (`currentDate`). Từ chối booking nếu vượt quá giới hạn ngày đặt trước tối đa tương ứng với hạng thành viên hiện tại của khách hàng (Tích hợp FR-06).
- Triển khai phân loại buổi đặt lịch vào `MORNING`, `AFTERNOON`, hoặc `EVENING` (Tích hợp FR-07).
- Triển khai phân loại và kiểm tra sức chứa buổi:
  - Nếu buổi đặt lịch là **buổi tương lai** hoặc buổi hiện tại **chưa được kích hoạt** (Tích hợp FR-09): Kiểm tra tổng sức chứa của buổi đó (Sức chứa slot chính + Sức chứa slot chờ phụ). Nếu còn chỗ thì nhận booking ở trạng thái `WAITING`. Nếu đầy, từ chối tạo.
  - Nếu buổi đặt lịch là **buổi hiện tại đã được kích hoạt** (Tích hợp FR-08):
    - Nếu slot chính của buổi phục vụ còn chỗ trống: xếp booking vào cuối hàng chờ chính (FIFO Queue).
    - Nếu slot chính đã đầy nhưng hàng chờ phụ (Waitlist) còn chỗ trống: đưa booking vào hàng chờ phụ (Priority Queue).
    - Nếu cả hai đều đầy: từ chối booking.
- Lưu dữ liệu booking mới tạo xuống file `bookings.txt`.

## Không nằm trong phạm vi
- Issue này chỉ xử lý tạo booking, không triển khai tính năng thanh toán, hủy hay hoàn tất booking.
- Admin không được override quy tắc booking window hay sức chứa buổi của tiệm.

## Quy tắc nghiệp vụ / Yêu cầu liên quan
- Giới hạn ngày đặt trước tối đa (booking window):
  - Member: tối đa 7 ngày.
  - Silver: tối đa 10 ngày.
  - Gold: tối đa 12 ngày.
  - Platinum: tối đa 14 ngày.
- Giới hạn sức chứa cứng theo buổi:
  - MORNING: 10 slot chính, 3 slot chờ phụ (Tổng sức chứa = 13).
  - AFTERNOON: 10 slot chính, 3 slot chờ phụ (Tổng sức chứa = 13).
  - EVENING: 5 slot chính, 2 slot chờ phụ (Tổng sức chứa = 7).

## Grouping Justification
- **Grouped FRs**: FR-05 (Tạo booking), FR-06 (Kiểm tra booking window), FR-07 (Xác định buổi), FR-08 (Xử lý booking buổi hiện tại), FR-09 (Xử lý booking buổi tương lai), một phần FR-24 (Auto ID cho Booking), và một phần FR-26 (Validate thông tin đặt lịch).
- **Lý do gộp**: Quy trình tạo booking (FR-05) là một giao dịch nghiệp vụ thống nhất. Không thể tạo booking thành công nếu thiếu bước sinh mã ID (FR-24), xác thực xe/khách (FR-26), tính khoảng cách ngày đặt trước (FR-06), phân loại buổi đặt (FR-07) và kiểm tra xem buổi đó đã kích hoạt để đẩy trực tiếp vào Queue/Waitlist hay chưa (FR-08/FR-09). Việc tách nhỏ sẽ gây vỡ luồng xử lý và khó đảm bảo tính toàn vẹn dữ liệu.

## Acceptance Criteria
- [ ] Booking tạo thành công sinh mã dạng `BXXX` tăng dần bắt đầu bằng `B001`.
- [ ] Từ chối tạo booking nếu xe chọn không thuộc sở hữu của khách hàng đặt lịch.
- [ ] Từ chối tạo booking nếu ngày đặt nằm ngoài khoảng booking window của hạng thành viên khách hàng (Ví dụ: khách hạng Member cố tình đặt lịch trước 8 ngày bị từ chối).
- [ ] Booking đặt cho buổi tương lai thành công khi tổng số booking hiện tại của buổi đó nhỏ hơn giới hạn sức chứa cứng (Ví dụ: đặt cho buổi Sáng tương lai thứ 14 của Platinum thành công nếu tổng số booking của buổi đó $< 13$).
- [ ] Nếu buổi hiện tại đã kích hoạt, booking đặt ngay trong buổi được phân bổ đúng vào Main Queue (nếu slot chính còn chỗ), hoặc đưa vào Waitlist (nếu slot chính đầy và waitlist còn chỗ). Hiển thị rõ vị trí xếp chỗ cho người dùng.
- [ ] Ghi dữ liệu booking mới xuống file `bookings.txt` lập tức.

## Project Metadata
- Type: ✨ Feature (ID: 92e50fe6)
- Size: L
- Story Points: 5
- Estimation Reason: Logic đặt lịch có độ phức tạp cao, đòi hỏi tích hợp nhiều quy tắc nghiệp vụ kiểm tra chéo (Customer, Vehicles, Services, Simulation Time, Booking Window, Sức chứa buổi và trạng thái Kích hoạt). Giữ nguyên quy mô lớn để bảo trì tính nhất quán của luồng nghiệp vụ.
- Priority: ⬆️ High (ID: 06f6c1b8)
- Priority Reason: Luồng nghiệp vụ cốt lõi, là đầu vào dữ liệu cho toàn bộ các chức năng xếp hàng và điều phối phục vụ phía sau.
- Start Date: TBD
- Target Date: TBD

## Labels
- 🚀 Feature
- 🛠️ Backend
- 🔴 priority-high

## Relationships
- Parent: None
- Blocked by: .agent/report/github-issue-drafts/001-foundation-custom-data-structures.md, .agent/report/github-issue-drafts/002-persistence-file-storage.md, .agent/report/github-issue-drafts/003-entities-customer-management.md, .agent/report/github-issue-drafts/004-entities-vehicle-management.md, .agent/report/github-issue-drafts/005-entities-service-management.md, .agent/report/github-issue-drafts/006-simulation-time-settings.md
- Blocking: .agent/report/github-issue-drafts/008-queue-period-activation.md, .agent/report/github-issue-drafts/009-queue-view-monitoring.md, .agent/report/github-issue-drafts/010-process-service-processing.md, .agent/report/github-issue-drafts/012-cancellation-booking-cancellation.md, .agent/report/github-issue-drafts/015-cli-menu-integration.md
- Security alert: None

## Suggested Branch
`feature/booking-creation`

## Ghi chú cho người thực hiện
- Đảm bảo kiểm tra khoảng cách ngày chính xác bằng cách parse chuỗi ngày thành số ngày (epoch days) để trừ trực quan, tránh lỗi chênh lệch múi giờ.
- Viết thông báo phản hồi chi tiết sau khi tạo booking thành công chỉ rõ xe nằm ở "Slot chính", "Hàng chờ phụ" hay "Booking tương lai".
