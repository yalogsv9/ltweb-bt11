<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="header ${param.role == 'admin' ? 'admin' : ''}">
    <div class="brand">LTWeb - Đề 04</div>
    <nav>
        <a href="${ctx}/home">Trang Chủ</a>
        <a href="${ctx}/videos">Sản phẩm</a>
        <a href="${ctx}/cart">Giỏ hàng (<b style="color:#ffeb3b;">${sessionScope.cart != null ? sessionScope.cart.totalQuantity : 0}</b>)</a>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <a href="${ctx}/login">Đăng nhập</a>
            </c:when>
            <c:otherwise>
                <a href="${ctx}/logout">Đăng xuất (${sessionScope.user.username})</a>
            </c:otherwise>
        </c:choose>
        <%-- Chỉ admin mới thấy Trang quản trị --%>
        <c:if test="${not empty sessionScope.user and sessionScope.user.admin}">
            <a href="${ctx}/admin/home">Trang quản trị</a>
        </c:if>
    </nav>
</div>
