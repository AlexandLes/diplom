package com.auth0.example;

import com.auth0.example.dto.CarResponseDto;
import com.auth0.example.dto.CompleteOrderRequestDto;
import com.auth0.example.dto.OrderLineItemsDto;
import com.auth0.example.dto.OrderRequestDto;
import com.auth0.example.dto.OrderResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@Controller
@RequestMapping("/order")
public class OrderController {
    final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/form/{carId}")
    public String showCreateOrderForm(
            @PathVariable("carId") String carId,
            Model model,
            @AuthenticationPrincipal OidcUser principal
    ) {
        principal.getUserInfo().getEmail();

        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setCarId(carId);

        model.addAttribute("profile", principal.getClaims());
        model.addAttribute("order", orderRequestDto);
        return "orderForm";
    }

    @PostMapping("/post")
    public String createOrder(OrderRequestDto orderRequestDto, Model model, @AuthenticationPrincipal OidcUser principal) {
        final var isPidar = true;
        orderRequestDto.setUserEmail(principal.getEmail());
        orderRequestDto.setActive(true);

        final var carResponse = Arrays.stream(restTemplate.getForEntity("http://localhost:8080/api/car", CarResponseDto[].class)
                        .getBody())
                .filter(carResponseDto -> carResponseDto.getId().equals(orderRequestDto.getCarId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find car with id: %s".formatted(orderRequestDto.getCarId())));

        CompleteOrderRequestDto completeOrderRequestDto = new CompleteOrderRequestDto(
                principal.getEmail(),
                null,
                Collections.singletonList(
                        new OrderLineItemsDto(
                                null,
                                carResponse.getDailyFee(),
                                1,
                                carResponse.getCarCode()
                        )
                ),
                true,
                orderRequestDto.getDateOfRental(),
                orderRequestDto.getDateOfReturn(),
                null
        );

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(
                "http://localhost:8080/api/order",
                completeOrderRequestDto,
                String.class
        );

        return "redirect:/cars";
    }

    @GetMapping("/my-orders")
    public String getMyOrders(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("profile", principal.getClaims());

        ResponseEntity<OrderResponseDto> forEntity = restTemplate.getForEntity(
                "http://localhost:8080/api/order?userEmail=%s".formatted(principal.getEmail()),
                OrderResponseDto.class
        );

        model.addAttribute("orderResponse", forEntity.getBody());

        return "myorders";
    }
}
