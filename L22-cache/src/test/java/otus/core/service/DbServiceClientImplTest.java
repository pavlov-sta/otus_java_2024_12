package otus.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import otus.base.AbstractHibernateTest;
import ru.otus.hiber.crm.model.Address;
import ru.otus.hiber.crm.model.Client;
import ru.otus.hiber.crm.model.Phone;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SuppressWarnings("java:S125")
class DbServiceClientImplTest extends AbstractHibernateTest {

    @Test
    @DisplayName("должен сохранить и найти клиента, используя кэш")
    void shouldSaveAndFindClientUsingCache() {

        var client = new Client(
                null,
                "Vasya",
                new Address(null, "AnyStreet"),
                List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333")));

        var savedClient = dbServiceClient.saveClient(client);
        assertThat(savedClient).isNotNull();

        Client cachedClient = dbServiceClient
                .getClient(savedClient.getId())
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
        assertThat(cachedClient).isNotNull().usingRecursiveComparison().isEqualTo(savedClient);

        var loadedSavedClient = dbServiceClient.getClient(savedClient.getId());
        assertThat(loadedSavedClient)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedClient);
        verify(transactionManager, never()).doInReadOnlyTransaction(any());
    }

    @Test
    @DisplayName("gc должен очистить кэш ")
    void gcShouldClearCache() {
        int size = 200;
        for (int i = 0; i < size; i++) {
            dbServiceClient.saveClient(new Client(
                    null,
                    "Vasya",
                    new Address(null, "AnyStreet"),
                    List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))));
        }

        System.gc();

        assertThat(cache.size()).isLessThan(size);
    }
}
