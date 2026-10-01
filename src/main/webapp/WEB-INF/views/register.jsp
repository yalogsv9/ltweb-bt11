<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Đăng ký</title></head>
<body>
<div class="card">
    <h2>Đăng ký tài khoản</h2>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <form method="post" action="${pageContext.request.contextPath}/register">
        <label>Tên đăng nhập</label>
        <input type="text" name="username" value="${form.username}" required>
        <label>Mật khẩu</label>
        <input type="password" name="password" required>
        <label>Họ tên</label>
        <input type="text" name="fullname" value="${form.fullname}">
        <label>Email (nhận mã OTP)</label>
        <input type="email" name="email" value="${form.email}" required>
        <label>Số điện thoại</label>
        <input type="text" name="phone" value="${form.phone}">
        <p><button class="btn" type="submit">Đăng ký &amp; nhận OTP</button>
           <a href="${pageContext.request.contextPath}/login">Đã có tài khoản?</a></p>
    </form>
</div>
</body>
</html>
