package vn.nhan.service;

import java.util.ArrayList;
import java.util.List;

import vn.nhan.dao.IOrderDAO_24110291;
import vn.nhan.dao.OrderDAOImpl_24110291;
import vn.nhan.model.CartItem_24110291;
import vn.nhan.model.Cart_24110291;
import vn.nhan.model.OrderDetail_24110291;
import vn.nhan.model.Order_24110291;

public class OrderServiceImpl_24110291 implements IOrderService_24110291 {

    private final IOrderDAO_24110291 orderDAO;

    public OrderServiceImpl_24110291() {
        this.orderDAO = new OrderDAOImpl_24110291();
    }

    public OrderServiceImpl_24110291(IOrderDAO_24110291 orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public int createOrder(Order_24110291 order, Cart_24110291 cart) {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng rỗng, không thể tạo đơn hàng!");
        }

        order.setTotalAmount(cart.getTotalAmount());
        if (order.getPaymentMethod() == null || order.getPaymentMethod().trim().isEmpty()) {
            order.setPaymentMethod("COD");
        }
        if (order.getStatus() == null || order.getStatus().trim().isEmpty()) {
            order.setStatus("Đơn hàng mới");
        }

        List<OrderDetail_24110291> details = new ArrayList<>();
        for (CartItem_24110291 item : cart.getItems()) {
            OrderDetail_24110291 d = new OrderDetail_24110291();
            d.setVideoId(item.getVideo().getVideoId());
            d.setPrice(item.getPrice());
            d.setQuantity(item.getQuantity());
            d.setAmount(item.getSubTotal());
            details.add(d);
        }

        return orderDAO.createOrder(order, details);
    }

    @Override
    public Order_24110291 findById(int orderId) {
        return orderDAO.findById(orderId);
    }

    @Override
    public List<Order_24110291> findOrders(String username, String status) {
        return orderDAO.findByUsernameAndStatus(username, status);
    }

    @Override
    public List<Order_24110291> findAllOrders(String status) {
        return orderDAO.findAll(status);
    }
}
