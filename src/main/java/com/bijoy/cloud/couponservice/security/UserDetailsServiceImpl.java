package com.bijoy.cloud.couponservice.security;

import com.bijoy.cloud.couponservice.model.User;
import com.bijoy.cloud.couponservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            User loadedUser = optionalUser.get();
            return new org.springframework.security.core.userdetails.User(
                    loadedUser.getEmail(), loadedUser.getPassword(), loadedUser.getRoles());
        } else {
            throw new UsernameNotFoundException("User with email: {" + username + "} is not found!");
        }
    }
}
