package com.jashawn.ap_tracker.vendor.dto;

public record CreateVendorRequest(
        String name,
        String email,
        String phoneNumber
) {}
