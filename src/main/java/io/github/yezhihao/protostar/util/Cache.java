package io.github.yezhihao.protostar.util;

import java.util.HashMap;
import java.util.function.Function;

/**
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public class Cache<K, V> {

    private volatile HashMap<K, V> cache;

    public Cache() {
        this(32);
    }

    public Cache(int initialCapacity) {
        this.cache = new HashMap<>((int) (initialCapacity / 0.75) + 1);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public V get(K key, Function<K, V> function) {
        V value = cache.get(key);
        if (value == null) {
            synchronized (cache) {
                value = cache.get(key);
                if (value == null) {
                    cache.put(key, value = function.apply(key));
                }
            }
        }
        return value;
    }

    @Override
    public String toString() {
        return cache.toString();
    }
}