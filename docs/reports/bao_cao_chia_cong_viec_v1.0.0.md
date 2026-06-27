# BÁO CÁO CHIA CÔNG VIỆC DỰ ÁN

| Thuộc tính | Giá trị |
|---|---|
| Tên tài liệu | Bao cao chia cong viec du an |
| Phiên bản | v1.0.2 |
| Ngày tạo | 2026-06-26 |
| Sửa đổi gần nhất | 2026-06-27 |
| Ngôn ngữ | Tiếng Việt |
| Trạng thái | Hoàn thiện |
---

## 1. Lịch sử phiên bản

| Version | Ngày | Người thực hiện | Nội dung thay đổi |
|---|---|---|---|
| v1.0.0 | 2026-06-27 | Ngô Gia Long | Tạo báo cáo chia công việc lần đầu, dựa trên 15 GitHub issue draft và phần việc đã thực hiện gồm phân rã SRS.md, thiết lập GitHub issue/project. |
| v1.0.1 | 2026-06-27 | Ngô Gia Long | Đồng nhất với tài liệu quy trình phase/branch/merge; chốt #13 History Reports cho Người 2; cập nhật bảng phân công và thứ tự triển khai theo checkpoint merge. |

---

## 2. Nguyên tắc chia công việc

Việc chia task không dựa đơn thuần vào số lượng issue, mà dựa trên các yếu tố sau:

1. **Story Point (SP)**: điểm ước lượng độ nặng công việc. SP không phải số dòng code, mà thể hiện độ khó, rủi ro, lượng logic nghiệp vụ và effort cần bỏ ra.
2. **Dependency**: quan hệ phụ thuộc giữa các issue. Issue nào là nền tảng hoặc bị nhiều issue khác phụ thuộc thì có rủi ro cao hơn.
3. **Tính liên quan module**: các issue cùng một cụm nghiệp vụ nên giao cùng một người để giảm conflict code và giảm thời gian hiểu lại logic.
4. **Phần việc nền đã làm**: công việc phân rã SRS.md, tạo issue draft, setup GitHub Project/Issue, đồng bộ issue lên GitHub phải được tính vào tổng effort.

---

## 3. Cơ sở tính tổng effort

Theo bộ issue draft hiện tại, hệ thống có 15 issue chính với tổng cộng:

| Nhóm công việc | SP |
|---|---:|
| Tổng SP của 15 issue code | 42 |
| Phần phân rã SRS.md, tạo issue draft, setup GitHub Issue/Project, đồng bộ GitHub | 6 |
| **Tổng effort quy đổi** | **48** |

Vì nhóm có 4 người, mức chia công bằng mục tiêu là:

> **48 SP / 4 người = 12 SP/người**

---

## 4. Bảng phân công tổng quát

| Thành viên | Vai trò chính | Công việc chính | Tổng SP quy đổi |
|---|---|---|---:|
| Người 1 - Ngô Gia Long SE190732| BA/PM + Foundation + View | Phân rã SRS.md, setup GitHub, #1, #6, #9 | 12 |
| Người 2 | Nguyễn Anh Kiệt	SE190095 | #2, #3, #4, #5, #13 | 12 |
| Người 3 | Ngô Hoàng Thái Dương	SE190177 | #7, #8, #10, hỗ trợ nối CLI phần booking | 12 |
| Người 4 | Nguyễn Tuấn Minh	SE204693 | #11, #12, #14, khung CLI chính | 12 |

---

## 5. Chi tiết phân công theo từng thành viên

### 5.1. Người 1 - Ngô Gia Long

#### Vai trò

Người 1 giữ vai trò **BA/PM + nền tảng kỹ thuật + review tích hợp**.

- **BA** là Business Analyst, tức người phân tích yêu cầu nghiệp vụ.
- **PM** là Project Manager, tức người theo dõi tiến độ, chia việc và quản lý luồng công việc.
- **Foundation** là phần nền tảng mà nhiều chức năng khác phụ thuộc vào.

#### Phần việc đã hoàn thành / đã đóng góp

| Công việc | SP quy đổi |
|---|---:|
| Phân rã SRS.md thành các GitHub issue draft | 2 |
| Thiết lập GitHub issue/project/label/milestone nếu có | 2 |
| Đồng bộ issue lên GitHub và kiểm tra trạng thái issue | 1 |
| Theo dõi dependency, relationships, review tiến độ nhóm | 1 |
| **Tổng phần việc nền** | **6** |

#### Phần code nên nhận thêm

| Issue | Nội dung | SP |
|---|---|---:|
| #1 | Custom Data Structures | 3 |
| #6 | Simulation Time Settings | 1 |
| #9 | Queue View / Monitoring Views | 2 |
| **Tổng phần code** |  | **6** |

#### Tổng effort

| Loại công việc | SP |
|---|---:|
| Phần việc nền đã làm | 6 |
| Phần code nhận thêm | 6 |
| **Tổng** | **12** |

#### Lý do phân công

Ngô Gia Long đã làm phần phân rã yêu cầu và setup GitHub, đây là phần việc quan trọng để cả nhóm có thể bắt đầu làm đúng hướng. Vì vậy không nên tiếp tục giao quá nhiều issue code nặng. Các issue #1, #6, #9 phù hợp vì vừa có tính nền tảng, vừa giúp người phụ trách nắm được tổng thể hệ thống để review tích hợp cuối.

---

### 5.2. Người 2 - Nguyễn Anh Kiệt

#### Vai trò

Người 2 phụ trách **data layer**, các entity nền và báo cáo lịch sử của hệ thống.

- **Data layer** là tầng xử lý dữ liệu, chịu trách nhiệm đọc, ghi, lưu trữ, cập nhật dữ liệu.
- **Entity** là thực thể dữ liệu chính của hệ thống, ví dụ Customer, Vehicle, Service.

#### Phân công

| Issue | Nội dung | SP |
|---|---|---:|
| #2 | File Storage + Seed Data | 3 |
| #3 | Customer Management | 3 |
| #4 | Vehicle Management | 2 |
| #5 | Service Management + Selection Sort | 2 |
| #13 | History Reports | 2 |
| **Tổng** |  | **12** |

#### Lý do phân công

Các issue #2, #3, #4, #5 đều xoay quanh dữ liệu và thực thể nền. Issue #13 cũng nên giao cho Người 2 vì History Reports cần đọc dữ liệu từ `history.txt` và phụ thuộc vào format lưu trữ đã được thiết kế từ phần file storage. Giao cùng một người sẽ giúp giảm conflict, vì các chức năng này thường dùng chung file dữ liệu, format dữ liệu và logic validate.

Người này cần đảm bảo dữ liệu đủ ổn định để người 3 và người 4 có thể dùng cho booking, queue, completion, cancellation và history.

---

### 5.3. Người 3 - Ngô Hoàng Thái Dương

#### Vai trò

Người 3 phụ trách **core business flow** của hệ thống.

- **Core business flow** là luồng nghiệp vụ lõi, tức phần quyết định hệ thống rửa xe hoạt động đúng yêu cầu hay không.
- Trong dự án này, luồng lõi gồm tạo booking, kiểm tra điều kiện đặt lịch, đưa booking vào queue/waitlist và xử lý phục vụ.

#### Phân công

| Issue | Nội dung | SP |
|---|---|---:|
| #7 | Booking Creation and Validation | 5 |
| #8 | Service Period Activation | 3 |
| #10 | Service Processing and Payment | 2 |
| Hỗ trợ nối CLI phần booking/queue/process | 2 |
| **Tổng** |  | **12** |

#### Lý do phân công

Issue #7 là issue lớn nhất và phức tạp nhất trong hệ thống vì chứa nhiều rule nghiệp vụ:

- kiểm tra ngày đặt theo hạng thành viên;
- phân biệt current period và future period;
- xử lý main queue và waitlist;
- kiểm tra capacity;
- validate customer, vehicle, service;
- tạo booking đúng trạng thái.

Issue #8 và #10 nối tiếp trực tiếp từ booking nên giao chung cho một người để giữ logic thống nhất.

---

### 5.4. Người 4 - Nguyễn Tuấn Minh

#### Vai trò

Người 4 phụ trách **operation flow**, tức các thao tác vận hành sau khi booking đã được tạo.

Các thao tác này gồm hoàn tất booking, hủy booking, rollback bằng undo và chuẩn bị khung CLI chính.

- **Rollback** là khôi phục lại trạng thái trước đó khi người dùng hoàn tác một thao tác.
- **Undo** là chức năng hoàn tác hành động gần nhất.
- **CLI** là Command Line Interface, tức giao diện dòng lệnh mà người dùng nhập lựa chọn để thao tác.

#### Phân công

| Issue | Nội dung | SP |
|---|---|---:|
| #11 | Booking Completion + Loyalty Recalculation | 5 |
| #12 | Booking Cancellation + Queue Promotion | 3 |
| #14 | Undo Last Completed Booking | 3 |
| Khung CLI chính của #15 | 1 |
| **Tổng** |  | **12** |

#### Lý do phân công

Các issue #11, #12, #14 đều liên quan đến thay đổi trạng thái booking sau khi booking đã tồn tại. Đây là nhóm công việc có rủi ro cao vì dễ ảnh hưởng đến:

- trạng thái booking;
- queue/waitlist;
- history.txt;
- loyalty point;
- tier khách hàng;
- rollback khi undo.

Vì vậy nên giao cùng một người để kiểm soát trạng thái hệ thống nhất quán hơn.

---

## 6. Xử lý riêng issue #13 và #15

### 6.1. Issue #13 - History Reports

Issue #13 có độ nặng 2 SP và được chốt giao cho Nguyễn Anh Kiệt**.

Lý do:

1. Nguyễn Anh Kiệt đang phụ trách #2 File Storage + Seed Data nên hiểu rõ format dữ liệu và cách đọc/ghi file.
2. History Reports cần đọc dữ liệu từ `history.txt`, nên liên quan trực tiếp đến data layer.
3. Ngô Gia Long không nhận #13 vì đã phụ trách phân rã SRS.md, setup GitHub, #1, #6, #9 và review tích hợp.
4. Người 4 không nên ôm thêm #13 vì đã có #11, #12, #14, là cụm completion/cancellation/undo có rủi ro cao.

Vì vậy, #13 không còn là task linh hoạt nữa mà được cố định cho Người 2 để hai tài liệu phân công và quy trình merge thống nhất với nhau.

### 6.2. Issue #15 - CLI Menu Integration

Issue #15 không nên giao toàn bộ cho một người. Đây là issue tích hợp hệ thống nên nên chia theo module.

| Phần CLI | Người phụ trách |
|---|---|
| Main Menu + khung Admin/Customer Menu | Nguyễn Tuấn Minh |
| Menu customer/vehicle/service/data | Nguyễn Anh Kiệt |
| Menu booking/create/activate/process | Ngô Hoàng Thái Dương |
| Menu queue view/simulation setting | Ngô Gia Long |

---

## 7. Thứ tự triển khai theo checkpoint merge

Phần này đồng nhất với tài liệu **Quy trình phase, branch và merge theo GitHub Issue**. Mỗi checkpoint tương ứng với một cụm issue được làm trên branch riêng, tạo Pull Request, review và merge vào `main`.

**Checkpoint** là điểm chốt kiểm tra. Làm xong checkpoint nào thì test, Pull Request và merge checkpoint đó vào `main` trước khi tiếp tục issue phụ thuộc phía sau.

| Phase | Issue | Người chính | Mục tiêu | Điều kiện merge ngắn |
|---|---|---|---|---|
| Phase 0 | Init project | Ngô Gia Long | Tạo project Java chạy được | Project mở được trên NetBeans, `Main.java` chạy được |
| Phase 1 | #1 | Ngô Gia Long | Custom Data Structures | Data structures demo được |
| Phase 2 | #2 | Nguyễn Anh Kiệt | File Storage + Seed Data | File storage đọc/ghi được |
| Phase 3 | #3, #4, #5 | Nguyễn Anh Kiệt | Customer, Vehicle, Service Management | Entity management chạy được |
| Phase 4 | #6 | Ngô Gia Long | Simulation Time Settings | Set/lấy thời gian mô phỏng được |
| Phase 5 | #7 | Ngô Hoàng Thái Dương | Booking Creation and Validation | Tạo booking và đưa vào queue/waitlist được |
| Phase 6 | #8, #9 | Ngô Hoàng Thái Dương + Ngô Gia Long | Activate Period + Queue View | Activate period và view queue chạy được |
| Phase 7 | #10 | Ngô Hoàng Thái Dương | Service Processing and Payment | Process/payment chạy được |
| Phase 8 | #12 | Nguyễn Tuấn Minh | Booking Cancellation + Queue Promotion | Cancel và kéo waitlist lên đúng rule |
| Phase 9 | #11 | Nguyễn Tuấn Minh | Booking Completion + Loyalty Recalculation | Complete và cộng điểm đúng rule |
| Phase 10 | #14 | Nguyễn Tuấn Minh | Undo Last Completed Booking | Undo/rollback chạy được |
| Phase 11 | #13 | Nguyễn Anh Kiệt | History Reports | Report đọc từ history chạy được |
| Phase 12 | #15 | Cả nhóm | CLI Menu Integration | CLI chạy được luồng end-to-end |

**End-to-end** nghĩa là chạy được luồng từ đầu đến cuối, ví dụ tạo khách hàng, tạo xe, tạo booking, xử lý, hoàn tất và xem history.

## 8. Dependency chính cần lưu ý

| Issue | Phụ thuộc chính | Ghi chú |
|---|---|---|
| #1 | Không phụ thuộc | Nền tảng cấu trúc dữ liệu cho nhiều issue khác. |
| #2 | #1 | Cần cấu trúc dữ liệu để lưu danh sách động. |
| #3, #4, #5 | #1, #2 | Cần data layer ổn định. |
| #7 | #1, #2, #3, #4, #5, #6 | Booking cần customer, vehicle, service, thời gian mô phỏng và file storage. |
| #8 | #7 | Activation dùng booking đã tạo. |
| #9 | #7, #8 | View cần dữ liệu queue/booking đã tồn tại. |
| #10 | #7, #8 | Processing cần booking trong queue/service period. |
| #11 | #10 | Completion cần booking đã được phục vụ/thanh toán. |
| #12 | #7, #8 | Cancellation cần booking/queue/waitlist. |
| #13 | #11 | History report cần dữ liệu completed/history. |
| #14 | #11 | Undo cần completion trước đó để rollback. |
| #15 | Tất cả issue chính | CLI là bước tích hợp cuối. |

---

## 9. Quy tắc làm việc GitHub đề xuất

1. Mỗi issue nên làm trên một branch riêng.
2. Branch nên đặt theo tên gợi ý trong issue draft hoặc theo format:

```text
feature/<issue-number>-short-name
```

Ví dụ:

```text
feature/007-booking-creation-validation
```

3. Mỗi Pull Request phải ghi rõ:
   - issue liên quan;
   - chức năng đã làm;
   - cách test;
   - phần chưa làm hoặc cần người khác nối tiếp.
4. Không merge khi issue dependency phía trước chưa ổn định.
5. Với #15 CLI, mỗi người tự nối phần menu cho module của mình, không để một người gánh toàn bộ.
6. Ngô Gia Long nên giữ vai trò review tổng thể vì đã nắm tài liệu SRS, issue draft và GitHub Project.

---

## 10. Kết luận

Cách chia này công bằng hơn chia đều số issue vì đã tính cả:

- độ nặng theo Story Point;
- dependency giữa các issue;
- rủi ro nghiệp vụ;
- công phân rã SRS.md và setup GitHub đã thực hiện;
- tính liền mạch giữa các module.

Tổng effort quy đổi là 48 SP. Mỗi thành viên nhận khoảng 12 SP. Ngô Gia Long không nên nhận quá nhiều code nặng vì đã chịu phần phân tích yêu cầu, phân rã issue, setup GitHub và review tích hợp. Người 3 nhận ít issue hơn nhưng có #7 là issue lớn nhất. Người 4 nhận nhóm ít issue nhưng rủi ro cao vì liên quan completion, cancellation và undo. Người 2 nhận cụm data/entity và #13 History Reports để đảm bảo phần dữ liệu, file storage và báo cáo lịch sử thống nhất.

---

## 11. Câu có thể gửi cho nhóm

> Mình chia theo Story Point chứ không chia theo số lượng issue. Tổng code là 42 SP, phần mình đã làm gồm phân rã SRS.md, tạo issue draft, setup GitHub Project/Issue và đồng bộ issue tính 6 SP, tổng là 48 SP. Nhóm có 4 người nên mỗi người khoảng 12 SP. #13 History Reports giao luôn cho Người 2 vì liên quan trực tiếp đến file storage/history. CLI integration sẽ chia theo module, ai làm module nào thì nối menu module đó, không để một người gánh hết.

