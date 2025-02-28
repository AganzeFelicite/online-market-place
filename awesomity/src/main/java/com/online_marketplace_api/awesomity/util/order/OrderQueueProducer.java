package com.online_marketplace_api.awesomity.util.order;

import com.online_marketplace_api.awesomity.common.orderDTO.OrderMessage;
import com.online_marketplace_api.awesomity.util.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderQueueProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderQueueProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderToQueue(OrderMessage orderMessage) {
//        if (orderMessage.getAction() == null) {
//            // Default action is CREATE for new orders
//            orderMessage.setAction("CREATE");
//        }

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                orderMessage
        );
    }
}