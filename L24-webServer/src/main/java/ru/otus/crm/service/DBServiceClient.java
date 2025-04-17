package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import ru.otus.crm.model.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    List<Client> findAll();

    Optional<Client> findById(long id);

    Optional<Client> findByLogin(String login);
}
