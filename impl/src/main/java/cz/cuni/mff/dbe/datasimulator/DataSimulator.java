package cz.cuni.mff.dbe.datasimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.DataDistributionChange;

/**
 * Simulates data distribution changes on individual nodes of a distributed system.
 */
public interface DataSimulator {
    DataDistributionChange nextDataDistribution(DataDistribution dataDistribution, int nodeCount);
}
