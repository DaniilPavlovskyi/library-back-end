package com.daniil.library.dao;

import com.daniil.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorDAO extends JpaRepository<Author, Integer> {
    Author findByName(String name);
}
