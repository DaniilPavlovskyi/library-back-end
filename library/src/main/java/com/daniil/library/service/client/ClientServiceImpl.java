package com.daniil.library.service.client;

import com.daniil.library.dao.ClientDAO;
import com.daniil.library.entity.Client;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientDAO clientDAO;

    public ClientServiceImpl(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @Override
    public Client findByUsername(String username) {
        return clientDAO.findById(username).orElse(null);
    }

    @Override
    public void save(Client client) {
        clientDAO.save(client);
    }

    @Override
    public boolean existsByUsername(String username) {
        return clientDAO.existsById(username);
    }
}
