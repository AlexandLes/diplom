package com.diploma.orderservice.repository;

import com.diploma.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.userEmail = :userEmail AND o.isActive = true")
    Order getOrderByUserEmailAndIsActive(@Param("userEmail") String userEmail);

    @Query("SELECT o FROM Order o WHERE o.chatId = :chatId AND o.isActive = true order by o.id desc limit 1")
    Order getOrderByChatIdAndIsActive(@Param("chatId") Long chatId);

    @Query("SELECT o FROM Order o WHERE o.dateOfReturn = :tomorrowDate AND o.isActive = true")
    List<Order> getOrdersByDateOfReturn(@Param("tomorrowDate") LocalDate tomorrowDate);
    
}
