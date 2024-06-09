package com.diploma.carrentalservice.controller;

import com.diploma.carrentalservice.dto.request.CarRequestDto;
import com.diploma.carrentalservice.dto.response.CarResponseDto;
import com.diploma.carrentalservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
public class CarController {
private final CarService carService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCar(@RequestBody CarRequestDto carRequestDto) {
        carService.createCar(carRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CarResponseDto> getAllCars() {
       return carService.getAllCars();
    }
}
