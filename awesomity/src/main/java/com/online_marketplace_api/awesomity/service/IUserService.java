package com.online_marketplace_api.awesomity.service;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.RegisterRequest;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.VerificationResponse;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.AuthResponse;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.LoginRequest;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.PasswordChangeRequest;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs;
import com.online_marketplace_api.awesomity.common.userDTO.UserListResponse;
import com.online_marketplace_api.awesomity.common.userDTO.UserResponseDTO;
import com.online_marketplace_api.awesomity.common.userDTO.UserUpdateRequest;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    UserResponseDTO registerUser(RegisterRequest request);
    VerificationResponse verifyEmail(String token);
    AuthResponse login(LoginRequest request);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUser(Long id, UserUpdateRequest request);
    UserResponseDTO updatePassword(Long id, PasswordChangeRequest request);
    void deleteUser(Long id);
    UserListResponse getAllUsers(Pageable pageable);
    UserResponseDTO getUserProfile();
    void resendVerificationEmail(String email);
}