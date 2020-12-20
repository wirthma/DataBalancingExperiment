package cz.cuni.mff.dbe.nodecountsimulator;

/**
 * Simulates node count changes in a distributed system.
 */
public interface NodeCountSimulator {
    int nextNodeCount(int nodeCount);
}
