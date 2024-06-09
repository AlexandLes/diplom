package com.auth0.example.dto;

import java.time.LocalDate;
import java.util.List;

public class CompleteOrderRequestDto {
    private String userEmail;
    private Long chatId;
    private List<OrderLineItemsDto> orderLineItemsDtoList;
    private boolean isActive;
    private LocalDate dateOfRental;
    private LocalDate dateOfReturn;
    private LocalDate actualDateOfReturn;

    public CompleteOrderRequestDto() {
    }

    public CompleteOrderRequestDto(String userEmail, Long chatId, List<OrderLineItemsDto> orderLineItemsDtoList, boolean isActive, LocalDate dateOfRental, LocalDate dateOfReturn, LocalDate actualDateOfReturn) {
        this.userEmail = userEmail;
        this.chatId = chatId;
        this.orderLineItemsDtoList = orderLineItemsDtoList;
        this.isActive = isActive;
        this.dateOfRental = dateOfRental;
        this.dateOfReturn = dateOfReturn;
        this.actualDateOfReturn = actualDateOfReturn;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public List<OrderLineItemsDto> getOrderLineItemsDtoList() {
        return orderLineItemsDtoList;
    }

    public void setOrderLineItemsDtoList(List<OrderLineItemsDto> orderLineItemsDtoList) {
        this.orderLineItemsDtoList = orderLineItemsDtoList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDate getDateOfRental() {
        return dateOfRental;
    }

    public void setDateOfRental(LocalDate dateOfRental) {
        this.dateOfRental = dateOfRental;
    }

    public LocalDate getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(LocalDate dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    public LocalDate getActualDateOfReturn() {
        return actualDateOfReturn;
    }

    public void setActualDateOfReturn(LocalDate actualDateOfReturn) {
        this.actualDateOfReturn = actualDateOfReturn;
    }
}
