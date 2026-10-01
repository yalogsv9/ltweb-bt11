<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property='title'/> - User</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <sitemesh:write property='head'/>
</head>
<body>
    <jsp:include page="/WEB-INF/decorators/header.jsp"><jsp:param name="role" value="user"/></jsp:include>
    <div class="container">
        <sitemesh:write property='body'/>
    </div>
    <jsp:include page="/WEB-INF/decorators/footer.jsp"/>
</body>
</html>
