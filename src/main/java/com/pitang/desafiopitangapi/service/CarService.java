package com.pitang.desafiopitangapi.service;

import com.pitang.desafiopitangapi.exceptions.BusinessException;
import com.pitang.desafiopitangapi.infra.security.TokenService;
import com.pitang.desafiopitangapi.model.Car;
import com.pitang.desafiopitangapi.model.User;
import com.pitang.desafiopitangapi.repository.CarRepository;
import com.pitang.desafiopitangapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for handling car-related operations such as registration, update, retrieval, and deletion.
 * It interacts with the {@link CarRepository}, {@link UserRepository}, and {@link TokenService}.
 */
@Service
public class CarService {


    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CarRepository carRepository;

    /**
     * Finds all cars associated with the logged-in user.
     *
     * @author Robson Rodrigues
     * @param request The HTTP request containing the user's authentication token.
     * @return A list of cars associated with the logged-in user.
     */
    public List<Car> findAllByLoggedUser(HttpServletRequest request) {
        User user = getUserByToken(request);
        return carRepository.findByUserIdOrderByUsageCountDescModelAsc(user.getId());
    }

    /**
     * Finds a car by its ID and ensures it belongs to the logged-in user.
     *
     * @author Robson Rodrigues
     * @param id The ID of the car to be retrieved.
     * @param request The HTTP request containing the user's authentication token.
     * @return The car entity if found.
     * @throws EntityNotFoundException if the car is not found or does not belong to the logged-in user.
     */
    public Car findByIdAndLoggedUser(String id, HttpServletRequest request) {
        User user = getUserByToken(request);
        return carRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Car Not Found"));
    }

    /**
     * Registers a new car. Validates the car details, associates it with the logged-in user (based on token),
     * and checks if the license plate is already in use.
     *
     * @author Robson Rodrigues
     * @param car The car to be registered.
     * @param request The HTTP request containing the user's authentication token.
     * @return The saved car entity.
     * @throws BusinessException if the license plate already exists or if validation fails.
     */
    public Car register(Car car, HttpServletRequest request) {
        car.validate();
        if (request != null) {
            User user = getUserByToken(request);
            car.setUser(user);
        }
        if (carRepository.existsByLicensePlate(car.getLicensePlate())) {
            throw new BusinessException("License plate already exists", HttpStatus.BAD_REQUEST);
        }
        return carRepository.save(car);
    }


    /**
     * Updates a car's details. The car is validated, and the user's token is verified before making the update.
     * It checks whether the new license plate is already in use.
     *
     * @author Robson Rodrigues
     * @param id The ID of the car to be updated.
     * @param car The updated car entity.
     * @param request The HTTP request containing the user's authentication token.
     * @return The updated car entity.
     * @throws EntityNotFoundException if the car does not exist or does not belong to the logged-in user.
     * @throws BusinessException if the license plate is already in use.
     */
    public Car update(String id, Car car, HttpServletRequest request) {
        car.setId(id);
        car.validate();
        User user = getUserByToken(request);
        car.setUser(user);

        for (Car validationCar : user.getCars()) {
            if (validationCar.getId().equals(id)) {
                if (!validationCar.getLicensePlate().equals(car.getLicensePlate()) && carRepository.existsByLicensePlate(car.getLicensePlate())) {
                    throw new BusinessException("License plate already exists", HttpStatus.BAD_REQUEST);
                }

                if (Boolean.TRUE.equals(car.getUsage())) {
                    List<Car> carsInUse = carRepository.findByUserIdAndUsageTrue(user.getId());
                    for (Car inUseCar : carsInUse) {
                        if (!inUseCar.getId().equals(car.getId())) {
                            inUseCar.setUsage(false);
                        }
                    }
                    carRepository.saveAll(carsInUse);
                }

                return carRepository.save(car);
            }
        }
        throw new EntityNotFoundException("Car Not Found");
    }

    /**
     * Deletes a car based on its ID, ensuring it belongs to the logged-in user.
     *
     * @author Robson Rodrigues
     * @param id The ID of the car to be deleted.
     * @param request The HTTP request containing the user's authentication token.
     * @throws EntityNotFoundException if the car is not found or does not belong to the logged-in user.
     */
    public void delete(String id, HttpServletRequest request) {
        Car car = findByIdAndLoggedUser(id, request);
        carRepository.delete(car);
    }

    /**
     * Deletes the specified car.
     *
     * @author Robson Rodrigues
     * @param car The car to be deleted.
     */
    public void deleteByCar(Car car) {
        carRepository.delete(car);
    }

    /**
     * Validates a list of cars by calling the {@link Car#validate()} method for each car in the list.
     *
     * @author Robson Rodrigues
     * @param list The list of cars to be validated.
     * @throws BusinessException if any car in the list is invalid.
     */
    public void validateCarList(List<Car> list) {
        for (Car car : list) {
            car.validate();
        }
    }

    /**
     * Retrieves the logged-in user based on the authentication token from the HTTP request.
     *
     * @author Robson Rodrigues
     * @param request The HTTP request containing the authentication token.
     * @return The user associated with the token.
     * @throws BadCredentialsException if the token is invalid or the user is not found.
     */
    public User getUserByToken(HttpServletRequest request) {
        var token = this.tokenService.recoverToken(request);
        var login = tokenService.verifyToken(token);
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new BadCredentialsException("Invalid login"));
    }
    @Transactional
    public int resetCarUsage() {
        List<Car> carsInUse = carRepository.findByUsageTrue();
        carsInUse.forEach(car -> car.setUsage(false));
        carRepository.saveAll(carsInUse);
        return carsInUse.size();
    }
}
