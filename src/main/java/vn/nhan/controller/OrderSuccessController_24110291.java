package vn.nhan.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.nhan.model.Order_24110291;
import vn.nhan.service.IOrderService_24110291;
import vn.nhan.service.OrderServiceImpl_24110291;

@WebServlet("/order-success")
public class OrderSuccessController_24110291 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IOrderService_24110291 orderService = new OrderServiceImpl_24110291();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        int orderId = 0;
        try {
            if (idParam != null) {
                orderId = Integer.parseInt(idParam.trim());
            }
        } catch (Exception ignored) {
        }

        Order_24110291 order = (orderId > 0) ? orderService.findById(orderId) : null;
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/videos");
            return;
        }

        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/order-success.jsp").forward(req, resp);
    }
}
