package cz.cuni.mff.dbe.loadsimulator;

import cz.cuni.mff.dbe.model.LoadDistribution;
import cz.cuni.mff.dbe.model.LoadDistributionChange;

/**
 * Simulates load distribution changes on individual nodes of a distributed system.
 */
public interface LoadSimulator {
    LoadDistributionChange nextLoadDistribution(LoadDistribution loadDistribution);
}
