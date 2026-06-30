package com.jashawn.ap_tracker.invoice;

import com.jashawn.ap_tracker.invoice.dto.InvoiceResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InvoiceAssembler implements RepresentationModelAssembler<InvoiceResponse, EntityModel<InvoiceResponse>> {



    @Override
    public EntityModel<InvoiceResponse> toModel(@NonNull InvoiceResponse entity) {
        EntityModel<InvoiceResponse> model = EntityModel.of(entity,
                    linkTo(methodOn(InvoiceController.class).findInvoice(entity.id())).withSelfRel(),
                    linkTo(methodOn(InvoiceController.class).getAllInvoices()).withRel("invoices")
                );

        if (entity.status() == InvoiceStatus.PENDING_REVIEW) {
            model.add(linkTo(methodOn(InvoiceController.class).voidInvoice(entity.id())).withRel("void"));
            model.add(linkTo(methodOn(InvoiceController.class).approveInvoice(entity.id())).withRel("approve"));
            model.add(linkTo(methodOn(InvoiceController.class).rejectInvoice(entity.id())).withRel("reject"));
        }

        return model;
    }
}
