package com.bijoy.cloud.couponservice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SecurityService {
    boolean login(String username, String password, HttpServletRequest request, HttpServletResponse response);
}
