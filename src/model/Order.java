package model;

public class Order {
    private int orderId;
    private String customerName;
    private String customerPhone;
    double totalAmount;

    public Order(String customerName, String customerPhone, double totalAmount) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}