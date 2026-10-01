<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head><title>Sản phẩm</title></head>
<body>
<h2>Video theo danh mục</h2>
<c:forEach var="b" items="${blocks}">
    <table class="grid" style="margin-bottom:24px">
        <tr><th colspan="3">${b.category.categoryname} (${b.category.videoCount})</th></tr>
        <c:choose>
            <c:when test="${empty b.videos}">
                <tr><td colspan="3">Chưa có video.</td></tr>
            </c:when>
            <c:otherwise>
                <tr>
                    <c:forEach var="v" items="${b.videos}">
                        <td class="video-cell"><a href="${ctx}/video?id=${v.videoId}"><img class="poster" src="${v.poster}" alt="${v.title}"></a></td>
                    </c:forEach>
                    <c:forEach begin="${b.videos.size()}" end="2"><td class="video-cell"></td></c:forEach>
                </tr>
                <tr>
                    <c:forEach var="v" items="${b.videos}">
                        <td>
                            Tiêu đề: <a href="${ctx}/video?id=${v.videoId}"><b>${v.title}</b></a><br>
                            Mã video: ${v.videoId}<br>
                            Danh mục: ${v.categoryName}<br>
                            Giá bán: <b style="color:#b02a2a;"><fmt:formatNumber value="${v.price}" pattern="#,##0"/> đ</b><br>
                            <small style="color:#666;">Lượt xem: ${v.views} | Share: ${v.shareCount} | Like: ${v.likeCount}</small>
                            <form action="${ctx}/cart" method="post" style="margin-top:8px;">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="videoId" value="${v.videoId}">
                                <input type="hidden" name="quantity" value="1">
                                <button type="submit" class="btn" style="padding:4px 10px; font-size:12px;">+ Thêm vào giỏ</button>
                                <a href="${ctx}/video?id=${v.videoId}" class="btn gray" style="padding:4px 10px; font-size:12px;">Chi tiết</a>
                            </form>
                        </td>
                    </c:forEach>
                    <c:forEach begin="${b.videos.size()}" end="2"><td></td></c:forEach>
                </tr>
            </c:otherwise>
        </c:choose>
        <tr>
            <td colspan="3" class="pager">
                <c:if test="${b.page > 1}"><a href="${b.link(b.page - 1)}">&laquo;</a></c:if>
                <c:forEach var="i" begin="1" end="${b.totalPages}">
                    <c:choose>
                        <c:when test="${i == b.page}"><span class="cur">${i}</span></c:when>
                        <c:otherwise><a href="${b.link(i)}">${i}</a></c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:if test="${b.page < b.totalPages}"><a href="${b.link(b.page + 1)}">&raquo;</a></c:if>
            </td>
        </tr>
    </table>
</c:forEach>
</body>
</html>
