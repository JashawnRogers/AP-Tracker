package com.jashawn.ap_tracker.vendor;

import com.jashawn.ap_tracker.vendor.dto.CreateVendorRequest;
import com.jashawn.ap_tracker.vendor.dto.UpdateVendorRequest;
import com.jashawn.ap_tracker.vendor.dto.VendorResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class VendorController {

    private final VendorService service;
    private final VendorAssembler assembler;

    public VendorController(VendorService service, VendorAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping("/v1/vendors")
    public ResponseEntity<?> newVendor(@RequestBody @Validated CreateVendorRequest request) {
        EntityModel<VendorResponse> vendorModel = assembler.toModel(service.createVendor(request));

        return ResponseEntity
                .created(vendorModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(vendorModel);
    }

    @GetMapping("/v1/vendors/{id}")
    public ResponseEntity<?> getVendor(@PathVariable Long id) {
        EntityModel<VendorResponse> vendorModel = assembler.toModel(service.findVendor(id));

        return ResponseEntity
                .ok(vendorModel);
    }

    @GetMapping("/v1/vendors")
    public CollectionModel<EntityModel<VendorResponse>> getAllVendors() {

        List<EntityModel<VendorResponse>> vendors = service.getAllVendors().stream()
                .map(vendor -> EntityModel.of(
                        vendor,
                        linkTo(methodOn(VendorController.class).getVendor(vendor.id())).withSelfRel(),
                        linkTo(methodOn(VendorController.class).getAllVendors()).withRel("vendors")
                ))
                .toList();

        return CollectionModel.of(vendors,
                linkTo(methodOn(VendorController.class).getAllVendors()).withSelfRel()
                );
    }

    @PutMapping("/v1/vendors")
    public ResponseEntity<?> updateVendor(@RequestBody UpdateVendorRequest request) {
        EntityModel<VendorResponse> vendorModel = assembler.toModel(service.updateVendor(request));

        return ResponseEntity
                .created(vendorModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(vendorModel);
    }

    @DeleteMapping("/v1/vendors/{id}")
    public ResponseEntity<?> deactivateVendor(@PathVariable long id) {
        service.deactivateVendor(id);

        return ResponseEntity.noContent().build();
    }
}
