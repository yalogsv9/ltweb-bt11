package vn.nhan.model;

import java.io.Serializable;

public class CartItem_24110291 implements Serializable {
    private static final long serialVersionUID = 1L;

    private Video_24110291 video;
    private int quantity;
    private double price;

    public CartItem_24110291() {
    }

    public CartItem_24110291(Video_24110291 video, int quantity, double price) {
        this.video = video;
        this.quantity = quantity;
        this.price = price;
    }

    public Video_24110291 getVideo() {
        return video;
    }

    public void setVideo(Video_24110291 video) {
        this.video = video;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSubTotal() {
        return price * quantity;
    }
}
