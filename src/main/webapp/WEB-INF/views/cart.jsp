<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Giỏ hàng</title>
</head>
<body>
    <h2>Giỏ hàng của bạn</h2>

    <c:if test="${not empty flashMsg}">
        <div class="ok" style="padding:10px; background:#e6f4ea; border-radius:4px; margin-bottom:12px;">
            ${flashMsg}
        </div>
    </c:if>
    <c:if test="${not empty flashErr}">
        <div class="error" style="padding:10px; background:#fce8e6; border-radius:4px; margin-bottom:12px;">
            ${flashErr}
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty cart or empty cart.items}">
            <div style="text-align:center; padding: 40px 20px; background:#fff; border:1px solid #ccd; border-radius:6px;">
                <p style="font-size:18px; color:#555;">Giỏ hàng của bạn hiện đang trống!</p>
                <p><a href="${ctx}/videos" class="btn">Tiếp tục xem sản phẩm</a></p>
            </div>
        </c:when>
        <c:otherwise>
            <table class="grid" style="margin-bottom:16px;">
                <thead>
                    <tr>
                        <th style="width:50px; text-align:center;">STT</th>
                        <th style="width:120px; text-align:center;">Hình ảnh</th>
                        <th>Tên sản phẩm</th>
                        <th style="width:130px; text-align:right;">Đơn giá</th>
                        <th style="width:150px; text-align:center;">Số lượng (1-99)</th>
                        <th style="width:140px; text-align:right;">Thành tiền</th>
                        <th style="width:90px; text-align:center;">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${cart.items}" varStatus="status">
                        <tr>
                            <td style="text-align:center; vertical-align:middle;">${status.index + 1}</td>
                            <td style="text-align:center; vertical-align:middle;">
                                <a href="${ctx}/video?id=${item.video.videoId}">
                                    <img src="${item.video.poster}" alt="${item.video.title}" style="width:100px; height:60px; object-fit:cover; border-radius:4px;">
                                </a>
                            </td>
                            <td style="vertical-align:middle;">
                                <a href="${ctx}/video?id=${item.video.videoId}" style="font-weight:bold; color:#1f3a5f; text-decoration:none;">
                                    ${item.video.title}
                                </a>
                                <br><small style="color:#777;">Mã: ${item.video.videoId} | Danh mục: ${item.video.categoryName}</small>
                            </td>
                            <td style="text-align:right; vertical-align:middle;">
                                <fmt:formatNumber value="${item.price}" pattern="#,##0"/> đ
                            </td>
                            <td style="text-align:center; vertical-align:middle;">
                                <form action="${ctx}/cart" method="post" class="inline-form" style="display:flex; align-items:center; justify-content:center; gap:4px;">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="videoId" value="${item.video.videoId}">
                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="99" style="width:60px; padding:6px; text-align:center; border:1px solid #ccd; border-radius:4px;" required>
                                    <button type="submit" class="btn" style="padding:6px 10px; font-size:12px;" title="Cập nhật">Lưu</button>
                                </form>
                            </td>
                            <td style="text-align:right; vertical-align:middle; font-weight:bold; color:#b02a2a;">
                                <fmt:formatNumber value="${item.subTotal}" pattern="#,##0"/> đ
                            </td>
                            <td style="text-align:center; vertical-align:middle;">
                                <form action="${ctx}/cart" method="post" class="inline-form" onsubmit="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này?');">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="videoId" value="${item.video.videoId}">
                                    <button type="submit" class="btn danger" style="padding:6px 10px; font-size:12px;">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr style="background:#f4f6f8; font-weight:bold; font-size:16px;">
                        <td colspan="4" style="text-align:right; vertical-align:middle;">Tổng cộng:</td>
                        <td style="text-align:center; vertical-align:middle;">${cart.totalQuantity} sản phẩm</td>
                        <td style="text-align:right; vertical-align:middle; color:#b02a2a; font-size:18px;">
                            <fmt:formatNumber value="${cart.totalAmount}" pattern="#,##0"/> đ
                        </td>
                        <td></td>
                    </tr>
                </tbody>
            </table>

            <div style="display:flex; justify-content:space-between; align-items:center; margin-top:20px; flex-wrap:wrap; gap:10px;">
                <div style="display:flex; gap:10px;">
                    <a href="${ctx}/videos" class="btn gray">« Tiếp tục mua hàng</a>
                    <form action="${ctx}/cart" method="post" class="inline-form" onsubmit="return confirm('Bạn có chắc muốn xóa toàn bộ giỏ hàng?');">
                        <input type="hidden" name="action" value="clear">
                        <button type="submit" class="btn danger">Xóa toàn bộ giỏ</button>
                    </form>
                </div>
                <div>
                    <a href="${ctx}/checkout" class="btn" style="background:#1c7c3a; font-size:16px; padding:10px 24px; font-weight:bold;">
                        Tiến hành đặt hàng (COD) »
                    </a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>
