package ru.ifmo.practice.gateway.controller.ui.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.LongRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.dto.entity.UserStorage;

import java.util.function.Consumer;

public class PaymentFormLayout extends VerticalLayout {

    private final TextField creditCardField = new TextField();
    private final TextField cvvField = new TextField();
    private final ExpirationDateField dateField = new ExpirationDateField();
    private final TextField holderField = new TextField();
    private final Binder<CreditCardView> binder = new Binder<>();

    private final Consumer<CreditCardView> submitAction;
    private final UserStorage userStorage;

    public PaymentFormLayout(Consumer<CreditCardView> submitAction,
                             UserStorage userStorage) {
        this.submitAction = submitAction;
        this.userStorage = userStorage;

        init();
    }

    private void init() {
        creditCardField.setLabel("Номер карты");
        creditCardField.setMaxLength(19);
        creditCardField.setPattern("[0-9]*");
        creditCardField.setMaxWidth("100%");
        creditCardField.setMinWidth("80%");
        binder.forField(creditCardField)
                .asRequired("Номер карты не должен быть пустым")
                .withConverter(Long::valueOf, Number::toString)
                .withValidator(new LongRangeValidator("Карта должна быть длиной от 12 до 19 цифр",
                        (long) 1e11, Long.MAX_VALUE))
                .withValidator(new CreditCardNumberValidator("Ошибка в номере карты"))
                .bind(CreditCardView::getNumber, CreditCardView::setNumber);
        cvvField.setLabel("CVC / CVV");
        cvvField.setMaxLength(3);
        cvvField.setPattern("[0-9]{3}");
        cvvField.setPlaceholder("000");
        cvvField.setMaxWidth("100%");
        cvvField.setMinWidth("80%");
        binder.forField(cvvField)
                .asRequired("CVV не должен быть пустым")
                .withValidator(new StringLengthValidator("CVV должен быть длиной в 3 цифры",
                        3, 3))
                .bind(CreditCardView::getCvv, CreditCardView::setCvv);
        binder.forField(dateField)
                .asRequired("Установите месяц и год окончания действия карты")
                .withValidator(new StringLengthValidator(
                        "Установите месяц или год окончания действия карты", 5, 5))
                .bind(CreditCardView::getExpirationDate, CreditCardView::setExpirationDate);
        holderField.setLabel("Держатель карты");
        holderField.setMaxWidth("100%");
        holderField.setMinWidth("80%");
        holderField.setPlaceholder("MR CARDHOLDER");
        binder.forField(holderField)
                .asRequired("Напишите имя и фамилию держателя карты")
                .bind(CreditCardView::getHolder, CreditCardView::setHolder);
        var submit = new Button("Оплатить");
        submit.addClickListener(this::onSubmit);
        add(creditCardField, cvvField, dateField, holderField, submit);
    }

    private void onSubmit(ClickEvent<Button> ignored) {
        if (binder.writeBeanIfValid(userStorage.getCreditCardView())) {
            submitAction.accept(userStorage.getCreditCardView());
        } else {
            binder.validate();
        }
    }
}
