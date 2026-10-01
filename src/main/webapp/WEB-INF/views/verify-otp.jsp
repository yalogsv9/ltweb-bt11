<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Xác thực OTP</title></head>
<body>
<div class="card">
    <h2>Xác thực OTP</h2>
    <p>Mã OTP (6 số) đã được gửi tới email <b>${sessionScope.pendingUser.email}</b>. Mã có hiệu lực 5 phút.</p>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <form method="post" action="${pageContext.request.contextPath}/verify-otp">
        <label>Mã OTP</label>
        <input type="text" name="otp" maxlength="6" required autofocus>
        <p><button class="btn" type="submit">Kích hoạt tài khoản</button>
           <a href="${pageContext.request.contextPath}/register">Đăng ký lại</a></p>
    </form>
</div>
</body>
</html>
