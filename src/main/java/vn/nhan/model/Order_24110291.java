package vn.nhan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order_24110291 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderId;
    private String username;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;
    private double totalAmount;
    private String paymentMethod = "COD";
    private String status = "Đơn hàng mới";
    private Date createdAt;
    private List<OrderDetail_24110291> items = new ArrayList<>();

    public Order_24110291() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderDetail_24110291> getItems() {
        return items;
    }

    public void setItems(List<OrderDetail_24110291> items) {
        this.items = items;
    }

    /**
     * Tra ve CSS class/style mau sac cho badge trang thai.
     */
    public String getStatusBadgeClass() {
        if (status == null) return "badge-default";
        switch (status.trim()) {
            case "Đơn hàng mới":
                return "badge-primary";
            case "Đã xác nhận":
                return "badge-info";
            case "Chuẩn bị hàng":
                return "badge-warning";
            case "Vận chuyển":
                return "badge-shipping";
            case "Giao hàng":
                return "badge-delivery";
            case "Đã giao":
                return "badge-success";
            case "Đơn hàng hủy":
                return "badge-danger";
            case "Đơn hàng hoàn":
                return "badge-refund";
            default:
                return "badge-default";
        }
    }
}
