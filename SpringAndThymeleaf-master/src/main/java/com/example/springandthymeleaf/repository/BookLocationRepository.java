package com.example.springandthymeleaf.repository;

import com.example.springandthymeleaf.entity.BookLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLocationRepository extends JpaRepository<BookLocation, Long> {
}
