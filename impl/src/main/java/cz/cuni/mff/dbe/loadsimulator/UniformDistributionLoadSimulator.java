package cz.cuni.mff.dbe.loadsimulator;

import cz.cuni.mff.dbe.model.*;
import cz.cuni.mff.dbe.util.metrics.Metrics;
import cz.cuni.mff.dbe.util.rand.Rand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A {@link LoadSimulator} that maintains a stable load distribution where each data items is assigned load from
 * a defined uniform random distribution.
 */
@Component
@ConditionalOnProperty(name = "loadsimulator", havingValue = "uniformdistribution")
public final class UniformDistributionLoadSimulator implements LoadSimulator {
    /**
     * @param seed The seed for the pseudo-random generator.
     * @param minLoad The minimal possible load value assignable to a data item.
     * @param maxLoad The maximal possible load value assignable to a data item.
     */
    public UniformDistributionLoadSimulator(
            @Value("${loadsimulator.uniformdistribution.seed}") int seed,
            @Value("${loadsimulator.uniformdistribution.minload}") int minLoad,
            @Value("${loadsimulator.uniformdistribution.maxload}") int maxLoad
    ) {
        Assert.isTrue(minLoad >= 0, "Only non-negative load values are supported!");
        Assert.isTrue(maxLoad >= 0, "Only non-negative load values are supported!");

        random = new Random(seed);
        this.minLoad = minLoad;
        this.maxLoad = maxLoad;
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
                            currentLoad = Rand.randInt(random, minLoad, maxLoad);
                            changedLoad.put(dataItem, currentLoad);
                        }

                        loadSum += currentLoad;
                    }

                    Metrics.record(
                            iterationNumber,
                            "loadsimulator.node" + entry.getKey().getId(),
                            loadSum
                    );
                }
        );

        return new LoadDistributionChange(changedLoad);
    }

    private Random random;

    /**
     * The minimal possible load value assignable to a data item.
     */
    private int minLoad;

    /**
     * The maximal possible load value assignable to a data item.
     */
    private int maxLoad;
}
