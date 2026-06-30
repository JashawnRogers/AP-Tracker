package com.jashawn.ap_tracker.invoice;

import com.jashawn.ap_tracker.exception.ResourceNotFoundException;
import com.jashawn.ap_tracker.invoice.dto.CreateInvoiceRequest;
import com.jashawn.ap_tracker.invoice.dto.InvoiceDtoMapper;
import com.jashawn.ap_tracker.invoice.dto.InvoiceResponse;
import com.jashawn.ap_tracker.invoice.dto.UpdateInvoiceRequest;
import com.jashawn.ap_tracker.vendor.Vendor;
import com.jashawn.ap_tracker.vendor.VendorRepository;
import com.jashawn.ap_tracker.vendor.dto.VendorDtoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final VendorRepository vendorRepository;
    private final VendorDtoMapper vendorDtoMapper;
    private final InvoiceDtoMapper invoiceDtoMapper;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          VendorRepository vendorRepository,
                          VendorDtoMapper vendorDtoMapper,
                          InvoiceDtoMapper invoiceDtoMapper) {
        this.invoiceRepository = invoiceRepository;
        this.vendorRepository = vendorRepository;
        this.vendorDtoMapper = vendorDtoMapper;
        this.invoiceDtoMapper = invoiceDtoMapper;
    }

    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {

      Vendor vendor = vendorRepository.findById(request.vendorId())
              .orElseThrow(() -> new ResourceNotFoundException("Vendor not found."));



        Invoice invoice = Invoice.create(
                vendor,
                request.invoiceNumber(),
                request.invoiceDate(),
                request.dueDate(),
                request.amount(),
                request.description()
        );

        Invoice saved = invoiceRepository.save(invoice);

        return invoiceDtoMapper.toDto(saved, vendorDtoMapper.toDto(vendor));
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoice(long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));

        return invoiceDtoMapper.toDto(invoice, vendorDtoMapper.toDto(invoice.getVendor()));
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
        return invoiceDtoMapper.toDto(saved, vendorDtoMapper.toDto(saved.getVendor()));

    }

    /*
        Queries the whole database #notgood
     */
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoice -> invoiceDtoMapper.toDto(invoice, vendorDtoMapper.toDto(invoice.getVendor())))
                .toList();
    }

    @Transactional
    public InvoiceResponse approveInvoice(long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));

        invoice.approve();

        Invoice saved = invoiceRepository.save(invoice);

        return invoiceDtoMapper.toDto(saved, vendorDtoMapper.toDto(saved.getVendor()));
    }

    @Transactional
    public InvoiceResponse rejectInvoice(long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));

        invoice.reject();

        Invoice saved = invoiceRepository.save(invoice);

        return invoiceDtoMapper.toDto(saved, vendorDtoMapper.toDto(saved.getVendor()));
    }

    @Transactional
    public void voidInvoice(long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));
        invoice.voidIt();
        invoiceRepository.save(invoice);
    }

    @Transactional
    public InvoiceResponse payInvoice(long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));

        invoice.pay();

        Invoice saved = invoiceRepository.save(invoice);

        return invoiceDtoMapper.toDto(saved, vendorDtoMapper.toDto(saved.getVendor()));
    }
}
