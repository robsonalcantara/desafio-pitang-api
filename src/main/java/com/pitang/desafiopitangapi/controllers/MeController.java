package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.dto.UserDTO;
import com.pitang.desafiopitangapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling requests related to the currently logged-in user.
 * Provides an endpoint to retrieve the details of the logged-in user.
 */
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;

    /**
     * Retrieves the details of the currently logged-in user.
     *
     * @author Robson Rodrigues
     * @param request The HTTP request containing authentication information.
     * @return A {@link ResponseEntity} containing the logged-in user's details in a {@link UserDTO} object.
     */
    @GetMapping
    ResponseEntity<UserDTO> findByMe(HttpServletRequest request) {
        return ResponseEntity.ok(userService.findByMe(request));
    }

}
