package com.altech.electronicstore.assignment.auth;

import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.models.UserProfile;
import com.altech.electronicstore.assignment.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final CustomUserDetailsService userDetailsService;

    public UserProfile getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User userDetails) {
            return userDetailsService.loadUserProfile(userDetails.getUsername());
        }

        throw new APIException(APICode.USER_NOT_FOUND);
    }
}