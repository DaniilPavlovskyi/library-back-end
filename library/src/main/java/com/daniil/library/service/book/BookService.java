package com.daniil.library.service.book;

import com.daniil.library.dto.BookDTO;
import com.daniil.library.entity.Author;
import com.daniil.library.entity.Book;
import org.springframework.data.domain.Page;


public interface BookService {

    BookDTO findById(int id);

    Book findObjectById(int id);

    Page<Book> findAll(int page, int size);

    void save(Book book);
    void deleteById(int id);

    void updateBook(BookDTO bookDTO, Author author);
}
