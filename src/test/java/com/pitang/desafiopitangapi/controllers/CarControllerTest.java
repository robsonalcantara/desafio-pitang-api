package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.model.Car;
import com.pitang.desafiopitangapi.model.User;
import com.pitang.desafiopitangapi.service.CarService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CarControllerTest {

    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    private Car car;

    @BeforeEach
    public void setup() {
        car = new Car();
        car.setId(UUID.randomUUID().toString());
        car.setYear(2022);
        car.setLicensePlate("ABC-1234");
        car.setModel("Model X");
        car.setColor("Blue");
        car.setUser(new User());
    }

    @Test
    @DisplayName("Car created")
    public void testRegister() {
        HttpServletRequest request = new MockHttpServletRequest();

        Mockito.when(carService.register(Mockito.any(), Mockito.any(HttpServletRequest.class))).thenReturn(car);
        ResponseEntity<Car> response = carController.register(car, request);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Car.class, Objects.requireNonNull(response.getBody()).getClass());
    }

    @Test
    @DisplayName("User's car list found")
    public void testFindAllByLoggedUser(){
        HttpServletRequest request = new MockHttpServletRequest();

        Mockito.when(carService.findAllByLoggedUser(request)).thenReturn(new ArrayList<>(List.of(car)));
        ResponseEntity<List<Car>> response = carController.findAllByLoggedUser(request);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ArrayList.class, response.getBody().getClass());
        assertEquals(Car.class, response.getBody().getFirst().getClass());
    }

    @Test
    @DisplayName("Car found")
    public void testFindById() {
        HttpServletRequest request = new MockHttpServletRequest();

        Mockito.when(carService.findByIdAndLoggedUser(car.getId(), request)).thenReturn(car);
        ResponseEntity<Car> response = carController.findById(car.getId(), request);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Car.class, response.getBody().getClass());
    }

    @Test
    @DisplayName("Car updated")
    public void testUpdate() {
        HttpServletRequest request = new MockHttpServletRequest();

        Mockito.when(carService.update(Mockito.eq(car.getId()), Mockito.any(Car.class), Mockito.eq(request))).thenReturn(car);
        ResponseEntity<Car> response = carController.update(car.getId(), car, request);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Car.class, response.getBody().getClass());
    }

    @Test
    @DisplayName("Car deleted")
    public void testDelete() {
        HttpServletRequest request = new MockHttpServletRequest();

        Mockito.doNothing().when(carService).delete(car.getId(), request);
        ResponseEntity<Void> response = carController.delete(car.getId(), request);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}