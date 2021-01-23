package cz.cuni.mff.dbe.nodecountsimulator;

import cz.cuni.mff.dbe.model.DataDistribution;

/**
 * A {@link NodeCountSimulator} that maintains a stable given count of system nodes.
 */
public final class StableNodeCountSimulator implements NodeCountSimulator {
    public StableNodeCountSimulator(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    @Override
    public int nextNodeCount(int iterationNumber, int nodeCount, DataDistribution dataDistribution) {
        dataDistribution.update(this.nodeCount);
        return this.nodeCount;
    }

    private final int nodeCount;
}
