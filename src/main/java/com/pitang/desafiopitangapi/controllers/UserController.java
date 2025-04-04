package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.dto.UserDTO;
import com.pitang.desafiopitangapi.service.UserService;
import lombok.RequiredArgsConstructor;

import com.pitang.desafiopitangapi.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing user-related operations.
 * Provides endpoints for user registration, retrieval, update, and deletion.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Registers a new user in the system.
     *
     * @param body The user data to be registered.
     * @return A {@link ResponseEntity} with the status of the creation and the registered user.
     * @throws BusinessException if there are validation errors or the user already exists.
     */
    @PostMapping
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO body) throws BusinessException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(body));
    }

    /**
     * Retrieves a list of all users.
     *
     * @author Robson Rodrigues
     * @return A {@link ResponseEntity} containing a list of {@link UserDTO} objects.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Retrieves a specific user by their ID.
     *
     * @author Robson Rodrigues
     * @param id The ID of the user to retrieve.
     * @return A {@link ResponseEntity} containing the user data.
     */
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    /**
     * Updates an existing user.
     *
     * @author Robson Rodrigues
     * @param id The ID of the user to update.
     * @param userDTO The updated user data.
     * @return A {@link ResponseEntity} containing the updated user data.
     * @throws BusinessException if there are validation errors during the update.
     */
    @PutMapping("{id}")
    public ResponseEntity<UserDTO> update(@PathVariable String id, @RequestBody UserDTO userDTO) throws BusinessException {
        return ResponseEntity.ok(userService.update(id, userDTO));
    }

    /**
     * Deletes a user by their ID.
     *
     * @author Robson Rodrigues
     * @param id The ID of the user to delete.
     * @return A {@link ResponseEntity} with no content to indicate the successful deletion.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
