package com.diploma.orderservice.service;

import com.diploma.orderservice.dto.InventoryResponse;
import com.diploma.orderservice.dto.OrderLineItemsDto;
import com.diploma.orderservice.dto.OrderRequestDto;
import com.diploma.orderservice.dto.OrderResponseDto;
import com.diploma.orderservice.model.Order;
import com.diploma.orderservice.model.OrderLineItems;
import com.diploma.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequestDto orderRequestDto) throws ExecutionException, InterruptedException {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        if (orderRequestDto.getChatId() != null) {
            order.setChatId(orderRequestDto.getChatId());
        }
        order.setDateOfRental(orderRequestDto.getDateOfRental());
        order.setDateOfReturn(orderRequestDto.getDateOfReturn());
        order.setUserEmail(orderRequestDto.getUserEmail());
        order.setActive(true);
        List<OrderLineItems> orderLineItems = orderRequestDto.getOrderLineItemsDtoList().stream()
                .map(this::mapFromDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);
        List<String> carCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getCarCode)
                .toList();

        CompletableFuture<InventoryResponse[]> future = CompletableFuture.supplyAsync(() -> webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("carCode", carCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block());

        InventoryResponse[] inventoryResponsArray = future.get();
        boolean result = Arrays.stream(inventoryResponsArray).allMatch(InventoryResponse::isInStock);
        if (result) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Car is not in stock");
        }

    }

    private OrderLineItems mapFromDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setDailyFee(orderLineItemsDto.getDailyFee());
        orderLineItems.setCarCode(orderLineItemsDto.getCarCode());
        return orderLineItems;
    }
    private OrderLineItemsDto mapToDto(OrderLineItems orderLineItems) {
        OrderLineItemsDto orderLineItemsDto = new OrderLineItemsDto();
        orderLineItemsDto.setQuantity(orderLineItems.getQuantity());
        orderLineItemsDto.setDailyFee(orderLineItems.getDailyFee());
        orderLineItemsDto.setCarCode(orderLineItems.getCarCode());
        return orderLineItemsDto;
    }
    public OrderResponseDto mapToOrderResponseDto(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(order.getId());
        orderResponseDto.setChatId(order.getChatId());
        orderResponseDto.setOrderLineItemsDtoList(order.getOrderLineItemsList().stream().map(this::mapToDto).toList());
        orderResponseDto.setActive(order.isActive());
        orderResponseDto.setOrderNumber(order.getOrderNumber());
        orderResponseDto.setUserEmail(order.getUserEmail());
        orderResponseDto.setDateOfRental(order.getDateOfRental());
        orderResponseDto.setDateOfReturn(order.getDateOfReturn());
        orderResponseDto.setActualDateOfReturn(order.getActualDateOfReturn());
        return orderResponseDto;
    }

    public Order updateOrderChatId(String userEmail, Long chatId) {
        Order order = orderRepository.getOrderByUserEmailAndIsActive(userEmail);
        order.setChatId(chatId);
        return orderRepository.save(order);
    }

    public Order updateOrderIsActive(Long chatId) {
        Order order = orderRepository.getOrderByChatIdAndIsActive(chatId);
        order.setActive(false);
        return order;
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderByUserEmail(String userEmail) {
        OrderResponseDto orderResponseDto;
        orderResponseDto = mapToOrderResponseDto(orderRepository.getOrderByUserEmailAndIsActive(userEmail));
        return orderResponseDto;
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderByChatId(Long chatId) {
        OrderResponseDto orderResponseDto;
        orderResponseDto = mapToOrderResponseDto(orderRepository.getOrderByChatIdAndIsActive(chatId));
        return orderResponseDto;
    }

    public List<OrderResponseDto> getOrdersByDateOfReturn(LocalDate tomorrow) {
       return orderRepository.getOrdersByDateOfReturn(tomorrow).stream()
                .map(o -> mapToOrderResponseDto(o)).collect(Collectors.toList());
    }
}
