package com.pitang.desafiopitangapi.dto;

import com.pitang.desafiopitangapi.model.User;

/**
 * DTO (Data Transfer Object) used for sending the response
 * after a successful login. Contains the authenticated user
 * and the generated JWT token.
 */
public record ResponseDTO(User user, String token) {
}
