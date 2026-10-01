<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Đặt hàng thành công</title>
</head>
<body>
    <div style="background:#fff; border:1px solid #ccd; border-radius:8px; padding:30px; max-width:800px; margin:20px auto;">
        <div style="text-align:center; margin-bottom:24px;">
            <div style="display:inline-block; width:64px; height:64px; line-height:64px; border-radius:50%; background:#e6f4ea; color:#1c7c3a; font-size:36px; font-weight:bold;">
                ✓
            </div>
            <h2 style="color:#1c7c3a; margin:14px 0 6px;">Đặt hàng thành công!</h2>
            <p style="color:#555; margin:0;">
                Cảm ơn bạn đã tin tưởng mua sắm. Đơn hàng của bạn đã được ghi nhận vào hệ thống.
            </p>
        </div>

        <div style="background:#f8f9fa; border:1px solid #e2e8f0; border-radius:6px; padding:18px; margin-bottom:20px;">
            <div style="display:flex; justify-content:space-between; flex-wrap:wrap; gap:12px; margin-bottom:12px; border-bottom:1px solid #e2e8f0; padding-bottom:10px;">
                <div>
                    <b>Mã đơn hàng:</b> <span style="color:#1f3a5f; font-size:18px; font-weight:bold;">#${order.orderId}</span>
                </div>
                <div>
                    <b>Thời gian đặt:</b> <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                </div>
                <div>
                    <b>Trạng thái đơn:</b> <span style="background:#1f3a5f; color:#fff; padding:3px 10px; border-radius:12px; font-size:13px; font-weight:bold;">${order.status}</span>
                </div>
            </div>

            <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap:12px; font-size:14px;">
                <div>
                    <p style="margin:4px 0;"><b>Người nhận:</b> ${order.receiverName}</p>
                    <p style="margin:4px 0;"><b>Số điện thoại:</b> ${order.receiverPhone}</p>
                    <p style="margin:4px 0;"><b>Địa chỉ giao:</b> ${order.receiverAddress}</p>
                </div>
                <div>
                    <p style="margin:4px 0;"><b>Phương thức thanh toán:</b> <span style="color:#1c7c3a; font-weight:bold;">${order.paymentMethod} (Thanh toán khi nhận hàng)</span></p>
                    <c:if test="${not empty order.note}">
                        <p style="margin:4px 0;"><b>Ghi chú:</b> <i>${order.note}</i></p>
                    </c:if>
                </div>
            </div>
        </div>

        <h3 style="color:#1f3a5f; margin-bottom:10px;">Chi tiết sản phẩm đã đặt:</h3>
        <table class="grid" style="margin-bottom:20px;">
            <thead>
                <tr>
                    <th style="width:50px; text-align:center;">STT</th>
                    <th style="width:90px; text-align:center;">Ảnh</th>
                    <th>Tên sản phẩm</th>
                    <th style="width:130px; text-align:right;">Đơn giá</th>
                    <th style="width:90px; text-align:center;">Số lượng</th>
                    <th style="width:140px; text-align:right;">Thành tiền</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${order.items}" varStatus="status">
                    <tr>
                        <td style="text-align:center; vertical-align:middle;">${status.index + 1}</td>
                        <td style="text-align:center; vertical-align:middle;">
                            <img src="${item.videoPoster}" alt="${item.videoTitle}" style="width:70px; height:45px; object-fit:cover; border-radius:4px;">
                        </td>
                        <td style="vertical-align:middle;">
                            <b>${item.videoTitle}</b><br>
                            <small style="color:#777;">Mã sản phẩm: ${item.videoId}</small>
                        </td>
                        <td style="text-align:right; vertical-align:middle;">
                            <fmt:formatNumber value="${item.price}" pattern="#,##0"/> đ
                        </td>
                        <td style="text-align:center; vertical-align:middle; font-weight:bold;">
                            ${item.quantity}
                        </td>
                        <td style="text-align:right; vertical-align:middle; font-weight:bold; color:#b02a2a;">
                            <fmt:formatNumber value="${item.amount}" pattern="#,##0"/> đ
                        </td>
                    </tr>
                </c:forEach>
                <tr style="background:#f4f6f8; font-size:16px;">
                    <td colspan="5" style="text-align:right; font-weight:bold;">Tổng tiền thanh toán (COD):</td>
                    <td style="text-align:right; font-weight:bold; color:#b02a2a; font-size:18px;">
                        <fmt:formatNumber value="${order.totalAmount}" pattern="#,##0"/> đ
                    </td>
                </tr>
            </tbody>
        </table>

        <div style="display:flex; justify-content:center; gap:16px; margin-top:20px; flex-wrap:wrap;">
            <a href="${ctx}/videos" class="btn">Tiếp tục mua sắm</a>
            <a href="${ctx}/order-history" class="btn" style="background:#2e7d32;">Xem lịch sử đơn hàng</a>
        </div>
    </div>
</body>
</html>
