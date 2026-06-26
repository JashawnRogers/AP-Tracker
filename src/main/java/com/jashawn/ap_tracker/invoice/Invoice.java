package com.jashawn.ap_tracker.invoice;

import com.jashawn.ap_tracker.exception.BadRequestException;
import com.jashawn.ap_tracker.vendor.Vendor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private String invoiceNumber;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static Invoice create(
            Vendor vendor,
            String invoiceNumber,
            LocalDate invoiceDate,
            LocalDate dueDate,
            BigDecimal amount,
            String description
    ) {
        if (vendor == null) {
            throw new BadRequestException("Invoice must have a vendor.");
        }

        if (invoiceNumber == null ||invoiceNumber.isBlank()) {
            throw new BadRequestException("Invoice must have an invoice number.");
        }

        if (invoiceDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("Invoice date cannot be in the future.");
        }

        if (invoiceDate.isAfter(dueDate)) {
            throw new BadRequestException("Invoice date cannot be after due date.");
        }

        if (dueDate.isBefore(invoiceDate)) {
            throw new BadRequestException("Due date cannot be before invoice date.");
        }

        if (amount == null) {
            throw new BadRequestException("Invoice must have an amount.");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Amount must be at least $0.");
        }

        if (description == null || description.isBlank()) {
            throw new BadRequestException("Invoice must have a description.");
        }

        return Invoice.builder()
                .vendor(vendor)
                .invoiceNumber(invoiceNumber)
                .invoiceDate(invoiceDate)
                .dueDate(dueDate)
                .amount(amount)
                .description(description)
                .build();
    }

    public void approve() {
        if (this.status != InvoiceStatus.PENDING_REVIEW) {
            throw new BadRequestException("Only pending invoices can be approved.");
        }

        this.status = InvoiceStatus.APPROVED;
    }

    public void pay() {
        if (this.status != InvoiceStatus.APPROVED) {
            throw new BadRequestException("Only approved invoices can be paid.");
        }

        this.status = InvoiceStatus.PAID;
    }

    public void reject() {
        if (this.status == InvoiceStatus.VOIDED || this.status == InvoiceStatus.PAID) {
            throw new BadRequestException("Cannot modify closed invoices.");
        }

        this.status = InvoiceStatus.REJECTED;
    }

    public void voidIt() {
        if (this.status == InvoiceStatus.PAID) {
            throw new BadRequestException("Cannot modify closed invoices.");
        }

        this.status = InvoiceStatus.VOIDED;
    }

    public void pendingReview() {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.VOIDED) {
            throw new BadRequestException("Cannot modify closed invoices.");
        }

        this.status = InvoiceStatus.PENDING_REVIEW;
    }

    public void updateVendor(Vendor vendor) {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.VOIDED) {
            throw new BadRequestException("Cannot modify closed invoices.");
        }

        this.vendor = vendor;
    }

    public void updateDescription(String description) {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.VOIDED) {
            throw new BadRequestException("Cannot modify closed invoices.");
        }

        this.description = description;
    }

    public void updateInvoiceNumber(String invoiceNumber) {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.VOIDED) {
            throw new BadRequestException("Cannot modify closed invoices.");
        }

        this.invoiceNumber = invoiceNumber;
    }

    public void updateInvoiceDate(LocalDate invoiceDate) {
        if (invoiceDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("Invoice date cannot be a future date.");
        }

        if (invoiceDate.isAfter(this.getDueDate())) {
            throw new BadRequestException("Invoice date cannot be after due date.");
        }

        this.invoiceDate = invoiceDate;
    }

    public void updateDueDate(LocalDate dueDate) {
        if (dueDate.isBefore(this.getInvoiceDate())) {
            throw new BadRequestException("Due date cannot be before invoice date.");
        }

        this.dueDate = dueDate;
    }

    public void updateAmount(BigDecimal amount) {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.VOIDED) {
            throw new BadRequestException("Cannot modify closed invoices.");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Amount must be at least $0.");
        }

        this.amount = amount;
    }

    @PrePersist
    private void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
        this.status = InvoiceStatus.PENDING_REVIEW;
    }

    @PreUpdate
    private void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
