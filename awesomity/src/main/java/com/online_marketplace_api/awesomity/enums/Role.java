package com.online_marketplace_api.awesomity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User roles")
public enum Role {
    ADMIN, SELLER, BUYER
}