package lab9;

/**
 * Created by sandeeptiwari on 3/16/17.
 */

import java.util.*;
import java.util.Map.Entry;

public class MyHashMap<K extends Comparable<K>, V> implements Map61B<K, V> {
//    private static int initialSize = 4;
    private static int maxCapacity = 1 << 30;
    private int capacity;
    private static int DEFAULT_CAPACITY = 11;
    private static double DEFAULT_LOAD_FACTOR = 0.75f;
    private  double loadFactor;
    private int size = 0;
    private int count;
    private HashEntry<K, V>[] bucks;
    Set<Entry<K, V>>[] lList;
    private int threshold;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);

    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize, double loadFactor) {
        if (initialSize < 0) {
            throw new IllegalArgumentException();
        }
        if (!(loadFactor > 0)) {
            throw new IllegalArgumentException();
        }
        if (initialSize == 0) {
            initialSize = 1;
        }
        bucks = (HashEntry<K, V>[]) new HashEntry[initialSize];
        this.loadFactor = loadFactor;
        threshold = (int) (initialSize * loadFactor);
//        if (initialSize > maxCapacity) {
//            capacity = maxCapacity;
//        } else {
//            capacity = placeShift(initialSize);
//        }
//        this.loadFactor = loadFactor;
//        lList = new Set[capacity];
    }

    public int placeShift(int cap) {
        capacity = 1;
        while (capacity < cap) {
            capacity <<= 1;
        }
        return capacity;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        size = 0;
        removeVals();
    }

    public void removeVals() {
        for (int i = 0; i < capacity; i++) {
            if (lList[i] != null) {
                lList[i].clear();
            }
        }
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        HashEntry<K, V> entry = bucks[index];
        while (entry != null) {
            if (key.equals(entry.getKey())) {
                entry.access();
                V val = entry.getValue();
            } else {
                entry = entry.next;
            }

        }
        count += 1;
        if (++size > threshold) {
            rehash();
            index = hash(key);
        }
        addEntry(key, value, index, true);

//        if (get(key) != null) {
//            int index = hash(key.hashCode());
//            Set<Entry<K, V>> space = lList[index];
//            for (Entry<K, V> e : space) {
//                if (e.getKey().equals(key)) {
//                    V oVal = e.getValue();
//                }
//            }
//        }
    }

    private void rehash() {
        HashEntry<K, V>[] oldBucks = bucks;
        int newCap = (2 * bucks.length) + 1;
        threshold = (int) (newCap * loadFactor);
        bucks = (HashEntry<K, V>[]) new HashEntry[newCap];
        for (int i = oldBucks.length - 1; i >= 0; i--) {
            HashEntry<K, V> entry = oldBucks[i];
            while (entry != null) {
                int index = hash(entry.getKey());
                HashEntry<K, V> dest = bucks[index];
                HashEntry<K, V> next = entry.next;
                entry.next = bucks[index];
                bucks[index] = entry;
                entry = next;
            }
        }
    }

    private void addEntry(K key, V value, int index, boolean call) {
        HashEntry<K, V> entry = new HashEntry<K, V>(key, value);
        entry.next = bucks[index];
        bucks[index] = entry;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<K>();
        for (int i = 0; i < capacity; i++) {
            if (lList[i] != null) {
                Set<Entry<K, V>> space = lList[i];
                for (Entry<K, V> e : space) {
                    keySet.add(e.getKey());
                }
            }
        }
        return keySet;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        HashEntry<K, V> entry = bucks[index];
        while (entry != null) {
            if (key.equals(entry.getKey())) {
                return entry.getValue();
            }
            entry = entry.next;
        }
//        if (lList[index] != null) {
//            Set<Entry<K, V>> space = lList[index];
//            for (Entry<K, V> e : space) {
//                if (e.getKey().equals(key)) {
//                    return e.getValue();
//                }
//            }
//        }
        return null;
    }

//    private int hasher(int x) {
//        return hash(x) & (capacity - 1);
//    }

//    private static int hash(int x) {
//        x ^= (x >>> 20) ^ (x >>> 4);
//        return x ^ (x >>> 7) ^ (x >>> 4);
//    }
    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % bucks.length);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    static class HashEntry<K, V> extends AbstractMap.SimpleEntry<K, V> {
        HashEntry<K, V> next;
        HashEntry(K key, V value) {
            super(key, value);
        }

        void access() {

        }
    }

//    static class Entry<K, V> {
//        K key;
//        V value;
//        Entry<K,V> next;
//
//        public Entry(K key, V value, Entry<K,V> next){
//            this.key = key;
//            this.value = value;
//            this.next = next;
//        }
//    }
}
