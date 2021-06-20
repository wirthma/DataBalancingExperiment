package cz.cuni.mff.dbe.datasimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.util.ds.TokenRing;

/**
 * Simulates data distribution changes on individual nodes of a distributed system.
 */
public interface DataSimulator {
    /**
     * @param iterationNumber Number of the current iteration.
     * @param dataDistribution Current data distribution.
     * @param nodeSet Current system nodes.
     */
    DataDistributionChange nextDataDistribution(
        int iterationNumber,
        DataDistribution dataDistribution,
        TokenRing<Integer, Node> nodeSet
    );
}
