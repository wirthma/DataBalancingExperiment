package cz.cuni.mff.dbe.algorithm.slicer;

/**
 * Represents a slice, as defined by the Weighted-Move algorithm.
 * <p>
 * The implementation represents a slice only by its lower bound. It assumes that the key ring is covered by slices
 * so the upper bound is unnecessary and even error-prone to store, the upper bound is given by the lower bound of
 * the next slice on the ring.
 */
final class Slice implements Comparable {
    public Slice(Integer lowerBound) {
        this.lowerBound = lowerBound;
    }

    @Override
    public int compareTo(Object o) {
        return o instanceof Slice ? lowerBound - ((Slice) o).lowerBound : 1;
    }

    @Override
    public int hashCode() {
        return lowerBound;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Slice && ((Slice) o).lowerBound == lowerBound;
    }

    private Integer lowerBound;
}
