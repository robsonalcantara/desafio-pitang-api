package com.pitang.desafiopitangapi.infra.security;

import com.pitang.desafiopitangapi.model.User;
import com.pitang.desafiopitangapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Custom implementation of {@link UserDetailsService} used by Spring Security.
 * This service loads the user's details from the database based on the login (username).
 * It is responsible for authenticating and providing user information during the login process.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads user details from the database by the provided username (login).
     *
     * @param username the login (username) of the user to load
     * @return the user details for the authenticated user
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
