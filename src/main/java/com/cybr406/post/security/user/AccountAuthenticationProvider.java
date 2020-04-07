package com.cybr406.post.security.user;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AccountAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Update this method to verify user details using the Account microservice.
        // You will need to use the WebClient class to make calls to /check-user in Account
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Update this method to indicate this AuthenticationProvider supports UsernamePasswordAuthenticationToken
        return false;
    }

}
