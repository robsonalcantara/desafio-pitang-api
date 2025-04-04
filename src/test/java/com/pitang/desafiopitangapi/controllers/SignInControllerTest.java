package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.dto.LoginRequestDTO;
import com.pitang.desafiopitangapi.dto.ResponseDTO;
import com.pitang.desafiopitangapi.dto.UserDTO;
import com.pitang.desafiopitangapi.infra.security.TokenService;
import com.pitang.desafiopitangapi.model.User;
import com.pitang.desafiopitangapi.repository.UserRepository;
import com.pitang.desafiopitangapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SignInControllerTest {

    @InjectMocks
    private SignInController signInController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    private User user;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@test.com");
        user.setBirthday(new Date());
        user.setLogin("test");
        user.setPassword("password123");
        user.setPhone("123456789");

        loginRequestDTO = new LoginRequestDTO(user.getLogin(), user.getPassword());
    }

    @Test
    @DisplayName("Successfull sign-in")
    public void testSignInSuccess() {
        Mockito.when(userRepository.findByLogin(loginRequestDTO.login())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())).thenReturn(true);
        Mockito.when(tokenService.generateToken(user)).thenReturn("mockedToken");

        ResponseEntity<?> response = signInController.signIn(loginRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseDTO.class, response.getBody().getClass());

        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("mockedToken", responseDTO.token());
        assertEquals(user, responseDTO.user());

    }

    @Test
    @DisplayName("Failed sign-in due to invalid credentials")
    public void testSignInFailureInvalidCredentials() {
        Mockito.when(userRepository.findByLogin(loginRequestDTO.login())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            signInController.signIn(loginRequestDTO);
        });

        assertEquals("Invalid login or password", exception.getMessage());
    }
}
