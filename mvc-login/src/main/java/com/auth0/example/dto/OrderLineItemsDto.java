package com.auth0.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


public class OrderLineItemsDto {
    private Long id;
    private BigDecimal dailyFee;
    private Integer quantity;
    private String carCode;

    public OrderLineItemsDto() {
    }

    public OrderLineItemsDto(Long id, BigDecimal dailyFee, Integer quantity, String carCode) {
        this.id = id;
        this.dailyFee = dailyFee;
        this.quantity = quantity;
        this.carCode = carCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDailyFee() {
        return dailyFee;
    }

    public void setDailyFee(BigDecimal dailyFee) {
        this.dailyFee = dailyFee;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCarCode() {
        return carCode;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }
}
