package com.example.demo.service.impl;

import com.example.demo.exceptions.GeneralException;
import com.example.demo.exceptions.OrderNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.PageResponse;
import com.example.demo.model.dto.OrderDTO;
import com.example.demo.model.dto.OrderItemDTO;
import com.example.demo.model.dto.OrderRequestDTO;
import com.example.demo.model.entity.Book;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.OrderItem;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.BookService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final BookService bookService;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService,
                            BookService bookService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    public PageResponse<OrderDTO> getAllOrdersByUser(int page, int size, long userId) {
        Page<Order> allOrderPageable = orderRepository.findAllByUsers(PageRequest.of(page, size), userService.getUserByUserId(userId));
        return OrderMapper.toOrderDTOResponse(allOrderPageable);
    }

    @Override
    public PageResponse<OrderDTO> getOrdersByPurchasedDate(Pageable pageable, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Page<Order> orders;
        if(startDate == null || endDate == null){
            throw new GeneralException("You should enter start and end date", HttpStatus.BAD_REQUEST);
        }
        try{
            orders = orderRepository.findByPurchasedDateBetween(pageable,
                    LocalDateTime.parse(startDate, formatter), LocalDateTime.parse(endDate, formatter));
        }catch (DateTimeParseException e){
            throw new GeneralException("Date format is not true, Please fill according to yyyy-MM-dd HH:mm:ss ", HttpStatus.BAD_REQUEST);
        }
        return OrderMapper.toOrderDTOResponse(orders);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderRequestDTO orderDTO) {
        if (orderDTO.getOrderItems().stream().
                anyMatch(orderItemDTO -> orderItemDTO.getQuantity()>10 || orderItemDTO.getQuantity()<=0)){
            throw new GeneralException("Please select a quantity just between 0 - 10", HttpStatus.BAD_REQUEST);
        }
        Order order = new Order();
        setOrderAddress(orderDTO, order);
        updateBookStock(orderDTO.getOrderItems());
        order.setUsers(userService.getUserByUserId(orderDTO.getUserId()));
        List<OrderItem> orderItems = orderDTO.getOrderItems()
                .stream()
                .map(item -> OrderMapper.toOrderItem(item, order, bookService.getBookById(item.getBookId())))
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setPurchasedDate(LocalDateTime.now());
        Double totalPrice = order.getOrderItems()
                .stream().map(item -> item.getQuantity() * item.getBook().getPrice()).reduce(0.0, Double::sum);
        order.setTotalPrice(totalPrice);
        return OrderMapper.toOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDTO getOrderById(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with given id is not found", HttpStatus.NOT_FOUND));
        return OrderMapper.toOrderDto(order);
    }

    private void setOrderAddress(OrderRequestDTO orderDTO, Order order) {
        if (orderDTO.isDifferentReceiver()
                && orderDTO.getReceiverName() == null
                && orderDTO.getAddress() == null) {
            throw new GeneralException("Please enter an address", HttpStatus.BAD_REQUEST);
        }
        if (orderDTO.isDifferentReceiver()) {
            order.setReceiverName(orderDTO.getReceiverName());
            order.setAddress(orderDTO.getAddress());
            return;
        }
        String address = userService.getUserByUserId(orderDTO.getUserId()).getAddress();
        order.setAddress(address);
    }

    private void updateBookStock(List<OrderItemDTO> orderItems) {
        orderItems.forEach(item -> {
            Book book = bookService.getBookById(item.getBookId());
            if (book.getStock() - item.getQuantity() < 0) {
                throw new GeneralException("You exceed the stock, please be sure the stock is enough", HttpStatus.NOT_ACCEPTABLE);
            }
            bookService.updateBookStock(book, book.getStock() - item.getQuantity());
        });
    }
}
