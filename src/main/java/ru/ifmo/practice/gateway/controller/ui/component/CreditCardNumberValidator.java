package ru.ifmo.practice.gateway.controller.ui.component;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;

public class CreditCardNumberValidator extends AbstractValidator<Long> {

    public CreditCardNumberValidator(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public ValidationResult apply(Long value, ValueContext valueContext) {
        if (value == null) {
            return this.toResult(value, false);
        } else {
            var stringValue = Long.toString(value);
//            var isValid = CreditCardValidator.validateCardNumber(stringValue); TODO uncomment this line
            var isValid = true;
            return this.toResult(value, isValid);
        }
    }
}
