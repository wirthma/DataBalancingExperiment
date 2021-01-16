package cz.cuni.mff.dbe.nodecountsimulator;

/**
 * Simulates node count changes in a distributed system.
 */
public interface NodeCountSimulator {
    /**
     * @param iterationNumber Number of the current iteration.
     */
    int nextNodeCount(int iterationNumber, int nodeCount);
}
