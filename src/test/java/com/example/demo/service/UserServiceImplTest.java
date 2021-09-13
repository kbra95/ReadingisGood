package com.example.demo.service;

import com.example.demo.exceptions.GeneralException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.model.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.OrderServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    User user = new User();

    @BeforeEach
    void setUp(){
        user.setId(1L);
        user.setUsername("test");
        user.setEmail("kk@gmail.com");
        user.setFirstName("test");
        user.setLastName("test");
        user.setAddress("test");
        user.setPassword("test");
    }
    @Test
    void getUserByUserIdTest(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        User userByUserId = userService.getUserByUserId(1);
        assertNotNull(userByUserId);
        assertEquals(user.getId(),userByUserId.getId());
    }

    @Test
    void getUserByUserIdTest_WhenUserNotFound(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByUserId(1));
        assertTrue(exception.getMessage().contains("User is given with id " + 1 + " is not found"));
    }
}
