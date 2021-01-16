package cz.cuni.mff.dbe.nodecountsimulator;

/**
 * A {@link NodeCountSimulator} that maintains a stable given count of system nodes.
 */
public final class StableNodeCountSimulator implements NodeCountSimulator {
    public StableNodeCountSimulator(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    @Override
    public int nextNodeCount(int nodeCount) {
        return this.nodeCount;
    }

    private final int nodeCount;
}
