package ru.otus.atm.impl;

import ru.otus.atm.ATM;
import ru.otus.atm.BanknoteStorage;
import ru.otus.atm.Cassette;
import ru.otus.atm.WithdrawalStrategy;
import ru.otus.model.Banknote;

public class ATMImpl implements ATM {

    private final BanknoteStorage banknoteStorage;
    private final WithdrawalStrategy withdrawalStrategy;

    public ATMImpl(BanknoteStorage banknoteStorage, WithdrawalStrategy withdrawalStrategy) {
        this.banknoteStorage = banknoteStorage;
        this.withdrawalStrategy = withdrawalStrategy;
    }

    @Override
    public void deposit(Banknote banknote, int count) {
        Cassette cassette = banknoteStorage.getCassette(banknote);
        if (cassette == null) {
            cassette = new CassetteImpl();
            banknoteStorage.addCassette(banknote, cassette);
        }
        cassette.addBanknotes(count);
    }

    @Override
    public int getBalance() {
        return banknoteStorage.getBalance();
    }

    @Override
    public boolean withdrawal(int amount) {
        return withdrawalStrategy.withdrawal(amount, banknoteStorage);
    }
}
