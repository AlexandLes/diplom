package com.diploma.paymentservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:application.properties")
public class PaymentConf {
    @Value("${liqpay.public}")
    private String publicKey;
    @Value("${liqpay.private}")
    private String privateKey;
}
