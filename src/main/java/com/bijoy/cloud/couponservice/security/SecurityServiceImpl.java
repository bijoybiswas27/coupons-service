package com.bijoy.cloud.couponservice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Override
    public boolean login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        boolean result = false;
        UsernamePasswordAuthenticationToken token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            token = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(), password, userDetails.getAuthorities());
            authenticationManager.authenticate(token);
            result = token.isAuthenticated();
        } catch (Exception ex) {
            System.out.println("Exception occurred: " + ex.getMessage());
            result = false;
        }
        if (result) {
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(token);
            securityContextRepository.saveContext(context, request, response);
        }
        return result;
    }
}
