package ru.ifmo.practice.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.ifmo.practice.gateway.api.InvoiceApiDelegate;

@Controller
@RequiredArgsConstructor
public class InvoiceController implements InvoiceApiDelegate {
}
