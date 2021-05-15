package cz.cuni.mff.dbe.datasimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.NodeSet;

/**
 * Simulates data distribution changes on individual nodes of a distributed system.
 */
public interface DataSimulator {
    /**
     * @param iterationNumber Number of the current iteration.
     * @param dataDistribution Current data distribution.
     * @param nodes Current system nodes.
     */
    DataDistributionChange nextDataDistribution(int iterationNumber, DataDistribution dataDistribution, NodeSet nodes);
}
