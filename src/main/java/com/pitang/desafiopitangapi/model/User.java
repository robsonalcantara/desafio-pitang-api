package com.pitang.desafiopitangapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pitang.desafiopitangapi.dto.UserDTO;
import com.pitang.desafiopitangapi.exceptions.BusinessException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Represents a user entity with its associated properties and methods.
 * This class is mapped to the "USERS" table in the database.
 */
@Entity
@Table(name = "USERS")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class User {

    /**
     * The unique identifier of the user.
     * It is generated automatically using UUID strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "USER_ID")
    private String id;

    /**
     * The first name of the user.
     * This field is mandatory.
     */
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    /**
     * The last name of the user.
     * This field is mandatory.
     */
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    /**
     * The email address of the user.
     * This field is mandatory and must match a valid email format.
     */
    @Column(name = "EMAIL", nullable = false)
    private String email;

    /**
     * The birthday of the user.
     * This field is mandatory and is formatted as "yyyy-MM-dd".
     */
    @Column(name = "BIRTHDAY", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * The login of the user.
     * This field is mandatory.
     */
    @Column(name = "LOGIN", nullable = false)
    private String login;

    /**
     * The password of the user.
     * This field is mandatory.
     */
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    /**
     * The phone number of the user.
     * This field is mandatory and must match a valid phone number pattern.
     */
    @Column(name = "PHONE", nullable = false)
    private String phone;

    /**
     * The creation date of the user's account.
     * This field is mandatory.
     */
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDate createdAt;

    /**
     * The date the user last logged in.
     * This field may be null if the user hasn't logged in yet.
     */
    @Column(name = "LAST_LOGIN")
    private LocalDate lastLogin;

    /**
     * The list of cars associated with the user.
     * This is a one-to-many relationship with the Car entity.
     */
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Car> cars;

    /**
     * Validates the fields of the user.
     * Throws a {@link BusinessException} if any of the fields are missing or invalid.
     * The validation checks include:
     * <ul>
     *     <li>First name, last name, email, birthday, login, password, and phone cannot be null or empty.</li>
     *     <li>Email must match a valid email format.</li>
     *     <li>Phone number must match a valid phone number pattern.</li>
     * </ul>
     *
     * @throws BusinessException if any validation fails.
     */
    public void validate() {
        if(firstName == null || firstName.isEmpty())
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);
        if(lastName == null || lastName.isEmpty())
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);
        if(email == null || email.isEmpty())
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);
        if(birthday == null)
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);
        if(login == null || login.isEmpty())
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);
        if(password == null || password.isEmpty())
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);
        if(phone == null || phone.isEmpty())
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);

        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"))
            throw new BusinessException("Invalid fields", HttpStatus.BAD_REQUEST);

        String regex = "^(\\+\\d{1,2}\\s?)?\\(?\\d{2,3}\\)?\\s?-?\\d{4,5}-?\\d{4}$|^\\d{8,9}$";

        if (!phone.matches(regex)) {
            throw new BusinessException("Invalid fields", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Converts a {@link User} entity to a {@link UserDTO}.
     *
     * @author Robson Rodrigues
     * @param user The user entity to be converted.
     * @return A {@link UserDTO} containing the user's data.
     */
    public static UserDTO toDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setBirthday(user.getBirthday());
        userDTO.setLogin(user.getLogin());
        userDTO.setPhone(user.getPhone());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setLastLogin(user.getLastLogin());
        return userDTO;
    }
}
