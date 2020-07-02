package ru.ifmo.practice.gateway.controller.ui;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.builder.InvoicePostViewBuilder;
import ru.ifmo.practice.gateway.client.CustomerApi;
import ru.ifmo.practice.gateway.controller.ui.component.InvoiceInformationCard;
import ru.ifmo.practice.gateway.controller.ui.component.PaymentFormLayout;
import ru.ifmo.practice.gateway.dto.entity.UserStorage;

import java.util.List;
import java.util.Objects;

@Slf4j
@Route(value = "payment", layout = MainView.class)
public class PaymentFormView extends FormLayout implements HasUrlParameter<String> {

    private final CustomerApi customerApi;
    private final UserStorage userStorage;
    private final InvoicePostViewBuilder invoicePostViewBuilder;
    private final InvoiceInformationCard invoiceInformationCard;
    private final PaymentFormLayout paymentFormLayout;
    private final Label errorLabel = new Label("Ошибка. Укажите параметр sum в GET-параметрах страницы.");

    public PaymentFormView(CustomerApi customerApi,
                           UserStorage userStorage,
                           InvoicePostViewBuilder invoicePostViewBuilder) {
        this.customerApi = customerApi;
        this.userStorage = userStorage;
        this.invoicePostViewBuilder = invoicePostViewBuilder;
        this.invoiceInformationCard = new InvoiceInformationCard(userStorage);
        this.paymentFormLayout = new PaymentFormLayout(this::submitForm, userStorage);
        add(invoiceInformationCard, paymentFormLayout, errorLabel);
        invoiceInformationCard.update();
        toggleShow(userStorage.getInvoice() != null);
    }

    private void submitForm(CreditCardView creditCardView) {
        var id = userStorage.getInvoice().getId();
        try {
            customerApi.createTransaction(id, creditCardView);
        } catch (HttpClientErrorException clientException) {
            log.error("Client misconfiguration", clientException);
            new Notification("Невозможно отправить форму").open();
            return;
        } catch (HttpServerErrorException serverException) {
            var response = serverException.getResponseBodyAsString();
            new Notification(String.format("Ошибка обработки данных! %s", response)).open();
            return;
        }
        this.getUI().ifPresent(ui -> ui.navigate(PaymentStatusView.class));
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String sum) {
        List<String> list = sum == null ? List.of() : List.of(sum);
        event.getLocation()
                .getQueryParameters()
                .getParameters()
                .getOrDefault("sum", list)
                .stream()
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(this::setSum);
    }

    private void setSum(String sum) {
        Double sumNumber;
        try {
            sumNumber = Double.parseDouble(sum);
        } catch (NumberFormatException ignored) {
            return;
        }
        var oldSum = userStorage.getInvoiceSum() != null ? userStorage.getInvoiceSum() : -1d;
        if (Math.abs(sumNumber - oldSum) < 1e-9) {
            return;
        }
        userStorage.setInvoiceSum(sumNumber);
        var invoicePostView = invoicePostViewBuilder.build(userStorage.getInvoiceSum());
        var invoiceView = customerApi.addInvoice(invoicePostView);
        userStorage.setInvoice(invoiceView);
        invoiceInformationCard.update();
        toggleShow(true);
    }

    private void toggleShow(boolean showPaymentForm) {
        paymentFormLayout.setVisible(showPaymentForm);
        errorLabel.setVisible(!showPaymentForm);
    }
}
