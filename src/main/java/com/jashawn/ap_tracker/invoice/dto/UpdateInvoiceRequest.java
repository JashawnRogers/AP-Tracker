package com.jashawn.ap_tracker.invoice.dto;

import com.jashawn.ap_tracker.invoice.InvoiceStatus;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInvoiceRequest(
        long id,

        @Nullable
        Long vendorId,

        @Nullable
        String invoiceNumber,

        @Nullable
        LocalDate invoiceDate,

        @Nullable
        LocalDate dueDate,

        @Nullable
        BigDecimal amount,

        @Nullable
        InvoiceStatus status,

        @Nullable
        String description
) {
}
