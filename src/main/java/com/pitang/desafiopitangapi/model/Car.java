package com.pitang.desafiopitangapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pitang.desafiopitangapi.exceptions.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Represents a car entity.
 * This class is mapped to the "CARS" table in the database.
 */
@Entity
@Table(name = "CARS")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Car {

    /**
     * The unique identifier of the car.
     * It is generated automatically using UUID strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CAR_ID")
    private String id;

    /**
     * The year of manufacture of the car.
     * This field is mandatory.
     */
    @Column(name = "CAR_YEAR", nullable = false)
    private Integer year;

    /**
     * The license plate of the car.
     * This field is mandatory and follows a specific format: "XXX-1234".
     */
    @Column(name = "LICENSE_PLATE", nullable = false)
    private String licensePlate;

    /**
     * The model of the car.
     * This field is mandatory.
     */
    @Column(name = "MODEL", nullable = false)
    private String model;

    /**
     * The color of the car.
     * This field is mandatory.
     */
    @Column(name = "COLOR", nullable = false)
    private String color;

    @Column(name = "USAGE", nullable = false)
    private Boolean usage = false;

    @Column(name = "USAGE_COUNT", nullable = false)
    private Integer usageCount = 0;

    /**
     * The user associated with the car.
     * This is a many-to-one relationship to the User entity.
     */
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonBackReference
    private User user;

    /**
     * Validates the fields of the car.
     * Throws a {@link BusinessException} if any of the fields are missing or invalid.
     * The validation checks include:
     * <ul>
     *     <li>Year cannot be in the future.</li>
     *     <li>License plate must match the pattern "XXX-1234".</li>
     *     <li>Model and color cannot be empty.</li>
     *     <li>Year, license plate, model, and color cannot be null.</li>
     * </ul>
     *
     * @throws BusinessException if any validation fails.
     */
    public void validate() {
        if(year == null)
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);
        if(licensePlate == null || licensePlate.isEmpty())
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);
        if(model == null || model.isEmpty())
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);
        if(color == null || color.isEmpty())
            throw new BusinessException("Missing fields", HttpStatus.BAD_REQUEST);

        if(year > LocalDateTime.now().getYear())
            throw new BusinessException("Invalid fields", HttpStatus.BAD_REQUEST);

        if (!licensePlate.matches("(?i)^[A-Z]{3}-\\d{4}$") &&
                !licensePlate.matches("(?i)^[A-Z]{3}\\d[A-Z]\\d{2}$"))
            throw new BusinessException("Invalid fields", HttpStatus.BAD_REQUEST);

        if (usage == null)
            usage = false;

        if (usageCount == null || usageCount < 0)
            usageCount = 0;

    }
}
