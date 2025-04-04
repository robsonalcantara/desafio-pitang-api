package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.exceptions.BusinessException;
import com.pitang.desafiopitangapi.model.Car;
import com.pitang.desafiopitangapi.service.CarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class responsible for handling HTTP requests related to {@link Car}.
 * Provides endpoints for creating, getting, updating, and deleting car records.
 */
@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    /**
     * Registers a new car in the system.
     *
     * @author Robson Rodrigues
     * @param car The car to be registered.
     * @param request The HTTP request containing authentication information.
     * @return A {@link ResponseEntity} containing the created car and an HTTP status of {@code CREATED}.
     * @throws BusinessException if validation of the car fails.
     */
    @PostMapping
    public ResponseEntity<Car> register(@RequestBody Car car, HttpServletRequest request) throws BusinessException {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.register(car, request));
    }

    /**
     * @author Robson Rodrigues
     * Retrieves all cars associated with the currently logged-in user.
     *
     * @author Robson Rodrigues
     * @param request The HTTP request containing authentication information.
     * @return A {@link ResponseEntity} containing a list of cars.
     */
    @GetMapping
    public ResponseEntity<List<Car>> findAllByLoggedUser(HttpServletRequest request) {
        List<Car> listCar = carService.findAllByLoggedUser(request);
        return ResponseEntity.ok(listCar);
    }

    /**
     * Retrieves a car by its ID, ensuring it belongs to the currently logged-in user.
     *
     * @author Robson Rodrigues
     * @param id The ID of the car to be retrieved.
     * @param request The HTTP request containing authentication information.
     * @return A {@link ResponseEntity} containing the found car.
     * @throws BusinessException if the car is not found or does not belong to the logged-in user.
     */
    @GetMapping("{id}")
    public ResponseEntity<Car> findById(@PathVariable String id, HttpServletRequest request) throws BusinessException {
        return ResponseEntity.ok(carService.findByIdAndLoggedUser(id, request));
    }

    /**
     * Updates an existing car's details.
     *
     * @author Robson Rodrigues
     * @param id The ID of the car to be updated.
     * @param car The car with updated information.
     * @param request The HTTP request containing authentication information.
     * @return A {@link ResponseEntity} containing the updated car.
     * @throws BusinessException if validation of the car fails or the car is not found.
     */
    @PutMapping("{id}")
    public ResponseEntity<Car> update(@PathVariable String id, @RequestBody Car car, HttpServletRequest request) throws BusinessException {
        return ResponseEntity.ok(carService.update(id, car, request));
    }

    /**
     * Deletes a car by its ID.
     *
     * @author Robson Rodrigues
     * @param id The ID of the car to be deleted.
     * @param request The HTTP request containing authentication information.
     * @return A {@link ResponseEntity} with HTTP status {@code NO_CONTENT} indicating successful deletion.
     * @throws BusinessException if the car is not found or does not belong to the logged-in user.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, HttpServletRequest request) throws BusinessException {
        carService.delete(id, request);
        return ResponseEntity.noContent().build();
    }
}
