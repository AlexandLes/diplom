package com.diploma.carrentalservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarResponseDto {
    private String id;
    private String brand;
    private String model;
    private String carCode;
    private BigDecimal dailyFee;
}
