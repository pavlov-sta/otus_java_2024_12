package ru.otus;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
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
        Map<Banknote, Integer> initialBanknotes = Map.of(
                Banknote.FIFTY, 10,
                Banknote.TEN, 5,
                Banknote.ONE_HUNDRED, 10);
        atm = new ATMImpl(banknoteStorage, withdrawalStrategy, initialBanknotes);
    }

    @Test
    void testDepositAndBalance() {
        addBalance();
        assertEquals(2100, atm.getBalance());
    }

    @Test
    void testWriteOff() {
        addBalance();
        assertEquals(2100, atm.getBalance());
        Map<Banknote, Integer> withdrawnBanknotes = atm.withdrawal(500);
        assertNotNull(withdrawnBanknotes);
        assertEquals(1600, atm.getBalance());
        assertEquals(Map.of(Banknote.ONE_HUNDRED, 5), withdrawnBanknotes);
    }

    @Test
    void testWithdrawalIfNotEnoughFunds() {
        addBalance();
        assertEquals(2100, atm.getBalance());
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            atm.withdrawal(3700);
        });
        assertEquals("Insufficient funds in the ATM.", exception.getMessage());
        assertEquals(2100, atm.getBalance());
    }

    @Test
    void testWithdrawalIfUnableToDispense() {
        addBalance();
        assertEquals(2100, atm.getBalance());
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            atm.withdrawal(55); // Например, сумма, которую невозможно выдать с текущими номиналами банкнот
        });
        assertEquals("Unable to dispense the requested amount with available banknotes.", exception.getMessage());
        assertEquals(2100, atm.getBalance());
    }

    private void addBalance() {
        atm.deposit(Banknote.FIFTY, 5);
        atm.deposit(Banknote.TEN, 5);
        atm.deposit(Banknote.FIFTY, 5);
    }
}
