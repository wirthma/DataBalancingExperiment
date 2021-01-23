package cz.cuni.mff.dbe.nodecountsimulator;

import cz.cuni.mff.dbe.model.DataDistribution;

/**
 * Simulates node count changes in a distributed system.
 */
public interface NodeCountSimulator {
    /**
     * @param iterationNumber Number of the current iteration.
     * @param nodeCount The current node count.
     * @param dataDistribution The current data distribution.
     */
    int nextNodeCount(int iterationNumber, int nodeCount, DataDistribution dataDistribution);
}
