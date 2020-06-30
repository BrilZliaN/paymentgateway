package ru.ifmo.practice.gateway.builder;

import org.springframework.stereotype.Component;
import ru.ifmo.practice.gateway.api.models.CreditCardView;
import ru.ifmo.practice.gateway.dto.entity.Card;

@Component
public class CardBuilder {

    public Card build(CreditCardView view) {
        var card = new Card();
        card.setNumber(view.getNumber());
        card.setOwner(view.getHolder());
        card.setValid(view.getExpirationDate());
        return card;
    }
}
