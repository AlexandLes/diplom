package com.diploma.notificationservice.dto;

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

    public OrderRequestDto(OrderResponseDto orderResponseDto) {
        this.userEmail = orderResponseDto.getUserEmail();
        this.chatId = orderResponseDto.getChatId();
        this.orderLineItemsDtoList = orderResponseDto.getOrderLineItemsDtoList();
        this.isActive = orderResponseDto.isActive();
        this.actualDateOfReturn = orderResponseDto.getActualDateOfReturn();
        this.dateOfRental = orderResponseDto.getDateOfRental();
        this.dateOfReturn = orderResponseDto.getDateOfReturn();
    }
}

