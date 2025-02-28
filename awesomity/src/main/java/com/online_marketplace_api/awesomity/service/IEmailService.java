package com.online_marketplace_api.awesomity.service;

import com.online_marketplace_api.awesomity.Entity.Order;

public interface IEmailService {
    void sendVerificationEmail(String to, String token);

    void sendOrderFailureNotification(Order order, String reason);

    void sendOrderConfirmation(Order order);

    void sendOrderStatusUpdate(Order order);

    void sendOrderCancellationConfirmation(Order order);
}

