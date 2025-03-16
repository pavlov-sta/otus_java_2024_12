package ru.otus.atm;

public interface Cassette {

    void addBanknotes(int count);

    int getBalance(int value);

    int getCount();

    void removeBanknotes(int count);
}
