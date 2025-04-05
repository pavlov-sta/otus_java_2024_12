package otus.core.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import otus.base.AbstractHibernateTest;
import ru.otus.hiber.crm.model.Address;
import ru.otus.hiber.crm.model.Client;
import ru.otus.hiber.crm.model.Phone;
import ru.otus.hiber.crm.service.DbServiceClientImpl;

@SuppressWarnings("java:S125")
class DbServiceClientImplTest extends AbstractHibernateTest {

    @Test
    @DisplayName("должен сохранить и найти клиента, используя кэш")
    void shouldSaveAndFindClientUsingCache() {
        LogCaptor logCaptor = LogCaptor.forClass(DbServiceClientImpl.class);

        var client = new Client(
                null,
                "Vasya",
                new Address(null, "AnyStreet"),
                List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333")));

        var savedClient = dbServiceClient.saveClient(client);
        assertThat(savedClient).isNotNull();

        var loadedSavedClient = dbServiceClient.getClient(savedClient.getId());
        assertThat(loadedSavedClient)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedClient);

        List<String> logs = logCaptor.getLogs();
        assertThat(logs)
                .isNotEmpty()
                .anyMatch(log -> log.contains("notify: key=" + savedClient.getId())
                        && log.contains("value=" + savedClient)
                        && log.contains("action=get"))
                .anyMatch(log -> log.contains("notify: key=" + savedClient.getId())
                        && log.contains("value=" + savedClient)
                        && log.contains("action=put"));
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
