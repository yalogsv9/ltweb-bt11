-- =====================================================================
-- KỊCH BẢN KIỂM THỬ THAY ĐỔI 8 TRẠNG THÁI ĐƠN HÀNG TRÊN SQL SERVER
-- MSSV: 24110291 - Mã đề: 04
-- =====================================================================
-- Mục đích: Chạy các câu lệnh dưới đây trong SSMS để thay đổi trạng thái
-- đơn hàng trong Database, sau đó F5 trang http://localhost:8080/order-history
-- để quan sát tab lọc và trạng thái thay đổi trực quan tương ứng.
-- =====================================================================

USE LTWebDe04;
GO

-- 1. Xem danh sách đơn hàng hiện có
SELECT OrderId, Username, ReceiverName, ReceiverPhone, TotalAmount, Status, CreatedAt 
FROM Orders 
ORDER BY OrderId DESC;
GO

-- 2. Dữ liệu mẫu ban đầu cho 8 trạng thái (chạy một lần để test nhanh nếu cần)
-- Thêm đơn hàng mẫu cho tài khoản 'user1' (hoặc khách) nếu chưa có đơn
IF NOT EXISTS (SELECT 1 FROM Orders WHERE OrderId = 1)
BEGIN
    SET IDENTITY_INSERT Orders ON;
    INSERT INTO Orders (OrderId, Username, ReceiverName, ReceiverPhone, ReceiverAddress, Note, TotalAmount, PaymentMethod, Status, CreatedAt)
    VALUES (1, N'user1', N'Nguyễn Văn A', '0900000001', N'Số 1 Võ Văn Ngân, Thủ Đức', N'Giao giờ hành chính', 150000, N'COD', N'Đơn hàng mới', GETDATE());
    SET IDENTITY_INSERT Orders OFF;

    INSERT INTO OrderDetails (OrderId, VideoId, Price, Quantity, Amount)
    VALUES (1, N'V001', 50000, 3, 150000);
END
GO

-- =====================================================================
-- 3. CÁC LỆNH UPDATE TRỰC TIẾP TRẠNG THÁI ĐƠN HÀNG (Thay OrderId = 1 bằng mã đơn cần test)
-- =====================================================================

-- Trạng thái 1: Đơn hàng mới
UPDATE Orders SET Status = N'Đơn hàng mới' WHERE OrderId = 1;
-- Quan sát: Tab "Đơn hàng mới" hiển thị đơn #1 với badge màu xanh navy.

-- Trạng thái 2: Đã xác nhận
UPDATE Orders SET Status = N'Đã xác nhận' WHERE OrderId = 1;
-- Quan sát: Tab "Đã xác nhận" hiển thị đơn #1 với badge màu xanh dương.

-- Trạng thái 3: Chuẩn bị hàng
UPDATE Orders SET Status = N'Chuẩn bị hàng' WHERE OrderId = 1;
-- Quan sát: Tab "Chuẩn bị hàng" hiển thị đơn #1 với badge màu cam.

-- Trạng thái 4: Vận chuyển
UPDATE Orders SET Status = N'Vận chuyển' WHERE OrderId = 1;
-- Quan sát: Tab "Vận chuyển" hiển thị đơn #1 với badge màu tím.

-- Trạng thái 5: Giao hàng
UPDATE Orders SET Status = N'Giao hàng' WHERE OrderId = 1;
-- Quan sát: Tab "Giao hàng" hiển thị đơn #1 với badge màu xanh mòng két.

-- Trạng thái 6: Đã giao
UPDATE Orders SET Status = N'Đã giao' WHERE OrderId = 1;
-- Quan sát: Tab "Đã giao" hiển thị đơn #1 với badge màu xanh lá cây.

-- Trạng thái 7: Đơn hàng hủy
UPDATE Orders SET Status = N'Đơn hàng hủy' WHERE OrderId = 1;
-- Quan sát: Tab "Đơn hàng hủy" hiển thị đơn #1 với badge màu đỏ.

-- Trạng thái 8: Đơn hàng hoàn
UPDATE Orders SET Status = N'Đơn hàng hoàn' WHERE OrderId = 1;
-- Quan sát: Tab "Đơn hàng hoàn" hiển thị đơn #1 với badge màu nâu.
GO
