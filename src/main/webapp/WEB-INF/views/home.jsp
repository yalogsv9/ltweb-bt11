<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Trang chủ</title></head>
<body>
<h1>Trang chủ</h1>
<p>Chào mừng bạn đến với website video.</p>
<p><a class="btn" href="${pageContext.request.contextPath}/videos">Xem video theo danh mục</a></p>
<c:if test="${empty sessionScope.user}">
    <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
</c:if>
</body>
</html>
