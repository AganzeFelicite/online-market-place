package com.online_marketplace_api.awesomity.tests;

import com.online_marketplace_api.awesomity.common.userDTO.*;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.PasswordChangeRequest;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.RegisterRequest;
import com.online_marketplace_api.awesomity.controllers.UserController;
import com.online_marketplace_api.awesomity.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@DirtiesContext
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private IUserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "Password123!");
        UserResponseDTO userResponseDTO = new UserResponseDTO(1L, "John", "Doe", "john.doe@example.com");
        when(userService.registerUser(registerRequest)).thenReturn(userResponseDTO);

        ResponseEntity<UserRegistrationResponse> response = userController.registerUser(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody().getUserResponse());
        assertEquals("Registration successful! Please check your email for verification.", response.getBody().getMessage());
        verify(userService, times(1)).registerUser(registerRequest);
    }

    @Test
    public void testVerifyEmail() {
        String token = "verification-token";
        String successMessage = "Email verified successfully";

        AuthDTOs.VerificationResponse verificationResponse = AuthDTOs.VerificationResponse.builder()
                .message(successMessage)
                .success(true)
                .build();

        when(userService.verifyEmail(token)).thenReturn(verificationResponse);

        ResponseEntity<?> response = userController.verifyEmail(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(verificationResponse, response.getBody());
        verify(userService, times(1)).verifyEmail(token);
    }
    @Test
    public void testGetUserProfile() {
        UserResponseDTO userResponseDTO = new UserResponseDTO(1L, "John", "Doe", "john.doe@example.com");
        when(userService.getUserProfile()).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> response = userController.getUserProfile();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
        verify(userService, times(1)).getUserProfile();
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        UserResponseDTO userResponseDTO = new UserResponseDTO(userId, "John", "Doe", "john.doe@example.com");
        when(userService.getUserById(userId)).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        UserUpdateRequest updateRequest = new UserUpdateRequest("Jane", "Smith", "jane.smith@example.com", "123 Street", "123456789");
        UserResponseDTO userResponseDTO = new UserResponseDTO(userId, "Jane", "Smith", "jane.smith@example.com");
        when(userService.updateUser(userId, updateRequest)).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> response = userController.updateUser(userId, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Jane", response.getBody().getFirstName());
        assertEquals("Smith", response.getBody().getLastName());
        assertEquals("jane.smith@example.com", response.getBody().getEmail());
        verify(userService, times(1)).updateUser(userId, updateRequest);
    }

    @Test
    public void testUpdatePassword() {
        Long userId = 1L;
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("oldPassword", "newPassword", "newPassword");
        UserResponseDTO userResponseDTO = new UserResponseDTO(userId, "John", "Doe", "john.doe@example.com");
        when(userService.updatePassword(userId, passwordChangeRequest)).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> response = userController.updatePassword(userId, passwordChangeRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
        verify(userService, times(1)).updatePassword(userId, passwordChangeRequest);
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    public void testGetAllUsers() {
        List<UserResponseDTO> userList = Arrays.asList(
                new UserResponseDTO(1L, "John", "Doe", "john.doe@example.com"),
                new UserResponseDTO(2L, "Jane", "Smith", "jane.smith@example.com")
        );

        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setUsers(userList);
        userListResponse.setCurrentPage(0);
        userListResponse.setTotalUsers(2);
        userListResponse.setTotalPages(1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        when(userService.getAllUsers(any(Pageable.class))).thenReturn(userListResponse);

        ResponseEntity<UserListResponse> response = userController.getAllUsers(0, 10, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userListResponse, response.getBody());
        assertEquals(2, response.getBody().getUsers().size());
        verify(userService, times(1)).getAllUsers(any(Pageable.class));
    }


    @Test
    public void testGetAllUsersWithCustomPagination() {
        // Test with custom pagination and sorting parameters
        Pageable customPageable = PageRequest.of(2, 5, Sort.by(Sort.Direction.DESC, "email"));
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setCurrentPage(2);
        userListResponse.setTotalUsers(15);
        userListResponse.setTotalPages(3);

        when(userService.getAllUsers(any(Pageable.class))).thenReturn(userListResponse);

        ResponseEntity<UserListResponse> response = userController.getAllUsers(0, 10, "id", "asc");
    }


    @Test
    public void testResendVerificationEmail() {
        String email = "john.doe@example.com";
        doNothing().when(userService).resendVerificationEmail(email);

        ResponseEntity<Void> response = userController.resendVerificationEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).resendVerificationEmail(email);
    }


}