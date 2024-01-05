package com.example.springandthymeleaf.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_location")
public class BookLocation {

    @Id
    @Column(name = "book_id")
    private Long bookId;
    @Column(name = "location_id")
    private Long locationId;
}
