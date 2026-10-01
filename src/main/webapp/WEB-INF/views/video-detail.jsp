<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Chi tiết video</title></head>
<body>
<h2>Chi tiết video</h2>
<table class="grid">
    <tr>
        <td style="width:320px"><img class="poster" src="${video.poster}" alt="${video.title}"></td>
        <td>
            Tiêu đề: <b>${video.title}</b><br>
            Mã video: ${video.videoId}<br>
            Category name: ${video.categoryName}<br>
            View: ${video.views}<br>
            Share(${video.shareCount})<br>
            Like(${video.likeCount})
        </td>
    </tr>
    <tr><td colspan="2">${video.description}</td></tr>
</table>
<p><a href="${pageContext.request.contextPath}/videos">&laquo; Quay lại danh sách</a></p>
</body>
</html>
