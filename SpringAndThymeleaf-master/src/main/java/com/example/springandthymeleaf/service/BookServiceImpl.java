package com.example.springandthymeleaf.service;

import com.example.springandthymeleaf.DTO.BookDto;
import com.example.springandthymeleaf.entity.Book;
import com.example.springandthymeleaf.entity.Location;
import com.example.springandthymeleaf.entity.Role;
import com.example.springandthymeleaf.entity.User;
import com.example.springandthymeleaf.repository.LocationRepository;
import com.example.springandthymeleaf.repository.BookRepository;
import com.example.springandthymeleaf.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final LogService logService;

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, LogService logService, LocationRepository locationRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.logService = logService;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveBook(BookDto bookDto, Principal principal) {

        Book book = new Book();
        book.setName(bookDto.getName());
        book.setBookName(principal.getName());
        book.setQuantity(bookDto.getQuantity());
        log.error("в saveBook " + book.getName() + book.getBookName() + book.getQuantity());

        Location location = locationRepository.findByName("ул. Ленина, д.5");
        if (location == null) {
            location = checkLocationExist();
        }
        book.setLocation(List.of(location));

        bookRepository.save(book);

    }

    @Override
    public List<BookDto> findAllBooks(Principal principal) {
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

        }
        return books.stream()
                .map((book) -> mapToBookDto(book))
                .collect(Collectors.toList());
    }

    private BookDto mapToBookDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.setName(book.getName());
        bookDto.setUserName(book.getBookName());
        bookDto.setId(book.getId());
        bookDto.setQuantity(book.getQuantity());
        bookDto.setLocationBook(book.getLocation().stream()
                .map(Location::getName)
                .collect(Collectors.joining(", ")));
        return bookDto;
    }

    @Override
    public void updateBook(Book book) {
        bookRepository.save(book);

    }

    @Override
    public User findBookById(Long bookId) {
        return null;
    }

    @Override
    public Book findBookByBookName(String bookName) {
        return bookRepository.findByBookName(bookName);
    }

    private Location checkLocationExist() {
        Location location = new Location();
        location.setName("ул. Ленина, д.5");
        return locationRepository.save(location);
    }
}
