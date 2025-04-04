package com.pitang.desafiopitangapi.repository;

import com.pitang.desafiopitangapi.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on the {@link Car} entity.
 * Extends {@link JpaRepository} to provide standard JPA functionality.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, String> {

    /**
     * Checks if a car with the given license plate already exists in the database.
     *
     * @author Robson Rodrigues
     * @param licensePlate The license plate of the car to be checked.
     * @return {@code true} if a car with the given license plate exists, {@code false} otherwise.
     */
    boolean existsByLicensePlate(String licensePlate);

    /**
     * Retrieves a list of cars associated with the user specified by their ID.
     *
     * @author Robson Rodrigues
     * @param id The ID of the user whose cars are to be retrieved.
     * @return A list of cars associated with the specified user ID.
     */
    List<Car> findByUserId(String id);

    /**
     * Retrieves a car by its ID and the ID of the user who owns it.
     *
     * @author Robson Rodrigues
     * @param carId The ID of the car to be retrieved.
     * @param userId The ID of the user who owns the car.
     * @return An {@link Optional} containing the car if found, or an empty {@link Optional} if not found.
     */
    Optional<Car> findByIdAndUserId(String carId, String userId);

    /**
     * Retrieves a list of cars that are currently in use (i.e., have usage set to true).
     *
     * @author Robson Rodrigues
     * @return A list of cars where the usage field is set to true.
     */
    List<Car> findByUsageTrue();

    /**
     * Retrieves a list of cars associated with the specified user ID,
     * ordered by usage count in descending order.
     * If multiple cars have the same usage count, they are sorted
     * alphabetically by model.
     *
     * <p>This method ensures that the most frequently used cars appear
     * first. If two or more cars have the same usage count, their model
     * names are used as a tiebreaker in ascending order.</p>
     *
     * @author Robson Rodrigues
     * @param id The ID of the user whose cars are to be retrieved.
     * @return A list of cars associated with the specified user ID, sorted
     *         by usage count in descending order and model name in ascending order.
     */
    List<Car> findByUserIdOrderByUsageCountDescModelAsc(String id);

    /**
     * Retrieves all cars for a given user that are currently in use.
     *
     * @author Robson Rodrigues
     * @param userId The ID of the user whose cars are being retrieved.
     * @return A list of cars that are currently marked as in use for the given user.
     */
    List<Car> findByUserIdAndUsageTrue(String userId);
}
