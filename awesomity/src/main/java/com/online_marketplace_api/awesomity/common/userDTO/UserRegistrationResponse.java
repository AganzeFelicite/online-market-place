package com.online_marketplace_api.awesomity.common.userDTO;

public class UserRegistrationResponse {
    private UserResponseDTO userResponse;
    private String message;

    // Constructor
    public UserRegistrationResponse(UserResponseDTO userResponse, String message) {
        this.userResponse = userResponse;
        this.message = message;
    }

    // Getters and Setters
    public UserResponseDTO getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponseDTO userResponse) {
        this.userResponse = userResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
