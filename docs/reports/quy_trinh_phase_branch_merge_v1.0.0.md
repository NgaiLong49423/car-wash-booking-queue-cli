# QUY TRÌNH PHASE, BRANCH VÀ MERGE THEO GITHUB ISSUE

| Thuộc tính | Giá trị |
|---|---|
| Tên tài liệu | Quy trinh phase, branch va merge the GitHub issue |
| Phiên bản | v1.0.1 |
| Ngày tạo | 2026-06-26 |
| Sửa đổi gần nhất | 2026-06-27 |
| Ngôn ngữ | Tiếng Việt |
| Trạng thái | Hoàn thiện |
---

---

## 1. Lịch sử phiên bản

| Version | Ngày | Người thực hiện | Nội dung thay đổi |
|---|---|---|---|
| v1.0.0 | 2026-06-27 | Ngô Gia Long | Tạo tài liệu quy trình phase, branch và merge theo từng GitHub Issue. |
| v1.0.1 | 2026-06-27 | Ngô Gia Long | Đồng nhất với báo cáo chia công việc; chốt #13 History Reports cho Người 2 ở toàn bộ tài liệu. |

---

## 2. Nguyên tắc chính

Dự án sẽ áp dụng quy trình:

```text
1 GitHub Issue = 1 Branch = 1 Pull Request = 1 lần merge vào main
```

Trong đó:

- **GitHub Issue** là một đầu việc cụ thể cần làm.
- **Branch** là nhánh code riêng để làm một issue, không ảnh hưởng trực tiếp đến `main`.
- **Pull Request (PR)** là yêu cầu đưa code từ branch vào `main`.
- **Merge** là thao tác gộp code đã hoàn thành vào nhánh chính.
- **main** là nhánh chính, luôn phải giữ ở trạng thái chạy được.

Quy trình này giúp nhóm tránh việc nhiều người code lẫn vào nhau, giảm conflict và dễ kiểm soát lỗi.

---

## 3. Quy tắc bắt buộc khi làm việc

### 3.1. Mỗi issue phải có branch riêng

Không làm nhiều issue trên cùng một branch.

Ví dụ đúng:

```text
feature/001-custom-data-structures
feature/002-file-storage-seed-data
feature/003-customer-management
```

Ví dụ không nên làm:

```text
feature/all-features
feature/data-and-booking
feature/final-code
```

---

### 3.2. Branch mới phải tạo từ main mới nhất

Trước khi tạo branch mới, luôn chạy:

```bash
git checkout main
git pull origin main
```

Sau đó mới tạo branch:

```bash
git checkout -b feature/001-custom-data-structures
```

Không nên tạo branch mới từ branch cũ, trừ khi nhóm đã thống nhất rõ.

---

### 3.3. Làm xong issue nào thì PR và merge issue đó

Không giữ branch quá lâu. Khi issue đã chạy được và test được, tạo Pull Request vào `main`.

Sau khi PR được merge, tất cả thành viên phải cập nhật lại `main`:

```bash
git checkout main
git pull origin main
```

Sau đó mới tiếp tục issue tiếp theo.

---

### 3.4. main phải luôn chạy được

Không merge code nếu:

- project không compile;
- `Main.java` không chạy;
- làm hỏng chức năng đã merge trước đó;
- chưa test thủ công case cơ bản;
- chưa ghi rõ cách test trong Pull Request.

**Compile** nghĩa là code Java dịch được thành chương trình chạy được, không bị lỗi cú pháp hoặc lỗi thiếu class.

---

## 4. Quy trình làm một issue

Mỗi issue sẽ đi theo vòng sau:

```text
1. Pull main mới nhất
2. Tạo branch từ main
3. Code đúng phạm vi issue
4. Test local
5. Commit
6. Push branch
7. Tạo Pull Request
8. Review
9. Merge vào main
10. Cả nhóm pull main mới nhất
11. Chuyển sang issue tiếp theo
```

Lệnh mẫu:

```bash
git checkout main
git pull origin main

git checkout -b feature/001-custom-data-structures

# code...

git add .
git commit -m "feat: implement custom data structures"
git push origin feature/001-custom-data-structures
```

Sau khi PR được merge:

```bash
git checkout main
git pull origin main
```

---

## 5. Quy ước đặt tên branch

Format chung:

```text
feature/<issue-number>-<short-name>
```

Ví dụ:

| Issue | Branch |
|---|---|
| #1 Custom Data Structures | `feature/001-custom-data-structures` |
| #2 File Storage + Seed Data | `feature/002-file-storage-seed-data` |
| #3 Customer Management | `feature/003-customer-management` |
| #4 Vehicle Management | `feature/004-vehicle-management` |
| #5 Service Management + Selection Sort | `feature/005-service-management-selection-sort` |
| #6 Simulation Time Settings | `feature/006-simulation-time-settings` |
| #7 Booking Creation and Validation | `feature/007-booking-creation-validation` |
| #8 Service Period Activation | `feature/008-service-period-activation` |
| #9 Queue View / Monitoring Views | `feature/009-queue-view-monitoring` |
| #10 Service Processing and Payment | `feature/010-service-processing-payment` |
| #11 Booking Completion + Loyalty Recalculation | `feature/011-booking-completion-loyalty` |
| #12 Booking Cancellation + Queue Promotion | `feature/012-booking-cancellation-promotion` |
| #13 History Reports | `feature/013-history-reports` |
| #14 Undo Last Completed Booking | `feature/014-undo-last-completed-booking` |
| #15 CLI Menu Integration | `feature/015-cli-menu-integration` |

---

## 6. Thứ tự phase và thứ tự merge đề xuất

Do các issue có quan hệ phụ thuộc, nhóm không nên làm và merge tùy ý. Nên merge theo thứ tự sau:

```text
Phase 0: Init project
Phase 1: #1
Phase 2: #2
Phase 3: #3, #4, #5
Phase 4: #6
Phase 5: #7
Phase 6: #8, #9
Phase 7: #10
Phase 8: #12
Phase 9: #11
Phase 10: #14
Phase 11: #13
Phase 12: #15
```

---

## 7. Chi tiết từng phase

### Phase 0 - Khởi tạo project

**Người chính:** Ngô Gia Long  
**Mục tiêu:** Tạo project Java chạy được ban đầu.

Công việc:

```text
- Tạo project AutoWashQueueCLI.
- Tạo Main.java chạy được.
- Tạo package nền nếu cần:
  autowash.cli
  autowash.model
  autowash.service
  autowash.storage
  autowash.datastructure
  autowash.util
- Tạo README.md ngắn hướng dẫn chạy project.
```

Điều kiện merge:

```text
- Project mở được trên NetBeans.
- Main.java chạy được.
- Chưa cần có logic nghiệp vụ.
```

---

### Phase 1 - Custom Data Structures

**Issue:** #1 Custom Data Structures  
**Người chính:** Ngô Gia Long  
**Branch:** `feature/001-custom-data-structures`

Mục tiêu:

```text
- Tự cài đặt các cấu trúc dữ liệu nền.
- Các issue sau có thể dùng lại.
```

Gợi ý nội dung:

```text
- CustomList<T>
- CustomQueue<T>
- CustomStack<T>
- Node<T> nếu cần
```

Điều kiện merge:

```text
- Có demo/test thủ công cơ bản.
- Main.java vẫn chạy được.
- Không dùng ArrayList, LinkedList, Queue, Stack cho logic chính nếu yêu cầu môn CSD cần tự cài.
```

---

### Phase 2 - File Storage + Seed Data

**Issue:** #2 File Storage + Seed Data  
**Người chính:** Người 2  
**Branch:** `feature/002-file-storage-seed-data`

Mục tiêu:

```text
- Có cơ chế đọc/ghi file.
- Có dữ liệu mẫu để test.
- Load dữ liệu vào custom data structures.
```

Điều kiện merge:

```text
- Đọc được file mẫu.
- Ghi được file mẫu.
- Không làm hỏng format dữ liệu.
- Main.java vẫn chạy được.
```

---

### Phase 3 - Entity Management

**Issue:** #3, #4, #5  
**Người chính:** Người 2  

Branch:

```text
feature/003-customer-management
feature/004-vehicle-management
feature/005-service-management-selection-sort
```

Thứ tự merge:

```text
1. #3 Customer Management
2. #4 Vehicle Management
3. #5 Service Management + Selection Sort
```

Mục tiêu:

```text
- Quản lý được Customer.
- Quản lý được Vehicle.
- Quản lý được Service.
- Có Selection Sort cho service nếu issue yêu cầu.
```

Điều kiện merge:

```text
- CRUD cơ bản chạy được nếu issue yêu cầu.
- Dữ liệu đọc/ghi không bị hỏng.
- Không phá storage đã merge trước đó.
```

**CRUD** là Create, Read, Update, Delete, nghĩa là thêm, xem, sửa, xóa.

---

### Phase 4 - Simulation Time Settings

**Issue:** #6 Simulation Time Settings  
**Người chính:** Ngô Gia Long  
**Branch:** `feature/006-simulation-time-settings`

Mục tiêu:

```text
- Có thời gian mô phỏng để kiểm tra booking theo ngày/giờ/ca.
```

Điều kiện merge:

```text
- Set được ngày/giờ mô phỏng.
- Lấy được ngày/giờ hiện tại của hệ thống mô phỏng.
- Không phụ thuộc booking.
```

---

### Phase 5 - Booking Creation and Validation

**Issue:** #7 Booking Creation and Validation  
**Người chính:** Người 3  
**Branch:** `feature/007-booking-creation-validation`

Mục tiêu:

```text
- Tạo booking hợp lệ.
- Validate customer, vehicle, service.
- Kiểm tra điều kiện đặt lịch.
- Đưa booking vào main queue hoặc waitlist đúng rule.
```

Điều kiện merge:

```text
- Tạo booking được.
- Validate dữ liệu đầu vào.
- Queue/waitlist hoạt động đúng rule cơ bản.
- Không làm complete/cancel/undo ở phase này.
```

**Validate** là kiểm tra dữ liệu có hợp lệ hay không.

---

### Phase 6 - Service Period Activation và Queue View

#### Phase 6.1 - Service Period Activation

**Issue:** #8 Service Period Activation  
**Người chính:** Người 3  
**Branch:** `feature/008-service-period-activation`

Điều kiện merge:

```text
- Activate được service period.
- Booking đúng ca được xử lý đúng trạng thái.
- Không phá queue/waitlist.
```

**Activate** là kích hoạt, tức chuyển một ca rửa xe sang trạng thái đang hoạt động để xử lý booking trong ca đó.

#### Phase 6.2 - Queue View / Monitoring Views

**Issue:** #9 Queue View / Monitoring Views  
**Người chính:** Ngô Gia Long  
**Branch:** `feature/009-queue-view-monitoring`

Điều kiện merge:

```text
- Xem được main queue.
- Xem được waitlist.
- Xem được booking theo ca nếu có.
- Không thay đổi logic booking.
```

---

### Phase 7 - Service Processing and Payment

**Issue:** #10 Service Processing and Payment  
**Người chính:** Người 3  
**Branch:** `feature/010-service-processing-payment`

Mục tiêu:

```text
- Xử lý booking đang được phục vụ.
- Tính tiền dịch vụ.
- Cập nhật trạng thái xử lý phù hợp.
```

Điều kiện merge:

```text
- Process được booking từ queue.
- Tính tiền đúng.
- Không cộng loyalty point nếu phần đó thuộc #11.
```

**Process** là xử lý nghiệp vụ, trong dự án này là xử lý xe đang được phục vụ.

---

### Phase 8 - Booking Cancellation + Queue Promotion

**Issue:** #12 Booking Cancellation + Queue Promotion  
**Người chính:** Người 4  
**Branch:** `feature/012-booking-cancellation-promotion`

Mục tiêu:

```text
- Hủy booking.
- Nếu có chỗ trống thì kéo booking từ waitlist lên main queue.
```

Điều kiện merge:

```text
- Cancel booking được.
- Queue promotion đúng rule.
- Không cancel booking đã completed nếu rule không cho phép.
```

**Queue promotion** là kéo booking từ waitlist lên main queue khi có chỗ trống.

---

### Phase 9 - Booking Completion + Loyalty Recalculation

**Issue:** #11 Booking Completion + Loyalty Recalculation  
**Người chính:** Người 4  
**Branch:** `feature/011-booking-completion-loyalty`

Mục tiêu:

```text
- Hoàn tất booking.
- Cộng điểm loyalty.
- Tính lại hạng khách hàng nếu đủ điều kiện.
```

Điều kiện merge:

```text
- Complete booking được.
- Cộng điểm đúng rule.
- Không cộng điểm hai lần cho cùng một booking.
- Recalculate tier đúng rule.
```

**Loyalty** là điểm khách hàng thân thiết.  
**Recalculate tier** là tính lại hạng khách hàng dựa trên điểm hiện tại.

---

### Phase 10 - Undo Last Completed Booking

**Issue:** #14 Undo Last Completed Booking  
**Người chính:** Người 4  
**Branch:** `feature/014-undo-last-completed-booking`

Mục tiêu:

```text
- Hoàn tác booking đã complete gần nhất.
- Rollback điểm và trạng thái nếu cần.
```

Điều kiện merge:

```text
- Undo được completed booking gần nhất.
- Rollback loyalty point.
- Rollback tier nếu cần.
- Không làm hỏng dữ liệu history.
```

**Rollback** là khôi phục lại trạng thái trước đó.

---

### Phase 11 - History Reports

**Issue:** #13 History Reports  
**Người chính:** Người 2  
**Branch:** `feature/013-history-reports`

Mục tiêu:

```text
- Xem báo cáo lịch sử booking đã hoàn tất.
```

Điều kiện merge:

```text
- Đọc được dữ liệu history.
- Hiển thị report đúng yêu cầu.
- Không sửa logic complete nếu không cần.
```

---

### Phase 12 - CLI Menu Integration

**Issue:** #15 CLI Menu Integration  
**Người chính:** Cả nhóm  
**Branch tổng:** `feature/015-cli-menu-integration`

Nếu muốn giảm conflict, có thể tách nhỏ:

```text
feature/015-cli-shell
feature/015-cli-data-menu
feature/015-cli-booking-menu
feature/015-cli-operation-menu
feature/015-cli-view-menu
```

Phân công:

| Phần CLI | Người phụ trách |
|---|---|
| Main Menu + khung Admin/Customer Menu | Người 4 |
| Menu customer/vehicle/service/data | Người 2 |
| Menu booking/create/activate/process | Người 3 |
| Menu queue view/simulation setting | Ngô Gia Long |
| Review flow tổng thể | Ngô Gia Long |

Điều kiện merge:

```text
- Menu chạy được.
- Gọi đúng service tương ứng.
- Không làm hỏng chức năng đã merge.
- Có test luồng chính từ đầu đến cuối.
```

**CLI** là Command Line Interface, tức giao diện dòng lệnh/console.

---

## 8. Bảng tổng hợp phase - issue - người phụ trách

| Phase | Issue | Người chính | Branch | Điều kiện merge ngắn |
|---|---|---|---|---|
| 0 | Init project | Ngô Gia Long | `main` hoặc `feature/000-init-project` | Project chạy được |
| 1 | #1 | Ngô Gia Long | `feature/001-custom-data-structures` | Data structures demo được |
| 2 | #2 | Người 2 | `feature/002-file-storage-seed-data` | File storage chạy được |
| 3 | #3 | Người 2 | `feature/003-customer-management` | Customer chạy được |
| 3 | #4 | Người 2 | `feature/004-vehicle-management` | Vehicle chạy được |
| 3 | #5 | Người 2 | `feature/005-service-management-selection-sort` | Service + sort chạy được |
| 4 | #6 | Ngô Gia Long | `feature/006-simulation-time-settings` | Simulation time chạy được |
| 5 | #7 | Người 3 | `feature/007-booking-creation-validation` | Booking tạo được |
| 6 | #8 | Người 3 | `feature/008-service-period-activation` | Activate period chạy được |
| 6 | #9 | Ngô Gia Long | `feature/009-queue-view-monitoring` | View queue chạy được |
| 7 | #10 | Người 3 | `feature/010-service-processing-payment` | Process/payment chạy được |
| 8 | #12 | Người 4 | `feature/012-booking-cancellation-promotion` | Cancel/promotion chạy được |
| 9 | #11 | Người 4 | `feature/011-booking-completion-loyalty` | Complete/loyalty chạy được |
| 10 | #14 | Người 4 | `feature/014-undo-last-completed-booking` | Undo chạy được |
| 11 | #13 | Người 2 | `feature/013-history-reports` | Report chạy được |
| 12 | #15 | Cả nhóm | `feature/015-cli-menu-integration` | CLI chạy end-to-end |

**End-to-end** nghĩa là chạy được luồng từ đầu đến cuối, ví dụ tạo khách hàng, tạo xe, tạo booking, xử lý, hoàn tất và xem history.

---

## 9. Checklist trước khi tạo Pull Request

Trước khi tạo PR, người làm issue phải tự kiểm tra:

```text
[ ] Code compile được.
[ ] Main.java chạy được.
[ ] Chức năng đúng phạm vi issue.
[ ] Không làm chức năng ngoài issue nếu chưa thống nhất.
[ ] Không làm hỏng dữ liệu mẫu.
[ ] Không sửa format file lung tung.
[ ] Đã test ít nhất 2-3 case cơ bản.
[ ] Commit message rõ nghĩa.
```

---

## 10. Nội dung Pull Request nên ghi

Mỗi Pull Request nên có nội dung:

```markdown
## Issue liên quan

Closes #<issue-number>

## Chức năng đã làm

- ...

## Cách test

- ...

## Ghi chú

- Phần chưa làm:
- Phần cần issue sau nối tiếp:
```

Ví dụ:

```markdown
## Issue liên quan

Closes #1

## Chức năng đã làm

- Implement CustomList<T>.
- Implement CustomQueue<T>.
- Implement CustomStack<T>.

## Cách test

- Chạy Main.java.
- Thêm/xóa phần tử trong CustomList.
- Enqueue/dequeue trong CustomQueue.
- Push/pop trong CustomStack.

## Ghi chú

- Chưa nối vào booking flow.
- Issue #2 có thể dùng CustomList để load dữ liệu file.
```

---

## 11. Quy tắc review và merge

Một Pull Request chỉ được merge khi:

```text
- Có ít nhất 1 người khác review.
- Người review chạy thử hoặc đọc code tối thiểu.
- Không có conflict với main.
- Không phá chức năng đã merge trước đó.
- Đúng phạm vi issue.
```

Nếu có conflict:

```text
1. Người tạo PR pull main mới nhất.
2. Resolve conflict ở branch của mình.
3. Test lại.
4. Push lại branch.
5. Nhờ review lại.
```

**Conflict** là xung đột code, xảy ra khi nhiều người sửa cùng một vùng code hoặc cùng một file theo cách Git không tự gộp được.

---

## 12. Câu đề xuất gửi cho nhóm

> Mình đề xuất nhóm làm theo quy trình: 1 GitHub Issue = 1 branch = 1 Pull Request = 1 lần merge vào main. Mỗi issue làm xong, test chạy được thì PR và merge ngay vào main, sau đó cả nhóm pull main mới nhất rồi mới làm issue tiếp theo. Như vậy main luôn là bản chạy được, giảm conflict và dễ kiểm soát lỗi. Thứ tự làm sẽ theo dependency: init project, #1, #2, #3/#4/#5, #6, #7, #8/#9, #10, #12, #11, #14, #13, cuối cùng #15 CLI integration.

---

## 13. Kết luận

Quy trình này phù hợp với nhóm làm dự án CSD vì:

- mỗi issue có phạm vi rõ;
- mỗi người làm trên branch riêng;
- main luôn giữ bản ổn định;
- dễ review;
- dễ rollback nếu merge sai;
- giảm tình trạng cuối dự án mới gom code rồi conflict nặng.

Nhóm nên ưu tiên merge theo dependency, không merge tùy ý. Issue nào là nền cho issue sau thì phải hoàn thành, test và merge trước.
