package com.example.springandthymeleaf.service;

import com.example.springandthymeleaf.entity.Book;

import java.security.Principal;
import java.util.List;

public interface BookServiceNotForAll {

    List<Book> booksForUsers(Principal principal);
}
