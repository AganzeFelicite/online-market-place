package com.online_marketplace_api.awesomity.controllers;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.PasswordChangeRequest;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.RegisterRequest;
import com.online_marketplace_api.awesomity.common.userDTO.UserListResponse;
import com.online_marketplace_api.awesomity.common.userDTO.UserRegistrationResponse;
import com.online_marketplace_api.awesomity.common.userDTO.UserResponseDTO;
import com.online_marketplace_api.awesomity.common.userDTO.UserUpdateRequest;
import com.online_marketplace_api.awesomity.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;


    @Operation(summary = "Register a new user", description = "This endpoint allows users to register with their details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")

    public ResponseEntity<UserRegistrationResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        // Register the user
        UserResponseDTO response = userService.registerUser(request);

        // Create the custom response with the message
        String message = "Registration successful! Please check your email for verification.";
        UserRegistrationResponse customResponse = new UserRegistrationResponse(response, message);

        return new ResponseEntity<>(customResponse, HttpStatus.CREATED);
    }


    @Operation(summary = "Verify user email", description = "This endpoint allows users to verify their email addresses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email successfully verified"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(userService.verifyEmail(token));
    }

    @Operation(summary = "Get user profile", description = "This endpoint allows users to get their profile details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }


    @Operation(summary = "Get user by ID", description = "This endpoint allows users to get user details by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Update user details", description = "This endpoint allows users to update their details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }


    @Operation(summary = "Update user password", description = "This endpoint allows users to update their passwords.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PutMapping("/{id}/password")

    public ResponseEntity<UserResponseDTO> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody PasswordChangeRequest request) {
        return ResponseEntity.ok(userService.updatePassword(id, request));
    }

    @Operation(summary = "Delete user by id", description = "This endpoint allows users to delete their accounts.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get all users", description = "This endpoint allows users to get all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping

    public ResponseEntity<UserListResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @Operation(summary = "Resend verification email", description = "This endpoint allows users to resend verification emails.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification email successfully resent"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resendVerificationEmail(@RequestParam String email) {
        userService.resendVerificationEmail(email);
        return ResponseEntity.ok().build();
    }
}