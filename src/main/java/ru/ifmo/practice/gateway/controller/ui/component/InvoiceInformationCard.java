package ru.ifmo.practice.gateway.controller.ui.component;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.ifmo.practice.gateway.dto.entity.UserStorage;

public class InvoiceInformationCard extends VerticalLayout {

    private static final String INVOICE_TEXT = "Покупка №%d";
    private static final String SUM_TEXT = "Сумма: %.2f руб.";

    private final Label invoiceLabel = new Label("");
    private final Label sumLabel = new Label("");
    private final UserStorage userStorage;

    public InvoiceInformationCard(UserStorage userStorage) {
        this.userStorage = userStorage;
        update();
        add(invoiceLabel, sumLabel);
    }

    public void update() {
        var invoice = userStorage.getInvoice();
        if (invoice != null) {
            invoiceLabel.setText(String.format(INVOICE_TEXT, invoice.getId()));
            sumLabel.setText(String.format(SUM_TEXT, invoice.getSum()));
        }
    }

}
