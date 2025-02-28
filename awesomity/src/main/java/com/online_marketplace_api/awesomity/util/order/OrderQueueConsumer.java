package com.online_marketplace_api.awesomity.util.order;
import com.online_marketplace_api.awesomity.Entity.Order;
import com.online_marketplace_api.awesomity.Repository.IOrderRepository;

import com.online_marketplace_api.awesomity.common.orderDTO.OrderMessage;
import com.online_marketplace_api.awesomity.enums.OrderStatus;
import com.online_marketplace_api.awesomity.service.IEmailService;
import com.online_marketplace_api.awesomity.service.IProductService;
import com.online_marketplace_api.awesomity.util.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderQueueConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderQueueConsumer.class);

    private final IOrderRepository orderRepository;

    private final IProductService inventoryService;
    private final IEmailService emailService;

    @Autowired
    public OrderQueueConsumer(
            IOrderRepository orderRepository,
            IProductService inventoryService,

            IEmailService emailService) {
        this.orderRepository = orderRepository;

        this.inventoryService = inventoryService;
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    @Transactional
    public void processOrder(OrderMessage orderMessage) {
        logger.info("Processing order: {}, action: {}", orderMessage.getOrderNumber(), orderMessage.getAction());

        try {
            Order order = orderRepository.findById(orderMessage.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderMessage.getOrderId()));

            switch(orderMessage.getAction()) {
                case "CREATE":
                    processNewOrder(order);
                    break;
                case "STATUS_UPDATE":
                    processStatusUpdate(order);
                    break;
                case "CANCELLED":
                    processCancellation(order);
                    break;
                default:
                    logger.warn("Unknown action: {}", orderMessage.getAction());
            }

        } catch (Exception e) {
            logger.error("Error processing order: " + e.getMessage(), e);
            // You could implement retry logic or move to a dead letter queue here

        }
    }

    private void processNewOrder(Order order) {
        try {

                    emailService.sendOrderConfirmation(order);

//                    logger.info("Order processed successfully: {}", order.getOrderNumber());



        } catch (Exception e) {
            handleOrderFailure(order, e.getMessage());
        }
    }

    private void processStatusUpdate(Order order) {
        // Send email notification about status update
        emailService.sendOrderStatusUpdate(order);
//        logger.info("Order status update notification sent: {}", order.getOrderNumber());
    }

    private void processCancellation(Order order) {
        try {



            emailService.sendOrderCancellationConfirmation(order);

//            logger.info("Order cancellation processed: {}", order.getOrderNumber());
        } catch (Exception e) {
            logger.error("Error processing cancellation for order {}",
                     e.getMessage(), e);
        }
    }

    private void handleOrderFailure(Order order, String reason) {
//        logger.error("Order processing failed: {} - Reason: {}", order.getOrderNumber(), reason);

        // Update order status to FAILED
//        order.setStatus(OrderStatus.FAILED);
//        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // Notify customer about the failure
        emailService.sendOrderFailureNotification(order, reason);
    }
}