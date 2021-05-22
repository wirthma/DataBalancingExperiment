package cz.cuni.mff.dbe.util.rand;

import java.util.Random;

/**
 * Provides generation of random objects.
 */
public final class Rand {
    /**
     * Returns a random integer from the given interval (inclusive).
     */
    public static int randInt(Random r, int min, int max) {
        return min + r.nextInt(max + 1 - min);
    }
}
