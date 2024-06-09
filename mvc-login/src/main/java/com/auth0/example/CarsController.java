package com.auth0.example;

import com.auth0.example.dto.CarResponseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class CarsController {

    final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/cars")
    public String getCars(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("profile", principal.getClaims());
        principal.getUserInfo().getEmail();

        List<CarResponseDto> cars = Arrays.stream(restTemplate.getForEntity("http://localhost:8080/api/car", CarResponseDto[].class)
                        .getBody())
                .toList();

        model.addAttribute("cars", cars);

        return "cars";
    }
}
