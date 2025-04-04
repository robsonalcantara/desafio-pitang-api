package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.infra.RestErrorMessage;
import com.pitang.desafiopitangapi.service.UserService;
import lombok.RequiredArgsConstructor;
import com.pitang.desafiopitangapi.dto.LoginRequestDTO;
import com.pitang.desafiopitangapi.dto.ResponseDTO;
import com.pitang.desafiopitangapi.infra.security.TokenService;
import com.pitang.desafiopitangapi.model.User;
import com.pitang.desafiopitangapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling user sign-in requests.
 * Provides an endpoint for user authentication and token generation.
 */
@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class SignInController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserService userService;

    /**
     * Authenticates the user based on the provided login credentials.
     * If valid, generates a JWT token and updates the user's last login timestamp.
     *
     * @author Robson Rodrigues
     * @param body The login request containing the user's login and password.
     * @return A {@link ResponseEntity} containing the user details and JWT token.
     * @throws BadCredentialsException if the login or password is incorrect.
     */
    @PostMapping()
    public ResponseEntity signIn(@RequestBody LoginRequestDTO body) {
        User user = userRepository.findByLogin(body.login()).orElseThrow(() -> new BadCredentialsException("Invalid login or password"));
        if (passwordEncoder.matches(body.password(), user.getPassword())){
            String token = tokenService.generateToken(user);
            userService.updateLastLogin(user);
            return ResponseEntity.ok(new ResponseDTO(user, token));
        }
        throw new BadCredentialsException("Invalid login or password");
    }

}
