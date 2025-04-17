package ru.otus.service;

import ru.otus.crm.model.Client;
import ru.otus.crm.model.RoleClient;
import ru.otus.crm.service.DBServiceClient;

public class ClientAuthServiceImpl implements ClientAuthService {

    private final DBServiceClient dbServiceClient;

    public ClientAuthServiceImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceClient
                .findByLogin(login)
                .map(client -> checkRoleAndPassword(client, password))
                .orElse(false);
    }

    private boolean checkRoleAndPassword(Client client, String password) {
        return client.getPassword().equals(password)
                && client.getRoleClient() != null
                && RoleClient.ADMIN.equals(client.getRoleClient());
    }
}
