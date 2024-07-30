package com.example.lap6.Model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty(message = "Id should be not empty !")
    @Size(min = 3 , message = "Id length should be more than 2 characters !")
    private String id ;

    @NotEmpty(message = "Name should be not empty !")
    @Size(min = 5 , message = "Name length should be more than 2 characters !")
    @Pattern(regexp = "^[a-zA-Z]+$" , message = "Name should be only letters")
    private String name ;

    @NotNull(message = "Age should be not empty !")
    @Positive(message = "Age should be a number !")
    @Min(value = 26 , message = "Age should be more than 25")
    @Max(value = 99, message = "Age should be less than 100")
    private int age ;

    @Email(message = "Email should be valid email address format")
    private String email ;

    @Pattern(regexp = "^05\\d{8}$" , message = "Phone Number should start with 05")
    @Size(min = 10 , max = 10 , message = "Phone Number should contain exactly 10 digits")
    private String phoneNumber;

    @NotEmpty(message = "Position should be not empty !")
    @Pattern(regexp = "^(supervisor|coordinator)" , message = "Position should be supervisor or coordinator !")
    private String position ;

    @AssertFalse(message ="On leave should be initially false !")
    private boolean onLeave ;

    @NotNull(message = "Employment Year should be not null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Employment Year should be in the past or present")
    private LocalDate hireDate ;


    @NotNull(message = "Annual Leave date should be not null")
    @Positive(message = "Annual Leave should be a positive number !")
    private int annualLeave ;

}
