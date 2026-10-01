package vn.nhan.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartTest_24110291 {

    private Cart_24110291 cart;
    private Video_24110291 v1;
    private Video_24110291 v2;

    @BeforeEach
    void setUp() {
        cart = new Cart_24110291();

        v1 = new Video_24110291();
        v1.setVideoId("V001");
        v1.setTitle("Video 1");
        v1.setPrice(50000.0);

        v2 = new Video_24110291();
        v2.setVideoId("V002");
        v2.setTitle("Video 2");
        v2.setPrice(75000.0);
    }

    @Test
    void testAddToCart_NewItem() {
        cart.add(v1, 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getTotalQuantity());
        assertEquals(100000.0, cart.getTotalAmount());
    }

    @Test
    void testAddToCart_Accumulate() {
        cart.add(v1, 2);
        cart.add(v1, 3);
        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getTotalQuantity());
        assertEquals(250000.0, cart.getTotalAmount());
    }

    @Test
    void testAddToCart_LimitMax99() {
        cart.add(v1, 90);
        cart.add(v1, 20); // Vuot qua 99
        assertEquals(99, cart.getItem("V001").getQuantity());
        assertEquals(99, cart.getTotalQuantity());
    }

    @Test
    void testUpdateQuantity_Valid() {
        cart.add(v1, 1);
        cart.update("V001", 5);
        assertEquals(5, cart.getItem("V001").getQuantity());
        assertEquals(250000.0, cart.getTotalAmount());
    }

    @Test
    void testUpdateQuantity_MaxClamp() {
        cart.add(v1, 1);
        cart.update("V001", 150);
        assertEquals(99, cart.getItem("V001").getQuantity());
    }

    @Test
    void testUpdateQuantity_ZeroOrNegativeRemovesItem() {
        cart.add(v1, 1);
        cart.update("V001", 0);
        assertNull(cart.getItem("V001"));
        assertTrue(cart.isEmpty());

        cart.add(v2, 2);
        cart.update("V002", -5);
        assertNull(cart.getItem("V002"));
        assertTrue(cart.isEmpty());
    }

    @Test
    void testRemoveItem() {
        cart.add(v1, 1);
        cart.add(v2, 2);
        assertEquals(2, cart.getItems().size());

        cart.remove("V001");
        assertNull(cart.getItem("V001"));
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getTotalQuantity());
    }

    @Test
    void testClearCart() {
        cart.add(v1, 2);
        cart.add(v2, 3);
        assertFalse(cart.isEmpty());

        cart.clear();
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getTotalQuantity());
        assertEquals(0.0, cart.getTotalAmount());
    }
}
