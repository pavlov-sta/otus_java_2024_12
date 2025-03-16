package ru.otus.atm.impl;

import java.util.Map;
import java.util.TreeMap;
import ru.otus.atm.BanknoteStorage;
import ru.otus.atm.Cassette;
import ru.otus.model.Banknote;

public class BanknoteStorageImpl implements BanknoteStorage {

    private final Map<Banknote, Cassette> cassetteMap = new TreeMap<>((a, b) -> b.getValue() - a.getValue());

    @Override
    public void addCassette(Banknote banknote, Cassette cassette) {
        cassetteMap.putIfAbsent(banknote, cassette);
    }

    @Override
    public Cassette getCassette(Banknote banknote) {
        return cassetteMap.get(banknote);
    }

    @Override
    public int getBalance() {
        return cassetteMap.entrySet().stream()
                .mapToInt(e -> e.getValue().getBalance(e.getKey().getValue()))
                .sum();
    }

    @Override
    public Map<Banknote, Cassette> getCassettes() {
        return cassetteMap;
    }
}
