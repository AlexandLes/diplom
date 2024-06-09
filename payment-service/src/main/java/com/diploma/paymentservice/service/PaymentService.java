package com.diploma.paymentservice.service;

import com.diploma.paymentservice.config.PaymentConf;
import com.diploma.paymentservice.dto.PaymentRequestDto;
import com.diploma.paymentservice.dto.PaymentResponseDto;
import com.diploma.paymentservice.model.Payment;
import com.diploma.paymentservice.repository.PaymentRepository;
import com.liqpay.LiqPay;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private static final String url = "http://localhost:8080/api/notification";
    private final PaymentConf paymentConf; // Assuming this is a configuration class with the public and private keys

    public String generateLiqPayForm(PaymentRequestDto paymentRequestDto) {
        LiqPay liqPay = new LiqPay(paymentConf.getPublicKey(), paymentConf.getPrivateKey());
        HashMap<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("amount", paymentRequestDto.getPaymentAmount().toString());
        params.put("currency", "UAH");
        params.put("description", "Rental");
        params.put("order_id", paymentRequestDto.getOrderNumber());
        params.put("version", "3");
        params.put("result_url", url);
        return liqPay.cnb_form(params);
    }
    public  Payment toPayment(PaymentRequestDto paymentResponseDto) {
        if (paymentResponseDto == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setOrderNumber(paymentResponseDto.getOrderNumber());
        payment.setPaymentAmount(paymentResponseDto.getPaymentAmount());
        payment.setPaymentDate(paymentResponseDto.getPaymentDate());
        return payment;
    }
    public Payment savePayment(Payment payment) {
       return paymentRepository.save(payment);
    }
    public  PaymentResponseDto toPaymentResponseDto(Payment payment) {
        if (payment == null) {
            return null;
        }
        return PaymentResponseDto.builder()
                .id(payment.getId())
                .paymentAmount(payment.getPaymentAmount())
                .orderNumber(payment.getOrderNumber())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

    // Метод для маппінгу з PaymentRequestDto до Payment
    public  PaymentResponseDto toPaymentResponseDto(PaymentRequestDto paymentRequestDto, Long id) {
        if (paymentRequestDto == null) {
            return null;
        }
        return PaymentResponseDto.builder()
                .id(id)
                .paymentAmount(paymentRequestDto.getPaymentAmount())
                .orderNumber(paymentRequestDto.getOrderNumber())
                .paymentDate(paymentRequestDto.getPaymentDate())
                .build();
    }
}
