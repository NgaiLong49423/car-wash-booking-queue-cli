<!-- antigravity-pr-review -->

Đây là kết quả review tự động cho PR #29, dựa trên diff mới nhất, Issue liên quan, tài liệu dự án và các kiểm tra hiện có.

## Bối cảnh review

- PR: `feat(history): implement global and customer history reports`
- Base / head: `main` ← `feature/013-history-reports`
- Issue liên kết: [#13](https://github.com/NgaiLong49423/car-wash-booking-queue-cli/issues/13)
- Đã đối chiếu: `docs/requirements/PRD.md`, `docs/requirements/SRS.md` (FR-20, UC-06, UC-16, §3.12), Issue #13 và diff mới nhất.
- CI: `build` thất bại tại commit `76868111bcff93d667c4b7f2de2d7dcc7f16f44d`.

## Đánh giá Acceptance Criteria của Issue #13

| Tiêu chí | Trạng thái | Bằng chứng |
|---|---|---|
| Customer chỉ xem lịch sử `COMPLETED` của chính mình | Có bằng chứng triển khai | Customer Portal tra Customer bằng ID đăng nhập, sau đó truyền Customer hiện tại vào hàm lọc. Chưa thể chạy xác minh do lỗi compile bên dưới. |
| Admin xem toàn bộ history từ `history.txt` | Đạt | `displayGlobalHistory(historyList)` hiển thị danh sách đã nạp. |
| Admin lọc theo mã khách hàng | Đạt | `displayCustomerHistory(historyList, custId)` lọc không phân biệt hoa/thường. |
| Hiển thị số tiền và loyalty | Đạt | Cả hai báo cáo in `Amount Paid` và `Loyalty Points`. |

✅ Đã kiểm tra

- Commit mới nhất đã xóa mock history không nhất quán trước đó; không còn lỗi dữ liệu seed.
- Báo cáo Admin thực hiện đúng luồng xem toàn bộ/lọc theo mã khách hàng của UC-16.
- Tổng doanh thu đã được bổ sung cho báo cáo toàn cục.
- Bổ sung Customer Portal đã xử lý finding trước về thiếu `currentCustomer` về mặt thiết kế.

## Findings

### [High] Customer Portal không build được do gọi sai API của `Customer`

- File: `AutoWashQueueCLI/src/app/Main.java`
- Line: 325
- Evidence: CI Java CI báo `cannot find symbol: method getCustomerId()` tại `currentCustomer.getCustomerId()`. Lớp `model.Customer` chỉ định nghĩa `getId()`.
- Problem: lời gọi `getCustomerId()` không tồn tại, nên toàn bộ ứng dụng không biên dịch được.
- Impact: Customer Portal mới thêm và toàn bộ PR không thể chạy; không thể xác minh UC-06 dù luồng đã được bổ sung.
- Recommendation: thay `currentCustomer.getCustomerId()` bằng `currentCustomer.getId()`, sau đó chạy lại Ant/CI và kiểm tra luồng Customer Portal bằng một Customer có và không có history.
- Merge blocking: Có

Nguồn yêu cầu:

- Issue #13 — Acceptance Criteria: Customer chỉ thấy lịch sử `COMPLETED` của chính mình.
- `docs/requirements/SRS.md`, UC-06: Customer Menu phải xác định `currentCustomer` và lọc history theo khách đó. Luồng bổ sung phù hợp yêu cầu này nhưng hiện chưa thể chạy vì lỗi compile.

## Quyết định review

- Kết quả: YÊU CẦU THAY ĐỔI
- GitHub review state: REQUEST_CHANGES
- Lỗi chặn merge còn lại: 1
- Điều kiện tiếp theo: Đổi lời gọi thành `currentCustomer.getId()`, để CI build xanh, rồi kiểm tra Customer Portal với dữ liệu history có/không có bản ghi.
- Quyền quyết định cuối cùng: Người duy trì repository

Vui lòng khắc phục hoặc điều chỉnh phạm vi Issue/PR, sau đó yêu cầu review lại.
