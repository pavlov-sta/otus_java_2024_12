package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, WeakReference<V>> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, new WeakReference<>(value));
        notificationListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        notificationListeners(key, value.get(), "remove");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notificationListeners(key, value.get(), "get");
        return value.get();
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }


    @Override
    public int size() {
        int size = 0;
        for (Map.Entry<K, WeakReference<V>> entry : cache.entrySet()) {
            if (entry.getValue().get() != null) {
                size++;
            }
        }
        return size;
    }

    private void notificationListeners(K key, V value, String action) {
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, value, action);
        }
    }
}
