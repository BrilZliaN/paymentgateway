package ru.ifmo.practice.coordiantor.coordinator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionStatusView {
    @JsonProperty("answerCode")
    private String answerCode = null;

    public TransactionStatusView answerCode(String answerCode) {
        this.answerCode = answerCode;
        return this;
    }
}
