package com.example.order_service.service;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.RevenueResponse;
import com.example.order_service.enums.Status;
import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.exception.UserNotFoundException;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.model.Order;
import com.example.order_service.model.User;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;


    public OrderRequest createOrder(OrderRequest request, String userName) {

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Order order = new Order();
        order.setOrderId("ORD" + System.currentTimeMillis());
        order.setUserId(user.getId());
        order.setStatus(request.getStatus());
        order.setTotalAmount(request.getTotalAmount());
        order.setCreatedAt(LocalDateTime.now());

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
        List<Order> order = orderRepository.findByUserIdAndDeletedFalse(userId);
        if (order.isEmpty()) {
            logger.warn("No Orders found for the userId { } :" + userId);
            throw new UserNotFoundException("No User found for userId :" + userId);
        } else {
            logger.info("Number of Orders found for given UserId : " + order.size());
        }
        return order.stream().map(OrderMapper::convertToDTO).toList();
    }

    public List<OrderRequest> getOrderByStatus(Status status) {
        logger.info("Fetching Orders by Status :" + status);
        List<Order> order = orderRepository.findByStatusAndDeletedFalse(status);
        if (order.isEmpty()) {
            logger.warn("Status for given orders not found");
        }
        return order.stream().map(OrderMapper::convertToDTO).toList();
    }

    public void deleteOrder(String orderId) {
        logger.info("Deleting Order by the OrderId :" + orderId);
        Order order = orderRepository.findByOrderIdAndDeletedFalse(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found :" + orderId));
        order.setDeleted(true);
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


}