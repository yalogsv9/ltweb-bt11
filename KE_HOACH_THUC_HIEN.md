# Đặc tả kỹ thuật (Spec) & Kế hoạch triển khai (Plan)

> **Dự án:** LTWeb De04 - MSSV: 24110291  
> **Tác giả commit:** Yalog <nguyenyalog@gmail.com> (Không có co-author)  
> **Thời điểm bắt đầu:** 01/10/2026

---

## 1. TỔNG QUAN YÊU CẦU & MỐC THỜI GIAN

| STT | Chức năng | Hạn chót (Deadline) | Trạng thái |
|:---:|:---|:---:|:---:|
| **Câu 1** | **Chức năng Giỏ hàng**: Thêm, xóa, sửa số lượng (giới hạn 1 - 99), cập nhật giỏ hàng, xóa toàn bộ | **10h45, 01/10/2026** | Đang lên kế hoạch |
| **Câu 2** | **Chức năng Thanh toán COD**: Form nhập thông tin nhận hàng, xác nhận giỏ hàng, đặt hàng COD, tạo đơn hàng | **10h45, 01/10/2026** | Đang lên kế hoạch |
| **Câu 3** | **Chức năng Lịch sử đặt hàng & Lọc theo 8 trạng thái**: Đơn hàng mới, Đã xác nhận, Chuẩn bị hàng, Vận chuyển, Giao hàng, Đã giao, Đơn hàng hủy, Đơn hàng hoàn | **22h45, 05/10/2026** | Đang lên kế hoạch |

---

## 2. KIẾN TRÚC & THIẾT KẾ DỮ LIỆU (DATABASE SCHEMA)

Hiện tại dự án đang sử dụng:
- **Backend:** Java Servlet, JSP, JSTL, SiteMesh 3, JDBC thuần.
- **Server:** Embedded Apache Tomcat 9 (`Launcher_24110291.java`).
- **Database:** Microsoft SQL Server (`LTWebDe04`).

### 2.1. Bổ sung trường giá cho sản phẩm (Video)
Trong bảng `Videos`, bổ sung hoặc quy ước trường `Price` (DECIMAL(18,2) / double). Mặc định nếu chưa có cột trong DB, ta sẽ thêm cột `Price` vào bảng `Videos` (ví dụ `ALTER TABLE Videos ADD Price DECIMAL(18,2) DEFAULT 50000; UPDATE Videos SET Price = 50000 WHERE Price IS NULL;`) để sản phẩm có giá bán hiển thị trực quan.

### 2.2. Bảng `Orders` (Đơn hàng)
Lưu thông tin tổng quát của một đơn hàng:
```sql
CREATE TABLE Orders (
    OrderId INT IDENTITY(1,1) PRIMARY KEY,
    Username VARCHAR(50) NULL,             -- Khách hàng đã đăng nhập (hoặc NULL nếu vãng lai)
    ReceiverName NVARCHAR(100) NOT NULL,   -- Tên người nhận
    ReceiverPhone VARCHAR(20) NOT NULL,    -- SĐT người nhận
    ReceiverAddress NVARCHAR(255) NOT NULL,-- Địa chỉ giao hàng
    Note NVARCHAR(500) NULL,               -- Ghi chú đơn hàng
    TotalAmount DECIMAL(18,2) NOT NULL,    -- Tổng tiền đơn hàng
    PaymentMethod NVARCHAR(50) NOT NULL DEFAULT N'COD', -- 'COD'
    Status NVARCHAR(50) NOT NULL DEFAULT N'Đơn hàng mới', -- 8 trạng thái
    CreatedAt DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Orders_Users FOREIGN KEY (Username) REFERENCES Users(Username) ON DELETE SET NULL
);
```

### 2.3. Bảng `OrderDetails` (Chi tiết đơn hàng)
Lưu các món hàng trong từng đơn:
```sql
CREATE TABLE OrderDetails (
    DetailId INT IDENTITY(1,1) PRIMARY KEY,
    OrderId INT NOT NULL,
    VideoId VARCHAR(50) NOT NULL,
    Price DECIMAL(18,2) NOT NULL,
    Quantity INT NOT NULL,
    Amount DECIMAL(18,2) NOT NULL,
    CONSTRAINT FK_OrderDetails_Orders FOREIGN KEY (OrderId) REFERENCES Orders(OrderId) ON DELETE CASCADE,
    CONSTRAINT FK_OrderDetails_Videos FOREIGN KEY (VideoId) REFERENCES Videos(VideoId)
);
```

---

## 3. ĐẶC TẢ CHI TIẾT TỪNG CHỨC NĂNG

### CÂU 1: CHỨC NĂNG GIỎ HÀNG (CART)
- **Lưu trữ:** Lưu trong `HttpSession` với key `"cart"`.
- **Lớp mô hình (Model):**
  - `CartItem_24110291`: 
    - `Video_24110291 video`
    - `int quantity`
    - `double price`
    - `double getSubTotal()`: `price * quantity`
  - `Cart_24110291`:
    - `Map<String, CartItem_24110291> items`: key là `videoId`
    - `void add(Video_24110291 video, int quantity)`: Thêm mới hoặc cộng dồn số lượng.
    - `void update(String videoId, int quantity)`: Giới hạn `1 <= quantity <= 99`. Nếu `< 1` tự xóa hoặc gán 1.
    - `void remove(String videoId)`: Xóa item.
    - `void clear()`: Xóa sạch giỏ hàng.
    - `int getTotalQuantity()`: Tổng số lượng sản phẩm.
    - `double getTotalAmount()`: Tổng thành tiền.
- **Giao diện & Chức năng Người dùng:**
  - Nút **"Thêm vào giỏ"** tại trang `/videos` (danh sách) và trang `/video?id=...` (chi tiết).
  - Menu header: Hiển thị icon/link **"Giỏ hàng (X)"** với X là tổng số lượng.
  - Trang giỏ hàng `/cart` (`WEB-INF/views/cart.jsp`):
    - Bảng hiển thị: Ảnh poster, Tên sản phẩm, Đơn giá, Ô số lượng có nút `+` và `-` hoặc form cập nhật, Thành tiền, Nút xóa món.
    - Giới hạn số lượng từ 1 đến 99, ngăn chặn nhập số âm hoặc số quá lớn.
    - Nút "Xóa toàn bộ giỏ hàng".
    - Tổng tiền giỏ hàng.
    - Nút "Tiếp tục xem sản phẩm" và nút **"Tiến hành đặt hàng (COD)"**.
- **Servlet điều khiển:**
  - `CartController_24110291` (`@WebServlet("/cart")`):
    - `GET /cart`: Hiển thị giỏ hàng.
    - `POST /cart?action=add`: Nhận `videoId`, `quantity` (mặc định 1), thêm vào giỏ và redirect lại kèm thông báo.
    - `POST /cart?action=update`: Nhận `videoId`, `quantity`, cập nhật số lượng.
    - `POST /cart?action=remove`: Nhận `videoId`, xóa món khỏi giỏ.
    - `POST /cart?action=clear`: Xóa hết giỏ.

---

### CÂU 2: CHỨC NĂNG THANH TOÁN COD (CASH ON DELIVERY)
- **Luồng nghiệp vụ:**
  1. Người dùng bấm **"Tiến hành đặt hàng"** từ trang `/cart`.
  2. Nếu giỏ hàng rỗng: thông báo và chuyển hướng về `/videos`.
  3. Mở trang thanh toán `/checkout` (`WEB-INF/views/checkout.jsp`):
     - Bên trái: Form điền thông tin:
       - Họ và tên người nhận (`fullname`, bắt buộc).
       - Số điện thoại (`phone`, bắt buộc, validate định dạng).
       - Địa chỉ giao hàng chi tiết (`address`, bắt buộc).
       - Ghi chú giao hàng (`note`, tùy chọn).
       - Phương thức thanh toán: Chọn mặc định **"Thanh toán khi nhận hàng (COD)"**.
       - Nếu người dùng đã đăng nhập (`sessionScope.user`): Tự động điền sẵn Họ tên và SĐT của user.
     - Bên phải: Bảng tóm tắt đơn hàng (Danh sách món, số lượng, đơn giá, tổng tiền thanh toán).
  4. Người dùng bấm **"Xác nhận đặt hàng"**:
     - Servlet `CheckoutController_24110291` (`POST /checkout`):
       - Kiểm tra validate dữ liệu đầu vào.
       - Mở transaction DB:
         - Lưu vào bảng `Orders` với `PaymentMethod = 'COD'`, `Status = N'Đơn hàng mới'`.
         - Lưu từng chi tiết sản phẩm vào bảng `OrderDetails`.
         - Commit transaction.
       - Xóa sạch giỏ hàng trong session (`cart.clear()`).
       - Redirect sang trang kết quả đặt hàng thành công `/order-success?orderId=...` kèm thông báo mã đơn, hướng dẫn kiểm tra lịch sử đơn hàng.
- **Model & DAO & Service:**
  - `Order_24110291`, `OrderDetail_24110291`.
  - `IOrderDAO_24110291`, `OrderDAOImpl_24110291`.
  - `IOrderService_24110291`, `OrderServiceImpl_24110291`.

---

### CÂU 3: CHỨC NĂNG LỊCH SỬ ĐẶT HÀNG & BỘ LỌC 8 TRẠNG THÁI
- **Danh sách 8 trạng thái chuẩn theo đề bài:**
  1. `Đơn hàng mới`
  2. `Đã xác nhận`
  3. `Chuẩn bị hàng`
  4. `Vận chuyển`
  5. `Giao hàng`
  6. `Đã giao`
  7. `Đơn hàng hủy`
  8. `Đơn hàng hoàn`
- **Giao diện Lịch sử đơn hàng (`/order-history` - `WEB-INF/views/order-history.jsp`):**
  - Thanh tab điều hướng trạng thái:
    - `Tất cả`
    - `Đơn hàng mới`
    - `Đã xác nhận`
    - `Chuẩn bị hàng`
    - `Vận chuyển`
    - `Giao hàng`
    - `Đã giao`
    - `Đơn hàng hủy`
    - `Đơn hàng hoàn`
  - Khi click vào tab nào, URL có query param `status=...` (ví dụ: `/order-history?status=Đã xác nhận`).
  - Danh sách đơn hàng tương ứng được tải lên từ CSDL:
    - Header đơn hàng: Mã đơn, Thời gian đặt, Trạng thái (hiển thị Badge màu sắc trực quan: xanh lá cho đã giao, vàng cho đang chuẩn bị/giao, đỏ cho hủy/hoàn...).
    - Thông tin người nhận, SĐT, Địa chỉ nhận hàng, Phương thức thanh toán (COD).
    - Bảng sản phẩm trong đơn: Ảnh poster, Tên sản phẩm, Đơn giá, Số lượng, Thành tiền.
    - Tổng tiền đơn hàng.
- **Truy vấn DB:**
  - `findOrders(String username, String status)`: Nếu `status` rỗng/null thì lấy tất cả; nếu có `status` thì lọc `WHERE Status = ?`.
  - Hỗ trợ khách hàng đăng nhập xem đơn của chính mình, hoặc nếu chưa đăng nhập có thể tra cứu theo SĐT hoặc mã đơn hàng.
- **Script kiểm thử DB:**
  - Cung cấp sẵn các câu lệnh SQL mẫu để giáo viên / người kiểm thử có thể chạy trực tiếp trong SQL Server Management Studio (SSMS) nhằm thay đổi trạng thái đơn hàng và quan sát giao diện web phản ánh ngay tức thì:
    ```sql
    -- Ví dụ thay đổi trạng thái đơn hàng #1 sang các trạng thái khác nhau:
    UPDATE Orders SET Status = N'Đã xác nhận' WHERE OrderId = 1;
    UPDATE Orders SET Status = N'Chuẩn bị hàng' WHERE OrderId = 1;
    UPDATE Orders SET Status = N'Vận chuyển' WHERE OrderId = 1;
    UPDATE Orders SET Status = N'Giao hàng' WHERE OrderId = 1;
    UPDATE Orders SET Status = N'Đã giao' WHERE OrderId = 1;
    UPDATE Orders SET Status = N'Đơn hàng hủy' WHERE OrderId = 1;
    UPDATE Orders SET Status = N'Đơn hàng hoàn' WHERE OrderId = 1;
    ```

---

## 4. KẾ HOẠCH TRIỂN KHAI THEO TỪNG BƯỚC (STEP-BY-STEP PLAN)

### Bước 0: Chuẩn bị Git & CSDL
1. Khởi tạo Git repository (`git init`), cấu hình author `Yalog <nguyenyalog@gmail.com>`.
2. Tạo file `.gitignore` để loại bỏ các thư mục build (`target/`, `.settings/`, `tomcat.8080/`).
3. Commit ban đầu dự án gốc:
   - Commit: `Initial commit: project base source code`
4. Cập nhật file SQL migration: tạo bảng `Orders`, `OrderDetails`, bổ sung trường `Price` nếu cần.

### Bước 1: Triển khai Câu 1 - Chức năng Giỏ hàng (Hạn 10h45, 01/10/2026)
1. Tạo Model: `CartItem_24110291`, `Cart_24110291`.
2. Cập nhật `Video_24110291` và `VideoDAOImpl_24110291` để hỗ trợ hiển thị giá tiền sản phẩm (`price`).
3. Tạo Controller: `CartController_24110291` hỗ trợ các action `add`, `update`, `remove`, `clear`.
4. Tạo View: `WEB-INF/views/cart.jsp` hiển thị giỏ hàng, bảng tính tiền, nút thay đổi số lượng, nút xóa.
5. Cập nhật `header.jsp` (link Giỏ hàng kèm badge số lượng), `videos.jsp` và `video-detail.jsp` (thêm nút "Thêm vào giỏ").
6. Kiểm thử các luồng: thêm hàng, tăng giảm số lượng (1 - 99), xóa món, xóa hết.
7. **Review Câu 1 đạt** -> Commit Câu 1:
   - Author: `Yalog <nguyenyalog@gmail.com>` (không co-author)
   - Message: `feat: implement cart feature with add, edit, remove, and quantity limit`

### Bước 2: Triển khai Câu 2 - Chức năng Thanh toán COD (Hạn 10h45, 01/10/2026)
1. Tạo Model: `Order_24110291`, `OrderDetail_24110291`.
2. Tạo DAO & Service: `IOrderDAO_24110291`, `OrderDAOImpl_24110291`, `IOrderService_24110291`, `OrderServiceImpl_24110291`.
3. Tạo Controller: `CheckoutController_24110291` (GET form thanh toán, POST xử lý đặt hàng COD).
4. Tạo View: `WEB-INF/views/checkout.jsp` và `WEB-INF/views/order-success.jsp`.
5. Cho phép người dùng thường đăng nhập để liên kết đơn hàng với tài khoản (tinh chỉnh `LoginController_24110291` nếu người dùng đăng nhập mua hàng).
6. Kiểm thử luồng: thanh toán COD từ giỏ hàng, xác nhận đơn hàng, kiểm tra dữ liệu lưu vào DB, xóa giỏ hàng sau khi đặt.
7. **Review Câu 2 đạt** -> Commit Câu 2:
   - Author: `Yalog <nguyenyalog@gmail.com>` (không co-author)
   - Message: `feat: implement COD order checkout and order creation`

### Bước 3: Triển khai Câu 3 - Lịch sử đặt hàng & Lọc theo 8 trạng thái (Hạn 22h45, 05/10/2026)
1. Bổ sung phương thức truy vấn lọc theo trạng thái trong `IOrderDAO_24110291` & `IOrderService_24110291`.
2. Tạo Controller: `OrderHistoryController_24110291` (`/order-history`).
3. Tạo View: `WEB-INF/views/order-history.jsp` hiển thị 8 tab trạng thái và danh sách đơn hàng.
4. Thêm link "Lịch sử đơn hàng" vào `header.jsp`.
5. Tạo file hướng dẫn và script SQL test cập nhật trạng thái đơn hàng (`sql/test-order-status.sql`).
6. Kiểm thử luồng: thay đổi trực tiếp trạng thái trong DB và quan sát web lọc chính xác theo từng trạng thái.
7. **Review Câu 3 đạt** -> Commit Câu 3:
   - Author: `Yalog <nguyenyalog@gmail.com>` (không co-author)
   - Message: `feat: implement order history with 8 status filter tabs`

---

## 5. QUY TẮC COMMIT & AN TOÀN
- Author bắt buộc: `Yalog <nguyenyalog@gmail.com>`
- Không thêm bất kỳ dòng Co-authored-by nào.
- Commit message: Đúng 1 dòng ngắn gọn, súc tích mô tả việc đã hoàn thành.
- Mỗi câu chỉ commit 1 lần sau khi đã thực hiện và review kiểm thử đạt yêu cầu.
