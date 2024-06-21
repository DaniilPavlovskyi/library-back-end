package com.daniil.library.service.authority;

import com.daniil.library.dao.AuthorityDAO;
import com.daniil.library.entity.Authority;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityDAO authorityDAO;

    public AuthorityServiceImpl(AuthorityDAO authorityDAO) {
        this.authorityDAO = authorityDAO;
    }


    @Override
    public Authority findByName(String name) {
        return authorityDAO.findByAuthority(name);
    }

    @Override
    public void save(Authority authority) {
        authorityDAO.save(authority);
    }
}
