# SRS



| Item         | Value          |
| ------------ | -------------- |
| Project name | Smart Car Wash |
| Version      | 0.1.1          |
| Created Dare | 14-06-2026     |
| Last Update  | 14-06-2026     |

### **1.1 Decomposition – Phân rã bài toán**

#### **1. Quản lý người dùng**

Hệ thống cần lưu và xử lý thông tin người dùng.

Bao gồm:

* Đăng nhập / đăng xuất
* Phân quyền CUSTOMER / ADMIN
* Xem thông tin cá nhân
* Lưu điểm, hạng thành viên, tổng tiền chi tiêu, tổng số lần rửa

**2. Quản lý hạng thành viên khách hàng**

| Tier     | Requirement                     |
| -------- | ------------------------------- |
| Member   | Đăng ký member + 1 lần rửa xe   |
| Silver   | 5 lần rửa xe Hoặc Tiêu 2M VND   |
| Gold     | 15 lần rửa xe Hoặc Tiêu 6M VND  |
| Platinum | 30 lần rửa xe Hoặc Tiêu 15M VND |

#### **3. Quản lý xe của khách hàng**

Mỗi khách hàng có thể có một hoặc nhiều xe.

Bao gồm:

* Thêm xe
* Xem danh sách xe
* Chọn xe khi đặt lịch
* Lưu biển số, hãng xe, mẫu xe, màu xe

#### **4. Quản lý dịch vụ rửa xe**

Hệ thống cần có danh sách các dịch vụ để khách chọn.

Bao gồm:

* Xem dịch vụ
* Giá dịch vụ
* Thời gian rửa
* Trạng thái dịch vụ còn hoạt động hay không

#### **5. Đặt lịch rửa xe**

Đây là chức năng chính của customer.

Bao gồm:

* Chọn xe
* Chọn dịch vụ
* Chọn ngày theo booking window của tier
* Chọn khung giờ
* Kiểm tra slot còn trống hay đã đầy
* Tạo booking

#### **6. Thanh toán**

Hệ thống không tích hợp cổng thanh toán thật, chỉ mô phỏng thanh toán.

Bao gồm:

* Customer bấm Pay Now
* Admin Collect Payment
* Chọn phương thức CASH hoặc BANKING
* Cập nhật trạng thái thanh toán

#### **7. Hoàn tất rửa xe và cập nhật Loyalty**

Khi booking đã PAID và được chuyển sang COMPLETED, hệ thống cập nhật dữ liệu tích lũy cho khách hàng.

Bao gồm:

* Tăng tổng tiền đã chi
* Tăng tổng số lần rửa
* Cộng điểm
* Xét lại hạng thành viên

#### **8. Lịch sử rửa xe**

Chỉ những booking đã hoàn thành mới được đưa vào lịch sử.

Bao gồm:

* Ngày rửa
* Xe đã rửa
* Dịch vụ
* Số tiền đã trả
* Điểm nhận được
* Phương thức thanh toán
