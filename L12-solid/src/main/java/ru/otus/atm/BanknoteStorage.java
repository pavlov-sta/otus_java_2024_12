package ru.otus.atm;

import java.util.Map;
import ru.otus.model.Banknote;

public interface BanknoteStorage {

    void addCassette(Banknote banknote, Cassette cassette);

    Cassette getCassette(Banknote banknote);

    int getBalance();

    Map<Banknote, Cassette> getCassettes();
}
