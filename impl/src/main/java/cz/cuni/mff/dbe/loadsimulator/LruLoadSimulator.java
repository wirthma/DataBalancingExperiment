package cz.cuni.mff.dbe.loadsimulator;

import cz.cuni.mff.dbe.model.*;
import cz.cuni.mff.dbe.util.metrics.Metrics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link LoadSimulator} that simulates greatest load on least recently created items. This simulates a LRU cache.
 *
 * The data item's load is exactly equal its timestamp multiplied by the given coefficient, which specifies how quickly
 * data items are becoming old and less frequently used in the system (e.g. less frequently queried from the LRU cache).
 */
@Component
@ConditionalOnProperty(name = "loadsimulator", havingValue = "lru")
public final class LruLoadSimulator implements LoadSimulator {
    public LruLoadSimulator(
            @Value("${loadsimulator.lru.obsoletingCoef}") int obsoletingCoef
    ) {
        this.obsoletingCoef = obsoletingCoef;
    }

    @Override
    public LoadDistributionChange nextLoadDistribution(
            int iterationNumber,
            LoadDistribution loadDistribution,
            DataDistribution dataDistribution
    ) {
        Map<DataItem, Integer> changedLoad = new HashMap<>();

        dataDistribution.getNodeToDataMap().entrySet().forEach(
                (Map.Entry<Node, List<DataItem>> entry) -> {
                    int loadSum = 0;

                    for (DataItem dataItem : entry.getValue()) {
                        Integer currentLoad = loadDistribution.getItemToLoadMap().get(dataItem);

                        if (currentLoad == null) {
                            int load = dataItem.getTimestamp() * obsoletingCoef;
                            loadSum += load;
                            changedLoad.put(dataItem, load);
                        } else {
                            loadSum += currentLoad;
                        }
                    }

                    Metrics.record(
                            iterationNumber,
                            "loadsimulator.node" + entry.getKey().getId() + ".size",
                            loadSum
                    );
                }
        );

        return new LoadDistributionChange(changedLoad);
    }

    private int obsoletingCoef;
}
