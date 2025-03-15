package se.kumliens.ringring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates an all-arguments constructor
@Builder // Generates a builder pattern for the class
@Accessors(fluent = true) // Generates record-style getters and setters
public class User {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String status;
    private LocalDateTime createdAt;
}
