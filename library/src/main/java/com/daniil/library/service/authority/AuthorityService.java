package com.daniil.library.service.authority;

import com.daniil.library.entity.Authority;

public interface AuthorityService {

    Authority findByName(String authority);

    void save(Authority authority);

}
