package com.example.order_service.service;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.RevenueResponse;
import com.example.order_service.dto.TrackOrder;
import com.example.order_service.dto.UpdateStatusRequest;
import com.example.order_service.enums.Status;
import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.exception.UserNotFoundException;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.model.Order;
import com.example.order_service.model.User;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.order_service.mapper.OrderMapper.convertToDTO;


@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;


    private final MongoTemplate mongoTemplate;


    public OrderRequest createOrder(OrderRequest request, String userName) {

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Order order = Order.builder()
                .orderId("ORD" + System.currentTimeMillis())
                .userId(user.getId())
                .status(request.getStatus())
                .currency(request.getCurrency())
                .totalAmount(request.getTotalAmount())
                .createdAt(LocalDateTime.now())
                .UpdatedAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    public OrderRequest getOrderByOrderId(String orderId) {
        logger.info("Fetching Order by OrderId :" + orderId);
        Order order = orderRepository.findByOrderIdAndDeletedFalse(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found :" + orderId));
        return convertToDTO(order);
    }

    public List<OrderRequest> getOrderByUserId(String userId) {
        logger.info("Fetching Orders from UserId :" + userId);
        List<Order> orders = orderRepository.findByUserIdAndDeletedFalse(userId);
        if (orders.isEmpty()) {
            logger.warn("No Orders found for the userId { } :" + userId);
            throw new UserNotFoundException("No User found for userId :" + userId);
        } else {
            logger.info("Number of Orders found for given UserId : " + orders.size());
        }
        return orders.stream().map(OrderMapper::convertToDTO).toList();
    }

    public List<OrderRequest> getOrderByStatus(Status status) {
        logger.info("Fetching Orders by Status :" + status);
        List<Order> orders = orderRepository.findByStatusAndDeletedFalse(status);
        if (orders.isEmpty()) {
            logger.warn("Status for given orders not found");
        }
        return orders.stream().map(OrderMapper::convertToDTO).toList();
    }

    public void deleteOrder(String orderId) {
        logger.info("Deleting Order by the OrderId :" + orderId);
        Order order = orderRepository.findByOrderIdAndDeletedFalse(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found :" + orderId));
        order.setDeleted(true);
        order.setDeletedAt(LocalDateTime.now());
        orderRepository.save(order);

    }

    public Page<OrderRequest> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> order = orderRepository.findByDeletedFalse(pageable);
        return order.map(OrderMapper::convertToDTO);
    }

    public List<RevenueResponse> getRevenueByStatus() {
        logger.info("Get Revenue By Status...");
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("status").sum("totalAmount").as("totalRevenue"));
        AggregationResults<RevenueResponse> results = mongoTemplate.aggregate(aggregation, "orders", RevenueResponse.class);
        return results.getMappedResults();
    }

    public TrackOrder trackOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderIdAndDeletedFalse(orderId).orElseThrow(() -> new OrderNotFoundException("Order Not Found For OrderId :" + orderId));
        return TrackOrder.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public TrackOrder updateStatusByOrderId(String orderId, UpdateStatusRequest request) {
        Order order = orderRepository.findByOrderIdAndDeletedFalse(orderId).orElseThrow(() -> new OrderNotFoundException("Order Not Found for orderId :" + orderId));
        order.setStatus(request.getStatus());
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);
        return TrackOrder.builder()
                .orderId(saved.getOrderId())
                .status(saved.getStatus())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
}