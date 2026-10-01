<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head><title>Trang chủ Admin</title></head>
<body>
<h1>Trang chủ quản trị</h1>
<p>Xin chào <b>${sessionScope.user.fullname}</b> (${sessionScope.user.username}).</p>
<p><a class="btn" href="${pageContext.request.contextPath}/admin/users">Quản lý User</a></p>
</body>
</html>
