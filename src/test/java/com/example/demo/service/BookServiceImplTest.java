package com.example.demo.service;

import com.example.demo.exceptions.BookNotFoundException;
import com.example.demo.exceptions.GeneralException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.dto.BookDTO;
import com.example.demo.model.entity.Book;
import com.example.demo.model.entity.OrderItem;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.impl.BookServiceImpl;
import com.example.demo.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BookServiceImpl.class)
public class BookServiceImplTest {
    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;
    Book book;
    @BeforeEach
    void setUp(){
        book = new Book();
        book.setId(1L);
        book.setName("test");
        book.setAuthor("test");
        book.setPrice(12.0);
        book.setIsbn("2332");
        book.setStock(5);
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(1);
        book.setOrderItems(Collections.singletonList(orderItem));
    }
    @Test
    void getBookByIdTest(){
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        Book bookById = bookService.getBookById(1L);
        assertNotNull(bookById);
        assertEquals(book.getAuthor(),bookById.getAuthor());
    }

    @Test
    void getBookByIdTest_WhenNotFound(){
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(BookNotFoundException.class,
                () -> bookService.getBookById(1L));
        assertTrue(exception.getMessage().contains("Book with given id is not found"));
    }

    @Test
    void getBookDetailTest(){
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        BookDTO bookById = bookService.getBookDetail(1L);
        assertNotNull(bookById);
        assertEquals(book.getAuthor(),bookById.getAuthor());
    }

    @Test
    void createBookTest(){
        when(bookRepository.save(any())).thenReturn(book);
        BookDTO book = bookService.createBook(BookMapper.toBookDTO(this.book));
        assertNotNull(book);
    }

    @Test
    void updateBookStockTest(){
        when(bookRepository.save(any())).thenReturn(book);
        BookDTO bookDTO = bookService.updateBookStock(book,3);
        assertNotNull(bookDTO);
        assertEquals(bookDTO.getStock(),3);

    }

}
