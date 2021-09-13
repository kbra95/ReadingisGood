package com.example.demo.model.dto;

import com.example.demo.model.entity.Book;
import com.example.demo.model.entity.OrderItem;
import com.example.demo.model.entity.User;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequestDTO {
    private Long userId;
    private List<OrderItemDTO> orderItems;
    private String receiverName;
    private String address;
    private boolean differentReceiver;
}
