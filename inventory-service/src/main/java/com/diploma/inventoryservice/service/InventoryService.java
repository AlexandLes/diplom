package com.diploma.inventoryservice.service;

import com.diploma.inventoryservice.dto.InventoryResponse;
import com.diploma.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> carCode) {
       return inventoryRepository.findByCarCodeIn(carCode).stream()
               .map(inventory -> InventoryResponse.builder()
                       .carCode(inventory.getCarCode())
                       .isInStock(inventory.getQuantity() > 0)
                       .build())
               .toList();
    }
}
