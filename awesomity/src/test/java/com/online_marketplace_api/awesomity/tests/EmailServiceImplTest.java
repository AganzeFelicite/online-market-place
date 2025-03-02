package com.online_marketplace_api.awesomity.tests;

import com.online_marketplace_api.awesomity.Entity.Order;
import com.online_marketplace_api.awesomity.Entity.User;
import com.online_marketplace_api.awesomity.common.userDTO.UserResponseDTO;
import com.online_marketplace_api.awesomity.enums.OrderStatus;
import com.online_marketplace_api.awesomity.service.IEmailService;
import com.online_marketplace_api.awesomity.service.IUserService;
import com.online_marketplace_api.awesomity.service.impl.EmailServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private IUserService userService;

    @InjectMocks
    private EmailServiceImpl emailService;

    private Order order;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setEmail("john@example.com");

        order = new Order();
        order.setId(1L);
        order.setOrderNumber("12345");
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.valueOf(100.0));
        order.setStatus(OrderStatus.PENDING);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setEmail("john@example.com");
        when(userService.getUserById(1L)).thenReturn(userResponseDTO);
    }

//    @Test
//    void testSendVerificationEmail() throws MessagingException {
//        MimeMessage mimeMessage = mock(MimeMessage.class);
//        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        doNothing().when(mailSender).send(any(MimeMessage.class));
//
//        emailService.sendVerificationEmail("test@example.com", "token123");
//
//        verify(mailSender, times(1)).send(any(MimeMessage.class));
//    }

    @Test
    void testSendOrderFailureNotification() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendOrderFailureNotification(order, "Insufficient stock");

        verify(mailSender, times(2)).send(any(SimpleMailMessage.class)); // 1 for customer, 1 for admin
    }

    @Test
    void testSendOrderConfirmation() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendOrderConfirmation(order);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendOrderStatusUpdate() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendOrderStatusUpdate(order);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendOrderCancellationConfirmation() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendOrderCancellationConfirmation(order);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

//    @Test
//    void testSendVerificationEmail_MessagingException() throws MessagingException {
//        MimeMessage mimeMessage = mock(MimeMessage.class);
//        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        doThrow(new MessagingException("Test exception")).when(mailSender).send(any(MimeMessage.class));
//
//        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> emailService.sendVerificationEmail("test@example.com", "token123"));
//
//        verify(mailSender, times(1)).send(any(MimeMessage.class));
//    }
}