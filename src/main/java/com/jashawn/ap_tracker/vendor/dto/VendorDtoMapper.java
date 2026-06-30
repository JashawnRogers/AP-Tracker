package com.jashawn.ap_tracker.vendor.dto;

import com.jashawn.ap_tracker.vendor.Vendor;
import org.springframework.stereotype.Component;

@Component
public class VendorDtoMapper {

    public VendorResponse toDto(Vendor vendor) {
        return new VendorResponse(
                vendor.getId(),
                vendor.getName(),
                vendor.getEmail(),
                vendor.getPhoneNumber(),
                vendor.isActive(),
                vendor.getCreatedAt(),
                vendor.getUpdatedAt()
        );
    }
}
