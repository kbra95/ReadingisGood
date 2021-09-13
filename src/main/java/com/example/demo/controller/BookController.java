package com.example.demo.controller;

import com.example.demo.exceptions.GeneralException;
import com.example.demo.model.dto.BookDTO;
import com.example.demo.model.entity.Book;
import com.example.demo.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookDetail(@PathVariable Long bookId) {
        return new ResponseEntity<>(bookService.getBookDetail(bookId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO){
        return  new ResponseEntity<>(bookService.createBook(bookDTO),HttpStatus.CREATED);
    }

    @PutMapping("/{id}/stock/{stock}")
    public ResponseEntity<BookDTO> updateBookStock(@PathVariable @NotNull(message = " can not be null") Long id,
                                                   @PathVariable int stock){
        if(stock <= 0 ){
            throw new GeneralException("Stock can not be less than 0",HttpStatus.BAD_REQUEST);
        }
        Book bookById = bookService.getBookById(id);
        int newStock = bookById.getStock()+stock;
        return  new ResponseEntity<>(bookService.updateBookStock(bookById,newStock),HttpStatus.OK);
    }
}
