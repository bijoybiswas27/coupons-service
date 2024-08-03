package com.bijoy.cloud.couponservice.controller;

import com.bijoy.cloud.couponservice.model.Role;
import com.bijoy.cloud.couponservice.model.User;
import com.bijoy.cloud.couponservice.repository.UserRepository;
import com.bijoy.cloud.couponservice.security.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {
    @Autowired
    private SecurityService securityService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/showReg")
    public String showRegisterPage() {
        return "registerUser";
    }

    @PostMapping("/registerUser")
    public ModelAndView registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setId(2);
        role.setName("ROLE_USER");
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        ModelAndView mav = new ModelAndView("/login");
        mav.addObject("success", "User registered successfully!");
        return mav;
    }

    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String username, @RequestParam("password") String password,
                              HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        boolean loginResponse = securityService.login(username, password, request, response);
        if (loginResponse) {
            mav.setViewName("index");
        } else {
            mav.setViewName("login");
            mav.addObject("error", "Login failed, please try again!");
        }
        return mav;
    }
}
