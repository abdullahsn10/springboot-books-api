package com.abdul.postdb.controllers;

import com.abdul.postdb.domain.dto.BookDto;
import com.abdul.postdb.domain.entities.BookEntity;
import com.abdul.postdb.mappers.Mapper;
import com.abdul.postdb.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private BookService bookService;
    private Mapper<BookEntity, BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }


    // Create endpoint
    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn,
                                              @RequestBody BookDto book) {

        // convert BookDto into BookEntity
        BookEntity bookEntity = bookMapper.mapFrom(book);

        // check if the book exists
        boolean bookExists = bookService.isExists(isbn);

        // create/update the book
        BookEntity savedBookEntity = bookService.createUpdateBook(isbn, bookEntity);

        if(bookExists){
            // update
            return new ResponseEntity<>(bookMapper.mapTo(savedBookEntity), HttpStatus.OK);
        } else{
            // create
            return new ResponseEntity<>(bookMapper.mapTo(savedBookEntity), HttpStatus.CREATED);
        }

    }

    @GetMapping(path = "/books")
    public Page<BookDto> listBooks(Pageable pageable){
        Page<BookEntity> books = bookService.findAll(pageable);
        return books.map(bookMapper::mapTo);
    }

    @GetMapping("/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn){
        Optional<BookEntity> foundBook = bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDto bookDto = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(@PathVariable("isbn") String isbn,
                                                     @RequestBody BookDto bookDto) {
        // check if the book exists
        if(!bookService.isExists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity savedBookEntity = bookService.partialUpdate(isbn, bookEntity);
        return new ResponseEntity<>(bookMapper.mapTo(savedBookEntity),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable("isbn") String isbn){
        bookService.delete(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}
