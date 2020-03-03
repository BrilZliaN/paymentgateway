package ru.ifmo.practice.gateway.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.InvoiceView;

@Component
public class InvoiceViewModelAssembler implements RepresentationModelAssembler<InvoiceView, EntityModel<InvoiceView>> {

    @Override
    public EntityModel<InvoiceView> toModel(InvoiceView entity) {
        return new EntityModel<>(entity);
    }
}
