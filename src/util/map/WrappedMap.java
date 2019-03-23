package util.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Wrapper class that simply delegates all map operations to a backing map
 */
public class WrappedMap<K, V> implements Map<K, V> {

    protected final @NonNull Map<K, V> map;

    public WrappedMap(@NonNull Map<K, V> map) {
        this.map = map;
    }

    @Override public int size() {
        return map.size();
    }

    @Override public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override public V get(Object key) {
        return map.get(key);
    }

    @Override public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override public V remove(Object key) {
        return map.remove(key);
    }

    @Override public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override public void clear() {
        map.clear();
    }

    @Override public Set<K> keySet() {
        return map.keySet();
    }

    @Override public Collection<V> values() {
        return map.values();
    }

    @Override public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

}
