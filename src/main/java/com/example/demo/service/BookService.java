package com.example.demo.service;

import com.example.demo.model.dto.BookDTO;
import com.example.demo.model.entity.Book;

public interface BookService {

    BookDTO getBookDetail(Long bookId);

    BookDTO createBook(BookDTO bookDTO);

    BookDTO updateBookStock(Book book, int stock);

    Book getBookById(Long bookId);
}
