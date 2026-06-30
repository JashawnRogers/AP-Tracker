package com.jashawn.ap_tracker.invoice;

import com.jashawn.ap_tracker.invoice.dto.CreateInvoiceRequest;
import com.jashawn.ap_tracker.invoice.dto.InvoiceResponse;
import com.jashawn.ap_tracker.invoice.dto.UpdateInvoiceRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InvoiceController {

    private final InvoiceService service;
    private final InvoiceAssembler assembler;

    public InvoiceController(InvoiceService service, InvoiceAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping("/v1/invoices")
    public ResponseEntity<?> newInvoice(@RequestBody @Validated CreateInvoiceRequest request) {
        EntityModel<InvoiceResponse> model = assembler.toModel(service.createInvoice(request));

        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping
    public CollectionModel<EntityModel<InvoiceResponse>> getAllInvoices() {
        List<EntityModel<InvoiceResponse>> invoices = service.getAllInvoices().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(invoices,
                linkTo(methodOn(InvoiceController.class).getAllInvoices()).withSelfRel()
        );
    }

    @GetMapping("/v1/invoices/{id}")
    public ResponseEntity<?> findInvoice(long id) {
        return ResponseEntity.ok(assembler.toModel(service.getInvoice(id)));
    }

    @PutMapping("/v1/invoices")
    public ResponseEntity<?> updateInvoice(UpdateInvoiceRequest request) {
        return ResponseEntity.ok(assembler.toModel(service.updateInvoice(request)));
    }

    @DeleteMapping("/v1/invoices/{id}")
    public ResponseEntity<?> voidInvoice(@PathVariable long id) {
        service.voidInvoice(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/v1/invoices/approve/{id}")
    public ResponseEntity<?> approveInvoice(@PathVariable long id) {
        return ResponseEntity.ok(assembler.toModel(service.approveInvoice(id)));
    }

    @PutMapping("/v1/invoices/reject/{id}")
    public ResponseEntity<?> rejectInvoice(@PathVariable long id) {
        return ResponseEntity.ok(assembler.toModel(service.rejectInvoice(id)));
    }
}
