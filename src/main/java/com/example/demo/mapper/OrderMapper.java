package com.example.demo.mapper;

import com.example.demo.model.PageResponse;
import com.example.demo.model.dto.OrderDTO;
import com.example.demo.model.dto.OrderItemDTO;
import com.example.demo.model.entity.Book;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.OrderItem;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toOrderDto(Order order){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderItems(order.getOrderItems()
                .stream().map(OrderMapper::toOrderItemDTO).collect(Collectors.toList()));
        orderDTO.setUser(UserMapper.toUserDTO(order.getUsers()));
        orderDTO.setAddress(order.getAddress());
        orderDTO.setDifferentReceiver(order.isDifferentReceiver());
        orderDTO.setPurchasedDate(order.getPurchasedDate());
        orderDTO.setReceiverName(order.getReceiverName());
        orderDTO.setTotalPrice(order.getTotalPrice());
        return orderDTO;
    }

    public static OrderItem toOrderItem(OrderItemDTO orderItemDTO, Order order, Book book){
        OrderItem orderItem =  new OrderItem();
        orderItem.setId(orderItemDTO.getId());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setOrder(order);
        orderItem.setBook(book);
        return orderItem;
    }

    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem){
        OrderItemDTO orderItemDTO =  new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setQuantity(orderItem.getQuantity());;
        orderItemDTO.setBookId(orderItem.getBook().getId());
        return orderItemDTO;
    }
    public static PageResponse<OrderDTO> toOrderDTOResponse(Page<Order> allOrderPageable) {
        List<OrderDTO> orderDTOList = allOrderPageable.getContent()
                .stream().map(OrderMapper::toOrderDto).collect(Collectors.toList());

        PageResponse<OrderDTO> response = new  PageResponse<>();
        response.setContent(orderDTOList);
        response.setNumber(allOrderPageable.getNumber());
        response.setSize(allOrderPageable.getSize());
        response.setTotalElements(allOrderPageable.getTotalElements());
        response.setTotalPages(allOrderPageable.getTotalPages());
        return response;
    }
}
