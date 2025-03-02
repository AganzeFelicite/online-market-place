package com.online_marketplace_api.awesomity.tests;

import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.AuthResponse;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.LoginRequest;
import com.online_marketplace_api.awesomity.controllers.AuthController;
import com.online_marketplace_api.awesomity.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest("test@example.com", "password");
        AuthResponse response = new AuthResponse("token", null);

        when(userService.login(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}