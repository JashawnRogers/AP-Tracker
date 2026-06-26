package com.jashawn.ap_tracker.vendor.dto;

import java.time.LocalDateTime;

public record VendorResponse(
        long id,
        String name,
        String email,
        String phoneNumber,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
