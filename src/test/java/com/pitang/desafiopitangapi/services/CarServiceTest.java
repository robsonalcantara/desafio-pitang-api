package com.pitang.desafiopitangapi.services;

import com.pitang.desafiopitangapi.dto.UserDTO;
import com.pitang.desafiopitangapi.exceptions.BusinessException;
import com.pitang.desafiopitangapi.infra.security.TokenService;
import com.pitang.desafiopitangapi.model.Car;
import com.pitang.desafiopitangapi.model.User;
import com.pitang.desafiopitangapi.repository.CarRepository;
import com.pitang.desafiopitangapi.repository.UserRepository;
import com.pitang.desafiopitangapi.service.CarService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    private Car car;
    private User user;
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        car = new Car();
        car.setId(UUID.randomUUID().toString());
        car.setLicensePlate("ABC-1234");
        car.setModel("Model X");
        car.setYear(2022);
        car.setColor("Blue");

        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setLogin("test_user");
        user.setCars(new ArrayList<>(List.of(car)));

        request = mock(HttpServletRequest.class);
    }

    @Test
    @DisplayName("Register Car - Success")
    public void testRegister_Success() {
        Mockito.when(tokenService.recoverToken(request)).thenReturn("valid_token");
        Mockito.when(tokenService.verifyToken("valid_token")).thenReturn(user.getLogin());
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        Mockito.when(carRepository.existsByLicensePlate(car.getLicensePlate())).thenReturn(false);
        Mockito.when(carRepository.save(car)).thenReturn(car);

        Car savedCar = carService.register(car, request);

        assertNotNull(savedCar);
        assertEquals(Car.class, savedCar.getClass());
    }

    @Test
    @DisplayName("Register Car - License Plate Already Exists")
    public void testRegister_LicensePlateExists() {
        Mockito.when(tokenService.recoverToken(request)).thenReturn("valid_token");
        Mockito.when(tokenService.verifyToken("valid_token")).thenReturn(user.getLogin());
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        Mockito.when(carRepository.existsByLicensePlate(car.getLicensePlate())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            carService.register(car, request);
        });

        assertEquals("License plate already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }


    @Test
    @DisplayName("Find All Cars by Logged User - Success")
    public void testFindAllByLoggedUser() {
        Mockito.when(tokenService.recoverToken(request)).thenReturn("valid_token");
        Mockito.when(tokenService.verifyToken("valid_token")).thenReturn(user.getLogin());
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        Mockito.when(carRepository.findByUserId(user.getId())).thenReturn(user.getCars());

        List<Car> cars = carService.findAllByLoggedUser(request);

        assertNotNull(cars);
        assertEquals(ArrayList.class, cars.getClass());
        assertEquals(Car.class, cars.getFirst().getClass());
    }

    @Test
    @DisplayName("Find Car by ID and Logged User - Car Not Found")
    public void testFindByIdAndLoggedUser_CarNotFound() {
        Mockito.when(tokenService.recoverToken(request)).thenReturn("valid_token");
        Mockito.when(tokenService.verifyToken("valid_token")).thenReturn(user.getLogin());
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        Mockito.when(carRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            carService.findByIdAndLoggedUser(car.getId(), request);
        });

        assertEquals("Car Not Found", exception.getMessage());
    }

    @Test
    @DisplayName("Car Updated - Success")
    public void testUpdate() {
        Mockito.when(tokenService.recoverToken(request)).thenReturn("valid_token");
        Mockito.when(tokenService.verifyToken("valid_token")).thenReturn(user.getLogin());
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        Mockito.when(carRepository.existsByLicensePlate(anyString())).thenReturn(false);
        Mockito.when(carRepository.save(car)).thenReturn(car);

        Car updatedCar = carService.update(car.getId(), car, request);

        assertNotNull(updatedCar);
        assertEquals(car.getLicensePlate(), updatedCar.getLicensePlate());
    }

    @Test
    @DisplayName("Fail Update Car - Not Owned by User")
    public void testUpdate_CarNotOwnedByUser() {
        Mockito.when(tokenService.recoverToken(request)).thenReturn("valid_token");
        Mockito.when(tokenService.verifyToken("valid_token")).thenReturn(user.getLogin());
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        user.setCars(List.of());
        Mockito.when(carRepository.findByIdAndUserId(car.getId(), user.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> carService.update("some_invalid_id", car, request));

        assertEquals("Car Not Found", exception.getMessage());
        Mockito.verify(carRepository, Mockito.never()).save(car);
    }



    @Test
    @DisplayName("Car Deleted - Success")
    public void testDelete() {
        Mockito.when(tokenService.recoverToken(request)).thenReturn("valid_token");
        Mockito.when(tokenService.verifyToken("valid_token")).thenReturn(user.getLogin());
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        Mockito.when(carRepository.findByIdAndUserId(car.getId(), user.getId())).thenReturn(Optional.of(car));

        assertDoesNotThrow(() -> carService.delete(car.getId(), request));

        Mockito.verify(carRepository, Mockito.times(1)).delete(car);
    }

    @Test
    @DisplayName("Fail Delete Car - Not Owned by User")
    public void testDelete_CarNotOwnedByUser() {
        Mockito.when(tokenService.recoverToken(request)).thenReturn("valid_token");
        Mockito.when(tokenService.verifyToken("valid_token")).thenReturn(user.getLogin());
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        Mockito.when(carRepository.findByIdAndUserId(car.getId(), user.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> carService.delete(car.getId(), request));

        assertEquals("Car Not Found", exception.getMessage());
        Mockito.verify(carRepository, Mockito.never()).delete(car);
    }
}
