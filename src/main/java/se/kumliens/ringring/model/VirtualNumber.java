package se.kumliens.ringring.model;

import java.time.LocalDateTime;

public record VirtualNumber(
        String id,
        String number,
        String officeId,
        String status,
        LocalDateTime createdAt
) {}