package ru.ifmo.practice.gateway.logic.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.practice.gateway.api.models.TransactionReadinessView;
import ru.ifmo.practice.gateway.api.models.TransactionStatusView;
import ru.ifmo.practice.gateway.dto.PaymentData;
import ru.ifmo.practice.gateway.dto.entity.Transaction;
import ru.ifmo.practice.gateway.helper.ExceptionFactory;
import ru.ifmo.practice.gateway.service.dao.TransactionDaoAdapter;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestPaymentOperation {

    @Value("${coordinator.request-uri}")
    private String uri;

    @Value("${coordinator.source-pattern}")
    private String source;

    private final TransactionDaoAdapter transactionDaoAdapter;

    public void process(Transaction transaction) {
        updateTransactionStatus(transaction, TransactionReadinessView.TypeEnum.PROCESSING);
        try {
            var restTemplate = new RestTemplate();
            var paymentData = new PaymentData(transaction, source + transaction.getId());
            var result = restTemplate.postForEntity(uri, paymentData, TransactionReadinessView.class);
            if (result.getStatusCode() != HttpStatus.OK) {
                updateTransactionStatus(transaction, TransactionReadinessView.TypeEnum.FAIL);
                throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "платежная система отказала в транзакции");
            } else {
                updateTransactionStatus(transaction, TransactionReadinessView.TypeEnum.WAITING_FOR_BANK_RESPONSE);
            }
        } catch (RestClientException e) {
            log.error(String.format("Can't send data to bank, transaction %d will fail", transaction.getId()), e);
            updateTransactionStatus(transaction, TransactionReadinessView.TypeEnum.FAIL);
//            throw ExceptionFactory.newException(HttpStatus.BAD_REQUEST, "ошибка передачи транзакции");
        }
    }

    private void updateTransactionStatus(Transaction transaction, TransactionReadinessView.TypeEnum typeEnum) {
        transactionDaoAdapter.updateTransactionStatus(transaction.getId(), new TransactionStatusView()
                .answerCode(typeEnum.toString()));
    }

}
