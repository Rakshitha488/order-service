package com.example.order_service.mapper;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.model.Order;

import java.time.LocalDateTime;

public class OrderMapper {
    public static OrderRequest convertToDTO(Order order) {
        OrderRequest orderdto = new OrderRequest();
        orderdto.setOrderId(order.getOrderId());
        orderdto.setUserId(order.getUserId());
        orderdto.setStatus(order.getStatus());
        orderdto.setTotalAmount(order.getTotalAmount());
        orderdto.setCreatedAt(LocalDateTime.now());
        return orderdto;

    }
}
