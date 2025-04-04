package com.pitang.desafiopitangapi.dto;

/**
 * DTO (Data Transfer Object) for handling login requests.
 * Contains the user's login credentials: login and password.
 */
public record LoginRequestDTO (String login, String password) {
}
