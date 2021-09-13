package com.example.demo.controller;

import com.example.demo.mapper.BookMapper;
import com.example.demo.model.dto.BookDTO;
import com.example.demo.model.entity.Book;
import com.example.demo.service.BookService;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    Book book;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        book = new Book();
        book.setId(1L);
        book.setName("test");
        book.setAuthor("test");
        book.setPrice(10.0);
        book.setIsbn("12324");
        book.setStock(5);
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
    void getBookDetailTest() {
        when(bookService.getBookDetail(any())).thenReturn(BookMapper.toBookDTO(book));
        mockMvc.perform(
                get("/books/{bookId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @SneakyThrows
    void createBookTest() {
        when(bookService.getBookDetail(any())).thenReturn(BookMapper.toBookDTO(book));
        mockMvc.perform(post("/books")
                .content(asJsonString(BookMapper.toBookDTO(book))).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void updateBookStockTest() {
        BookDTO bookDTO = BookMapper.toBookDTO(book);
        when(bookService.getBookById(any())).thenReturn(book);
        bookDTO.setStock(3);
        when(bookService.updateBookStock(any(Book.class),anyInt())).thenReturn(bookDTO);
        mockMvc.perform(put("/books/{id}/stock/{stock}" , 1,2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
