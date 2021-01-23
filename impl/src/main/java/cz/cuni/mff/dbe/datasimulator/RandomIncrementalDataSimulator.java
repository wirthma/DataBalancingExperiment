package cz.cuni.mff.dbe.datasimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.util.data.DataItemGen;
import cz.cuni.mff.dbe.util.node.NodeGen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A {@link DataSimulator} that adds one data item at a time into random node in the system.
 */
public final class RandomIncrementalDataSimulator implements DataSimulator {
    /**
     * @param seed The seed for the pseudo-random generator.
     */
    public  RandomIncrementalDataSimulator(int seed) {
        random = new Random(seed);
    }

    @Override
    public DataDistributionChange nextDataDistribution(
            int iterationNumber,
            DataDistribution dataDistribution,
            int nodeCount
    ) {
        if (nodeCount <= 0) {
            return new DataDistributionChange();
        }

        Map<Node, List<DataItem>> createdDataItems = new HashMap<>();
        createdDataItems.put(NodeGen.getNth(random.nextInt(nodeCount)), DataItemGen.generateList(1));
        return new DataDistributionChange(createdDataItems, new HashMap<>());
    }

    private final Random random;
}
