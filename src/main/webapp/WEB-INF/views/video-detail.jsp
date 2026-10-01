<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head><title>Chi tiết video - ${video.title}</title></head>
<body>
<h2>Chi tiết sản phẩm</h2>

<c:if test="${not empty sessionScope.flashMsg}">
    <div class="ok" style="padding:10px; background:#e6f4ea; border-radius:4px; margin-bottom:12px;">
        ${sessionScope.flashMsg}
        <c:remove var="flashMsg" scope="session"/>
    </div>
</c:if>

<table class="grid">
    <tr>
        <td style="width:320px"><img class="poster" src="${video.poster}" alt="${video.title}"></td>
        <td>
            <h3 style="margin-top:0; color:#1f3a5f;">${video.title}</h3>
            <p><b>Mã sản phẩm:</b> ${video.videoId}</p>
            <p><b>Danh mục:</b> ${video.categoryName}</p>
            <p><b>Giá bán:</b> <span style="color:#b02a2a; font-size:20px; font-weight:bold;"><fmt:formatNumber value="${video.price}" pattern="#,##0"/> đ</span></p>
            <p><small style="color:#666;">Lượt xem: ${video.views} | Lượt chia sẻ: ${video.shareCount} | Lượt thích: ${video.likeCount}</small></p>
            
            <form action="${ctx}/cart" method="post" style="margin-top:16px; background:#f4f6f8; padding:12px; border-radius:6px; display:inline-block;">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="videoId" value="${video.videoId}">
                <input type="hidden" name="redirect" value="cart">
                <label for="quantity" style="font-weight:bold; margin-right:8px;">Số lượng:</label>
                <input type="number" id="quantity" name="quantity" value="1" min="1" max="99" style="width:70px; padding:6px; text-align:center; border:1px solid #ccd; border-radius:4px;" required>
                <button type="submit" class="btn" style="margin-left:8px; padding:8px 16px; font-weight:bold;">+ Thêm vào giỏ hàng</button>
            </form>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <h4>Mô tả chi tiết:</h4>
            <p>${video.description}</p>
        </td>
    </tr>
</table>
<p style="margin-top:16px;">
    <a href="${ctx}/videos" class="btn gray">&laquo; Quay lại danh sách sản phẩm</a>
    <a href="${ctx}/cart" class="btn" style="background:#2e7d32;">Xem giỏ hàng &raquo;</a>
</p>
</body>
</html>
