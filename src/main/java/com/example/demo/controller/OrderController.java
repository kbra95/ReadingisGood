package com.example.demo.controller;

import com.example.demo.model.PageResponse;
import com.example.demo.model.dto.OrderDTO;
import com.example.demo.model.dto.OrderRequestDTO;
import com.example.demo.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderDTO>> getAllOrdersByUser(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam long userId) {

        return new ResponseEntity<>(orderService.getAllOrdersByUser(page, size, userId), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable long id){
        return new ResponseEntity<>(orderService.getOrderById(id),HttpStatus.OK);
    }

    @GetMapping("/byPurchasedDate")
    public ResponseEntity<PageResponse<OrderDTO>> getAllOrders(@RequestParam String startDate,
                                                               @RequestParam String endDate,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size){
        return new ResponseEntity<>(orderService.getOrdersByPurchasedDate(PageRequest.of(page, size),startDate,endDate), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderDTO) {
        return new ResponseEntity<>(orderService.createOrder(orderDTO), HttpStatus.CREATED);
    }
}
