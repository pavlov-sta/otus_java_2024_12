package ru.otus.atm.impl;

import java.util.HashMap;
import java.util.Map;
import ru.otus.atm.BanknoteStorage;
import ru.otus.atm.Cassette;
import ru.otus.atm.WithdrawalStrategy;
import ru.otus.model.Banknote;

public class DefaultWithdrawalStrategy implements WithdrawalStrategy {

    @Override
    public Map<Banknote, Integer> withdrawal(int amount, BanknoteStorage banknoteStorage) throws IllegalStateException {
        if (amount > banknoteStorage.getBalance()) {
            throw new IllegalStateException("Insufficient funds in the ATM.");
        }

        Map<Banknote, Integer> withdrawnBanknotes = new HashMap<>();
        Map<Banknote, Integer> tempChanges = new HashMap<>();
        int remainingAmount = amount;

        for (var entry : banknoteStorage.getCassettes().entrySet()) {
            if (remainingAmount == 0) break;

            Banknote banknote = entry.getKey();
            Cassette cassette = entry.getValue();

            int value = banknote.getValue();
            int needed = remainingAmount / value;
            int available = cassette.getCount();
            int toWithdraw = Math.min(needed, available);

            if (toWithdraw > 0) {
                tempChanges.put(banknote, toWithdraw);
                remainingAmount -= toWithdraw * value;
            }
        }

        if (remainingAmount > 0) {
            throw new IllegalStateException("Unable to dispense the requested amount with available banknotes.");
        }

        // Apply changes to the cassettes
        for (var entry : tempChanges.entrySet()) {
            Banknote banknote = entry.getKey();
            int count = entry.getValue();
            Cassette cassette = banknoteStorage.getCassettes().get(banknote);
            cassette.removeBanknotes(count);
            withdrawnBanknotes.put(banknote, count);
        }

        return withdrawnBanknotes;
    }
}
