package com.jashawn.ap_tracker.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InvoiceResponse(
        long id,
        long vendorId,
        String invoiceNumber,
        LocalDate invoiceDate,
        LocalDate dueDate,
        BigDecimal amount,
        InvoiceStatus status,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
