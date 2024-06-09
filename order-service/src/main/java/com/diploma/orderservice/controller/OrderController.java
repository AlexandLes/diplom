package com.diploma.orderservice.controller;

import com.diploma.orderservice.dto.OrderRequestDto;
import com.diploma.orderservice.dto.OrderResponseDto;
import com.diploma.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequestDto orderRequestDto) throws ExecutionException, InterruptedException {
        orderService.placeOrder(orderRequestDto);
        return "Order Placed successfully";
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderResponseDto> updateOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderService.mapToOrderResponseDto(orderService.updateOrderChatId(orderRequestDto.getUserEmail(), orderRequestDto.getChatId())));

    }

    @PutMapping("/close")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderService.mapToOrderResponseDto(orderService.updateOrderIsActive(orderRequestDto.getChatId())));

    }

    @GetMapping()
    public OrderResponseDto getOrderByUserEmail(@RequestParam String userEmail) {
        return orderService.getOrderByUserEmail(userEmail);
    }

    @GetMapping("/remind")
    public List<OrderResponseDto> getOrderByUserEmail(@RequestParam LocalDate tomorrow) {
        return orderService.getOrdersByDateOfReturn(tomorrow);
    }
    @GetMapping("/close")
    public OrderResponseDto getOrderByUserChatId(@RequestParam Long chatId) {
        return orderService.getOrderByChatId(chatId);
    }
}
