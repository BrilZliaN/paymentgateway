package ru.ifmo.practice.coordiantor.coordinator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentData {
    public PaymentData(Transaction transaction, String source) {
        this.transaction = transaction;
        this.source = source;
    }
    @JsonProperty("transaction")
    public Transaction transaction;
    
    @JsonProperty("source")
    public String source;
}
