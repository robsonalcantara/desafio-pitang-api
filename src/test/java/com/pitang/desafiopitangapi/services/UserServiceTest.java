package com.pitang.desafiopitangapi.services;

import com.pitang.desafiopitangapi.dto.UserDTO;
import com.pitang.desafiopitangapi.exceptions.BusinessException;
import com.pitang.desafiopitangapi.model.User;
import com.pitang.desafiopitangapi.repository.UserRepository;
import com.pitang.desafiopitangapi.infra.security.TokenService;
import com.pitang.desafiopitangapi.model.Car;
import com.pitang.desafiopitangapi.service.CarService;
import com.pitang.desafiopitangapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarService carService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private HttpServletRequest request;

    private UserDTO userDTO;
    private User user;
    private Car car;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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

        user = UserDTO.toEntity(userDTO);
    }

    @Test
    @DisplayName("User Registered - Success")
    void testRegister_Success() throws BusinessException {
        when(userRepository.existsByLogin(userDTO.getLogin())).thenReturn(false);
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO registeredUser = userService.register(userDTO);

        assertNotNull(registeredUser);
        assertEquals("Test", registeredUser.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Register User - Login already Exists")
    void testRegister_LoginAlreadyExists() {
        when(userRepository.existsByLogin(userDTO.getLogin())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> userService.register(userDTO));

        assertEquals("Login already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("User Found By Id - Success")
    void testFindById_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDTO foundUser = userService.findById(user.getId());

        assertNotNull(foundUser);
        assertEquals("Test", foundUser.getFirstName());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Find By Id - User Not Found")
    void testFindById_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> userService.findById(user.getId()));

        assertEquals("Invalid Id", exception.getMessage());
    }

    @Test
    @DisplayName("User Found By Logged - Success")
    void testFindByMe_Success() {
        String token = "token";

        when(tokenService.recoverToken(request)).thenReturn(token);
        when(tokenService.verifyToken(token)).thenReturn(user.getLogin());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.findByMe(request);

        assertNotNull(userDTO);
        assertEquals("Test", userDTO.getFirstName());
        verify(tokenService, times(1)).recoverToken(request);
        verify(tokenService, times(1)).verifyToken(token);
    }

    @Test
    @DisplayName("User Updated - Success")
    void testUpdate_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO updatedUser = userService.update(user.getId(), userDTO);

        assertEquals("Test", updatedUser.getFirstName());
        assertEquals("Test", updatedUser.getLastName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Update User - User Not Found")
    void testUpdate_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> userService.update(user.getId(), new UserDTO()));

        assertEquals("Invalid Id", exception.getMessage());
    }

    @Test
    @DisplayName("User Deleted - Success")
    void testDelete_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.delete(user.getId());

        verify(userRepository, times(1)).delete(user);
        verify(carService, times(1)).deleteByCar(any(Car.class));
    }

    @Test
    @DisplayName("Delete User - User Not Found")
    void testDelete_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        BadCredentialsException exception =  assertThrows(BadCredentialsException.class, () -> userService.delete(user.getId()));

        assertEquals("Invalid Id", exception.getMessage());
    }
}
