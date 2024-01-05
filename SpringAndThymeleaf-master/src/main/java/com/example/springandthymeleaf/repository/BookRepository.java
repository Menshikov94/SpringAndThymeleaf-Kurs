package com.example.springandthymeleaf.repository;

import com.example.springandthymeleaf.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByBookName(String bookName);
}
