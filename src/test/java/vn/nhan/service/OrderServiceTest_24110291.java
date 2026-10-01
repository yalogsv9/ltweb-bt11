package vn.nhan.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import vn.nhan.dao.IOrderDAO_24110291;
import vn.nhan.model.Cart_24110291;
import vn.nhan.model.OrderDetail_24110291;
import vn.nhan.model.Order_24110291;
import vn.nhan.model.Video_24110291;

public class OrderServiceTest_24110291 {

    private IOrderService_24110291 orderService;
    private MockOrderDAO mockDAO;
    private Cart_24110291 cart;
    private Video_24110291 v1;
    private Video_24110291 v2;

    static class MockOrderDAO implements IOrderDAO_24110291 {
        List<Order_24110291> ordersList = new ArrayList<>();
        int nextId = 1;

        @Override
        public int createOrder(Order_24110291 order, List<OrderDetail_24110291> details) {
            order.setOrderId(nextId++);
            order.setItems(details);
            ordersList.add(order);
            return order.getOrderId();
        }

        @Override
        public Order_24110291 findById(int orderId) {
            for (Order_24110291 o : ordersList) {
                if (o.getOrderId() == orderId) return o;
            }
            return null;
        }

        @Override
        public List<OrderDetail_24110291> findDetailsByOrderId(int orderId) {
            Order_24110291 o = findById(orderId);
            return o != null ? o.getItems() : new ArrayList<>();
        }

        @Override
        public List<Order_24110291> findByUsername(String username) {
            return findByUsernameAndStatus(username, null);
        }

        @Override
        public List<Order_24110291> findByUsernameAndStatus(String username, String status) {
            List<Order_24110291> result = new ArrayList<>();
            for (Order_24110291 o : ordersList) {
                boolean matchUser = (username == null || username.equals(o.getUsername()));
                boolean matchStatus = (status == null || "all".equalsIgnoreCase(status) || status.equals(o.getStatus()));
                if (matchUser && matchStatus) {
                    result.add(o);
                }
            }
            return result;
        }

        @Override
        public List<Order_24110291> findAll(String status) {
            return findByUsernameAndStatus(null, status);
        }

        @Override
        public Map<String, Integer> countOrdersByStatus(String username) {
            Map<String, Integer> map = new HashMap<>();
            for (Order_24110291 o : ordersList) {
                if (username == null || username.equals(o.getUsername())) {
                    map.put(o.getStatus(), map.getOrDefault(o.getStatus(), 0) + 1);
                }
            }
            return map;
        }

        @Override
        public int countTotal(String username) {
            int count = 0;
            for (Order_24110291 o : ordersList) {
                if (username == null || username.equals(o.getUsername())) {
                    count++;
                }
            }
            return count;
        }
    }

    @BeforeEach
    void setUp() {
        mockDAO = new MockOrderDAO();
        orderService = new OrderServiceImpl_24110291(mockDAO);

        cart = new Cart_24110291();

        v1 = new Video_24110291();
        v1.setVideoId("V001");
        v1.setTitle("Video Am Nhac 1");
        v1.setPrice(50000.0);

        v2 = new Video_24110291();
        v2.setVideoId("V002");
        v2.setTitle("Video Am Nhac 2");
        v2.setPrice(60000.0);
    }

    @Test
    void testCreateOrder_EmptyCartThrowsException() {
        Order_24110291 order = new Order_24110291();
        order.setReceiverName("Nguyen Van A");
        order.setReceiverPhone("0901234567");
        order.setReceiverAddress("123 Duong ABC");

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(order, cart);
        });
    }

    @Test
    void testCreateOrder_COD_Success() {
        cart.add(v1, 2); // 100,000
        cart.add(v2, 1); // 60,000

        Order_24110291 order = new Order_24110291();
        order.setUsername("user1");
        order.setReceiverName("Nguyen Van A");
        order.setReceiverPhone("0901234567");
        order.setReceiverAddress("123 Duong ABC, Quan 1");
        order.setNote("Giao gio hanh chinh");

        int orderId = orderService.createOrder(order, cart);

        assertEquals(1, orderId);
        Order_24110291 saved = mockDAO.findById(orderId);
        assertNotNull(saved);
        assertEquals("COD", saved.getPaymentMethod());
        assertEquals("Đơn hàng mới", saved.getStatus());
        assertEquals(160000.0, saved.getTotalAmount());
        assertEquals(2, saved.getItems().size());

        // Kiem tra chi tiet tung mon
        OrderDetail_24110291 d1 = saved.getItems().get(0);
        assertEquals("V001", d1.getVideoId());
        assertEquals(2, d1.getQuantity());
        assertEquals(100000.0, d1.getAmount());

        OrderDetail_24110291 d2 = saved.getItems().get(1);
        assertEquals("V002", d2.getVideoId());
        assertEquals(1, d2.getQuantity());
        assertEquals(60000.0, d2.getAmount());
    }

    @Test
    void test8OrderStatusesFilter() {
        String[] statuses = {
            "Đơn hàng mới",
            "Đã xác nhận",
            "Chuẩn bị hàng",
            "Vận chuyển",
            "Giao hàng",
            "Đã giao",
            "Đơn hàng hủy",
            "Đơn hàng hoàn"
        };

        // Tao 8 don hang tuong ung voi 8 trang thai
        cart.add(v1, 1);
        for (String st : statuses) {
            Order_24110291 o = new Order_24110291();
            o.setUsername("user1");
            o.setReceiverName("Khach " + st);
            o.setReceiverPhone("0900000000");
            o.setReceiverAddress("TP.HCM");
            o.setStatus(st);
            orderService.createOrder(o, cart);
        }

        assertEquals(8, orderService.countTotal("user1"));

        // Kiem tra loc rieng tung trang thai
        for (String st : statuses) {
            List<Order_24110291> list = orderService.findOrders("user1", st);
            assertEquals(1, list.size(), "Trạng thái " + st + " phải có đúng 1 đơn hàng");
            assertEquals(st, list.get(0).getStatus());
        }

        // Kiem tra loc 'all' hoac null
        List<Order_24110291> allOrders = orderService.findOrders("user1", null);
        assertEquals(8, allOrders.size());

        // Kiem tra dem so luong tung trang thai
        Map<String, Integer> counts = orderService.countOrdersByStatus("user1");
        assertEquals(8, counts.size());
        for (String st : statuses) {
            assertEquals(1, counts.get(st));
        }
    }
}
