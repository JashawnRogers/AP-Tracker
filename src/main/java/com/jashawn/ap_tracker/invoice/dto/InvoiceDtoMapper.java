package com.jashawn.ap_tracker.invoice.dto;

import com.jashawn.ap_tracker.invoice.Invoice;
import com.jashawn.ap_tracker.vendor.dto.VendorResponse;
import org.springframework.stereotype.Component;

@Component
public class InvoiceDtoMapper {

    public InvoiceResponse toDto(Invoice invoice, VendorResponse vendor) {
        return new InvoiceResponse(
                invoice.getId(),
                vendor,
                invoice.getInvoiceNumber(),
                invoice.getInvoiceDate(),
                invoice.getDueDate(),
                invoice.getAmount(),
                invoice.getStatus(),
                invoice.getDescription(),
                invoice.getCreatedAt(),
                invoice.getUpdatedAt()

        );
    }
}
