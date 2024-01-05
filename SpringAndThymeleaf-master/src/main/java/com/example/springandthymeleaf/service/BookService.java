package com.example.springandthymeleaf.service;

import com.example.springandthymeleaf.DTO.BookDto;
import com.example.springandthymeleaf.entity.Book;
import com.example.springandthymeleaf.entity.User;

import java.security.Principal;
import java.util.List;

public interface BookService {

    void saveBook(BookDto bookDto, Principal principal);

    List<BookDto> findAllBooks(Principal principal);

    void updateBook(Book book);

    User findBookById(Long bookId);
    Book findBookByBookName(String bookName);
}
