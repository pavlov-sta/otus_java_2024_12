package ru.otus.atm;

public interface WithdrawalStrategy {

    boolean withdrawal(int amount, BanknoteStorage storage);
}
