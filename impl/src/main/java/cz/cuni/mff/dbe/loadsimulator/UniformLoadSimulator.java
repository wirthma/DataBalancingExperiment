package cz.cuni.mff.dbe.loadsimulator;

import cz.cuni.mff.dbe.model.*;
import cz.cuni.mff.dbe.util.data.DataDistributionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link LoadSimulator} that simulates load of value 1 on each data item.
 */
public final class UniformLoadSimulator implements LoadSimulator {
    @Override
    public LoadDistributionChange nextLoadDistribution(
            int iterationNumber,
            LoadDistribution loadDistribution,
            DataDistribution dataDistribution
    ) {
        Map<DataItem, Integer> changedLoad = new HashMap<>();

        dataDistribution.getNodeToDataMap().entrySet().stream().flatMap(
                (Map.Entry<Node, List<DataItem>> entry) -> entry.getValue().stream()
        ).forEach(
                (DataItem dataItem) -> {
                    if (loadDistribution.getItemToLoadMap().containsKey(dataItem)) {
                        changedLoad.put(dataItem, 1);
                    }
                }
        );

        DataDistributionUtils.collectNodeSize(dataDistribution, iterationNumber, "loadsimulator");

        return new LoadDistributionChange();
    }
}
