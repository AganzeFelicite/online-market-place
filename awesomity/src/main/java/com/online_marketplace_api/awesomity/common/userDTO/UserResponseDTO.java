package com.online_marketplace_api.awesomity.common.userDTO;
import com.online_marketplace_api.awesomity.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String role;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponseDTO(long l, String john, String doe, String mail) {
        this.id = l;
        this.firstName = john;
        this.lastName = doe;
        this.email = mail;

    }


//    public static UserResponseDTO fromEntity(User user) {
//        return UserResponseDTO.builder()
//                .id(user.getId())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .email(user.getEmail())
//                .address(user.getAddress())
//                .phoneNumber(user.getPhoneNumber())
//                .role(user.getRole().name())
//                .emailVerified(user.isEmailVerified())
//                .createdAt(user.getCreatedAt())
//                .build();
//    }
}