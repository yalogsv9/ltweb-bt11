package vn.nhan.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart_24110291 implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 99;

    private final Map<String, CartItem_24110291> items = new LinkedHashMap<>();

    public Collection<CartItem_24110291> getItems() {
        return items.values();
    }

    public Map<String, CartItem_24110291> getItemMap() {
        return items;
    }

    public CartItem_24110291 getItem(String videoId) {
        return items.get(videoId);
    }

    /**
     * Them san pham vao gio. Neu da ton tai thi cong don so luong (gioi han toi da 99).
     */
    public void add(Video_24110291 video, int quantity) {
        if (video == null || quantity <= 0) {
            return;
        }
        String id = video.getVideoId();
        if (items.containsKey(id)) {
            CartItem_24110291 item = items.get(id);
            int newQty = item.getQuantity() + quantity;
            if (newQty > MAX_QUANTITY) {
                newQty = MAX_QUANTITY;
            }
            item.setQuantity(newQty);
        } else {
            int qty = Math.min(Math.max(quantity, MIN_QUANTITY), MAX_QUANTITY);
            items.put(id, new CartItem_24110291(video, qty, video.getPrice()));
        }
    }

    /**
     * Thay doi so luong san pham trong gio hang trong gioi han 1 - 99.
     * Neu quantity <= 0 thi xoa san pham khoi gio hang.
     */
    public void update(String videoId, int quantity) {
        if (videoId == null || !items.containsKey(videoId)) {
            return;
        }
        if (quantity <= 0) {
            items.remove(videoId);
        } else {
            int qty = Math.min(Math.max(quantity, MIN_QUANTITY), MAX_QUANTITY);
            items.get(videoId).setQuantity(qty);
        }
    }

    /**
     * Xoa 1 san pham khoi gio hang.
     */
    public void remove(String videoId) {
        if (videoId != null) {
            items.remove(videoId);
        }
    }

    /**
     * Xoa toan bo gio hang.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Tong so luong san pham trong gio.
     */
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem_24110291 item : items.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    /**
     * Tong tien cua gio hang.
     */
    public double getTotalAmount() {
        double total = 0.0;
        for (CartItem_24110291 item : items.values()) {
            total += item.getSubTotal();
        }
        return total;
    }

    /**
     * Kiem tra gio hang co rong khong.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
