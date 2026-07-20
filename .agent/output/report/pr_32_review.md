<!-- antigravity-pr-review -->
Đây là kết quả review tự động cho PR #32, dựa trên diff, Issue liên quan, tài liệu dự án và các kiểm tra hiện có.

### 1. Review Context
- **Pull Request**: feat(booking, service, payment): Add booking, service, payment features (#32)
- **Base branch**: `main`
- **Head branch**: `feature/007-booking-creation-validation`
- **Tài liệu đối chiếu**: `CONTRIBUTING.md`, `docs/requirements/SRS.md`, `docs/requirements/PRD.md`

### 2. Kiểm tra tiến độ (Acceptance Criteria)
| Tính năng | Status | Nhận xét |
|---|---|---|
| Triển khai BST cho Service/Booking | Met | Đã thêm `ServiceBSTree` và `BookingBSTree` |
| Complete / Cancel Booking | Not Met | Thiếu hoàn toàn logic kiểm tra thời gian dư để đẩy xe từ Waitlist lên (Waitlist Promotion) |
| Giới hạn phạm vi (Scope) | Not Met | Gộp quá nhiều tính năng (Payment, Complete, Cancel, Undo) vào chung nhánh của Booking Creation |

### 3. Phân tích lỗi (Findings)

#### [Critical] Vi phạm giới hạn phạm vi (Scope Creep)
- **File**: Nhiều file (đặc biệt là `BookingService.java`)
- **Evidence**: PR được tạo từ branch `feature/007-booking-creation-validation` nhưng lại nhồi nhét cả logic Payment, Complete, Cancel, và Undo booking.
- **Problem**: Vi phạm quy tắc **One Branch per Issue** của file `CONTRIBUTING.md`. Những tính năng này thuộc về các Issue riêng biệt (như FR-17, FR-18) và đang làm chồng chéo/conflict với các Pull Request khác (PR 28, PR 31).
- **Impact**: Khó review, dễ sinh ra conflict khi merge, và gây khó khăn cho việc rollback nếu có lỗi.
- **Recommendation**: Tách PR này ra. Branch `007` chỉ nên chứa phần Booking Creation Validation và BST implementation. Xóa bỏ các method `completeCurrentBooking`, `cancelBooking`, `undoLastCompletedBooking`, và `confirmPayment` khỏi PR này.
- **Merge blocking**: Yes

#### [High] Thiếu logic Waitlist Promotion khi Complete / Cancel
- **File**: `AutoWashQueueCLI/src/service/BookingService.java`
- **Line**: 1069-1121 (`completeCurrentBooking`), 1123-1144 (`cancelBooking`)
- **Evidence**: Không gọi bất kỳ logic nào liên quan đến `remainingMinutes` hay `waitlistQueue.poll()`.
- **Problem**: Tài liệu SRS và PRD bắt buộc hệ thống phải kiểm tra lại quỹ thời gian còn trống sau khi một xe hoàn tất hoặc bị hủy để quyết định kéo thêm xe từ Waitlist lên hàng chờ chính. Code hiện tại hoàn toàn bỏ qua việc này.
- **Impact**: Các xe nằm trong Waitlist sẽ bị kẹt vĩnh viễn không bao giờ được phục vụ. Hệ thống sai nghiệp vụ lõi.
- **Recommendation**: Nếu bạn vẫn giữ lại logic này trong nhánh (không khuyến khích), bạn bắt buộc phải bổ sung tính toán `usedMinutes`, `remainingMinutes` (theo công thức bao gồm `COMPLETED`, `SERVING`, `WAITING`) và điều phối Waitlist.
- **Merge blocking**: Yes

⚠️ **Git Workflow**
- Vui lòng revert/xóa bớt các thay đổi nằm ngoài phạm vi của issue hiện tại để tuân thủ `CONTRIBUTING.md`.

## Quyết định review
- Kết quả: YÊU CẦU THAY ĐỔI
- GitHub review state: REQUEST_CHANGES
- Lỗi chặn merge còn lại: 2
- Điều kiện tiếp theo: Tách nhỏ Scope của PR (loại bỏ code thừa) HOẶC bổ sung hoàn chỉnh logic Waitlist Promotion còn thiếu sót.
- Quyền quyết định cuối cùng: Người duy trì repository
