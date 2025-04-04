package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.dto.UserDTO;
import com.pitang.desafiopitangapi.model.Car;
import com.pitang.desafiopitangapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserDTO userDTO;
    private Car[] cars;
    private Car car;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID().toString());
        userDTO.setFirstName("Test");
        userDTO.setLastName("Test");
        userDTO.setEmail("test@test.com");
        userDTO.setBirthday(new Date());
        userDTO.setLogin("test");
        userDTO.setPassword("test");
        userDTO.setPhone("988888888");

        car = new Car();
        car.setYear(2022);
        car.setLicensePlate("ABC-1234");
        car.setModel("Model X");
        car.setColor("Blue");
        List<Car> cars = new ArrayList<>();
        cars.add(car);

        userDTO.setCars(cars);
    }

    @Test
    @DisplayName("User Created")
    public void testRegisterUser() {
        Mockito.when(userService.register(Mockito.any())).thenReturn(userDTO);
        ResponseEntity<UserDTO> response = userController.register(userDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(UserDTO.class, Objects.requireNonNull(response.getBody()).getClass());
    }

    @Test
    @DisplayName("Listed all users")
    public void testFindAll(){
        Mockito.when(userService.findAll()).thenReturn(new ArrayList<>(List.of(userDTO)));
        ResponseEntity<List<UserDTO>> response = userController.findAll();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ArrayList.class, response.getBody().getClass());
        assertEquals(UserDTO.class, response.getBody().getFirst().getClass());
    }

    @Test
    @DisplayName("User found")
    public void testGetUserById() {
        Mockito.when(userService.findById(userDTO.getId())).thenReturn(userDTO);
        ResponseEntity<UserDTO> response = userController.getUserById(userDTO.getId());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(UserDTO.class, response.getBody().getClass());
    }
    
    @Test
    @DisplayName("User updated")
    public void testUpdate(){
        Mockito.when(userService.update(Mockito.eq(car.getId()), Mockito.any(UserDTO.class))).thenReturn(userDTO);
        ResponseEntity<UserDTO> response = userController.update(car.getId(), userDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(UserDTO.class, response.getBody().getClass());

    }

    @Test
    @DisplayName("User deleted")
    public void testDelete() {
        Mockito.doNothing().when(userService).delete(car.getId());
        ResponseEntity<Void> response = userController.delete(car.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
