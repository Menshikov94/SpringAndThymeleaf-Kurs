package com.example.springandthymeleaf.service;

import com.example.springandthymeleaf.entity.Book;
import com.example.springandthymeleaf.entity.Role;
import com.example.springandthymeleaf.entity.User;
import com.example.springandthymeleaf.repository.BookRepository;
import com.example.springandthymeleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class BookServiceNotForAllImpl implements BookServiceNotForAll {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookServiceNotForAllImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Book> booksForUsers(Principal principal) {
        User user = userRepository.findByUserName(principal.getName());
        String s = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "));
        System.out.println(s);
        List<Book> books;
        if (s.equals("ROLE_USER")) {
            books = bookRepository.findAll().stream()
                    .filter(book -> book.getBookName().equals(principal.getName()))
                    .toList();

        } else {
            books = bookRepository.findAll();

        }  return books;







    }
}
