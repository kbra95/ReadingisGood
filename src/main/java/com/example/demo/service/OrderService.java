package com.example.demo.service;

import com.example.demo.model.PageResponse;
import com.example.demo.model.dto.OrderDTO;
import com.example.demo.model.dto.OrderRequestDTO;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    PageResponse<OrderDTO> getAllOrdersByUser(int page, int size, long userId);
    PageResponse<OrderDTO> getOrdersByPurchasedDate(Pageable pageable, String startDate, String endDate);
    OrderDTO createOrder(OrderRequestDTO orderDTO);
    OrderDTO getOrderById(long id);
}
