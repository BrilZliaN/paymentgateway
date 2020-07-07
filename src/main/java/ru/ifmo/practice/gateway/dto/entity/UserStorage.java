package ru.ifmo.practice.gateway.dto.entity;

import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.api.models.InvoiceView;
import ru.ifmo.practice.gateway.api.models.TransactionReadinessView;

import java.util.UUID;

@Getter
@Setter
@Component
@VaadinSessionScope
public class UserStorage {

    private UUID request = UUID.randomUUID();

    private InvoiceView invoice;
    private Double invoiceSum;
    private CreditCardView creditCardView = new CreditCardView();
    private TransactionReadinessView status = new TransactionReadinessView()
            .type(TransactionReadinessView.TypeEnum.UNKNOWN);

}
