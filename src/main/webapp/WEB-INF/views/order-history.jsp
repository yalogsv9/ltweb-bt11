<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Lịch sử đặt hàng</title>
    <style>
        .status-tabs {
            display: flex;
            gap: 6px;
            overflow-x: auto;
            border-bottom: 2px solid #ddd;
            margin-bottom: 24px;
            padding-bottom: 4px;
        }
        .status-tab {
            padding: 10px 14px;
            text-decoration: none;
            color: #444;
            font-weight: 500;
            border-radius: 6px 6px 0 0;
            white-space: nowrap;
            background: #e9ecef;
            transition: all .2s;
            font-size: 14px;
        }
        .status-tab:hover {
            background: #dde2e6;
            color: #1f3a5f;
        }
        .status-tab.active {
            background: #1f3a5f;
            color: #fff;
            font-weight: bold;
        }
        .status-tab .count {
            display: inline-block;
            background: rgba(0,0,0,0.15);
            padding: 2px 6px;
            border-radius: 10px;
            font-size: 12px;
            margin-left: 4px;
        }
        .status-tab.active .count {
            background: rgba(255,255,255,0.3);
            color: #fff;
        }
        .order-card {
            background: #fff;
            border: 1px solid #ccd;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            overflow: hidden;
        }
        .order-card-header {
            background: #f8f9fa;
            border-bottom: 1px solid #eee;
            padding: 12px 18px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 10px;
        }
        .order-card-body {
            padding: 16px 18px;
        }
        .order-card-footer {
            background: #fcfcfc;
            border-top: 1px solid #eee;
            padding: 12px 18px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
        }
        /* Badge mau theo 8 trang thai */
        .badge {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 14px;
            font-size: 13px;
            font-weight: bold;
            color: #fff;
        }
        .badge-moi         { background: #1f3a5f; } /* Don hang moi */
        .badge-xacnhan     { background: #0288d1; } /* Da xac nhan */
        .badge-chuanbi     { background: #f57c00; } /* Chuan bi hang */
        .badge-vanchuyen   { background: #7b1fa2; } /* Van chuyen */
        .badge-giaohang    { background: #00897b; } /* Giao hang */
        .badge-dagiao      { background: #2e7d32; } /* Da giao */
        .badge-huy         { background: #c62828; } /* Don hang huy */
        .badge-hoan        { background: #6d4c41; } /* Don hang hoan */
        .badge-khac        { background: #757575; }
    </style>
</head>
<body>
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:14px; flex-wrap:wrap; gap:10px;">
        <h2>Lịch sử đặt hàng</h2>
        <c:if test="${empty sessionScope.user}">
            <div style="font-size:14px; color:#555; background:#fff3cd; padding:8px 12px; border-radius:4px; border:1px solid #ffeeba;">
                Bạn đang xem ở chế độ khách. <a href="${ctx}/login" style="font-weight:bold; color:#856404;">Đăng nhập</a> để lưu đơn vào tài khoản cá nhân.
            </div>
        </c:if>
        <c:if test="${not empty sessionScope.user}">
            <div style="font-size:14px; color:#333;">
                Tài khoản: <b>${sessionScope.user.username}</b> (${sessionScope.user.fullname})
            </div>
        </c:if>
    </div>

    <!-- Thanh 8 Tab Loc Trang Thai + Tab Tat Ca -->
    <div class="status-tabs">
        <a href="${ctx}/order-history?status=all" class="status-tab ${currentStatus == 'all' ? 'active' : ''}">
            Tất cả <span class="count">${totalOrders}</span>
        </a>

        <a href="${ctx}/order-history?status=Đơn hàng mới" class="status-tab ${currentStatus == 'Đơn hàng mới' ? 'active' : ''}">
            Đơn hàng mới <span class="count">${statusCounts['Đơn hàng mới'] != null ? statusCounts['Đơn hàng mới'] : 0}</span>
        </a>

        <a href="${ctx}/order-history?status=Đã xác nhận" class="status-tab ${currentStatus == 'Đã xác nhận' ? 'active' : ''}">
            Đã xác nhận <span class="count">${statusCounts['Đã xác nhận'] != null ? statusCounts['Đã xác nhận'] : 0}</span>
        </a>

        <a href="${ctx}/order-history?status=Chuẩn bị hàng" class="status-tab ${currentStatus == 'Chuẩn bị hàng' ? 'active' : ''}">
            Chuẩn bị hàng <span class="count">${statusCounts['Chuẩn bị hàng'] != null ? statusCounts['Chuẩn bị hàng'] : 0}</span>
        </a>

        <a href="${ctx}/order-history?status=Vận chuyển" class="status-tab ${currentStatus == 'Vận chuyển' ? 'active' : ''}">
            Vận chuyển <span class="count">${statusCounts['Vận chuyển'] != null ? statusCounts['Vận chuyển'] : 0}</span>
        </a>

        <a href="${ctx}/order-history?status=Giao hàng" class="status-tab ${currentStatus == 'Giao hàng' ? 'active' : ''}">
            Giao hàng <span class="count">${statusCounts['Giao hàng'] != null ? statusCounts['Giao hàng'] : 0}</span>
        </a>

        <a href="${ctx}/order-history?status=Đã giao" class="status-tab ${currentStatus == 'Đã giao' ? 'active' : ''}">
            Đã giao <span class="count">${statusCounts['Đã giao'] != null ? statusCounts['Đã giao'] : 0}</span>
        </a>

        <a href="${ctx}/order-history?status=Đơn hàng hủy" class="status-tab ${currentStatus == 'Đơn hàng hủy' ? 'active' : ''}">
            Đơn hàng hủy <span class="count">${statusCounts['Đơn hàng hủy'] != null ? statusCounts['Đơn hàng hủy'] : 0}</span>
        </a>

        <a href="${ctx}/order-history?status=Đơn hàng hoàn" class="status-tab ${currentStatus == 'Đơn hàng hoàn' ? 'active' : ''}">
            Đơn hàng hoàn <span class="count">${statusCounts['Đơn hàng hoàn'] != null ? statusCounts['Đơn hàng hoàn'] : 0}</span>
        </a>
    </div>

    <!-- Danh sach don hang -->
    <c:choose>
        <c:when test="${empty orders}">
            <div style="text-align:center; padding:50px 20px; background:#fff; border:1px solid #ccd; border-radius:8px;">
                <p style="font-size:18px; color:#666;">
                    <c:choose>
                        <c:when test="${currentStatus == 'all'}">Hiện chưa có đơn hàng nào!</c:when>
                        <c:otherwise>Không có đơn hàng nào ở trạng thái "<b>${currentStatus}</b>"!</c:otherwise>
                    </c:choose>
                </p>
                <a href="${ctx}/videos" class="btn" style="margin-top:10px;">Mua sắm ngay</a>
            </div>
        </c:when>
        <c:otherwise>
            <c:forEach var="o" items="${orders}">
                <div class="order-card">
                    <!-- Header the don hang -->
                    <div class="order-card-header">
                        <div>
                            <b>Mã đơn hàng:</b> <span style="color:#1f3a5f; font-size:16px; font-weight:bold;">#${o.orderId}</span>
                            <span style="margin: 0 8px; color:#ccc;">|</span>
                            <small style="color:#666;">Ngày đặt: <fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></small>
                        </div>
                        <div>
                            <c:choose>
                                <c:when test="${o.status == 'Đơn hàng mới'}"><span class="badge badge-moi">${o.status}</span></c:when>
                                <c:when test="${o.status == 'Đã xác nhận'}"><span class="badge badge-xacnhan">${o.status}</span></c:when>
                                <c:when test="${o.status == 'Chuẩn bị hàng'}"><span class="badge badge-chuanbi">${o.status}</span></c:when>
                                <c:when test="${o.status == 'Vận chuyển'}"><span class="badge badge-vanchuyen">${o.status}</span></c:when>
                                <c:when test="${o.status == 'Giao hàng'}"><span class="badge badge-giaohang">${o.status}</span></c:when>
                                <c:when test="${o.status == 'Đã giao'}"><span class="badge badge-dagiao">${o.status}</span></c:when>
                                <c:when test="${o.status == 'Đơn hàng hủy'}"><span class="badge badge-huy">${o.status}</span></c:when>
                                <c:when test="${o.status == 'Đơn hàng hoàn'}"><span class="badge badge-hoan">${o.status}</span></c:when>
                                <c:otherwise><span class="badge badge-khac">${o.status}</span></c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Thong tin nguoi nhan -->
                    <div style="background:#fcfcfc; padding:10px 18px; border-bottom:1px solid #f0f0f0; font-size:13px; color:#555;">
                        <b>Người nhận:</b> ${o.receiverName} - <b>SĐT:</b> ${o.receiverPhone} - <b>Địa chỉ:</b> ${o.receiverAddress}
                        <c:if test="${not empty o.note}">
                            - <b>Ghi chú:</b> <i>${o.note}</i>
                        </c:if>
                    </div>

                    <!-- Chi tiet san pham trong don -->
                    <div class="order-card-body">
                        <table class="grid" style="border:0;">
                            <tbody>
                                <c:forEach var="item" items="${o.items}">
                                    <tr style="border-bottom: 1px dashed #eee;">
                                        <td style="width:70px; border:0; padding:8px 4px; vertical-align:middle;">
                                            <img src="${item.videoPoster}" alt="${item.videoTitle}" style="width:65px; height:42px; object-fit:cover; border-radius:4px;">
                                        </td>
                                        <td style="border:0; padding:8px 8px; vertical-align:middle;">
                                            <a href="${ctx}/video?id=${item.videoId}" style="color:#1f3a5f; text-decoration:none; font-weight:bold;">
                                                ${item.videoTitle}
                                            </a>
                                            <br><small style="color:#888;">Mã: ${item.videoId}</small>
                                        </td>
                                        <td style="width:120px; border:0; text-align:center; padding:8px; vertical-align:middle;">
                                            x${item.quantity}
                                        </td>
                                        <td style="width:140px; border:0; text-align:right; padding:8px; vertical-align:middle; color:#666;">
                                            <fmt:formatNumber value="${item.price}" pattern="#,##0"/> đ
                                        </td>
                                        <td style="width:150px; border:0; text-align:right; padding:8px; vertical-align:middle; font-weight:bold; color:#b02a2a;">
                                            <fmt:formatNumber value="${item.amount}" pattern="#,##0"/> đ
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- Footer the don hang -->
                    <div class="order-card-footer">
                        <div>
                            <span style="font-size:13px; color:#555;">Hình thức: <b>${o.paymentMethod} (Thanh toán khi nhận hàng)</b></span>
                        </div>
                        <div style="font-size:16px;">
                            Tổng tiền thanh toán: <b style="color:#b02a2a; font-size:18px;"><fmt:formatNumber value="${o.totalAmount}" pattern="#,##0"/> đ</b>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</body>
</html>
