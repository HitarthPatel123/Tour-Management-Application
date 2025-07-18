package com.tours.backend.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "This is required!!")
    @Size(min = 2, max = 20, message = "Min 2 and Max 20 charcters are only allowed!!")
    private String name;

    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message="Invalid Email!!")
    @Column(unique = true)
    private String email;

    @NotBlank(message ="This is required !!")
    @Size(min=5, message ="Password must be at least 5 characters long!!")
    private String password;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be a valid 10-digit number!!")
    @Column(unique = true)
    private String contactNumber;


    private String role;
    private boolean enabled;
}
