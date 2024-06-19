package com.daniil.library.service.author;

import com.daniil.library.dao.AuthorDAO;
import com.daniil.library.entity.Author;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService{

    private final AuthorDAO authorDAO;

    public AuthorServiceImpl(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    @Override
    public Author findById(int id) {
        return authorDAO.findById(id).orElse(null);
    }

    @Override
    public Author findByName(String name) {
        return authorDAO.findByName(name);
    }
}
