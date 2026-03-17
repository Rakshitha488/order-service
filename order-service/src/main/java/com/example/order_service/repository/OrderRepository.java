package com.example.order_service.repository;

import com.example.order_service.enums.Status;
import com.example.order_service.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findByOrderIdAndDeletedFalse(String orderId);

    List<Order> findByUserIdAndDeletedFalse(String userId);

    List<Order> findByStatusAndDeletedFalse(Status status);

    Page<Order> findByDeletedFalse(Pageable pageable);


}