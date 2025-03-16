package ru.otus.atm;

import java.util.Map;
import ru.otus.model.Banknote;

public interface ATM {

    void deposit(Banknote banknote, int count);

    int getBalance();

    Map<Banknote, Integer> withdrawal(int amount);
}
