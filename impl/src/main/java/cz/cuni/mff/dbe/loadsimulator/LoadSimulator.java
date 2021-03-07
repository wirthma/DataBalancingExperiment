package cz.cuni.mff.dbe.loadsimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.LoadDistribution;
import cz.cuni.mff.dbe.model.LoadDistributionChange;

/**
 * Simulates load distribution changes on individual nodes of a distributed system.
 */
public interface LoadSimulator {
    /**
     * @param iterationNumber Number of the current iteration.
     * @param loadDistribution Current load distribution.
     * @param dataDistribution Current data distribution.
     */
    LoadDistributionChange nextLoadDistribution(
            int iterationNumber,
            LoadDistribution loadDistribution,
            DataDistribution dataDistribution
    );
}
