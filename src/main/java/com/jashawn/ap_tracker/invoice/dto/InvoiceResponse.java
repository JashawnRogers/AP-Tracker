package com.jashawn.ap_tracker.invoice.dto;

import com.jashawn.ap_tracker.invoice.InvoiceStatus;
import com.jashawn.ap_tracker.vendor.Vendor;
import com.jashawn.ap_tracker.vendor.dto.VendorResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InvoiceResponse(
        long id,
        VendorResponse vendor,
        String invoiceNumber,
        LocalDate invoiceDate,
        LocalDate dueDate,
        BigDecimal amount,
        InvoiceStatus status,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
