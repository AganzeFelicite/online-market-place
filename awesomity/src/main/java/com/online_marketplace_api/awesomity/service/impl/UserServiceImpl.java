package com.online_marketplace_api.awesomity.service.impl;

import com.online_marketplace_api.awesomity.common.exceptions.GlobalExceptionHandler;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.RegisterRequest;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.VerificationResponse;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.AuthResponse;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.LoginRequest;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.PasswordChangeRequest;

import com.online_marketplace_api.awesomity.Entity.User;

import com.online_marketplace_api.awesomity.Repository.IUserRepository;
import com.online_marketplace_api.awesomity.common.userDTO.UserListResponse;
import com.online_marketplace_api.awesomity.common.userDTO.UserResponseDTO;
import com.online_marketplace_api.awesomity.common.userDTO.UserUpdateRequest;
import com.online_marketplace_api.awesomity.enums.Role;

import com.online_marketplace_api.awesomity.security.JwtUtil;

import com.online_marketplace_api.awesomity.security.SecurityUtils;
import com.online_marketplace_api.awesomity.service.IEmailService;
import com.online_marketplace_api.awesomity.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final IEmailService emailService;

    @Autowired
    public UserServiceImpl(
            IUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            @Lazy IEmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public UserResponseDTO registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole() != null ? Role.valueOf(request.getRole()) : Role.BUYER)
                .emailVerified(false)
                .verificationToken(generateVerificationToken())
                .tokenExpiryDate(LocalDateTime.now().plusHours(24))
                .build();

        User savedUser = userRepository.save(user);
        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());

        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public VerificationResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Invalid verification token"));

        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification token has expired");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);

        return VerificationResponse.builder()
                .success(true)
                .message("Email verified successfully")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        if (!user.isEmailVerified()) {
            throw new GlobalExceptionHandler.UnauthorizedException("Email not verified");
        }

        String token = jwtUtil.generateToken(user);
        // Refresh token would be implemented here in a production environment

        return AuthResponse.builder()
                .token(token)
                .user(mapToUserResponse(user))
                .build();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = getUserEntityById(id);
        return mapToUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        User user = getUserEntityById(id);

        // Check if user is trying to update someone else's profile and is not an admin
        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new GlobalExceptionHandler.UnauthorizedException("You don't have permission to update this user");
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(request.getEmail());
            user.setEmailVerified(false);
            user.setVerificationToken(generateVerificationToken());
            user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));
            emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        return mapToUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDTO updatePassword(Long id, PasswordChangeRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();

        // Check if user is trying to update someone else's password
        if (!currentUser.getId().equals(id)) {
            throw new GlobalExceptionHandler.UnauthorizedException("You don't have permission to update this user's password");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password don't match");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return mapToUserResponse(userRepository.save(currentUser));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();

        // Check if user is trying to delete someone else and is not an admin
        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new GlobalExceptionHandler.UnauthorizedException("You don't have permission to delete this user");
        }

        User user = getUserEntityById(id);
        userRepository.delete(user);
    }

    @Override
    public UserListResponse getAllUsers(Pageable pageable) {
        User currentUser = SecurityUtils.getCurrentUser();

        // Only admins can list all users
        if (currentUser.getRole() != Role.ADMIN) {
            throw new GlobalExceptionHandler.UnauthorizedException("You don't have permission to access this resource");
        }

        Page<User> userPage = userRepository.findAll(pageable);
        List<UserResponseDTO> users = userPage.getContent().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());

        return UserListResponse.builder()
                .users(users)
                .totalUsers(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .currentPage(pageable.getPageNumber())
                .build();
    }

    @Override
    public UserResponseDTO getUserProfile() {
        User currentUser = SecurityUtils.getCurrentUser();
        return mapToUserResponse(currentUser);
    }

    @Override
    @Transactional
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("User not found with email: " + email));

        if (user.isEmailVerified()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        user.setVerificationToken(generateVerificationToken());
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
    }

    // Helper methods
    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("User not found with id: " + id));
    }

    private UserResponseDTO mapToUserResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().toString())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}