package com.jashawn.ap_tracker.vendor;

import com.jashawn.ap_tracker.vendor.dto.VendorResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class VendorAssembler implements RepresentationModelAssembler<VendorResponse, EntityModel<VendorResponse>> {

    @Override
    public EntityModel<VendorResponse> toModel(@NonNull VendorResponse entity) {
        EntityModel<VendorResponse> model = EntityModel.of(entity,
                linkTo(methodOn(VendorController.class).getVendor(entity.id())).withSelfRel()
        );

        if (entity.active()) {
            model.add(linkTo(methodOn(VendorController.class).deactivateVendor(entity.id())).withRel("deactivate"));
        }

        return model;
    }

}
