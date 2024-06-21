package com.daniil.library.controller;


import com.daniil.library.dto.BookDTO;
import com.daniil.library.dto.LoanRequestDTO;
import com.daniil.library.entity.Author;
import com.daniil.library.entity.Book;
import com.daniil.library.entity.Client;
import com.daniil.library.entity.Loan;
import com.daniil.library.security.JwtUtil;
import com.daniil.library.service.author.AuthorService;
import com.daniil.library.service.book.BookService;
import com.daniil.library.service.client.ClientService;
import com.daniil.library.service.loan.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookRESTControllerTest {

    @InjectMocks
    private BookRESTController bookRESTController;
    @Mock
    private BookService bookService;
    @Mock
    private AuthorService authorService;
    @Mock
    private ClientService clientService;
    @Mock
    private LoanService loanService;
    @Mock
    private JwtUtil jwtUtil;


    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookRESTController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    void createBookWithAuthorShouldBeOk() throws Exception {
        BookDTO newBook = new BookDTO(
                null,
                "The Great Gatsby",
                "Fiction",
                "F. Scott Fitzgerald",
                1925,
                true
        );
        String bookJson = objectMapper.writeValueAsString(newBook);

        when(authorService.findByName("F. Scott Fitzgerald")).thenReturn(new Author());
        mockMvc.perform(post("/api/create-book")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated());

        verify(authorService, times(1)).findByName("F. Scott Fitzgerald");
        verify(bookService, times(1)).save(any(Book.class));
    }

    @Test
    void createBookWithoutAuthorShouldBeBadRequest() throws Exception {
        BookDTO newBook = new BookDTO(
                null,
                "The Great Gatsby",
                "Fiction",
                "F. Scott Fitzgerald",
                1925,
                true
        );
        String bookJson = objectMapper.writeValueAsString(newBook);

        when(authorService.findByName("F. Scott Fitzgerald")).thenReturn(null);
        mockMvc.perform(post("/api/create-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isBadRequest());

        verify(authorService, times(1)).findByName("F. Scott Fitzgerald");
        verify(bookService, times(0)).save(any(Book.class));
    }

    @Test
    void getBookThatExistsAndShouldReturnBook() throws Exception {
        BookDTO newBook = new BookDTO(
                1,
                "The Great Gatsby",
                "Fiction",
                "F. Scott Fitzgerald",
                1925,
                true
        );
        when(bookService.findById(1)).thenReturn(newBook);
        mockMvc.perform(get("/api/books/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("The Great Gatsby"))
                .andExpect(jsonPath("$.category").value("Fiction"))
                .andExpect(jsonPath("$.publicationYear").value(1925))
                .andExpect(jsonPath("$.author").value("F. Scott Fitzgerald"))
                .andExpect(jsonPath("$.available").value(true));

        verify(bookService, times(1)).findById(1);
    }

    @Test
    void getBookThatNotExistsAndShouldReturnNotFound() throws Exception {
        when(bookService.findById(1)).thenReturn(null);
        mockMvc.perform(get("/api/books/{id}", 1))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).findById(1);
    }

    @Test
    void updateBookWithAuthorAndCorrectIdShouldBeOk() throws Exception {
        BookDTO newBook = new BookDTO(
                1,
                "The Great Gatsby",
                "Fiction",
                "F. Scott Fitzgerald",
                1925,
                true
        );
        String bookJson = objectMapper.writeValueAsString(newBook);

        when(bookService.findById(1)).thenReturn(newBook);
        Author mockAuthor = new Author();
        when(authorService.findByName("F. Scott Fitzgerald")).thenReturn(mockAuthor);

        mockMvc.perform(put("/api/update-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk());

        verify(authorService, times(1)).findByName("F. Scott Fitzgerald");
        verify(bookService, times(1)).updateBook(newBook, mockAuthor);
    }

    @Test
    void updateBookWithoutAuthorShouldBeBadRequest() throws Exception {
        BookDTO newBook = new BookDTO(
                1,
                "The Great Gatsby",
                "Fiction",
                "F. Scott Fitzgerald",
                1925,
                true
        );
        String bookJson = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(put("/api/update-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isBadRequest());

        verify(authorService, times(1)).findByName("F. Scott Fitzgerald");
        verify(bookService, times(0)).updateBook(any(BookDTO.class), any(Author.class));
    }

    @Test
    void createLoanWithAllCorrectDataShouldBeOk() throws Exception {
        LoanRequestDTO loanRequestDTO = new LoanRequestDTO();
        loanRequestDTO.setBookId(1);
        loanRequestDTO.setDate(LocalDate.now().plusDays(10));

        Book mockBook = new Book();
        mockBook.setPresent(true);

        Client mockClient = new Client();

        Loan mockLoan = new Loan();
        mockLoan.setBook(mockBook);
        mockLoan.setClient(mockClient);
        mockLoan.setStart(LocalDate.now());
        mockLoan.setStart(LocalDate.now().plusDays(10));
        mockLoan.setStatus("started");

        when(jwtUtil.extractUsername("mock_token")).thenReturn("mock_user");
        when(clientService.findByUsername("mock_user")).thenReturn(mockClient);
        when(bookService.findObjectById(1)).thenReturn(mockBook);

        String loanJson = objectMapper.writeValueAsString(loanRequestDTO);

        mockMvc.perform(post("/api/new-loan")
                        .header("Authorization", "Bearer mock_token" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanJson))
                .andExpect(status().isCreated());

        verify(loanService, times(1)).save(mockLoan);
    }
}
