package com.jashawn.ap_tracker.vendor.dto;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record CreateVendorRequest(
        @NonNull
        String name,
        @Nullable
        String email,
        @Nullable
        String phoneNumber
) {}
