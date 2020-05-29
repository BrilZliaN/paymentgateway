package ru.ifmo.practice.coordiantor.coordinator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.practice.coordiantor.coordinator.entity.PaymentData;
import ru.ifmo.practice.coordiantor.coordinator.entity.Transaction;
import ru.ifmo.practice.coordiantor.coordinator.entity.TransactionStatusView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CoordinationController {

    private final List<PaymentData> saved = new ArrayList<>();

    @PostMapping("/accept")
    public ResponseEntity<Void> processTransaction(@RequestBody PaymentData paymentData) throws InterruptedException {
        saved.add(paymentData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/accept")
    public ResponseEntity<Void> answerRequests() {
        for (var entry : saved) {
            RestTemplate restTemplate = new RestTemplate();
            var status = new TransactionStatusView().answerCode("SUCCESS");
            restTemplate.put(entry.source, status);
            System.out.println(entry.source);
        }
        return ResponseEntity.accepted().build();
    }

}
