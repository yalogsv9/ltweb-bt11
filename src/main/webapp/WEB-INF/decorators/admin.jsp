<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property='title'/> - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <sitemesh:write property='head'/>
</head>
<body>
    <jsp:include page="/WEB-INF/decorators/header.jsp"><jsp:param name="role" value="admin"/></jsp:include>
    <div class="container">
        <p>
            <a class="btn gray" href="${pageContext.request.contextPath}/admin/home">Trang chủ admin</a>
            <a class="btn gray" href="${pageContext.request.contextPath}/admin/users">Quản lý User</a>
        </p>
        <sitemesh:write property='body'/>
    </div>
    <jsp:include page="/WEB-INF/decorators/footer.jsp"/>
</body>
</html>
