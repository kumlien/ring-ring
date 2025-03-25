package se.kumliens.ringring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates an all-arguments constructor
@Builder // Generates a builder pattern for the class
public class User {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
    private LocalDateTime createdAt;
}
