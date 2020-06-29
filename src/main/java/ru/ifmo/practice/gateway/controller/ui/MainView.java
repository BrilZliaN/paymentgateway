package ru.ifmo.practice.gateway.controller.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

@Push
@Route("")
public class MainView extends Div implements RouterLayout {

    public MainView() {
        var layout = new VerticalLayout();
        var label = new Label("payment-gateway");
        var allTransactionsLink = new RouterLink("View all transactions", AllTransactionsView.class);
        var openPaymentForm = new RouterLink("Open payment form", PaymentFormView.class);
        var openPaymentStatus = new RouterLink("Open payment status", PaymentStatusView.class);
        layout.add(label, allTransactionsLink, openPaymentForm, openPaymentStatus);
        add(layout);
    }
}
