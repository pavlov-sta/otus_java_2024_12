package ru.otus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.atm.ATM;
import ru.otus.atm.BanknoteStorage;
import ru.otus.atm.WithdrawalStrategy;
import ru.otus.atm.impl.ATMImpl;
import ru.otus.atm.impl.BanknoteStorageImpl;
import ru.otus.atm.impl.DefaultWithdrawalStrategy;
import ru.otus.model.Banknote;

class ATMTest {

    private ATM atm;

    @BeforeEach
    public void init() {
        WithdrawalStrategy withdrawalStrategy = new DefaultWithdrawalStrategy();
        BanknoteStorage banknoteStorage = new BanknoteStorageImpl();
        atm = new ATMImpl(banknoteStorage, withdrawalStrategy);
    }

    @Test
    void testDepositAndBalance() {
        addBalance();
        assertEquals(550, atm.getBalance());
    }

    @Test
    void testWriteOff() {
        addBalance();
        assertEquals(550, atm.getBalance());
        assertTrue(atm.withdrawal(500));
        assertEquals(50, atm.getBalance());
    }

    @Test
    void testWithdrawalOffIfNotEnoughFunds() {
        addBalance();
        assertEquals(550, atm.getBalance());
        assertFalse(atm.withdrawal(700));
        assertEquals(550, atm.getBalance());
    }

    private void addBalance() {
        atm.deposit(Banknote.FIFTY, 5);
        atm.deposit(Banknote.TEN, 5);
        atm.deposit(Banknote.FIFTY, 5);
    }
}
