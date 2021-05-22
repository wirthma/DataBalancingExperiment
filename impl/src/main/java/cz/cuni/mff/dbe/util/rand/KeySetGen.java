package cz.cuni.mff.dbe.util.rand;

import java.util.*;

/**
 * Provides stable, uniformly-random generation of key-sets for the given objects, independently on their content
 * (hash, etc). Key-sets are absolutely unique, no key repeats in the same key-set or two different key-sets.
 * <p>
 * The given objects must have equals and hashCode properly implemented.
 */
public class KeySetGen<O> {
    /**
     * @param keySetSize Number of unique, random keys in each key-set.
     */
    public KeySetGen(int keySetSize, Random random) {
        this.keySetSize = keySetSize;
        this.random = random;
    }

    /**
     * Returns the random key-set assigned to the given object. If none exists yet, it creates one (and stores
     * it for future calls of this method).
     */
    public List<Integer> getKeySet(O object) {
        List<Integer> keySet = objects2KeySets.get(object);
        if (keySet != null) {
            return keySet;
        }

        keySet = new ArrayList<>();
        for (int i = 0; i < keySetSize; ++i) {
            while (true) { // we assume there will be only a reasonable amount of keys so this will end reasonably fast
                int key = random.nextInt();
                if (!keys2Objects.containsKey(key)) {
                    keys2Objects.put(key, object);
                    keySet.add(key);
                    break;
                }
            }
        }
        objects2KeySets.put(object, keySet);
        return keySet;
    }

    /**
     * Returns the object represented by the given key.
     */
    public O getObject(Integer key) {
        return keys2Objects.get(key);
    }

    /**
     * Number of unique, random keys in each key-set.
     */
    private int keySetSize;

    /**
     * Stores key-sets for objects. Each object has exactly one key-set.
     */
    private Map<O, List<Integer>> objects2KeySets = new HashMap<>();

    /**
     * Maps keys from all the key-sets to the corresponding objects.
     */
    private Map<Integer, O> keys2Objects = new HashMap<>();

    private Random random;
}
