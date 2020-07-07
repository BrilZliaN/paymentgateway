package ru.ifmo.practice.gateway.controller.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestClientException;
import ru.ifmo.practice.gateway.api.models.TransactionView;
import ru.ifmo.practice.gateway.client.CustomerApi;

import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Route(value = "transactions", layout = MainView.class)
class AllTransactionsView extends VerticalLayout {

    private final CustomerApi customerApi;
    private final ProgressBar progressBar = new ProgressBar();
    private final Grid<TransactionView> grid;

    public AllTransactionsView(CustomerApi customerApi) {
        this.customerApi = customerApi;
        progressBar.setIndeterminate(true);
        grid = new Grid<>(TransactionView.class);

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(this::update, 1, 300, TimeUnit.SECONDS);
        add(progressBar, grid);
    }

    private void update() {
        this.getUI().ifPresent(this::update);
    }

    private void update(UI ui) {
        ui.access(() -> new Notification("Обновление данных...",  1000).open());
        Collection<TransactionView> transactions;
        try {
            transactions = customerApi.getTransactions();
        } catch (RestClientException clientException) {
            var data = clientException.getMessage();
            ui.access(() -> new Notification("Невозможно получить данные: " + data,  10000).open());
            return;
        }
        ui.access(() -> {
            progressBar.setVisible(false);
            grid.setItems(transactions);
            ui.push();
        });
    }

}
