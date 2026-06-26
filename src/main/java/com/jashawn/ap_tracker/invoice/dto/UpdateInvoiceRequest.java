package com.jashawn.ap_tracker.invoice.dto;

import com.jashawn.ap_tracker.invoice.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInvoiceRequest(
        long id,
        Long vendorId,
        String invoiceNumber,
        LocalDate invoiceDate,
        LocalDate dueDate,
        BigDecimal amount,
        InvoiceStatus status,
        String description
) {
}
