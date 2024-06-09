package com.diploma.paymentservice.controller;

import com.diploma.paymentservice.config.PaymentConf;
import com.diploma.paymentservice.dto.PaymentRequestDto;
import com.diploma.paymentservice.dto.PaymentResponseDto;
import com.diploma.paymentservice.model.Payment;
import com.diploma.paymentservice.service.PaymentService;
import com.liqpay.LiqPay;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    private final Map<String, String> map = new HashMap<>();
    @Autowired
    private PaymentService paymentService;
    @PostMapping()
    @ResponseBody
    public String createPayment(@RequestBody PaymentRequestDto paymentRequestDto, Model model) {
        String html = paymentService.generateLiqPayForm(paymentRequestDto);
        map.put(paymentRequestDto.getOrderNumber(),html);
        Payment payment = paymentService.toPayment(paymentRequestDto);
        paymentService.savePayment(payment);
        return "http://localhost:8080/api/payment/form?orderNumber=%s".formatted(paymentRequestDto.getOrderNumber());
    }

    @GetMapping("/form")
    public String getPaymentForm(@RequestParam String orderNumber, Model model) {
        model.addAttribute("form", map.get(orderNumber));
        return "index"; // Назва шаблону Thymeleaf, який ми створимо нижче
    }
}
