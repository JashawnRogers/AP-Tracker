package com.jashawn.ap_tracker.vendor;

import com.jashawn.ap_tracker.exception.ResourceNotFoundException;
import com.jashawn.ap_tracker.vendor.dto.CreateVendorRequest;
import com.jashawn.ap_tracker.vendor.dto.UpdateVendorRequest;
import com.jashawn.ap_tracker.vendor.dto.VendorDtoMapper;
import com.jashawn.ap_tracker.vendor.dto.VendorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendorService {
    private final VendorRepository repository;
    private final VendorDtoMapper mapper;

    public VendorService(VendorRepository repository, VendorDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public VendorResponse createVendor(CreateVendorRequest request) {
        Vendor vendor = Vendor.create(
                request.name(),
                request.email(),
                request.phoneNumber()
        );

        Vendor saved = repository.save(vendor);

        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public VendorResponse findVendor(Long id) {
        Vendor vendor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found."));

        return mapper.toDto(vendor);
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

        return mapper.toDto(saved);
    }


    @Transactional(readOnly = true)
    public Page<VendorResponse> getAllVendors(String name, Boolean active, PageRequest pageRequest) {
        Specification<Vendor> spec = Specification
                .where(VendorSpecifications.hasName(name))
                .and(VendorSpecifications.isActive(active));

        return repository.findAll(spec, pageRequest)
                .map(mapper::toDto);
    }

    @Transactional
    public void deactivateVendor(long id) {
        Vendor vendor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found."));

        vendor.deactivate();

        repository.save(vendor);
    }
}
