package cz.cuni.mff.dbe.loadsimulator;

import cz.cuni.mff.dbe.model.LoadDistribution;
import cz.cuni.mff.dbe.model.LoadDistributionChange;

import java.util.HashMap;

/**
 * A {@link LoadSimulator} that simulates no load on any data item.
 */
public final class NoLoadSimulator implements LoadSimulator {
    @Override
    public LoadDistributionChange nextLoadDistribution(LoadDistribution loadDistribution) {
        return new LoadDistributionChange(new HashMap<>());
    }
}
