package vn.nhan.service;

import java.util.List;

import vn.nhan.model.Cart_24110291;
import vn.nhan.model.Order_24110291;

public interface IOrderService_24110291 {
    /**
     * Tao don hang tu thong tin nhan hang va gio hang hien tai.
     * @return OrderId vua tao
     */
    int createOrder(Order_24110291 order, Cart_24110291 cart);

    /**
     * Tim don hang theo ID day du thong tin san pham.
     */
    Order_24110291 findById(int orderId);

    /**
     * Lay danh sach don hang theo username va loc theo trang thai (neu co).
     */
    List<Order_24110291> findOrders(String username, String status);

    /**
     * Lay tat ca don hang (cho phep loc theo trang thai).
     */
    List<Order_24110291> findAllOrders(String status);

    /**
     * Dem so luong don hang theo tung trang thai.
     */
    java.util.Map<String, Integer> countOrdersByStatus(String username);

    /**
     * Dem tong so luong don hang.
     */
    int countTotal(String username);
}
