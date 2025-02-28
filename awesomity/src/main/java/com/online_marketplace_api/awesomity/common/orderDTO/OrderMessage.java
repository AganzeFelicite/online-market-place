package com.online_marketplace_api.awesomity.common.orderDTO;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderMessage implements Serializable {
    private Long orderId;
    private String orderNumber;
    private Long userId;
    private String userName;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private String paymentId;
    private String action;

    // Default Constructor
    public OrderMessage() {}

    // Parameterized Constructor
    public OrderMessage(Long orderId, String orderNumber, Long userId, String userName,
                        BigDecimal totalAmount, String shippingAddress, String paymentMethod,
                        String paymentId, String action) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.userName = userName;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.paymentId = paymentId;
        this.action = action;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", totalAmount=" + totalAmount +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
