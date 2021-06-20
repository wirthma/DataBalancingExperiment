package cz.cuni.mff.dbe.nodesetsimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.model.NodeSetChange;
import cz.cuni.mff.dbe.util.ds.TokenRing;

/**
 * Simulates node set changes in a distributed system.
 */
public interface NodeSetSimulator {
    /**
     * @param iterationNumber Number of the current iteration.
     * @param nodeSet The current set of nodes.
     * @param dataDistribution The current data distribution.
     */
    NodeSetChange nextNodeSet(int iterationNumber, TokenRing<Integer, Node> nodeSet, DataDistribution dataDistribution);
}
