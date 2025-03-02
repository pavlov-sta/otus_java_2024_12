package ru.otus.atm.impl;

import ru.otus.atm.Cassette;

public class CassetteImpl implements Cassette {

    private int count;

    @Override
    public void addBanknotes(int count) {
        this.count += count;
    }

    @Override
    public int getBalance(int value) {
        return value * count;
    }

    @Override
    public int getCount() {
        return count;
    }
}
