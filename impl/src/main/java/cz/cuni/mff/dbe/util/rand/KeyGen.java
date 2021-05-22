package cz.cuni.mff.dbe.util.rand;

import java.util.Random;

/**
 * Provides stable, uniformly-random generation of unique keys for the given objects, independently on their content
 * (hash, etc).
 * <p>
 * The given objects must have equals and hashCode properly implemented.
 */
public class KeyGen<O> {
    public KeyGen(Random random) {
        this.keySetGen = new KeySetGen<>(1, random);
    }

    /**
     * Returns the random key assigned to the given object. If none exists yet, it creates a random, unique key for
     * the object (and stores it for future calls of this method).
     */
    public int getKey(O object) {
        return keySetGen.getKeySet(object).get(0);
    }

    private KeySetGen<O> keySetGen;
}
