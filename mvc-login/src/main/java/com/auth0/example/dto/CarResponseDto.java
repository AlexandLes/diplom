package com.auth0.example.dto;


import java.math.BigDecimal;

public class CarResponseDto {
    private String id;
    private String brand;
    private String model;
    private String carCode;
    private BigDecimal dailyFee;

    public CarResponseDto() {
    }

    public CarResponseDto(String id, String brand, String model, String carCode, BigDecimal dailyFee) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.carCode = carCode;
        this.dailyFee = dailyFee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCarCode() {
        return carCode;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }

    public BigDecimal getDailyFee() {
        return dailyFee;
    }

    public void setDailyFee(BigDecimal dailyFee) {
        this.dailyFee = dailyFee;
    }
}