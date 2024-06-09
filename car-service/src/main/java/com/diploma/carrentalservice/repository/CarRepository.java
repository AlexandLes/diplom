package com.diploma.carrentalservice.repository;

import com.diploma.carrentalservice.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarRepository extends MongoRepository<Car, String> {

}
