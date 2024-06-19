package com.daniil.library.service.book;

import com.daniil.library.dao.BookDAO;
import com.daniil.library.dto.BookDTO;
import com.daniil.library.entity.Author;
import com.daniil.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;

    public BookServiceImpl(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public BookDTO findById(int id) {
        Book book = bookDAO.findById(id).orElse(null);
        if (book == null) {
            return null;
        }
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(id);
        bookDTO.setTitle(book.getTitle());
        bookDTO.setCategory(book.getCategory());
        bookDTO.setAvailability(bookDTO.isAvailability());
        bookDTO.setPublicationYear(book.getPublicationYear());
        return bookDTO;
    }

    @Override
    public Page<Book> findAll(int page, int size) {
        return bookDAO.findAll(PageRequest.of(page, size));
    }

    @Override
    public void save(Book book) {
        bookDAO.save(book);
    }

    @Override
    public void deleteById(int id) {
        bookDAO.deleteById(id);
    }

    @Override
    public void updateBook(BookDTO bookDTO, Author author) {
        Book book = bookDAO.findById(bookDTO.getId()).get();
        book.setTitle(bookDTO.getTitle());
        book.setCategory(bookDTO.getCategory());
        book.setAuthor(author);
        book.setPublicationYear(bookDTO.getPublicationYear());
        bookDAO.save(book);
    }
}
