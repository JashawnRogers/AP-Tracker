package com.jashawn.ap_tracker.vendor.dto;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record UpdateVendorRequest(
        long id,

        @Nullable
        String name,

        @Nullable
        String email,

        @Nullable
        String phoneNumber,

        @Nullable
        Boolean active,

        @Nullable
        LocalDateTime createdAt,

        @Nullable
        LocalDateTime updatedAt
) {
}
