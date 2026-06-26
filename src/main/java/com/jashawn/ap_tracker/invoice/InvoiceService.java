package com.jashawn.ap_tracker.invoice;

import com.jashawn.ap_tracker.exception.ResourceNotFoundException;
import com.jashawn.ap_tracker.invoice.dto.CreateInvoiceRequest;
import com.jashawn.ap_tracker.invoice.dto.InvoiceResponse;
import com.jashawn.ap_tracker.invoice.dto.UpdateInvoiceRequest;
import com.jashawn.ap_tracker.vendor.Vendor;
import com.jashawn.ap_tracker.vendor.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final VendorRepository vendorRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, VendorRepository vendorRepository) {
        this.invoiceRepository = invoiceRepository;
        this.vendorRepository = vendorRepository;
    }

    @Transactional
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
                saved.getVendor(),
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

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoice(long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));

        return new InvoiceResponse(
                invoice.getId(),
                invoice.getVendor(),
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

    @Transactional
    public InvoiceResponse updateInvoice(UpdateInvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));

        if (request.vendorId() != null) {
//        Will throw NOT FOUND exception
            Vendor vendor = vendorRepository.getReferenceById(request.vendorId());

            invoice.updateVendor(vendor);
        }

        if (request.invoiceNumber() != null && !request.invoiceNumber().isBlank()) {
            invoice.updateInvoiceNumber(request.invoiceNumber());
        }

        if (request.invoiceDate() != null) {
            invoice.updateInvoiceDate(request.invoiceDate());
        }

        if (request.dueDate() != null) {
            invoice.updateDueDate(request.dueDate());
        }

        if (request.amount() != null) {
            invoice.updateAmount(request.amount());
        }

        if (request.status() != null) {
            if (request.status().equals(InvoiceStatus.PAID)) {
                invoice.pay();
            }

            if (request.status().equals(InvoiceStatus.REJECTED)) {
                invoice.reject();
            }

            if (request.status().equals(InvoiceStatus.VOIDED)) {
                invoice.voidIt();
            }

            if (request.status().equals(InvoiceStatus.APPROVED)) {
                invoice.approve();
            }

            if (request.status().equals(InvoiceStatus.PENDING_REVIEW)) {
                invoice.pendingReview();
            }
        }

        if (request.description() != null && !request.description().isBlank()) {
            invoice.updateDescription(request.description());
        }

        Invoice saved = invoiceRepository.save(invoice);
        return new InvoiceResponse(
                saved.getId(),
                saved.getVendor(),
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

    /*
        Queries the whole database #notgood
     */
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoice -> new InvoiceResponse(
                        invoice.getId(),
                        invoice.getVendor(),
                        invoice.getInvoiceNumber(),
                        invoice.getInvoiceDate(),
                        invoice.getDueDate(),
                        invoice.getAmount(),
                        invoice.getStatus(),
                        invoice.getDescription(),
                        invoice.getCreatedAt(),
                        invoice.getUpdatedAt()
                ))
                .toList();
    }
}
