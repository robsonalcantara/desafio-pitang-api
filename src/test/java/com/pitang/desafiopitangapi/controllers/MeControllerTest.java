package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.dto.UserDTO;
import com.pitang.desafiopitangapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MeControllerTest {

    @InjectMocks
    private MeController meController;

    @Mock
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID().toString());
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setEmail("test@test.com");
        userDTO.setBirthday(new Date());
        userDTO.setLogin("test");
        userDTO.setPassword("password123");
        userDTO.setPhone("123456789");
    }

    @Test
    @DisplayName("My user found")
    public void testFindByMe() {
        HttpServletRequest request = new MockHttpServletRequest();
        Mockito.when(userService.findByMe(request)).thenReturn(userDTO);
        ResponseEntity<UserDTO> response = meController.findByMe(request);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(UserDTO.class, response.getBody().getClass());
    }
}
