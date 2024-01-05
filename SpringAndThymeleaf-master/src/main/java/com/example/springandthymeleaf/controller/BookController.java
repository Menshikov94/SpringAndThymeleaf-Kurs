package com.example.springandthymeleaf.controller;

import com.example.springandthymeleaf.DTO.BookDto;
import com.example.springandthymeleaf.entity.Book;
import com.example.springandthymeleaf.entity.Location;
import com.example.springandthymeleaf.repository.LocationRepository;
import com.example.springandthymeleaf.repository.BookLocationRepository;
import com.example.springandthymeleaf.repository.BookRepository;
import com.example.springandthymeleaf.repository.UserRepository;
import com.example.springandthymeleaf.service.LogService;
import com.example.springandthymeleaf.service.BookService;
import com.example.springandthymeleaf.service.BookServiceNotForAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class BookController {

    private final LogService logService;
    private final UserRepository userRepository;

    private final BookRepository bookRepository;
    private final BookServiceNotForAll bookServiceNotForAll;
    private final BookService bookService;
    private final BookLocationRepository bookLocationRepository;
    private final LocationRepository locationRepository;

    public BookController(LogService logService, UserRepository userRepository, BookRepository bookRepository, BookServiceNotForAll bookServiceNotForAll, BookService bookService, BookLocationRepository bookLocationRepository, LocationRepository locationRepository) {
        this.logService = logService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookServiceNotForAll = bookServiceNotForAll;
        this.bookService = bookService;
        this.bookLocationRepository = bookLocationRepository;
        this.locationRepository = locationRepository;
    }


    @GetMapping("/book/books")
    public String books(Model model, Principal principal) {
        //List<Book> books = bookRepository.findAll();
        List<BookDto> books = bookService.findAllBooks(principal);
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book/addBookForm")
    public ModelAndView addBookForm(Principal principal) {
        ModelAndView mav = new ModelAndView("add-book-form");
        Book book = new Book();
        book.setBookName(principal.getName());
        mav.addObject("book", book);
        return mav;
    }

    @PostMapping("/book/saveBook")
    public String saveBook(@ModelAttribute("Book") BookDto bookDto, Model model, BindingResult result,
                           Principal principal) {
        log.error("connect -> registration" + " ---" + bookDto.getName());
        if (bookService != null) {
            Book existingBook = bookService.findBookByBookName(bookDto.getName());
            if (existingBook != null && existingBook.getName() != null && !existingBook.getName().isEmpty()) {
                log.error("пользователь с таким именем уже существует");
                result.rejectValue("BookName", null,
                        "вещь с таким именем уже существует");
                return "redirect:/book/books?error";
            } else {
                // Handle the case when userService is null
                // Log an error or throw an exception
            }
            if (result.hasErrors()) {
                model.addAttribute("book", bookDto);
                return "/book/showUpdateForm";
            }

            bookService.saveBook(bookDto, principal);

        }
        return "redirect:/book/books";
    }

    @GetMapping("/book/showUpdateForm")
    public ModelAndView showUpdateFormBook(@RequestParam Long bookId, Principal principal) {

        logService.saveLog("INFO", "User " + principal.getName() +
                " перешел на страницу showUpdateForm и пытается изменить вещь с ID = " + bookId);
        ModelAndView mav = new ModelAndView("update-book-form");
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        Book book = new Book();
        if (optionalBook.isPresent()) {
            book = optionalBook.get();
        }
        mav.addObject("book", book);
        return mav;
    }
    @PostMapping("/books/update")
    public String updateUser(@Valid @ModelAttribute("book") BookDto bookDto,
                             @RequestParam("location") String rolesValue,
                             Principal principal) {
        log.error("в /books/update " + bookDto.getId() + " " + bookDto.getName());

        if (bookService != null) {
            log.error("в /books/update bookService != null");

            Book updatedBook = new Book();
            updatedBook.setId(bookDto.getId());
            updatedBook.setBookName(principal.getName());
            updatedBook.setName(bookDto.getName());
            updatedBook.setQuantity(bookDto.getQuantity());

            String s;
            if (rolesValue.equals("1")) {
                s = "ул. Ленина, д.95";
            } else if (rolesValue.equals("2")){
                s = "ул. Пушкина, д.12";

            } else {
                s = "ул. Витаса, д.44";

            }
            Location location = locationRepository.findByName(s);
            logService.saveLog("INFO", "User " + principal.getName() +
                    " изменил пользователя с логином: " + updatedBook.getName() +
                    "\nпользователь после изменения: имя пользователя   '" + bookDto.getUserName() + "'\n его роль: " + s);


            updatedBook.setLocation(Arrays.asList(location));
            log.error("в /books/update bookService != null " + updatedBook.getName() + " " + updatedBook.getBookName() + updatedBook.getQuantity() + " " + s);

            // Сохраняем обновленного пользователя в базе данных
            bookService.updateBook(updatedBook);

            return "redirect:/book/books";



        }


        return "redirect:/book/books/profile?error";

    }
    @GetMapping("/book/deleteBook")
    public String deleteBook(@RequestParam Long bookId) {
        bookLocationRepository.deleteById(bookId);
        bookRepository.deleteById(bookId);
        return "redirect:/book/books";
    }




}


