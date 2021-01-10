package cz.cuni.mff.dbe.datasimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Node;

import java.util.*;

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
    public DataDistributionChange nextDataDistribution(DataDistribution dataDistribution, int nodeCount) {
        return createDataDistributionChange(nodeCount);
    }

    private DataItem createNewDataItem() {
        return new DataItem(nextDataItemId++);
    }

    private List<DataItem> createNewDataItemList() {
        List<DataItem> dataItemList = new ArrayList<>();

        dataItemList.add(createNewDataItem());

        return dataItemList;
    }

    private DataDistributionChange createDataDistributionChange(int nodeCount) {
        Map<Node, List<DataItem>> createdDataItems = new HashMap<>();

        createdDataItems.put(new Node(random.nextInt(nodeCount)), createNewDataItemList());

        return new DataDistributionChange(createdDataItems, new HashMap<>());
    }

    private final Random random;

    private int nextDataItemId = 0;
}
