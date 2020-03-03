package ru.ifmo.practice.gateway.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.TransactionReadinessView;

@Component
public class TransactionReadinessViewModelAssembler implements RepresentationModelAssembler<TransactionReadinessView, EntityModel<TransactionReadinessView>> {

    @Override
    public EntityModel<TransactionReadinessView> toModel(TransactionReadinessView entity) {
        return new EntityModel<>(entity);
    }
}
