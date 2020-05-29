package ru.ifmo.practice.gateway.logic.bank;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.practice.gateway.api.models.TransactionReadinessView;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.dto.entity.Transaction;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

import javax.crypto.spec.PSource;

@Component
@RequiredArgsConstructor
public class RequestPaymentOperation {

    @Getter
    @Setter
    private class PaymentData {
        public PaymentData(Transaction transaction, String source) {
            this.transaction = transaction;
            this.source = source;
        }

        public Transaction transaction;
        public String source;
    }

    private final String URI = "http://localhost:443/accept?Content-Type=application/json";
    private final String SOURCE = "http://localhost:80/v1/transaction/";

    private final TransactionDaoAdapter transactionDaoAdapter;

    public void process(Transaction transaction) {
        updateTransactionStatus(transaction, TransactionReadinessView.TypeEnum.PROCESSING);
        try {
            var restTemplate = new RestTemplate();
            var paymentData = new PaymentData(transaction, SOURCE + transaction.getId());
            var result = restTemplate.postForEntity(URI, paymentData, TransactionReadinessView.class);
            if (result.getStatusCode() != HttpStatus.OK) {
                updateTransactionStatus(transaction, TransactionReadinessView.TypeEnum.FAIL);
                throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "платежная система отказала в транзакции");
            } else {
                updateTransactionStatus(transaction, TransactionReadinessView.TypeEnum.WAITING_FOR_BANK_RESPONSE);
            }
        } catch (RestClientException e) {
            updateTransactionStatus(transaction, TransactionReadinessView.TypeEnum.FAIL);
            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "ошибка передачи транзакции");
        }
    }

    private void updateTransactionStatus(Transaction transaction, TransactionReadinessView.TypeEnum typeEnum) {
        transactionDaoAdapter.updateTransactionStatus(transaction.getId(), new TransactionStatusView()
                .answerCode(typeEnum.toString()));
    }

}
