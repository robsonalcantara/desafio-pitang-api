package com.pitang.desafiopitangapi.repository;

import com.pitang.desafiopitangapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on the {@link User} entity.
 * Extends {@link JpaRepository} to provide standard JPA functionality.
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Retrieves a user by their login.
     *
     * @author Robson Rodrigues
     * @param login The login of the user to be retrieved.
     * @return An {@link Optional} containing the user if found, or an empty {@link Optional} if not found.
     */
    Optional<User> findByLogin(String login);

    /**
     * Checks if a user with the given login exists in the database.
     *
     * @author Robson Rodrigues
     * @param login The login to check for existence.
     * @return {@code true} if a user with the given login exists, {@code false} otherwise.
     */
    boolean existsByLogin(String login);

    /**
     * Checks if a user with the given email exists in the database.
     *
     * @author Robson Rodrigues
     * @param email The email to check for existence.
     * @return {@code true} if a user with the given email exists, {@code false} otherwise.
     */
    boolean existsByEmail(String email);
}
