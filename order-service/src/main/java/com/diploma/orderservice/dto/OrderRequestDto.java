package com.diploma.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private String userEmail;
    private Long chatId;
    private List<OrderLineItemsDto> orderLineItemsDtoList;
    private boolean isActive;
    private LocalDate dateOfRental;
    private LocalDate dateOfReturn;
    private LocalDate actualDateOfReturn;
}
