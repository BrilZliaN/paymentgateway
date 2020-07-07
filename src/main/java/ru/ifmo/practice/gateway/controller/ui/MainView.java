package ru.ifmo.practice.gateway.controller.ui;

import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

@Push
@Route("")
public class MainView extends VerticalLayout implements RouterLayout {

    public MainView() {
        var bar = new MenuBar();
        bar.addItem(new RouterLink("Payment gateway", MainView.class));
        bar.addItem(new RouterLink("Payment form", PaymentFormView.class));
        bar.addItem(new RouterLink("Payment status", PaymentStatusView.class));
        bar.addItem(new RouterLink("All transactions", AllTransactionsView.class));
        add(bar);
        setHorizontalComponentAlignment(Alignment.CENTER, bar);
    }
}
