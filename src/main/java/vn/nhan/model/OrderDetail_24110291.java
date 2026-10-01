package vn.nhan.model;

import java.io.Serializable;

public class OrderDetail_24110291 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int detailId;
    private int orderId;
    private String videoId;
    private String videoTitle;
    private String videoPoster;
    private double price;
    private int quantity;
    private double amount;

    public OrderDetail_24110291() {
    }

    public OrderDetail_24110291(int orderId, String videoId, double price, int quantity, double amount) {
        this.orderId = orderId;
        this.videoId = videoId;
        this.price = price;
        this.quantity = quantity;
        this.amount = amount;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoPoster() {
        return videoPoster;
    }

    public void setVideoPoster(String videoPoster) {
        this.videoPoster = videoPoster;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
