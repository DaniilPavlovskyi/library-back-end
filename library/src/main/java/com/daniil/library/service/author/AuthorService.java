package com.daniil.library.service.author;

import com.daniil.library.entity.Author;

public interface AuthorService {

    Author findById(int id);
    Author findByName(String name);
}
