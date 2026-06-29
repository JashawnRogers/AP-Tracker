package com.jashawn.ap_tracker.vendor;

import com.jashawn.ap_tracker.exception.ResourceNotFoundException;
import com.jashawn.ap_tracker.vendor.dto.CreateVendorRequest;
import com.jashawn.ap_tracker.vendor.dto.UpdateVendorRequest;
import com.jashawn.ap_tracker.vendor.dto.VendorResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VendorService {
    private final VendorRepository repository;

    public VendorService(VendorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public VendorResponse createVendor(CreateVendorRequest request) {
        Vendor vendor = Vendor.create(
                request.name(),
                request.email(),
                request.phoneNumber()
        );

        Vendor saved = repository.save(vendor);

        return new VendorResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPhoneNumber(),
                saved.isActive(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public VendorResponse findVendor(Long id) {
        Vendor vendor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found."));

        return new VendorResponse(
                vendor.getId(),
                vendor.getName(),
                vendor.getEmail(),
                vendor.getPhoneNumber(),
                vendor.isActive(),
                vendor.getCreatedAt(),
                vendor.getUpdatedAt()
        );
    }

    @Transactional
    public VendorResponse updateVendor(UpdateVendorRequest request) {
        Vendor vendor = repository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found."));

        if (request.name() != null) {
            vendor.updateName(request.name());
        }

        if (request.email() != null) {
            vendor.updateEmail(request.email());
        }

        if (request.phoneNumber() != null) {
            vendor.updatePhoneNumber(request.phoneNumber());
        }

        if (request.active() != null) {
            if (request.active()) {
                vendor.activate();
            }

            if (!request.active()) {
                vendor.deactivate();
            }
        }

        Vendor saved = repository.save(vendor);

        return new VendorResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPhoneNumber(),
                saved.isActive(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }


    @Transactional(readOnly = true)
    public List<VendorResponse> getAllVendors() {
        return repository.findAll().stream()
                .map(invoice -> new VendorResponse(
                        invoice.getId(),
                        invoice.getName(),
                        invoice.getEmail(),
                        invoice.getPhoneNumber(),
                        invoice.isActive(),
                        invoice.getCreatedAt(),
                        invoice.getUpdatedAt()
                ))
                .toList();
    }

    @Transactional
    public void deactivateVendor(long id) {
        Vendor vendor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found."));

        vendor.deactivate();

        repository.save(vendor);
    }
}
