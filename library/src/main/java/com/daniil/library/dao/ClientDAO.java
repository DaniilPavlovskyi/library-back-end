package com.daniil.library.dao;

import com.daniil.library.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDAO extends JpaRepository<Client, String> {
}
