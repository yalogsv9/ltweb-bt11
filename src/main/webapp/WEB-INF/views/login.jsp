<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Đăng nhập</title></head>
<body>
<div class="card">
    <h2>Đăng nhập</h2>
    <c:if test="${param.registered == '1'}"><div class="ok">Kích hoạt thành công, hãy đăng nhập.</div></c:if>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <label>Tên đăng nhập</label>
        <input type="text" name="username" value="${username}" required>
        <label>Mật khẩu</label>
        <input type="password" name="password" required>
        <p><button class="btn" type="submit">Đăng nhập</button>
           <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
    </form>
</div>
</body>
</html>
