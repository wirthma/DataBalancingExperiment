package cz.cuni.mff.dbe.nodesetsimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.NodeSet;
import cz.cuni.mff.dbe.model.NodeSetChange;

/**
 * Simulates node set changes in a distributed system.
 */
public interface NodeSetSimulator {
    /**
     * @param iterationNumber Number of the current iteration.
     * @param nodeSet The current nodes.
     * @param dataDistribution The current data distribution.
     */
    NodeSetChange nextNodeSet(int iterationNumber, NodeSet nodeSet, DataDistribution dataDistribution);
}
