package com.pitang.desafiopitangapi.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pitang.desafiopitangapi.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Service class responsible for generating, verifying, and extracting JWT tokens.
 */
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secretKey;

    @Value("${api.security.token.expire.minutes}")
    private Long expireMinutes;

    /**
     * Generates a JWT token for the given user.
     * The token includes the issuer, subject (user login), and expiration date.
     *
     * @param user the user for whom the token is generated.
     * @return a JWT token string.
     * @throws RuntimeException if an error occurs during token creation.
     */
    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            String token = JWT.create()
                    .withIssuer("desafio-pitang-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Authentication error");
        }
    }

    /**
     * Calculates the expiration date for the token based on the configured expiration time in minutes.
     *
     * @return an Instant representing the expiration date and time.
     */
    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusMinutes(expireMinutes).toInstant(ZoneOffset.of("-03:00"));
    }

    /**
     * Verifies and decodes the given JWT token, extracting the subject (user login).
     *
     * @param token the JWT token to be verified.
     * @return the subject of the token (user login) if valid; null otherwise.
     */
    public String verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("desafio-pitang-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    /**
     * Extracts the JWT token from the Authorization header in the request, removing the "Bearer " prefix.
     *
     * @param request the HTTP request containing the Authorization header.
     * @return the extracted JWT token if present; null otherwise.
     */
    public String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
