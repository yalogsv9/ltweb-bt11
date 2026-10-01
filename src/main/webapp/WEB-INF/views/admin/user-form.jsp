<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>${mode == 'add' ? 'Thêm' : 'Cập nhật'} User</title></head>
<body>
<div class="card">
    <h2>${mode == 'add' ? 'Thêm user' : 'Cập nhật user'}</h2>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <form method="post" action="${pageContext.request.contextPath}/admin/users">
        <input type="hidden" name="mode" value="${mode}">
        <label>Username</label>
        <input type="text" name="username" value="${form.username}" ${mode == 'edit' ? 'readonly' : 'required'}>
        <label>Mật khẩu</label>
        <input type="text" name="password" value="${form.password}">
        <label>Họ tên</label>
        <input type="text" name="fullname" value="${form.fullname}">
        <label>Email</label>
        <input type="text" name="email" value="${form.email}">
        <label>Số điện thoại</label>
        <input type="text" name="phone" value="${form.phone}">
        <label>Ảnh (đường dẫn)</label>
        <input type="text" name="images" value="${form.images}">
        <label><input type="checkbox" name="admin" ${form.admin ? 'checked' : ''}> Admin</label>
        <label><input type="checkbox" name="active" ${form.active or mode == 'add' ? 'checked' : ''}> Active</label>
        <p><button class="btn" type="submit">Lưu</button>
           <a class="btn gray" href="${pageContext.request.contextPath}/admin/users">Hủy</a></p>
    </form>
</div>
</body>
</html>
