package se.kumliens.ringring.model;

import java.time.LocalDateTime;

public record User(
        String id,
        String email,
        String firstName,
        String lastName,
        String role,
        String status,
        LocalDateTime createdAt
) {}
