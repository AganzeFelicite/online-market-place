package com.online_marketplace_api.awesomity.security;
import com.online_marketplace_api.awesomity.Entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    // This method will fetch the currently authenticated user from the SecurityContext
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Return null if no authentication is found (i.e., no user is logged in)
        if (authentication == null) {
            return null;
        }

        // Return the current authenticated user
        return (User) authentication.getPrincipal();
    }
}
