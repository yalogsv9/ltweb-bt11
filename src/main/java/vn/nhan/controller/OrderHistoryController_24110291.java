package vn.nhan.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vn.nhan.model.Order_24110291;
import vn.nhan.model.User_24110291;
import vn.nhan.service.IOrderService_24110291;
import vn.nhan.service.OrderServiceImpl_24110291;

@WebServlet("/order-history")
public class OrderHistoryController_24110291 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IOrderService_24110291 orderService = new OrderServiceImpl_24110291();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User_24110291 user = (User_24110291) session.getAttribute("user");

        String status = req.getParameter("status");
        if (status != null) {
            status = status.trim();
            if ("all".equalsIgnoreCase(status) || status.isEmpty()) {
                status = null;
            }
        }

        String username = null;
        boolean isAdmin = (user != null && user.isAdmin());

        if (user != null && !isAdmin) {
            // Nguoi dung thuong da dang nhap: loc theo username cua ho
            username = user.getUsername();
        } else if (isAdmin) {
            // Admin co the xem tat ca hoac xem theo user cu the neu muon
            String queryUser = req.getParameter("username");
            if (queryUser != null && !queryUser.trim().isEmpty()) {
                username = queryUser.trim();
            }
        }

        // Lay danh sach don hang theo trang thai
        List<Order_24110291> orders;
        if (username != null) {
            orders = orderService.findOrders(username, status);
        } else {
            orders = orderService.findAllOrders(status);
        }

        // Lay thong ke so luong don hang theo 8 trang thai
        Map<String, Integer> statusCounts = orderService.countOrdersByStatus(username);
        int totalOrders = orderService.countTotal(username);

        req.setAttribute("orders", orders);
        req.setAttribute("currentStatus", status != null ? status : "all");
        req.setAttribute("statusCounts", statusCounts);
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("isAdmin", isAdmin);

        req.getRequestDispatcher("/WEB-INF/views/order-history.jsp").forward(req, resp);
    }
}
