package com.online_marketplace_api.awesomity.controllers;

import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.AuthResponse;
import com.online_marketplace_api.awesomity.common.userDTO.AuthDTOs.LoginRequest;
import com.online_marketplace_api.awesomity.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @Operation(
            summary = "User Login",
            description = "This endpoint allows users to log in to the system using their credentials."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid login credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody
            @Parameter(description = "Login request containing username and password")
            LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
