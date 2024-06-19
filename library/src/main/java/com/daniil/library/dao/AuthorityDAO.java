package com.daniil.library.dao;

import com.daniil.library.entity.Authority;
import com.daniil.library.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AuthorityDAO extends JpaRepository<Authority, Integer> {
    Authority findByAuthority(String authority);
}
