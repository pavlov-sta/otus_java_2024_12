package homework;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> customers = new TreeMap<>(Comparator.comparing(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = customers.firstEntry();
        return entry != null ? creataImmutableEntry(entry) : null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = customers.higherEntry(customer);
        return entry != null ? creataImmutableEntry(entry) : null;
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }

    private Map.Entry<Customer, String> creataImmutableEntry(Map.Entry<Customer, String> entry) {
        Customer customerCopy = new Customer(
                entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores());
        return new AbstractMap.SimpleEntry<>(customerCopy, entry.getValue());
    }
}
