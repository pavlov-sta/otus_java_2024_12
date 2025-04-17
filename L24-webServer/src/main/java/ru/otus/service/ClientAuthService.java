package ru.otus.service;

public interface ClientAuthService {
    boolean authenticate(String login, String password);
}
