package com.online_marketplace_api.awesomity.service.impl;
import com.online_marketplace_api.awesomity.Entity.Order;
import com.online_marketplace_api.awesomity.service.IEmailService;
import com.online_marketplace_api.awesomity.service.IUserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;
    private final IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, @Lazy IUserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    @Override
    public void sendVerificationEmail(String to, String token) {
        String subject = "Email Verification";
        String verificationLink = "http://localhost:8080/api/auth/verify-email?token=" + token;
        String content = "<p>Hello,</p>"
                + "<p>Please verify your email by clicking the link below:</p>"
                + "<p><a href=\"" + verificationLink + "\">Verify Email please as we are !n demo please pick the token from the url and go verify from th!s end po!nt /api/v1/users/verify </a></p>"
                + "<p>If you did not request this, please ignore this email.</p>";

        sendEmail(to, subject, content);
    }

    @Override
    /**
     * Sends a notification email to the customer when an order processing fails
     *
     * @param order The order that failed processing
     * @param reason The reason for the failure
     */
    public void sendOrderFailureNotification(Order order, String reason) {
        try {
            // Get the customer's email from the order or user service
            String customerEmail = getUserEmail(order.getUser().getId());

            // Create a simple mail message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(customerEmail);
            message.setSubject("Important: Issue with your order #" + order.getOrderNumber());

            // Build the email body
            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(order.getUser().getFirstName()).append(",\n\n");
            body.append("We regret to inform you that we encountered an issue while processing your order #")
                    .append(order.getOrderNumber()).append(".\n\n");

            body.append("Issue details: ").append(reason).append("\n\n");

            body.append("Your order has been placed on hold, and our customer service team has been notified. ");
            body.append("No charges have been applied to your payment method at this time.\n\n");

            body.append("What happens next:\n");
            body.append("- A customer service representative will review your order\n");
            body.append("- We'll contact you within 24 hours to resolve this issue\n");
            body.append("- You can also contact us directly at support@yourmarketplace.com\n\n");

            body.append("Order Summary:\n");
            body.append("Order Number: ").append(order.getOrderNumber()).append("\n");
            body.append("Order Date: ").append(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
            body.append("Total Amount: ").append(order.getTotalAmount()).append("\n\n");

            body.append("We apologize for any inconvenience this may have caused.\n\n");
            body.append("Sincerely,\n");
            body.append("The Customer Service Team\n");
            body.append("Your Marketplace");

            message.setText(body.toString());

            // Send the email
            mailSender.send(message);

            // Log the email notification
            logger.info("Order failure notification sent to {} for order #{}",
                    customerEmail, order.getOrderNumber());

            // Also notify admin/support team about the failure
            notifyAdminAboutFailure(order, reason);
        } catch (Exception e) {
            logger.error("Failed to send order failure notification for order #{}: {}",
                    order.getOrderNumber(), e.getMessage(), e);
        }
    }

    @Override
    public void sendOrderConfirmation(Order order) {
        try {
            // Get the customer's email from the order or user service
            String customerEmail = getUserEmail(order.getUser().getId());

            // Create a simple mail message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(customerEmail);
            message.setSubject("Order Confirmation: #" + order.getOrderNumber());

            // Build the email body
            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(order.getUser().getFirstName()).append(",\n\n");
            body.append("Thank you for your order! We've received your order and will begin processing it shortly.\n\n");

            body.append("Order Summary:\n");
            body.append("Order Number: ").append(order.getOrderNumber()).append("\n");
            body.append("Order Date: ").append(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
            body.append("Total Amount: ").append(order.getTotalAmount()).append("\n\n");

            body.append("You will receive another email once your order has been shipped. ");
            body.append("If you have any questions about your order, please contact us at support@yourmarketplace.com");

            message.setText(body.toString());

            // Send the email
            mailSender.send(message);

            // Log the email notification
            logger.info("Order confirmation sent to {} for order #{}",
                    customerEmail, order.getOrderNumber());
        } catch (Exception e) {
            logger.error("Failed to send order confirmation email for order #{}: {}",
                    order.getOrderNumber(), e.getMessage(), e);
        }
    }

    @Override
    public void sendOrderStatusUpdate(Order order) {
        try{
            // Get the customer's email from the order or user service
            String customerEmail = getUserEmail(order.getUser().getId());

            // Create a simple mail message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(customerEmail);
            message.setSubject("Order Status Update: #" + order.getOrderNumber());

            // Build the email body
            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(order.getUser().getFirstName()).append(",\n\n");
            body.append("We're writing to inform you that the status of your order #")
                    .append(order.getOrderNumber()).append(" has been updated.\n\n");

            body.append("Order Status: ").append(order.getStatus()).append("\n");
            body.append("Order Date: ").append(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
            body.append("Total Amount: ").append(order.getTotalAmount()).append("\n\n");

            body.append("You can track your order by visiting your account dashboard. ");
            body.append("If you have any questions about your order, please contact us at support@yourmarketplace.com");

            message.setText(body.toString());

            // Send the email
            mailSender.send(message);

            // Log the email notification
            logger.info("Order status update notification sent to {} for order #{}",
                    customerEmail, order.getOrderNumber());
        } catch (Exception e) {
            logger.error("Failed to send order status update email for order #{}: {}",
                    order.getOrderNumber(), e.getMessage(), e);
        }
    }

    @Override
    public void sendOrderCancellationConfirmation(Order order) {
        try {
            // Get the customer's email from the order or user service
            String customerEmail = getUserEmail(order.getUser().getId());

            // Create a simple mail message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(customerEmail);
            message.setSubject("Order Cancellation Confirmation: #" + order.getOrderNumber());

            // Build the email body
            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(order.getUser().getFirstName()).append(",\n\n");
            body.append("We're writing to confirm that your order #").append(order.getOrderNumber()).append(" has been cancelled.\n\n");

            body.append("Order Summary:\n");
            body.append("Order Number: ").append(order.getOrderNumber()).append("\n");
            body.append("Order Date: ").append(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
            body.append("Total Amount: ").append(order.getTotalAmount()).append("\n\n");

            body.append("Your payment has been refunded to your original payment method. ");
            body.append("Please allow 3-5 business days for the refund to reflect in your account.\n\n");

            body.append("If you have any questions about your order, please contact us at support@yourmarketplace.com");

            message.setText(body.toString());

            // Send the email
            mailSender.send(message);

            // Log the email notification
            logger.info("Order cancellation confirmation sent to {} for order #{}",
                    customerEmail, order.getOrderNumber());
        } catch (Exception e) {
            logger.error("Failed to send order cancellation confirmation email for order #{}: {}",
                    order.getOrderNumber(), e.getMessage(), e);
        }
    }

    /**
     * Notifies administrators/support team about an order failure
     */
    private void notifyAdminAboutFailure(Order order, String reason) {
        SimpleMailMessage adminMessage = new SimpleMailMessage();
        adminMessage.setTo("support@yourmarketplace.com");
        adminMessage.setSubject("ACTION REQUIRED: Order processing failure #" + order.getOrderNumber());

        StringBuilder body = new StringBuilder();
        body.append("Order #").append(order.getOrderNumber()).append(" failed processing.\n\n");
        body.append("Customer: ").append(order.getUser().getFirstName()).append(" (ID: ").append(order.getUser().getId()).append(")\n");
        body.append("Amount: ").append(order.getTotalAmount()).append("\n");
        body.append("Failure reason: ").append(reason).append("\n\n");
        body.append("The customer has been notified. Please review this order and take appropriate action.");

        adminMessage.setText(body.toString());
        mailSender.send(adminMessage);
    }

    /**
     * Get user email from user ID
     * This method would normally fetch the user's email from the user service or repository
     */
    private String getUserEmail(Long userId) {
        return userService.getUserById(userId).getEmail();
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}