package vn.nhan.dao;

import java.util.List;

import vn.nhan.model.OrderDetail_24110291;
import vn.nhan.model.Order_24110291;

public interface IOrderDAO_24110291 {
    /**
     * Tao don hang va cac chi tiet don hang trong 1 transaction.
     * @return OrderId vua tao
     */
    int createOrder(Order_24110291 order, List<OrderDetail_24110291> details);

    /**
     * Tim don hang theo ID.
     */
    Order_24110291 findById(int orderId);

    /**
     * Lay danh sach chi tiet mon hang cua mot don hang.
     */
    List<OrderDetail_24110291> findDetailsByOrderId(int orderId);

    /**
     * Lay danh sach don hang theo username (neu username null se lay tat ca).
     */
    List<Order_24110291> findByUsername(String username);

    /**
     * Lay danh sach don hang theo username va trang thai cu the.
     */
    List<Order_24110291> findByUsernameAndStatus(String username, String status);

    /**
     * Lay tat ca don hang (ho tro admin hoac tra cuu tat ca) co loc theo trang thai neu co.
     */
    List<Order_24110291> findAll(String status);

    /**
     * Dem so luong don hang theo tung trang thai cho username (hoac tat ca neu username null).
     */
    java.util.Map<String, Integer> countOrdersByStatus(String username);

    /**
     * Dem tong so don hang cho username (hoac tat ca neu username null).
     */
    int countTotal(String username);
}
