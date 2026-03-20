package com.example.order_service.controller;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.RevenueResponse;
import com.example.order_service.dto.TrackOrder;
import com.example.order_service.dto.UpdateStatusRequest;
import com.example.order_service.enums.Status;
import com.example.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderRequest> createOrder(@RequestBody OrderRequest request, Principal principal) {
        return ResponseEntity.ok(orderService.createOrder(request, principal.getName()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRequest> getOrderById(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrderByOrderId(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderRequest>> getOrderByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getOrderByUserId(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<List<OrderRequest>> getOrderByStatus(@RequestParam Status status) {
        return ResponseEntity.ok(orderService.getOrderByStatus(status));
    }

    @DeleteMapping("/users/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted Successfully for orderId :" + orderId);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public Page<OrderRequest> getAllOrders(@RequestParam int page, @RequestParam int size) {
        return orderService.getAllOrders(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/revenue-by-status")
    public ResponseEntity<List<RevenueResponse>> getRevenueByStatus() {
        return ResponseEntity.ok(orderService.getRevenueByStatus());
    }

    @GetMapping("/track/{orderId}")
    public ResponseEntity<TrackOrder> trackOrderById(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.trackOrderByOrderId(orderId));
    }

    @PreAuthorize(("hasRole('ADMIN')"))
    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<TrackOrder> updateStatusByOrderId(@PathVariable String orderId, @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(orderService.updateStatusByOrderId(orderId,request));
    }

}