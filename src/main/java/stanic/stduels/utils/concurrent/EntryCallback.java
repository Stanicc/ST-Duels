package stanic.stduels.utils.concurrent;

public interface EntryCallback<K, V> {
    void onAdd(K key, V value);
    void onRemove(K key, V value);
}