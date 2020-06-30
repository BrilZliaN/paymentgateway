package ru.ifmo.practice.gateway.controller.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestClientException;
import ru.ifmo.practice.gateway.client.CustomerApi;
import ru.ifmo.practice.gateway.controller.ui.component.InvoiceInformationCard;
import ru.ifmo.practice.gateway.dto.entity.UserStorage;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Push
@Route("status")
@ParentLayout(MainView.class)
public class PaymentStatusView extends FormLayout {

    private final CustomerApi customerApi;
    private final UserStorage userStorage;
    private final InvoiceInformationCard invoiceInformationCard;
    private final Label bankAnswerLabel = new Label("");

    public PaymentStatusView(CustomerApi customerApi, UserStorage userStorage) {
        this.customerApi = customerApi;
        this.userStorage = userStorage;
        this.invoiceInformationCard = new InvoiceInformationCard(userStorage);
        add(invoiceInformationCard, new VerticalLayout(
                        new Label("Статус последней транзакции по данной покупке"), bankAnswerLabel));
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(this::update, 5, 30, TimeUnit.SECONDS);
        updateStatus();
    }

    private void update() {
        this.getUI().ifPresent(this::update);
    }

    private void update(UI ui) {
        ui.access(() -> new Notification("Обновление статуса...",  1000).open());
        try {
            var id = userStorage.getInvoice().getId();
            var status = customerApi.getInvoiceStatus(id);
            userStorage.setStatus(status);
        } catch (NullPointerException | RestClientException ignored) {
        }
        ui.access(() -> {
            invoiceInformationCard.update();
            updateStatus();
            ui.push();
        });
    }

    private void updateStatus() {
        String status;
        switch (userStorage.getStatus().getType()) {
            case WAITING_FOR_BANK_RESPONSE:
                status = "Ожидание ответа от банка";
                break;
            case PROCESSING:
                status = "Обработка платежа";
                break;
            case SUCCESS:
                status = "Платеж прошел успешно";
                break;
            case FAIL:
                status = "Платеж не прошел";
                break;
            case UNKNOWN:
                status = "Ожидание платежных данных";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + userStorage.getStatus().getType());
        }
        bankAnswerLabel.setText(status);
    }

}
