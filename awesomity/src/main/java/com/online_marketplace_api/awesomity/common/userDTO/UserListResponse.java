package com.online_marketplace_api.awesomity.common.userDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
    private List<UserResponseDTO> users;
    private long totalUsers;
    private int totalPages;
    private int currentPage;

    public UserListResponse(List<UserResponseDTO> users, int i, int i1, long l, boolean b, boolean b1) {
    }
}