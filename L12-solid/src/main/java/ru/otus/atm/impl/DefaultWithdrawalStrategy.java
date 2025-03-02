package ru.otus.atm.impl;

import ru.otus.atm.BanknoteStorage;
import ru.otus.atm.Cassette;
import ru.otus.atm.WithdrawalStrategy;
import ru.otus.model.Banknote;

public class DefaultWithdrawalStrategy implements WithdrawalStrategy {

    @Override
    public boolean withdrawal(int amount, BanknoteStorage storage) {
        if (amount > storage.getBalance()) {
            return false;
        }

        for (var entry : storage.getCassettes().entrySet()) {
            if (amount == 0) break;

            Banknote banknote = entry.getKey();
            Cassette cassette = entry.getValue();

            int value = banknote.getValue();
            int needed = amount / value;
            int available = cassette.getCount();
            int toWithdraw = Math.min(needed, available);

            if (toWithdraw > 0) {
                cassette.addBanknotes(-toWithdraw);
                amount -= toWithdraw * value;
            }
        }
        return amount == 0;
    }
}
