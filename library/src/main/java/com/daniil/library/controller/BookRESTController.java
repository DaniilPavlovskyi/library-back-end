package com.daniil.library.controller;


import com.daniil.library.dto.BookDTO;
import com.daniil.library.entity.Author;
import com.daniil.library.entity.Book;
import com.daniil.library.entity.Client;
import com.daniil.library.entity.Loan;
import com.daniil.library.security.JwtUtil;
import com.daniil.library.service.author.AuthorService;
import com.daniil.library.service.book.BookService;
import com.daniil.library.service.client.ClientService;
import com.daniil.library.service.loan.LoanService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin(origins = "${link:http://localhost:3000}")
public class BookRESTController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final ClientService clientService;
    private final LoanService loanService;
    private final JwtUtil jwtUtil;

    public BookRESTController(BookService bookService, AuthorService authorService, ClientService clientService, LoanService loanService, JwtUtil jwtUtil) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.clientService = clientService;
        this.loanService = loanService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("api/books")
    public ResponseEntity<Map<String, Object>> getBooks(@RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "9") Integer size) {
        Map<String, Object> content = new HashMap<>();
        Page<Book> books = bookService.findAll(page - 1, size);
        books.getContent().forEach(e -> {
            e.setAuthor(null);
            e.setLoans(null);
        });
        content.put("books", books.getContent());
        content.put("currentPage", page);
        content.put("totalPages", books.getTotalPages());
        return ResponseEntity.ok(content);
    }

    @GetMapping("api/books/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable int id) {
        BookDTO bookDTO = bookService.findById(id);
        if (bookDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping("/api/author/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable String id) {
        Author author;
        try {
            int authorId = Integer.parseInt(id);
            author = authorService.findById(authorId);
            if (author == null) {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(author);
    }

    @PutMapping("api/update-book")
    public ResponseEntity<Void> updateBook(@RequestBody BookDTO bookDTO) {
        Integer bookId = bookDTO.getId();
        if (bookId == null) {
            return ResponseEntity.badRequest().build();
        }

        Author author = authorService.findByName(bookDTO.getAuthor());
        if (author == null) {
            return ResponseEntity.badRequest().build();
        }

        BookDTO book = bookService.findById(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        bookService.updateBook(bookDTO, author);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/delete-book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        if (bookService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("api/create-book")
    public ResponseEntity<Void> createBook(@RequestBody BookDTO bookDTO) {
        String title = bookDTO.getTitle();
        String category = bookDTO.getCategory();
        String authorName = bookDTO.getAuthor();
        int publicationYear = bookDTO.getPublicationYear();
        boolean availability = bookDTO.isAvailability();

        if (!StringUtils.hasText(title) || !StringUtils.hasText(category)) {
            return ResponseEntity.badRequest().build();
        }

        if (publicationYear < 1) {
            return ResponseEntity.badRequest().build();
        }

        Author author = authorService.findByName(authorName);
        if (author == null) {
            return ResponseEntity.badRequest().build();
        }

        Book book = new Book();
        book.setTitle(title);
        book.setCategory(category);
        book.setAuthor(author);
        book.setPublicationYear(publicationYear);
        book.setPresent(availability);

        bookService.save(book);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @PostMapping("/api/new-loan/{id}")
//    public ResponseEntity<Void> createLoan(
//            @PathVariable String id,
//            @RequestBody Map<String, String> date,
//            HttpServletRequest request
//    ) {
//        Loan loan = new Loan();
//        String bearerToken = request.getHeader("Authorization");
//        Client client = clientService.findByUsername(jwtUtil.extractUsername(bearerToken.substring(7)));
//        Book book;
//
//        try {
//            int bookId = Integer.parseInt(id);
//            book = bookService.findById(bookId);
//
//            if (book == null) {
//                return ResponseEntity.badRequest().build();
//            }
//
//            if (!book.isPresent()) {
//                return ResponseEntity.badRequest().build();
//            }
//
//            LocalDate loanDate = LocalDate.parse(date.get("date"));
//
//            loan.setBook(book);
//            loan.setStart(LocalDate.now());
//            loan.setEnd(loanDate);
//            loan.setClient(client);
//            loan.setStatus("started");
//
//            book.addLoan(loan);
//            book.setPresent(false);
//            client.addLoan(loan);
//
//            loanService.save(loan);
//            clientService.save(client);
//            bookService.save(book);
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }


}
