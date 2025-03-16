package ru.otus.atm;

import java.util.Map;
import ru.otus.model.Banknote;

public interface WithdrawalStrategy {

    Map<Banknote, Integer> withdrawal(int amount, BanknoteStorage banknoteStorage);
}
