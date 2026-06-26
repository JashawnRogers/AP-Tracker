package com.jashawn.ap_tracker.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateInvoiceRequest(
        long vendorId,
        String invoiceNumber,
        LocalDate invoiceDate,
        LocalDate dueDate,
        BigDecimal amount,
        String description
) {}
