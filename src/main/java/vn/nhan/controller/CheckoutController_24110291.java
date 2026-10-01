package vn.nhan.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vn.nhan.model.Cart_24110291;
import vn.nhan.model.Order_24110291;
import vn.nhan.model.User_24110291;
import vn.nhan.service.IOrderService_24110291;
import vn.nhan.service.OrderServiceImpl_24110291;

@WebServlet("/checkout")
public class CheckoutController_24110291 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IOrderService_24110291 orderService = new OrderServiceImpl_24110291();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Cart_24110291 cart = (Cart_24110291) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            session.setAttribute("flashErr", "Giỏ hàng của bạn đang trống! Vui lòng thêm sản phẩm trước khi thanh toán.");
            resp.sendRedirect(req.getContextPath() + "/videos");
            return;
        }

        // Tu dong dien thong tin neu nguoi dung da dang nhap
        User_24110291 user = (User_24110291) session.getAttribute("user");
        if (user != null) {
            req.setAttribute("fullname", user.getFullname());
            req.setAttribute("phone", user.getPhone());
        }

        req.setAttribute("cart", cart);
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Cart_24110291 cart = (Cart_24110291) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            session.setAttribute("flashErr", "Giỏ hàng rỗng, không thể đặt hàng!");
            resp.sendRedirect(req.getContextPath() + "/videos");
            return;
        }

        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String note = req.getParameter("note");
        String paymentMethod = req.getParameter("paymentMethod");
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            paymentMethod = "COD";
        }

        // Validate
        if (fullname == null || fullname.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()
                || address == null || address.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ: Họ tên, Số điện thoại và Địa chỉ nhận hàng!");
            req.setAttribute("fullname", fullname);
            req.setAttribute("phone", phone);
            req.setAttribute("address", address);
            req.setAttribute("note", note);
            req.setAttribute("cart", cart);
            req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
            return;
        }

        User_24110291 user = (User_24110291) session.getAttribute("user");
        String username = (user != null) ? user.getUsername() : null;

        Order_24110291 order = new Order_24110291();
        order.setUsername(username);
        order.setReceiverName(fullname.trim());
        order.setReceiverPhone(phone.trim());
        order.setReceiverAddress(address.trim());
        order.setNote(note != null ? note.trim() : "");
        order.setPaymentMethod(paymentMethod.trim());
        order.setStatus("Đơn hàng mới");

        try {
            int orderId = orderService.createOrder(order, cart);

            // Dat hang thanh cong -> xoa gio hang
            cart.clear();

            resp.sendRedirect(req.getContextPath() + "/order-success?id=" + orderId);
        } catch (Exception e) {
            req.setAttribute("error", "Đã có lỗi xảy ra khi tạo đơn hàng: " + e.getMessage());
            req.setAttribute("fullname", fullname);
            req.setAttribute("phone", phone);
            req.setAttribute("address", address);
            req.setAttribute("note", note);
            req.setAttribute("cart", cart);
            req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
        }
    }
}
