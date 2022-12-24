package com.taxi.controller;

import com.taxi.dto.OrderResponse;
import com.taxi.dto.OrderToCreate;
import com.taxi.enums.OrderStatusEnum;
import com.taxi.service.OrderService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping({"/newOrder"})
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<String> createOrder(@RequestParam String passengerId,
                                         @RequestBody OrderToCreate order) {
        try {
            orderService.save(passengerId, order);
            return ResponseEntity
                    .ok()
                    .body("Looking for the nearest car");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Some troubles with your order! Please try again!");
        }
    }

    @PutMapping("/updateOrder")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<String> updateOrder(@RequestParam Integer orderId,
                                              @RequestParam OrderStatusEnum status)
    {
        try {
            orderService.updateStatus(status, orderId);
            return ResponseEntity
                    .ok()
                    .body("Success change order status to " + status);
        } catch (NotFoundException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @GetMapping("/getWaiting")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<OrderResponse>> getOrder() {
        return ResponseEntity.ok().body(orderService.findAllWaitingOrders());
    }
}
