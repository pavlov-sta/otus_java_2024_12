package ru.otus.atm.impl;

import java.util.Map;
import ru.otus.atm.ATM;
import ru.otus.atm.BanknoteStorage;
import ru.otus.atm.Cassette;
import ru.otus.atm.WithdrawalStrategy;
import ru.otus.model.Banknote;

public class ATMImpl implements ATM {

    private final BanknoteStorage banknoteStorage;
    private final WithdrawalStrategy withdrawalStrategy;

    public ATMImpl(
            BanknoteStorage banknoteStorage,
            WithdrawalStrategy withdrawalStrategy,
            Map<Banknote, Integer> initialBanknotes) {
        this.banknoteStorage = banknoteStorage;
        this.withdrawalStrategy = withdrawalStrategy;
        initializeCassettes(initialBanknotes);
    }

    private void initializeCassettes(Map<Banknote, Integer> initialBanknotes) {
        for (Banknote banknote : Banknote.values()) {
            int count = initialBanknotes.getOrDefault(banknote, 0);
            Cassette cassette = new CassetteImpl();
            cassette.addBanknotes(count);
            banknoteStorage.addCassette(banknote, cassette);
        }
    }

    @Override
    public void deposit(Banknote banknote, int count) {
        Cassette cassette = banknoteStorage.getCassette(banknote);
        if (cassette == null) {
            throw new IllegalStateException("No cassette found for banknote: " + banknote);
        }
        cassette.addBanknotes(count);
    }

    @Override
    public int getBalance() {
        return banknoteStorage.getBalance();
    }

    @Override
    public Map<Banknote, Integer> withdrawal(int amount) {
        return withdrawalStrategy.withdrawal(amount, banknoteStorage);
    }
}
