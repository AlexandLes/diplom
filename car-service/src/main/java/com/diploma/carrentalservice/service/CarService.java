package com.diploma.carrentalservice.service;

import com.diploma.carrentalservice.dto.request.CarRequestDto;
import com.diploma.carrentalservice.dto.response.CarResponseDto;
import com.diploma.carrentalservice.model.Car;
import com.diploma.carrentalservice.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarService {
private final CarRepository carRepository;

    public void createCar(CarRequestDto carRequestDto) {
        Car car = Car.builder()
                .brand(carRequestDto.getBrand())
                .model(carRequestDto.getModel())
                .dailyFee(carRequestDto.getDailyFee())
                .carCode(carRequestDto.getCarCode())
                .build();
        carRepository.save(car);
        log.info("Car " + car.getId() + " is saved");
    }

    public List<CarResponseDto> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(this::mapToCarResponseDto).collect(Collectors.toList());
    }

    private CarResponseDto mapToCarResponseDto(Car car) {
        return  CarResponseDto.builder()
                .id(car.getId())
                .model(car.getModel())
                .brand(car.getBrand())
                .dailyFee(car.getDailyFee())
                .carCode(car.getCarCode())
                .build();
    }
}
