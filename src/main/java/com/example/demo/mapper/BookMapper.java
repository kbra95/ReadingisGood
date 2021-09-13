package com.example.demo.mapper;

import com.example.demo.model.dto.BookDTO;
import com.example.demo.model.entity.Book;

public class BookMapper {

    public static BookDTO toBookDTO(Book book){
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setName(book.getName());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setPrice(book.getPrice());
        bookDTO.setStock(book.getStock());
        return bookDTO;
    }
}
