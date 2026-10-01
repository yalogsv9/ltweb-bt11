<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head><title>Quản lý User</title></head>
<body>
<h2>Danh sách User</h2>
<c:if test="${not empty sessionScope.flash}">
    <div class="ok">${sessionScope.flash}</div>
    <c:remove var="flash" scope="session"/>
</c:if>
<p><a class="btn" href="${ctx}/admin/users?action=add">+ Thêm user</a></p>
<table class="grid">
    <tr><th>Username</th><th>Họ tên</th><th>Email</th><th>SĐT</th><th>Admin</th><th>Active</th><th>Thao tác</th></tr>
    <c:forEach var="u" items="${users}">
        <tr>
            <td>${u.username}</td>
            <td>${u.fullname}</td>
            <td>${u.email}</td>
            <td>${u.phone}</td>
            <td>${u.admin ? '✔' : ''}</td>
            <td>${u.active ? '✔' : '✘'}</td>
            <td>
                <a class="btn" href="${ctx}/admin/users?action=edit&username=${u.username}">Sửa</a>
                <form class="inline-form" method="post" action="${ctx}/admin/users"
                      onsubmit="return confirm('Xóa user ${u.username}?');">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="username" value="${u.username}">
                    <input type="hidden" name="page" value="${page}">
                    <button class="btn danger" type="submit">Xóa</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<div class="pager">
    <c:if test="${page > 1}"><a href="${ctx}/admin/users?page=${page - 1}">&laquo;</a></c:if>
    <c:forEach var="i" begin="1" end="${totalPages}">
        <c:choose>
            <c:when test="${i == page}"><span class="cur">${i}</span></c:when>
            <c:otherwise><a href="${ctx}/admin/users?page=${i}">${i}</a></c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${page < totalPages}"><a href="${ctx}/admin/users?page=${page + 1}">&raquo;</a></c:if>
</div>
</body>
</html>
