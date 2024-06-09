package com.diploma.paymentservice.repository;

import com.diploma.paymentservice.model.Payment;
import com.diploma.paymentservice.service.PaymentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
