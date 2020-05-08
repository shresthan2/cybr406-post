package com.cybr406.post.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountAuthenticationProvider implements AuthenticationProvider {

    private static final ParameterizedTypeReference<List<String>>
            typeRef = new ParameterizedTypeReference<List<String>>() {
    };


    @Autowired
    private WebClient webClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Update this method to verify user details using the Account microservice.
        // You will need to use the WebClient class to make calls to /check-user in Account
        try {
            List<SimpleGrantedAuthority>  roles = webClient.get()
                    .uri("/check-user")
                    .headers(headers -> headers.setBasicAuth("post","post"))
                    .header("x-username", authentication.getName())
                    .header("x-password", authentication.getCredentials().toString())
                    .retrieve()
                    .bodyToMono(typeRef)
                    .blockOptional(Duration.ofSeconds(5))
                    .orElseThrow(() -> new Exception("No response from account."))
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(
                    authentication.getName(),
                    authentication.getCredentials(),
                    roles);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Update this method to indicate this AuthenticationProvider supports UsernamePasswordAuthenticationToken
        return  UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
