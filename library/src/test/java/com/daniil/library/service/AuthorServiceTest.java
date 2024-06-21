package com.daniil.library.service;

import com.daniil.library.dao.AuthorDAO;
import com.daniil.library.entity.Author;
import com.daniil.library.service.author.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorDAO authorDAO;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void getExistingAuthorByIdShouldReturnAuthor() {
        Author mockAuthor = new Author();

        when(authorDAO.findById(1)).thenReturn(Optional.of(mockAuthor));

        Author author = authorService.findById(1);

        verify(authorDAO, times(1)).findById(1);
        assertSame(mockAuthor, author);
    }

    @Test
    void getNotExistingAuthorByIdShouldReturnNull() {
        when(authorDAO.findById(1)).thenReturn(Optional.empty());

        Author author = authorService.findById(1);

        verify(authorDAO, times(1)).findById(1);
        assertNull(author);
    }

    @Test
    void getExistingAuthorByNameShouldReturnAuthor() {
        Author mockAuthor = new Author();
        mockAuthor.setName("Author 1");

        when(authorDAO.findByName("Author 1")).thenReturn(mockAuthor);

        Author author = authorService.findByName("Author 1");

        verify(authorDAO, times(1)).findByName("Author 1");
        assertSame(mockAuthor, author);
    }

    @Test
    void getNotExistingAuthorByNameShouldReturnNull() {
        when(authorDAO.findByName(anyString())).thenReturn(null);

        Author author = authorService.findByName("Author 1");

        verify(authorDAO, times(1)).findByName("Author 1");
        assertNull(author);
    }

}
