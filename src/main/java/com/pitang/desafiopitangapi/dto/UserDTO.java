package com.pitang.desafiopitangapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.pitang.desafiopitangapi.model.Car;
import com.pitang.desafiopitangapi.model.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object (DTO) for transferring user data.
 * Used for user registration, updates, and information retrieval.
 * Includes user details, such as name, email, login, password, phone, and associated cars.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthday;
    private String login;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String phone;
    private List<Car> cars;
    private LocalDate createdAt;
    private LocalDate lastLogin;
    private PasswordEncoder passwordEncoder;

    /**
     * Converts the UserDTO to a User entity.
     *
     * @author Robson Rodrigues
     * @param dto the UserDTO to be converted
     * @return a User entity populated with the values from the DTO
     */
    public static User toEntity(UserDTO dto){
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setBirthday(dto.getBirthday());
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        if (dto.getCreatedAt() == null)
            user.setCreatedAt(LocalDate.now());
        else
            user.setCreatedAt(dto.getCreatedAt());
        if (dto.getLastLogin() != null)
            user.setLastLogin(dto.getLastLogin());
        user.setCars(dto.getCars());
        return user;
    }
}
