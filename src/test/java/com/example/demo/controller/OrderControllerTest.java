package com.example.demo.controller;


import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.PageResponse;
import com.example.demo.model.dto.OrderDTO;
import com.example.demo.model.dto.OrderItemDTO;
import com.example.demo.model.dto.OrderRequestDTO;
import com.example.demo.model.entity.Book;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.OrderItem;
import com.example.demo.model.entity.User;
import com.example.demo.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    Order order;
    User user;
    Book book;
    PageResponse<OrderDTO> orderItemDTOPageResponse;

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

    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("error when wrap json");
        }
    }

    @Test
    @SneakyThrows
    void getAllOrdersTest() {
        when(orderService.getAllOrdersByUser(eq(0), eq(10), eq(1))).thenReturn(orderItemDTOPageResponse);
        mockMvc.perform(
                get("/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page","0")
                        .param("size","10")
                        .param("userId" ,"1"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getOrderByIdTest() {
        when(orderService.getOrderById(anyInt())).thenReturn(OrderMapper.toOrderDto(order));
        mockMvc.perform(
                get("/orders/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void createOrderTest() {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setAddress("test");
        orderRequestDTO.setDifferentReceiver(false);
        orderRequestDTO.setUserId(1L);
        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setId(1L);
        orderItem.setQuantity(1);
        orderItem.setBookId(book.getId());
        orderRequestDTO.setOrderItems(Collections.singletonList(orderItem));

        mockMvc.perform(post("/orders")
                .content(asJsonString(orderRequestDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}