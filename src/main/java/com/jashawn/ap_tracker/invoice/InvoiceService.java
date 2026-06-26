package com.jashawn.ap_tracker.invoice;

import com.jashawn.ap_tracker.vendor.Vendor;
import com.jashawn.ap_tracker.vendor.VendorRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final VendorRepository vendorRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, VendorRepository vendorRepository) {
        this.invoiceRepository = invoiceRepository;
        this.vendorRepository = vendorRepository;
    }

    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
//        Will throw NOT FOUND exception
        Vendor vendor = vendorRepository.getReferenceById(request.vendorId());

        Invoice invoice = Invoice.create(
                vendor,
                request.invoiceNumber(),
                request.invoiceDate(),
                request.dueDate(),
                request.amount(),
                request.description()
        );

        Invoice saved = invoiceRepository.save(invoice);

        return new InvoiceResponse(
                saved.getId(),
                saved.getVendor().getId(),
                saved.getInvoiceNumber(),
                saved.getInvoiceDate(),
                saved.getDueDate(),
                saved.getAmount(),
                saved.getStatus(),
                saved.getDescription(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
