package ru.ifmo.practice.gateway.controller.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;

@Push
@Route("transactions")
@ParentLayout(MainView.class)
public class AllTransactionsView extends Div {

}
