# BÁO CÁO CHIA CÔNG VIỆC DỰ ÁN

**Dự án:** Car Wash Booking Queue CLI  
**Mục đích tài liệu:** Chia công việc công bằng cho 4 thành viên dựa trên độ nặng issue, dependency và phần việc nền đã thực hiện.  
**Ngôn ngữ:** Tiếng Việt  

---

## 1. Lịch sử phiên bản

| Version | Ngày | Người thực hiện | Nội dung thay đổi |
|---|---|---|---|
| v1.0.0 | 2026-06-27 | Ngô Gia Long | Tạo báo cáo chia công việc lần đầu, dựa trên 15 GitHub issue draft và phần việc đã thực hiện gồm phân rã SRS.md, thiết lập GitHub issue/project. |

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
| Người 1 - Ngô Gia Long | BA/PM + Foundation + View | Phân rã SRS.md, setup GitHub, #1, #6, #9 | 12 |
| Người 2 | Data & Entity | #2, #3, #4, #5, hỗ trợ seed/test data | 12 |
| Người 3 | Booking & Queue Core | #7, #8, #10, hỗ trợ nối CLI phần booking | 12 |
| Người 4 | Operation Flow | #11, #12, #14, khung CLI chính | 12 |

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

### 5.2. Người 2 - Data & Entity

#### Vai trò

Người 2 phụ trách **data layer** và các entity nền của hệ thống.

- **Data layer** là tầng xử lý dữ liệu, chịu trách nhiệm đọc, ghi, lưu trữ, cập nhật dữ liệu.
- **Entity** là thực thể dữ liệu chính của hệ thống, ví dụ Customer, Vehicle, Service.

#### Phân công

| Issue | Nội dung | SP |
|---|---|---:|
| #2 | File Storage + Seed Data | 3 |
| #3 | Customer Management | 3 |
| #4 | Vehicle Management | 2 |
| #5 | Service Management + Selection Sort | 2 |
| Hỗ trợ seed data, test dữ liệu mẫu, validate file | 2 |
| **Tổng** |  | **12** |

#### Lý do phân công

Các issue #2, #3, #4, #5 đều xoay quanh dữ liệu và thực thể nền. Giao cùng một người sẽ giúp giảm conflict, vì các chức năng này thường dùng chung file dữ liệu, format dữ liệu và logic validate.

Người này cần đảm bảo dữ liệu đủ ổn định để người 3 và người 4 có thể dùng cho booking, queue, completion, cancellation và history.

---

### 5.3. Người 3 - Booking & Queue Core

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

### 5.4. Người 4 - Operation Flow

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

Issue #13 có độ nặng 2 SP. Đây là task có thể chuyển linh hoạt cho người xong sớm.

Thứ tự ưu tiên nhận #13:

1. Người 2 nhận nếu đã xong phần data/entity sớm, vì history cần đọc dữ liệu từ `history.txt`.
2. Ngô Gia Long nhận nếu cần cân bằng thêm phần code.
3. Người 4 không nên ôm thêm nếu #11, #12, #14 chưa ổn định.

Không nên ép người 4 nhận thêm #13 ngay từ đầu, vì người 4 đã có cụm completion/cancellation/undo khá rủi ro.

### 6.2. Issue #15 - CLI Menu Integration

Issue #15 không nên giao toàn bộ cho một người. Đây là issue tích hợp hệ thống nên nên chia theo module.

| Phần CLI | Người phụ trách |
|---|---|
| Main Menu + khung Admin/Customer Menu | Người 4 |
| Menu customer/vehicle/service/data | Người 2 |
| Menu booking/create/activate/process | Người 3 |
| Menu queue view/simulation setting | Ngô Gia Long |
| Review flow tổng thể | Ngô Gia Long |

Lý do: CLI là điểm nối của toàn bộ hệ thống. Nếu một người làm hết thì người đó phải hiểu code của cả nhóm, dễ bị nghẽn và dễ phát sinh conflict khi merge.

---

## 7. Thứ tự triển khai đề xuất

### Phase 1 - Nền móng

| Thành viên | Công việc |
|---|---|
| Ngô Gia Long | #1 Custom Data Structures |
| Người 2 | Chuẩn bị model Customer/Vehicle/Service, format file |
| Người 3 | Đọc kỹ #7, chuẩn bị rule booking |
| Người 4 | Đọc kỹ #11/#12/#14, chuẩn bị status flow |

### Phase 2 - Data chạy được

| Thành viên | Công việc |
|---|---|
| Người 2 | #2, #3, #4, #5 |
| Ngô Gia Long | #6 Simulation Time Settings |
| Người 3 | Bắt đầu #7 sau khi có data cơ bản |
| Người 4 | Chuẩn bị test case completion/cancel/undo |

### Phase 3 - Booking và Queue

| Thành viên | Công việc |
|---|---|
| Người 3 | #7, #8, #10 |
| Ngô Gia Long | #9 Queue View / Monitoring Views |
| Người 4 | Bắt đầu #12 khi #7/#8 có bản chạy được |

### Phase 4 - Hoàn tất hệ thống

| Thành viên | Công việc |
|---|---|
| Người 4 | #11, #12, #14 |
| Người 2 hoặc Ngô Gia Long | #13 History Reports |
| Cả nhóm | Mỗi người nối CLI cho phần mình phụ trách |
| Ngô Gia Long | Review GitHub Project, issue status, merge/test tổng |

---

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

Tổng effort quy đổi là 48 SP. Mỗi thành viên nhận khoảng 12 SP. Ngô Gia Long không nên nhận quá nhiều code nặng vì đã chịu phần phân tích yêu cầu, phân rã issue, setup GitHub và review tích hợp. Người 3 nhận ít issue hơn nhưng có #7 là issue lớn nhất. Người 4 nhận nhóm ít issue nhưng rủi ro cao vì liên quan completion, cancellation và undo. Người 2 nhận cụm data/entity để đảm bảo nền dữ liệu ổn định cho cả nhóm.

---

## 11. Câu có thể gửi cho nhóm

> Mình chia theo Story Point chứ không chia theo số lượng issue. Tổng code là 42 SP, phần mình đã làm gồm phân rã SRS.md, tạo issue draft, setup GitHub Project/Issue và đồng bộ issue tính 6 SP, tổng là 48 SP. Nhóm có 4 người nên mỗi người khoảng 12 SP. CLI integration sẽ chia theo module, ai làm module nào thì nối menu module đó, không để một người gánh hết.

