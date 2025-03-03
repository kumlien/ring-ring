package se.kumliens.ringring.model;

import java.time.LocalDateTime;

public record Office(
        String id,
        String name,
        String address,
        LocalDateTime createdAt
) {}
