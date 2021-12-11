package io.github.yezhihao.protostar.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class Cache<K, V> {

    private volatile Map<K, V> cache;

    public Cache() {
        this(32);
    }

    public Cache(Map<K, V> cache) {
        this.cache = cache;
    }

    public Cache(int initialCapacity) {
        this.cache = new HashMap<>((int) (initialCapacity / 0.75) + 1);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public V get(K key, Supplier<V> function) {
        V value = cache.get(key);
        if (value == null) {
            synchronized (cache) {
                value = cache.get(key);
                if (value == null) {
                    cache.put(key, value = function.get());
                }
            }
        }
        return value;
    }

    public V put(K key, V value) {
        synchronized (cache) {
            cache.put(key, value);
        }
        return value;
    }

    @Override
    public String toString() {
        return cache.toString();
    }
}