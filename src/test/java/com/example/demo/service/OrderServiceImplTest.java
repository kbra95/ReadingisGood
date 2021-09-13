package com.example.demo.service;

import com.example.demo.exceptions.GeneralException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.PageResponse;
import com.example.demo.model.dto.OrderDTO;
import com.example.demo.model.dto.OrderItemDTO;
import com.example.demo.model.dto.OrderRequestDTO;
import com.example.demo.model.entity.Book;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.OrderItem;
import com.example.demo.model.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.impl.OrderServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderServiceImpl.class)
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private BookService bookService;

    Order order;
    User user;
    Book book;
    PageResponse<OrderDTO> orderItemDTOPageResponse;
    OrderRequestDTO orderRequestDTO ;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        order = new Order();
        user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setFirstName("test");
        user.setLastName("test lastname");
        user.setEmail("test@gmail.com");
        order.setId(1L);
        order.setAddress("test");
        order.setDifferentReceiver(false);
        order.setPurchasedDate(LocalDateTime.now());
        order.setUsers(user);
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setQuantity(2);

        book = new Book();
        book.setId(1L);
        book.setName("test");
        book.setAuthor("test");
        book.setPrice(10.0);
        book.setIsbn("12324");
        book.setStock(5);
        orderItem.setBook(book);
        order.setOrderItems(Collections.singletonList(orderItem));

        orderItemDTOPageResponse = new PageResponse<>();
        orderItemDTOPageResponse.setTotalPages(1);
        orderItemDTOPageResponse.setContent(Collections.singletonList(OrderMapper.toOrderDto(order)));
        orderItemDTOPageResponse.setNumber(0);
        orderItemDTOPageResponse.setTotalElements(1);

        orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setAddress("test");
        orderRequestDTO.setDifferentReceiver(false);
        orderRequestDTO.setUserId(1L);
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(1L);
        orderItemDTO.setQuantity(1);
        orderItemDTO.setBookId(book.getId());
        orderRequestDTO.setOrderItems(Collections.singletonList(orderItemDTO));

    }

    @Test
    void getAllOrdersByUserTest() {
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(userService.getUserByUserId(anyLong())).thenReturn(user);
        when(orderRepository.findAllByUsers(any(Pageable.class), any(User.class))).thenReturn(orderPage);
        PageResponse<OrderDTO> allOrdersByUser = orderService.getAllOrdersByUser(0, 10, 1);
        assertNotNull(allOrdersByUser);
        assertEquals(1, allOrdersByUser.getContent().get(0).getId());
        assertEquals("test", allOrdersByUser.getContent().get(0).getUser().getFirstName());
    }

    @Test
    void getOrdersByPurchasedDateTest_WhenStartDateIsNull() {
        Pageable pageable = PageRequest.of(0, 10);
        Exception exception = assertThrows(GeneralException.class, () -> orderService.getOrdersByPurchasedDate(pageable, null, null));
        assertTrue(exception.getMessage().contains("You should enter start and end date"));
    }

    @Test
    void getOrdersByPurchasedDateTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Pageable pageable = PageRequest.of(0, 10);
        String startDate = LocalDateTime.now().minusDays(1).format(formatter);
        String endDate = LocalDateTime.now().format(formatter);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findByPurchasedDateBetween(any(),any(),any()))
                .thenReturn(orderPage);
        PageResponse<OrderDTO> allOrdersByUser = orderService.getOrdersByPurchasedDate(pageable, startDate, endDate);

        assertNotNull(allOrdersByUser);
        assertEquals(1, allOrdersByUser.getContent().get(0).getId());
        assertEquals("test", allOrdersByUser.getContent().get(0).getUser().getFirstName());

    }

    @Test
    void getOrdersByPurchasedDateTest_WhenDateFormatInvalid() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Pageable pageable = PageRequest.of(0, 10);
        String startDate = LocalDateTime.now().minusDays(1).format(formatter);
        String endDate = LocalDateTime.now().format(formatter);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findByPurchasedDateBetween(any(),any(),any()))
                .thenReturn(orderPage);
        Exception exception = assertThrows(GeneralException.class, () -> orderService.getOrdersByPurchasedDate(pageable, startDate, endDate));
        assertTrue(exception.getMessage().contains("Date format is not true, Please fill according to yyyy-MM-dd HH:mm:ss "));
    }

    @Test
    void createOrderTest(){
        when(userService.getUserByUserId(anyLong())).thenReturn(user);
        when( bookService.getBookById(anyLong())).thenReturn(book);
        when(orderRepository.save(any())).thenReturn(order);
        OrderDTO returned = orderService.createOrder(orderRequestDTO);
        assertEquals(order.getId(),returned.getId());
    }

    @Test
    void createOrderTest_WhenQuantityInvalid(){
        when(userService.getUserByUserId(anyLong())).thenReturn(user);
        when( bookService.getBookById(anyLong())).thenReturn(book);
        when(orderRepository.save(any())).thenReturn(order);
        orderRequestDTO.getOrderItems().get(0).setQuantity(-2);
        Exception exception = assertThrows(GeneralException.class, () -> orderService.createOrder(orderRequestDTO));
        assertTrue(exception.getMessage().contains("Please select a quantity just between 0 - 10"));
    }

    @Test
    void getOrderByIdTest(){
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        OrderDTO orderById = orderService.getOrderById(1);
        assertEquals(order.getId(),orderById.getId());
    }

    @Test
    void getOrderByIdTest_WhenIdIvalid(){
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(GeneralException.class,
                () -> orderService.getOrderById(1));
        assertTrue(exception.getMessage().contains("Order with given id is not found"));
    }
}
