package cz.cuni.mff.dbe.loadsimulator;

import cz.cuni.mff.dbe.model.*;
import cz.cuni.mff.dbe.util.metrics.Metrics;
import cz.cuni.mff.dbe.util.parsing.ParsingException;
import cz.cuni.mff.dbe.util.parsing.String2Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

/**
 * A {@link LoadSimulator} that maintains a stable load distribution defined by the user.
 *
 * The distribution is defined using a map of load values to a relative volume of data items that should be assigned
 * the particular load value. For example, one can define that 50 percent of the data items have load A, 25 percent
 * have load B and the rest 25 percent have load C. The load distribution is always respected as much as possible by
 * the following technique: data item IDs are being mapped onto a randomly-shuffled array of load values where the
 * cardinalities of the load values in the array respect the defined distribution.
 */
@Component
@ConditionalOnProperty(name = "loadsimulator", havingValue = "stablecustomdistribution")
public final class StableCustomDistributionLoadSimulator implements LoadSimulator {
    /**
     * @param distributionStr String definition of the load distribution. The format is a comma separated list of
     *                        "load:volume" pairs. For example, "1000:50,2000:50" defines a fifty-fifty distribution
     *                        between load values 1000 and 2000.
     * @param seed The seed for the pseudo-random generator.
     */
    public StableCustomDistributionLoadSimulator(
            @Value("${loadsimulator.stablecustomdistribution.distribution}") String distributionStr,
            @Value("${loadsimulator.stablecustomdistribution.seed}") int seed
    ) throws ParsingException {
        init(String2Map.parseIntPairMap(distributionStr), seed);
    }

    /**
     * @param seed The seed for the pseudo-random generator.
     */
    protected void init(Map<Integer, Integer> distribution, int seed) {
        Assert.notEmpty(distribution, "Undefined load distribution!");
        Assert.isTrue(
                distribution.values().stream().allMatch((Integer i) -> i > 0),
                "Not-positive volume found in the load distribution!"
        );
        Assert.isTrue(
                distribution.values().stream().reduce((Integer i, Integer j) -> i + j).get() == 100,
                "Relative volumes in the load distribution do not sum up to 100 percent!"
        );

        Random random = new Random(seed);

        distributionArray = new Integer[100];
        Arrays.fill(distributionArray, null);

        // this might take long, it can be optimized but it is good enough
        distribution.forEach((Integer load, Integer volume) -> {
            for (int i = 0; i < volume; ++i) {
                while (true) {
                    int index = random.nextInt(distributionArray.length);
                    if (distributionArray[index] == null) {
                        distributionArray[index] = load;
                        break;
                    }
                }
            }
        });
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
                        Integer expectedLoad = distributionArray[dataItem.getId() % distributionArray.length];

                        if (currentLoad != expectedLoad) {
                            changedLoad.put(dataItem, expectedLoad);
                        }

                        loadSum += expectedLoad;
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

    private Integer[] distributionArray;
}
