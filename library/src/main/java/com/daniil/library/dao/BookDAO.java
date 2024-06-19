package com.daniil.library.dao;

import com.daniil.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDAO extends JpaRepository<Book, Integer> {

    Page<Book> findAll(Pageable pageable);

}
