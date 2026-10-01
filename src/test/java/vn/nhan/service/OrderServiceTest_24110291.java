package vn.nhan.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

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
        Order_24110291 savedOrder;
        List<OrderDetail_24110291> savedDetails;
        int nextId = 101;

        @Override
        public int createOrder(Order_24110291 order, List<OrderDetail_24110291> details) {
            this.savedOrder = order;
            this.savedDetails = details;
            order.setOrderId(nextId);
            return nextId;
        }

        @Override
        public Order_24110291 findById(int orderId) {
            if (savedOrder != null && savedOrder.getOrderId() == orderId) {
                savedOrder.setItems(savedDetails);
                return savedOrder;
            }
            return null;
        }

        @Override
        public List<OrderDetail_24110291> findDetailsByOrderId(int orderId) {
            return savedDetails;
        }

        @Override
        public List<Order_24110291> findByUsername(String username) {
            List<Order_24110291> list = new ArrayList<>();
            if (savedOrder != null && (username == null || username.equals(savedOrder.getUsername()))) {
                list.add(savedOrder);
            }
            return list;
        }

        @Override
        public List<Order_24110291> findByUsernameAndStatus(String username, String status) {
            List<Order_24110291> list = new ArrayList<>();
            if (savedOrder != null) {
                boolean matchUser = (username == null || username.equals(savedOrder.getUsername()));
                boolean matchStatus = (status == null || status.equals(savedOrder.getStatus()));
                if (matchUser && matchStatus) {
                    list.add(savedOrder);
                }
            }
            return list;
        }

        @Override
        public List<Order_24110291> findAll(String status) {
            return findByUsernameAndStatus(null, status);
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

        assertEquals(101, orderId);
        assertNotNull(mockDAO.savedOrder);
        assertEquals("COD", mockDAO.savedOrder.getPaymentMethod());
        assertEquals("Đơn hàng mới", mockDAO.savedOrder.getStatus());
        assertEquals(160000.0, mockDAO.savedOrder.getTotalAmount());
        assertEquals(2, mockDAO.savedDetails.size());

        // Kiem tra chi tiet tung mon
        OrderDetail_24110291 d1 = mockDAO.savedDetails.get(0);
        assertEquals("V001", d1.getVideoId());
        assertEquals(2, d1.getQuantity());
        assertEquals(100000.0, d1.getAmount());

        OrderDetail_24110291 d2 = mockDAO.savedDetails.get(1);
        assertEquals("V002", d2.getVideoId());
        assertEquals(1, d2.getQuantity());
        assertEquals(60000.0, d2.getAmount());
    }

    @Test
    void testFindById() {
        cart.add(v1, 1);
        Order_24110291 order = new Order_24110291();
        order.setReceiverName("Tran Van B");
        order.setReceiverPhone("0912345678");
        order.setReceiverAddress("456 Duong XYZ");

        int id = orderService.createOrder(order, cart);
        Order_24110291 found = orderService.findById(id);

        assertNotNull(found);
        assertEquals("Tran Van B", found.getReceiverName());
        assertEquals(1, found.getItems().size());
    }
}
