package cz.cuni.mff.dbe.util.ds;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implements a ring of key-value pairs where the ring ordering is given by the keys. One of the typical operations are
 * queries for a successor/predecessor of a given point on the ring.
 */
public final class TokenRing<Key extends Comparable, Value> {
    /**
     * @return All stored values.
     */
    public Collection<Map.Entry<Key, Value>> getAll() {
        return tree.entrySet();
    }

    /**
     * @return The key-value pair with the least key strictly greater than the given key or null if no such exists.
     */
    public Map.Entry<Key, Value> getStrictlyNext(Key key) {
        return tree.higherEntry(key);
    }

    /**
     * @return The key-value pair with the least key greater than or equal to the given key or null if no such exists.
     */
    public Map.Entry<Key, Value> getNext(Key key) {
        return tree.ceilingEntry(key);
    }

    /**
     * @return The key-value pair with the greatest key strictly less than the given key or null if no such exists.
     */
    public Map.Entry<Key, Value> getStrictlyPrevious(Key key) {
        return tree.lowerEntry(key);
    }

    /**
     * @return The key-value pair with the greatest key less than or equal to the given key or null if no such exists.
     */
    public Map.Entry<Key, Value> getPrevious(Key key) {
        return tree.floorEntry(key);
    }

    /**
     * @return The key-value pair with the least key strictly greater than the given key; or, if none such exists,
     * the key-value pair with the least key (if not the given key); or, if there are no other key-value pairs, null.
     */
    public Map.Entry<Key, Value> getStrictlyNextOnRing(Key key) {
        Map.Entry<Key, Value> next = getStrictlyNext(key);
        if (next != null) {
            return next;
        } else {
            Map.Entry<Key, Value> least = getLeast();
            return least != null && !least.getKey().equals(key) ? least : null;
        }
    }

    /**
     * @return The key-value pair with the least key than or equal to the given key; or, if none such exists,
     * the key-value pair with the least key; or, if there are no values, null.
     */
    public Map.Entry<Key, Value> getNextOnRing(Key key) {
        Map.Entry<Key, Value> next = getNext(key);
        return next != null ? next : getLeast();
    }

    /**
     * @return The key-value pair with the greatest key strictly less than the given key; or, if none such exists,
     * the key-value pair with the greatest key (if not the given key); or, if there are no other key-value pairs, null.
     */
    public Map.Entry<Key, Value> getStrictlyPreviousOnRing(Key key) {
        Map.Entry<Key, Value> previous = getStrictlyPrevious(key);
        if (previous != null) {
            return previous;
        } else {
            Map.Entry<Key, Value> greatest = getGreatest();
            return greatest != null && !greatest.getKey().equals(key) ? greatest : null;
        }
    }

    /**
     * @return The key-value pair with the greatest key less than or equal to the given key; or, if none such exists,
     * the key-value pair with the greatest key; or, if there are no values, null.
     */
    public Map.Entry<Key, Value> getPreviousOnRing(Key key) {
        Map.Entry<Key, Value> previous = getPrevious(key);
        return previous != null ? previous : getGreatest();
    }

    /**
     * @return The least key-value pair; or null if there are no values.
     */
    public Map.Entry<Key, Value> getLeast() {
        return tree.firstEntry();
    }

    /**
     * @return The greatest key-value pair; or null if there are no values.
     */
    public Map.Entry<Key, Value> getGreatest() {
        return tree.lastEntry();
    }

    /**
     * @return The value for the given key or null if none such exists.
     */
    public Value get(Key key) {
        return tree.get(key);
    }

    /**
     * @return The n-th key-value pair, ordered by the key ascending, or null if there is no such pair.
     */
    public Map.Entry<Key, Value> getNth(int n) {
        if (n < 0 || n >= size()) {
            return null;
        }

        Key nthKey = null;
        Iterator<Key> it = tree.navigableKeySet().iterator();
        for (int i = 0; i <= n; ++i) {
            nthKey = it.next();
        }
        return nthKey == null ? null : tree.floorEntry(nthKey);
    }

    /**
     * @return Number of key-value pairs in the {@link TokenRing}.
     */
    public int size() {
        return tree.size();
    }

    public boolean isEmpty() {
        return tree.isEmpty();
    }

    public void add(Key key, Value value) {
        tree.put(key, value);
    }

    public void remove(Key key) {
        tree.remove(key);
    }

    /**
     * Balanced binary searchable tree with the given key-value pairs.
     */
    private final TreeMap<Key, Value> tree = new TreeMap<>();
}
