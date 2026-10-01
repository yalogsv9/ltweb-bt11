<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Thanh toán đơn hàng (COD)</title>
    <style>
        .checkout-layout { display: flex; gap: 24px; flex-wrap: wrap; }
        .checkout-form { flex: 1 1 450px; background: #fff; padding: 20px; border-radius: 6px; border: 1px solid #ccd; }
        .checkout-summary { flex: 1 1 350px; background: #fff; padding: 20px; border-radius: 6px; border: 1px solid #ccd; }
        .form-group { margin-bottom: 14px; }
        .form-group label { display: block; margin-bottom: 6px; font-weight: bold; }
        .form-group input[type=text], .form-group textarea {
            width: 100%; padding: 10px; border: 1px solid #99a; border-radius: 4px; font-family: inherit; font-size: 14px;
        }
        .payment-box {
            border: 2px solid #1f3a5f; background: #e8edf3; padding: 12px; border-radius: 6px; margin-top: 10px;
        }
        .order-item { display: flex; align-items: center; justify-content: space-between; border-bottom: 1px dashed #ddd; padding: 8px 0; }
        .order-item img { width: 50px; height: 35px; object-fit: cover; border-radius: 4px; margin-right: 10px; }
    </style>
</head>
<body>
    <h2>Thanh toán đơn hàng (COD)</h2>

    <c:if test="${not empty error}">
        <div class="error" style="padding:10px; background:#fce8e6; border-radius:4px; margin-bottom:16px;">
            ${error}
        </div>
    </c:if>

    <div class="checkout-layout">
        <!-- Cot thong tin giao hang -->
        <div class="checkout-form">
            <h3 style="margin-top:0; color:#1f3a5f; border-bottom:2px solid #1f3a5f; padding-bottom:8px;">
                1. Thông tin người nhận hàng
            </h3>
            <form action="${ctx}/checkout" method="post" id="checkoutForm">
                <div class="form-group">
                    <label for="fullname">Họ và tên người nhận: <span style="color:red;">*</span></label>
                    <input type="text" id="fullname" name="fullname" value="${fullname}" placeholder="Ví dụ: Nguyễn Văn A" required>
                </div>

                <div class="form-group">
                    <label for="phone">Số điện thoại: <span style="color:red;">*</span></label>
                    <input type="text" id="phone" name="phone" value="${phone}" placeholder="Ví dụ: 0901234567" required>
                </div>

                <div class="form-group">
                    <label for="address">Địa chỉ nhận hàng chi tiết: <span style="color:red;">*</span></label>
                    <input type="text" id="address" name="address" value="${address}" placeholder="Số nhà, tên đường, phường/xã, quận/huyện, tỉnh/TP" required>
                </div>

                <div class="form-group">
                    <label for="note">Ghi chú đơn hàng (tùy chọn):</label>
                    <textarea id="note" name="note" rows="3" placeholder="Ghi chú thêm về thời gian giao hàng hoặc địa chỉ...">${note}</textarea>
                </div>

                <h3 style="margin-top:20px; color:#1f3a5f; border-bottom:2px solid #1f3a5f; padding-bottom:8px;">
                    2. Phương thức thanh toán
                </h3>
                <div class="payment-box">
                    <label style="cursor:pointer; display:flex; align-items:center; gap:8px; font-weight:bold; color:#1f3a5f;">
                        <input type="radio" name="paymentMethod" value="COD" checked>
                        <span>Thanh toán khi nhận hàng (COD - Cash On Delivery)</span>
                    </label>
                    <p style="margin: 6px 0 0 24px; font-size:13px; color:#555;">
                        Bạn sẽ thanh toán bằng tiền mặt trực tiếp cho nhân viên giao hàng khi nhận được kiện hàng.
                    </p>
                </div>

                <div style="margin-top:24px; display:flex; justify-content:space-between; align-items:center;">
                    <a href="${ctx}/cart" class="btn gray">« Quay lại giỏ hàng</a>
                    <button type="submit" class="btn" style="background:#1c7c3a; font-size:16px; padding:10px 24px; font-weight:bold;">
                        Xác nhận đặt hàng COD »
                    </button>
                </div>
            </form>
        </div>

        <!-- Cot tom tat don hang -->
        <div class="checkout-summary">
            <h3 style="margin-top:0; color:#1f3a5f; border-bottom:2px solid #1f3a5f; padding-bottom:8px;">
                Đơn hàng của bạn (${cart.totalQuantity} sản phẩm)
            </h3>

            <div style="max-height:360px; overflow-y:auto; margin-bottom:16px;">
                <c:forEach var="item" items="${cart.items}">
                    <div class="order-item">
                        <div style="display:flex; align-items:center;">
                            <img src="${item.video.poster}" alt="${item.video.title}">
                            <div>
                                <b style="font-size:14px; color:#222;">${item.video.title}</b><br>
                                <small style="color:#666;">Số lượng: ${item.quantity} x <fmt:formatNumber value="${item.price}" pattern="#,##0"/> đ</small>
                            </div>
                        </div>
                        <div style="font-weight:bold; color:#b02a2a; text-align:right;">
                            <fmt:formatNumber value="${item.subTotal}" pattern="#,##0"/> đ
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div style="background:#f4f6f8; padding:14px; border-radius:6px;">
                <div style="display:flex; justify-content:space-between; margin-bottom:8px;">
                    <span>Tạm tính:</span>
                    <b><fmt:formatNumber value="${cart.totalAmount}" pattern="#,##0"/> đ</b>
                </div>
                <div style="display:flex; justify-content:space-between; margin-bottom:8px;">
                    <span>Phí vận chuyển:</span>
                    <span style="color:#1c7c3a; font-weight:bold;">Miễn phí (COD)</span>
                </div>
                <hr style="border:0; border-top:1px solid #ccd; margin:10px 0;">
                <div style="display:flex; justify-content:space-between; font-size:18px; font-weight:bold;">
                    <span>Tổng thanh toán:</span>
                    <span style="color:#b02a2a;"><fmt:formatNumber value="${cart.totalAmount}" pattern="#,##0"/> đ</span>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
