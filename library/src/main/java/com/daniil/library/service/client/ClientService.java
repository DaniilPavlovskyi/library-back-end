package com.daniil.library.service.client;

import com.daniil.library.entity.Client;

public interface ClientService {

    Client findByUsername(String username);
    void save(Client client);

    boolean existsByUsername(String username);
}
