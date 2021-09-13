package com.example.demo.service.impl;

import com.example.demo.exceptions.BookNotFoundException;
import com.example.demo.exceptions.GeneralException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.dto.BookDTO;
import com.example.demo.model.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDTO getBookDetail(Long bookId){
        Book book = getBookById(bookId);
        return BookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setPrice(bookDTO.getPrice());
        book.setStock(bookDTO.getStock());
        return BookMapper.toBookDTO(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDTO updateBookStock(Book book,int stock) {
        book.setStock(stock);
        return BookMapper.toBookDTO(bookRepository.save(book));
    }


    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with given id is not found", HttpStatus.NOT_FOUND));
    }
}
