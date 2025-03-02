package ru.otus.atm;

import ru.otus.model.Banknote;

public interface ATM {

    void deposit(Banknote banknote, int count);

    int getBalance();

    boolean withdrawal(int amount);
}
