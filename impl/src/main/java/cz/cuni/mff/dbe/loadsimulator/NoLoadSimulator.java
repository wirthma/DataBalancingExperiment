package cz.cuni.mff.dbe.loadsimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.LoadDistribution;
import cz.cuni.mff.dbe.model.LoadDistributionChange;
import cz.cuni.mff.dbe.util.datadistribution.DataDistributionUtils;

import java.util.HashMap;

/**
 * A {@link LoadSimulator} that simulates no load on any data item.
 */
public final class NoLoadSimulator implements LoadSimulator {
    @Override
    public LoadDistributionChange nextLoadDistribution(
            int iterationNumber,
            LoadDistribution loadDistribution,
            DataDistribution dataDistribution
    ) {
        DataDistributionUtils.collectNodeSize(dataDistribution, iterationNumber, "noloadsimulator");

        return new LoadDistributionChange();
    }
}
