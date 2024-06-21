package com.daniil.library.service;

import com.daniil.library.dao.BookDAO;
import com.daniil.library.dto.BookDTO;
import com.daniil.library.entity.Book;
import com.daniil.library.service.book.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookDAO bookDAO;

    @Test
    void getExistingBookByIdShouldReturnDTO() {
        Book mockBook = new Book();
        mockBook.setId(1);
        mockBook.setTitle("mock book");
        mockBook.setCategory("mock category");
        mockBook.setPublicationYear(1985);
        mockBook.setPresent(true);

        when(bookDAO.findById(1)).thenReturn(Optional.of(mockBook));

        BookDTO bookDTO = bookService.findById(1);


        verify(bookDAO, times(1)).findById(1);
        assertEquals(mockBook.getId(), bookDTO.getId());
        assertEquals(mockBook.getTitle(), bookDTO.getTitle());
        assertEquals(mockBook.getCategory(), bookDTO.getCategory());
        assertEquals(mockBook.getPublicationYear(), bookDTO.getPublicationYear());
        assertEquals(mockBook.isPresent(), bookDTO.isAvailable());
    }

    @Test
    void getNotExistingBookByIdShouldReturnNull() {
        when(bookDAO.findById(1)).thenReturn(Optional.empty());
        BookDTO bookDTO = bookService.findById(1);

        verify(bookDAO, times(1)).findById(1);
        assertNull(bookDTO);
    }

}
